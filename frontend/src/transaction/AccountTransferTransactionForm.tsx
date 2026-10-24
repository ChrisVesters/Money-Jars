import type { JSX } from "react";
import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";

import { useMutation } from "@apollo/client/react";
import type { Account } from "@gql/graphql";
import {
	CreateAccountTransferTransactionDocument,
	UpdateAccountTransferTransactionDocument
} from "@gql/graphql";

import { TRANSACTION_REFETCH_QUERIES } from "./transactionQueries";
import type { AccountTransferTransactionItem } from "./transactionTypes";

export type AccountTransferTransactionFormProps = {
	transaction?: AccountTransferTransactionItem;
	accounts: Array<Account>;

	onClose: () => void;
};

const AccountTransferTransactionForm = (
	props: AccountTransferTransactionFormProps
): JSX.Element => {
	const { t } = useTranslation();

	const [createAccountTransferTransaction] = useMutation(
		CreateAccountTransferTransactionDocument
	);
	const [updateAccountTransferTransaction] = useMutation(
		UpdateAccountTransferTransactionDocument
	);

	const [date, setDate] = useState("");
	const [amount, setAmount] = useState("");
	const [description, setDescription] = useState("");
	const [sourceAccountId, setSourceAccountId] = useState("");
	const [targetAccountId, setTargetAccountId] = useState("");
	const [errors, setErrors] = useState<Record<string, string>>({});

	useEffect(() => {
		if (props.transaction) {
			setDate(props.transaction.date);
			setAmount(props.transaction.amount ?? "");
			setDescription(props.transaction.description ?? "");
			setSourceAccountId(props.transaction.sourceAccount.id);
			setTargetAccountId(props.transaction.targetAccount.id);
		} else {
			setDate(new Date().toISOString().split("T")[0]);
			setAmount("");
			setDescription("");
			setSourceAccountId(
				props.accounts.length > 0 ? props.accounts[0].id : ""
			);
			setTargetAccountId(
				props.accounts.length > 1 ? props.accounts[1].id : ""
			);
		}
	}, [props.transaction, props.accounts]);

	const validateForm = (): boolean => {
		const newErrors: Record<string, string> = {};

		if (!date) {
			newErrors.date = t("validationDateRequired");
		}

		if (!amount) {
			newErrors.amount = t("validationAmountRequired");
		} else if (
			Number.isNaN(Number.parseFloat(amount)) ||
			Number.parseFloat(amount) <= 0
		) {
			newErrors.amount = t("validationAmountInvalid");
		}

		if (!sourceAccountId) {
			newErrors.sourceAccountId = t("validationAccountRequired");
		}

		if (!targetAccountId) {
			newErrors.targetAccountId = t("validationAccountRequired");
		} else if (targetAccountId === sourceAccountId) {
			newErrors.targetAccountId = t("validationAccountsMustDiffer");
		}

		setErrors(newErrors);
		return Object.keys(newErrors).length === 0;
	};

	const handleSubmit = async (e: React.FormEvent) => {
		e.preventDefault();

		if (!validateForm()) {
			return;
		}

		const data = {
			date,
			amount,
			description: description || "",
			sourceAccountId,
			targetAccountId
		};

		try {
			if (props.transaction) {
				const result = await updateAccountTransferTransaction({
					variables: {
						id: props.transaction.id,
						req: data
					},
					refetchQueries: TRANSACTION_REFETCH_QUERIES
				});
				console.log("Update transaction result:", result);
			} else {
				const result = await createAccountTransferTransaction({
					variables: {
						req: data
					},
					refetchQueries: TRANSACTION_REFETCH_QUERIES
				});
				console.log("Create transaction result:", result);
			}
		} catch (error) {
			console.error("Error saving transaction:", error);
		}

		props.onClose();
		clear();
	};

	const handleCancel = (e: React.MouseEvent) => {
		e.preventDefault();
		props.onClose();

		clear();
	};

	const clear = (): void => {
		setDate(new Date().toISOString().split("T")[0]);
		setAmount("");
		setDescription("");
		setSourceAccountId(
			props.accounts.length > 0 ? props.accounts[0].id : ""
		);
		setTargetAccountId(
			props.accounts.length > 1 ? props.accounts[1].id : ""
		);
	};

	return (
		<form onSubmit={handleSubmit}>
			<div className="form-group">
				<label htmlFor="date">
					{t("date")} <span className="required">*</span>
				</label>
				<input
					id="date"
					type="date"
					value={date}
					onChange={e => setDate(e.target.value)}
					className={errors.date ? "input-error" : ""}
				/>
				{errors.date && (
					<span className="error-message">{errors.date}</span>
				)}
			</div>

			<div className="form-group">
				<label htmlFor="amount">
					{t("amount")} <span className="required">*</span>
				</label>
				<input
					id="amount"
					type="number"
					step="0.01"
					value={amount}
					onChange={e => setAmount(e.target.value)}
					className={errors.amount ? "input-error" : ""}
				/>
				{errors.amount && (
					<span className="error-message">{errors.amount}</span>
				)}
			</div>

			<div className="form-group">
				<label htmlFor="description">{t("description")}</label>
				<textarea
					id="description"
					value={description}
					onChange={e => setDescription(e.target.value)}
					rows={3}
				/>
			</div>

			<div className="form-group">
				<label htmlFor="sourceAccountId">
					{t("sourceAccount")} <span className="required">*</span>
				</label>
				<select
					id="sourceAccountId"
					value={sourceAccountId}
					onChange={e => setSourceAccountId(e.target.value)}
					className={errors.sourceAccountId ? "input-error" : ""}>
					{props.accounts.map(account => (
						<option key={account.id} value={account.id}>
							{account.name}
						</option>
					))}
				</select>
				{errors.sourceAccountId && (
					<span className="error-message">
						{errors.sourceAccountId}
					</span>
				)}
			</div>

			<div className="form-group">
				<label htmlFor="targetAccountId">
					{t("targetAccount")} <span className="required">*</span>
				</label>
				<select
					id="targetAccountId"
					value={targetAccountId}
					onChange={e => setTargetAccountId(e.target.value)}
					className={errors.targetAccountId ? "input-error" : ""}>
					{props.accounts.map(account => (
						<option key={account.id} value={account.id}>
							{account.name}
						</option>
					))}
				</select>
				{errors.targetAccountId && (
					<span className="error-message">
						{errors.targetAccountId}
					</span>
				)}
			</div>

			<div className="form-actions">
				<button type="button" onClick={handleCancel}>
					{t("cancel")}
				</button>
				<button type="submit">
					{props.transaction ? t("update") : t("create")}
				</button>
			</div>
		</form>
	);
};

export default AccountTransferTransactionForm;
