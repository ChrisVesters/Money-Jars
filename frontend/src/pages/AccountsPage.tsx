import type { JSX } from "react";
import { useTranslation } from "react-i18next";

import Overview from "@src/account/Overview";

import Page from "./Page";

const AccountsPage = (): JSX.Element => {
	const { t } = useTranslation();

	return (
		<Page title={t("accounts")}>
			<Overview />
		</Page>
	);
};

export default AccountsPage;
