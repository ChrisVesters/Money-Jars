import type { JSX } from "react";
import { useEffect, useState } from "react";

import type { CreateTransaction, Jar } from "@gql/graphql";
import type { TransactionItem } from "./transactionTypes";

export type TransactionFormProps = {
	transaction?: TransactionItem;
	jars: Array<Jar>;

	onClose: () => void;
	onConfirm: (transaction: CreateTransaction) => void;
};

// TODO: we can not re-use create for update, since most fields will be locked.
// TODO: Well, we can re-use it, but we won't be able to submit it.
// TODO: onConfirm should use Transaction instead of CreateTransaction
export default function TransactionForm(
	props: TransactionFormProps
): JSX.Element {
	const [date, setDate] = useState("");
	const [amount, setAmount] = useState("");
	const [beneficiary, setBeneficiary] = useState("");
	const [description, setDescription] = useState("");
	const [jarId, setJarId] = useState("");
	const [errors, setErrors] = useState<Record<string, string>>({});

	useEffect(() => {
		if (props.transaction) {
			setDate(props.transaction.date);
			setAmount(props.transaction.amount?.toString() ?? "");
			setBeneficiary(props.transaction.beneficiary ?? "");
			setDescription(props.transaction.description ?? "");
		} else {
			setDate(new Date().toISOString().split("T")[0]);
			setAmount("");
			setBeneficiary("");
			setDescription("");
			setJarId(props.jars.length > 0 ? props.jars[0].id : "");
		}
	}, [props.transaction, props.jars]);

	const validateForm = (): boolean => {
		const newErrors: Record<string, string> = {};

		if (!date) {
			newErrors.date = "Date is required";
		}

		if (!amount) {
			newErrors.amount = "Amount is required";
		} else if (isNaN(parseFloat(amount)) || parseFloat(amount) <= 0) {
			newErrors.amount = "Please enter a valid amount";
		}

		if (!beneficiary.trim()) {
			newErrors.beneficiary = "Beneficiary is required";
		}

		if (!jarId) {
			newErrors.jarId = "Jar is required";
		}

		setErrors(newErrors);
		return Object.keys(newErrors).length === 0;
	};

	const handleSubmit = async (e: React.FormEvent) => {
		e.preventDefault();

		if (!validateForm()) {
			return;
		}

		props.onConfirm({
			date,
			amount: parseFloat(amount),
			beneficiary,
			description: description || undefined,
			jarId
		});
	};

	return (
		<form onSubmit={handleSubmit}>
			<div className="form-group">
				<label htmlFor="date">
					Date <span className="required">*</span>
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
					Amount <span className="required">*</span>
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
				<label htmlFor="beneficiary">
					Beneficiary <span className="required">*</span>
				</label>
				<input
					id="beneficiary"
					type="text"
					value={beneficiary}
					onChange={e => setBeneficiary(e.target.value)}
					className={errors.beneficiary ? "input-error" : ""}
				/>
				{errors.beneficiary && (
					<span className="error-message">{errors.beneficiary}</span>
				)}
			</div>

			<div className="form-group">
				<label htmlFor="description">Description</label>
				<textarea
					id="description"
					value={description}
					onChange={e => setDescription(e.target.value)}
					rows={3}
				/>
			</div>

			<div className="form-group">
				<label htmlFor="jarId">
					Jar <span className="required">*</span>
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

			<div className="form-actions">
				<button type="button" onClick={props.onClose}>
					Cancel
				</button>
				<button type="submit">
					{props.transaction ? "Update" : "Create"}
				</button>
			</div>
		</form>
	);
}
