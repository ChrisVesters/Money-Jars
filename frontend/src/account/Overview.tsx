import { useState, type JSX } from "react";

import { useTranslation } from "react-i18next";
import { useMutation, useQuery } from "@apollo/client/react";

import IconPlus from "@assets/icons/IconPlus";
import {
	CreateAccountDocument,
	DeleteAccountDocument,
	GetAccountsDocument,
	UpdateAccountDocument,
	type Account,
	type UpdateAccount
} from "@gql/graphql";

import Modal from "../common/Modal";
import AccountForm from "./AccountForm";
import AccountView from "./AccountView";
import "../jar/jar.css";

const Overview = (): JSX.Element => {
	const { t } = useTranslation();
	const { data } = useQuery(GetAccountsDocument);

	const [createAccount] = useMutation(CreateAccountDocument);
	const [updateAccount] = useMutation(UpdateAccountDocument);
	const [deleteAccount] = useMutation(DeleteAccountDocument);

	const [accountFormVisible, setAccountFormVisible] = useState(false);
	const [selectedAccount, setSelectedAccount] = useState<Account | undefined>(
		undefined
	);
	const [selectedCardId, setSelectedCardId] = useState<string | undefined>(
		undefined
	);

	const openCreateAccountForm = (): void => {
		setSelectedAccount(undefined);
		setSelectedCardId(undefined);
		setAccountFormVisible(true);
	};

	const openEditAccountForm = (account: Account): void => {
		setSelectedAccount(account);
		setAccountFormVisible(true);
	};

	const handleSelectAccount = (accountId: string | undefined): void => {
		if (selectedCardId === accountId) {
			setSelectedCardId(undefined);
		} else {
			setSelectedCardId(accountId);
		}
	};

	const closeAccountForm = (): void => {
		setAccountFormVisible(false);
		setSelectedAccount(undefined);
		setSelectedCardId(undefined);
	};

	const handleSubmitAccount = async (
		account: UpdateAccount
	): Promise<void> => {
		try {
			if (selectedAccount) {
				await updateAccount({
					variables: {
						id: selectedAccount.id,
						req: account
					},
					refetchQueries: [{ query: GetAccountsDocument }]
				});
			} else {
				await createAccount({
					variables: {
						req: account
					},
					refetchQueries: [{ query: GetAccountsDocument }]
				});
			}
		} catch (error) {
			console.info(t("errorCreateAccount"), error);
		}

		closeAccountForm();
	};

	const handleDeleteAccount = async (accountId: string): Promise<void> => {
		if (!globalThis.confirm(t("confirmDeleteAccount"))) {
			return;
		}

		try {
			await deleteAccount({
				variables: {
					id: accountId
				},
				refetchQueries: [{ query: GetAccountsDocument }]
			});
		} catch (error) {
			console.info(t("errorDeleteAccount"), error);
		}

		closeAccountForm();
	};

	return (
		<>
			<div className="jar-grid">
				{data?.getAccounts.map(account => (
					<AccountView
						key={account.id}
						account={account}
						onEdit={openEditAccountForm}
						isSelected={selectedCardId === account.id}
						onSelect={() => handleSelectAccount(account.id)}
						onDelete={() => handleDeleteAccount(account.id)}
					/>
				))}
				<button
					type="button"
					className="jar-card jar-add-card"
					onClick={openCreateAccountForm}>
					<IconPlus />
				</button>
			</div>

			<Modal isOpen={accountFormVisible} onClose={closeAccountForm}>
				<AccountForm
					account={selectedAccount}
					onClose={closeAccountForm}
					onConfirm={handleSubmitAccount}
				/>
			</Modal>
		</>
	);
};

export default Overview;
