import type { JSX } from "react";
import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";

import { useMutation } from "@apollo/client/react";
import type { Jar } from "@gql/graphql";
import {
	CreateJarTransferTransactionDocument,
	UpdateJarTransferTransactionDocument
} from "@gql/graphql";

import { TRANSACTION_REFETCH_QUERIES } from "../transactionQueries";
import type { JarTransferTransactionItem } from "../transactionTypes";

export type JarTransferTransactionFormProps = {
	transaction?: JarTransferTransactionItem;
	jars: Array<Jar>;

	onClose: () => void;
};

const JarTransferTransactionForm = (
	props: JarTransferTransactionFormProps
): JSX.Element => {
	const { t } = useTranslation();

	const [createJarTransferTransaction] = useMutation(
		CreateJarTransferTransactionDocument
	);
	const [updateJarTransferTransaction] = useMutation(
		UpdateJarTransferTransactionDocument
	);

	const [date, setDate] = useState("");
	const [amount, setAmount] = useState("");
	const [description, setDescription] = useState("");
	const [sourceJarId, setSourceJarId] = useState("");
	const [targetJarId, setTargetJarId] = useState("");
	const [errors, setErrors] = useState<Record<string, string>>({});

	useEffect(() => {
		if (props.transaction) {
			setDate(props.transaction.date);
			setAmount(props.transaction.amount ?? "");
			setDescription(props.transaction.description ?? "");
			setSourceJarId(props.transaction.sourceJar.id);
			setTargetJarId(props.transaction.targetJar.id);
		} else {
			setDate(new Date().toISOString().split("T")[0]);
			setAmount("");
			setDescription("");
			setSourceJarId(props.jars.length > 0 ? props.jars[0].id : "");
			setTargetJarId(props.jars.length > 1 ? props.jars[1].id : "");
		}
	}, [props.transaction, props.jars]);

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

		if (!sourceJarId) {
			newErrors.sourceJarId = t("validationJarRequired");
		}

		if (!targetJarId) {
			newErrors.targetJarId = t("validationJarRequired");
		} else if (targetJarId === sourceJarId) {
			newErrors.targetJarId = t("validationJarsMustDiffer");
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
			sourceJarId,
			targetJarId
		};

		try {
			if (props.transaction) {
				const result = await updateJarTransferTransaction({
					variables: {
						id: props.transaction.id,
						req: data
					},
					refetchQueries: TRANSACTION_REFETCH_QUERIES
				});
				console.log("Update transaction result:", result);
			} else {
				const result = await createJarTransferTransaction({
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
		setSourceJarId(props.jars.length > 0 ? props.jars[0].id : "");
		setTargetJarId(props.jars.length > 1 ? props.jars[1].id : "");
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
				<label htmlFor="sourceJarId">
					{t("sourceJar")} <span className="required">*</span>
				</label>
				<select
					id="sourceJarId"
					value={sourceJarId}
					onChange={e => setSourceJarId(e.target.value)}
					className={errors.sourceJarId ? "input-error" : ""}>
					{props.jars.map(jar => (
						<option key={jar.id} value={jar.id}>
							{jar.name}
						</option>
					))}
				</select>
				{errors.sourceJarId && (
					<span className="error-message">{errors.sourceJarId}</span>
				)}
			</div>

			<div className="form-group">
				<label htmlFor="targetJarId">
					{t("targetJar")} <span className="required">*</span>
				</label>
				<select
					id="targetJarId"
					value={targetJarId}
					onChange={e => setTargetJarId(e.target.value)}
					className={errors.targetJarId ? "input-error" : ""}>
					{props.jars.map(jar => (
						<option key={jar.id} value={jar.id}>
							{jar.name}
						</option>
					))}
				</select>
				{errors.targetJarId && (
					<span className="error-message">{errors.targetJarId}</span>
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

export default JarTransferTransactionForm;
