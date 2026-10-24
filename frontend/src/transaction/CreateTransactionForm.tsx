import type { JSX } from "react";
import { useState } from "react";
import { useTranslation } from "react-i18next";

import type { Account, Jar } from "@gql/graphql";

import AccountTransferTransactionForm from "./AccountTransferTransactionForm";
import JarTransferTransactionForm from "./JarTransferTransactionForm";
import PaymentTransactionForm from "./PaymentTransactionForm";
import { TransactionType } from "./transactionTypes";

export type CreateTransactionFormProps = {
	jars: Array<Jar>;
	accounts: Array<Account>;

	onClose: () => void;
};

const CreateTransactionForm = (
	props: CreateTransactionFormProps
): JSX.Element => {
	const { t } = useTranslation();

	const [type, setType] = useState<TransactionType>(TransactionType.Payment);

	return (
		<>
			<div className="form-group">
				<label htmlFor="transactionType">{t("transactionType")}</label>
				<select
					id="transactionType"
					value={type}
					onChange={e => setType(e.target.value as TransactionType)}>
					<option value={TransactionType.Payment}>
						{t("paymentTransaction")}
					</option>
					<option value={TransactionType.JarTransfer}>
						{t("jarTransferTransaction")}
					</option>
					<option value={TransactionType.AccountTransfer}>
						{t("accountTransferTransaction")}
					</option>
				</select>
			</div>
			{type === TransactionType.Payment ? (
				<PaymentTransactionForm
					jars={props.jars}
					accounts={props.accounts}
					onClose={props.onClose}
				/>
			) : type === TransactionType.JarTransfer ? (
				<JarTransferTransactionForm
					jars={props.jars}
					onClose={props.onClose}
				/>
			) : (
				<AccountTransferTransactionForm
					accounts={props.accounts}
					onClose={props.onClose}
				/>
			)}
		</>
	);
};

export default CreateTransactionForm;
