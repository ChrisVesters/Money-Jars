import type { JSX } from "react";
import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";

import type { Account, UpdateAccount } from "@gql/graphql";

export type AccountFormProps = {
	account?: Account;
	onClose: () => void;
	onConfirm: (account: UpdateAccount) => void;
};

const AccountForm = (props: Readonly<AccountFormProps>): JSX.Element => {
	const { t } = useTranslation();

	const [name, setName] = useState(props.account?.name ?? "");
	const [description, setDescription] = useState(
		props.account?.description ?? ""
	);

	useEffect(() => {
		setName(props.account?.name ?? "");
		setDescription(props.account?.description ?? "");
	}, [props.account]);

	const handleSubmit = async (event: React.FormEvent): Promise<void> => {
		event.preventDefault();

		if (!name.trim()) {
			return;
		}

		props.onConfirm({ name: name.trim(), description: description.trim() });
	};

	return (
		<form onSubmit={handleSubmit}>
			<div className="form-group">
				<label htmlFor="account-name">{t("name")}</label>
				<input
					id="account-name"
					type="text"
					value={name}
					onChange={event => setName(event.target.value)}
				/>
			</div>
			<div className="form-group">
				<label htmlFor="account-description">{t("description")}</label>
				<input
					id="account-description"
					type="text"
					value={description}
					onChange={event => setDescription(event.target.value)}
				/>
			</div>

			<div className="form-actions">
				<button type="button" onClick={props.onClose}>
					{t("cancel")}
				</button>
				<button type="submit">
					{props.account ? t("update") : t("create")}
				</button>
			</div>
		</form>
	);
};

export default AccountForm;
