import type { JSX } from "react";
import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";

import type { UpdateBudget } from "@gql/graphql";

import { currentMonthPeriod } from "./budgetDates";
import type { BudgetItem } from "./budgetTypes";

export type BudgetFormProps = {
	budget?: BudgetItem;
	onClose: () => void;
	onConfirm: (budget: UpdateBudget) => Promise<boolean>;
};

const BudgetForm = (props: Readonly<BudgetFormProps>): JSX.Element => {
	const { t } = useTranslation();

	const [name, setName] = useState("");
	const [description, setDescription] = useState("");
	const [startDate, setStartDate] = useState("");
	const [endDate, setEndDate] = useState("");
	const [errors, setErrors] = useState<Record<string, string>>({});

	const reset = (budget?: BudgetItem): void => {
		const period = budget ?? currentMonthPeriod();
		setName(budget?.name ?? "");
		setDescription(budget?.description ?? "");
		setStartDate(period.startDate);
		setEndDate(period.endDate);
		setErrors({});
	};

	useEffect(() => {
		reset(props.budget);
	}, [props.budget]);

	const validateForm = (): boolean => {
		const newErrors: Record<string, string> = {};

		if (!name.trim()) {
			newErrors.name = t("validationNameRequired");
		}

		if (!startDate) {
			newErrors.startDate = t("validationStartDateRequired");
		}

		if (!endDate) {
			newErrors.endDate = t("validationEndDateRequired");
		} else if (startDate && endDate < startDate) {
			newErrors.endDate = t("validationEndDateBeforeStartDate");
		}

		setErrors(newErrors);
		return Object.keys(newErrors).length === 0;
	};

	const handleSubmit = async (event: React.FormEvent): Promise<void> => {
		event.preventDefault();

		if (!validateForm()) {
			return;
		}

		const saved = await props.onConfirm({
			name: name.trim(),
			description: description.trim(),
			startDate,
			endDate
		});
		if (saved) {
			reset();
		} else {
			setErrors({ form: t("errorSaveBudget") });
		}
	};

	const handleCancel = (): void => {
		props.onClose();
		reset();
	};

	return (
		<form onSubmit={handleSubmit}>
			<div className="form-group">
				<label htmlFor="budget-name">{t("name")}</label>
				<input
					id="budget-name"
					type="text"
					value={name}
					onChange={event => setName(event.target.value)}
					className={errors.name ? "input-error" : ""}
				/>
				{errors.name && (
					<span className="error-message">{errors.name}</span>
				)}
			</div>
			<div className="form-group">
				<label htmlFor="budget-description">{t("description")}</label>
				<textarea
					id="budget-description"
					value={description}
					onChange={event => setDescription(event.target.value)}
					rows={3}
				/>
			</div>
			<div className="form-group">
				<label htmlFor="budget-start-date">{t("startDate")}</label>
				<input
					id="budget-start-date"
					type="date"
					value={startDate}
					onChange={event => setStartDate(event.target.value)}
					className={errors.startDate ? "input-error" : ""}
				/>
				{errors.startDate && (
					<span className="error-message">{errors.startDate}</span>
				)}
			</div>
			<div className="form-group">
				<label htmlFor="budget-end-date">{t("endDate")}</label>
				<input
					id="budget-end-date"
					type="date"
					value={endDate}
					min={startDate}
					onChange={event => setEndDate(event.target.value)}
					className={errors.endDate ? "input-error" : ""}
				/>
				{errors.endDate && (
					<span className="error-message">{errors.endDate}</span>
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
					{props.budget ? t("update") : t("create")}
				</button>
			</div>
		</form>
	);
};

export default BudgetForm;
