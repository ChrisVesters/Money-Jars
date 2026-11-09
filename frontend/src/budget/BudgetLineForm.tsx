import type { JSX } from "react";
import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";

import { BudgetLineDirection } from "@gql/graphql";

import type { CategoryItem } from "@src/category/categoryTypes";

import type { BudgetLineItem } from "./budgetTypes";

type CategoryOption = { id: string; name: string };

export type BudgetLineFormValues = {
	categoryId: string;
	amount: string;
	direction: BudgetLineDirection;
};

export type BudgetLineFormProps = {
	line?: BudgetLineItem;
	/**
	 * The fixed category of a new line, for a category that has actual
	 * amounts but no line yet.
	 */
	category?: CategoryOption;
	direction: BudgetLineDirection;
	categories: Array<CategoryItem>;
	onClose: () => void;
	onConfirm: (values: BudgetLineFormValues) => Promise<boolean>;
};

const BudgetLineForm = (
	props: Readonly<BudgetLineFormProps>
): JSX.Element => {
	const { t } = useTranslation();

	const [categoryId, setCategoryId] = useState("");
	const [amount, setAmount] = useState("");
	const [direction, setDirection] = useState<BudgetLineDirection>(
		BudgetLineDirection.Expense
	);
	const [errors, setErrors] = useState<Record<string, string>>({});

	const reset = (
		categories: Array<CategoryItem>,
		defaultDirection: BudgetLineDirection,
		line?: BudgetLineItem,
		category?: CategoryOption
	): void => {
		setCategoryId(
			line?.category.id ?? category?.id ?? categories[0]?.id ?? ""
		);
		setAmount(line?.amount ?? "");
		setDirection(line?.direction ?? defaultDirection);
		setErrors({});
	};

	useEffect(() => {
		reset(props.categories, props.direction, props.line, props.category);
	}, [props.line, props.categories, props.direction, props.category]);

	const validateForm = (): boolean => {
		const newErrors: Record<string, string> = {};

		if (!categoryId) {
			newErrors.categoryId = t("validationCategoryRequired");
		}

		if (!amount) {
			newErrors.amount = t("validationAmountRequired");
		} else if (
			Number.isNaN(Number.parseFloat(amount)) ||
			Number.parseFloat(amount) < 0
		) {
			newErrors.amount = t("validationAmountInvalid");
		}

		setErrors(newErrors);
		return Object.keys(newErrors).length === 0;
	};

	const fixedCategory = props.line?.category ?? props.category;

	const handleSubmit = async (event: React.FormEvent): Promise<void> => {
		event.preventDefault();

		if (!validateForm()) {
			return;
		}

		const saved = await props.onConfirm({ categoryId, amount, direction });
		if (saved) {
			reset(props.categories, props.direction);
		} else {
			setErrors({ form: t("errorSaveBudgetLine") });
		}
	};

	const handleCancel = (): void => {
		props.onClose();
		reset(props.categories, props.direction);
	};

	return (
		<form onSubmit={handleSubmit}>
			<div className="form-group">
				<label htmlFor="budget-line-category">
					{t("category")} <span className="required">*</span>
				</label>
				{fixedCategory ? (
					<input
						id="budget-line-category"
						type="text"
						value={fixedCategory.name}
						disabled
					/>
				) : (
					<select
						id="budget-line-category"
						value={categoryId}
						onChange={event => setCategoryId(event.target.value)}
						className={errors.categoryId ? "input-error" : ""}>
						{props.categories.map(category => (
							<option key={category.id} value={category.id}>
								{category.name}
							</option>
						))}
					</select>
				)}
				{errors.categoryId && (
					<span className="error-message">{errors.categoryId}</span>
				)}
			</div>

			<div className="form-group">
				<label htmlFor="budget-line-direction">
					{t("direction")} <span className="required">*</span>
				</label>
				<select
					id="budget-line-direction"
					value={direction}
					onChange={event =>
						setDirection(event.target.value as BudgetLineDirection)
					}>
					<option value={BudgetLineDirection.Expense}>
						{t(BudgetLineDirection.Expense)}
					</option>
					<option value={BudgetLineDirection.Income}>
						{t(BudgetLineDirection.Income)}
					</option>
				</select>
			</div>

			<div className="form-group">
				<label htmlFor="budget-line-amount">
					{t("plannedAmount")} <span className="required">*</span>
				</label>
				<input
					id="budget-line-amount"
					type="number"
					step="0.01"
					min="0"
					value={amount}
					onChange={event => setAmount(event.target.value)}
					className={errors.amount ? "input-error" : ""}
				/>
				{errors.amount && (
					<span className="error-message">{errors.amount}</span>
				)}
			</div>

			{errors.form && (
				<span className="error-message">{errors.form}</span>
			)}

			<div className="form-actions">
				<button type="button" onClick={handleCancel}>
					{t("cancel")}
				</button>
				<button type="submit">
					{props.line ? t("update") : t("create")}
				</button>
			</div>
		</form>
	);
};

export default BudgetLineForm;
