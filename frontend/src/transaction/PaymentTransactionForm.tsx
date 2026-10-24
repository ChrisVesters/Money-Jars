import type { JSX } from "react";
import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";

import { useMutation } from "@apollo/client/react";
import type { Account, Jar } from "@gql/graphql";
import {
	CreatePaymentTransactionDocument,
	Direction,
	UpdatePaymentTransactionDocument
} from "@gql/graphql";
import { TRANSACTION_REFETCH_QUERIES } from "./transactionQueries";
import type { PaymentTransactionItem } from "./transactionTypes";
import IconOutgoing from "@assets/icons/IconOutgoing";
import IconIncoming from "@assets/icons/IconIncoming";

export type PaytmentTransactionFormProps = {
	transaction?: PaymentTransactionItem;
	jars: Array<Jar>;
	accounts: Array<Account>;

	onClose: () => void;
};

const PaymentTransactionForm = (props: PaytmentTransactionFormProps): JSX.Element => {
	const { t } = useTranslation();

	const [createPaymentTransaction] = useMutation(
		CreatePaymentTransactionDocument
	);
	const [updatePaymentTransaction] = useMutation(
		UpdatePaymentTransactionDocument
	);

	const [date, setDate] = useState("");
	const [amount, setAmount] = useState("");
	const [description, setDescription] = useState("");
	const [jarId, setJarId] = useState("");
	const [accountId, setAccountId] = useState("");
	const [counterparty, setCounterparty] = useState("");
	const [direction, setDirection] = useState(Direction.Outgoing);
	const [errors, setErrors] = useState<Record<string, string>>({});

	useEffect(() => {
		if (props.transaction) {
			setDate(props.transaction.date);
			setAmount(props.transaction.amount ?? "");
			setDescription(props.transaction.description ?? "");
			setJarId(props.transaction.jar.id);
			setAccountId(props.transaction.account.id);
			setCounterparty(props.transaction.counterparty ?? "");
			setDirection(props.transaction.direction);
		} else {
			setDate(new Date().toISOString().split("T")[0]);
			setAmount("");
			setCounterparty("");
			setDescription("");
			setJarId(props.jars.length > 0 ? props.jars[0].id : "");
			setAccountId(props.accounts.length > 0 ? props.accounts[0].id : "");
			setDirection(Direction.Outgoing);
		}
	}, [props.transaction, props.jars, props.accounts]);

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

		if (!counterparty.trim()) {
			newErrors.counterparty = t("validationCounterpartyRequired");
		}

		if (!jarId) {
			newErrors.jarId = t("validationJarRequired");
		}

		if (!accountId) {
			newErrors.accountId = t("validationAccountRequired");
		}

		if (!direction) {
			newErrors.direction = t("validationDirectionRequired");
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
			amount: amount,
			description: description || "",
			jarId,
			accountId,
			counterparty: counterparty,
			direction
		};

		try {
			if (props.transaction) {
				const result = await updatePaymentTransaction({
					variables: {
						id: props.transaction.id,
						req: data
					},
					refetchQueries: TRANSACTION_REFETCH_QUERIES
				});
				console.log("Update transaction result:", result);
			} else {
				const result = await createPaymentTransaction({
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
		setCounterparty("");
		setDescription("");
		setJarId(props.jars.length > 0 ? props.jars[0].id : "");
		setAccountId(props.accounts.length > 0 ? props.accounts[0].id : "");
		setDirection(Direction.Outgoing);
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
				{/*  TODO: Generic component */}
				<button
					type="button"
					onClick={() =>
						setDirection(
							direction === Direction.Outgoing
								? Direction.Incoming
								: Direction.Outgoing
						)
					}>
					{direction === Direction.Outgoing ? (
						<IconOutgoing />
					) : (
						<IconIncoming />
					)}
				</button>
				{errors.amount && (
					<span className="error-message">{errors.amount}</span>
				)}
			</div>

			<div className="form-group">
				<label htmlFor="counterparty">
					{t("counterparty")} <span className="required">*</span>
				</label>
				<input
					id="counterparty"
					type="text"
					value={counterparty}
					onChange={e => setCounterparty(e.target.value)}
					className={errors.counterparty ? "input-error" : ""}
				/>
				{errors.counterparty && (
					<span className="error-message">{errors.counterparty}</span>
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
				<label htmlFor="jarId">
					{t("jar")} <span className="required">*</span>
				</label>
				<select
					id="jarId"
					value={jarId}
					onChange={e => setJarId(e.target.value)}
					className={errors.jarId ? "input-error" : ""}>
					{props.jars.map(jar => (
						<option key={jar.id} value={jar.id}>
							{jar.name}
						</option>
					))}
				</select>
				{errors.jarId && (
					<span className="error-message">{errors.jarId}</span>
				)}
			</div>

			<div className="form-group">
				<label htmlFor="accountId">
					{t("account")} <span className="required">*</span>
				</label>
				<select
					id="accountId"
					value={accountId}
					onChange={e => setAccountId(e.target.value)}
					className={errors.accountId ? "input-error" : ""}>
					{props.accounts.map(account => (
						<option key={account.id} value={account.id}>
							{account.name}
						</option>
					))}
				</select>
				{errors.accountId && (
					<span className="error-message">{errors.accountId}</span>
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

export default PaymentTransactionForm;
