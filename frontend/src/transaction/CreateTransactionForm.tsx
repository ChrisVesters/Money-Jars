import type { JSX } from "react";
import { useState } from "react";
import { useTranslation } from "react-i18next";

import type { Account, Category, Jar } from "@gql/graphql";

import AccountTransferTransactionForm from "./accounttransfer/AccountTransferTransactionForm";
import JarTransferTransactionForm from "./jartransfer/JarTransferTransactionForm";
import PaymentTransactionForm from "./payment/PaymentTransactionForm";
import { TransactionType } from "./transactionTypes";

export type CreateTransactionFormProps = {
	jars: Array<Jar>;
	accounts: Array<Account>;
	categories: Array<Category>;

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
					categories={props.categories}
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
