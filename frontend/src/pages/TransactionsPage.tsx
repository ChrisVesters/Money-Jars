import type { JSX } from "react";
import { useTranslation } from "react-i18next";

import TransactionsOverview from "@src/transaction/TransactionsOverview";

import Page from "./Page";

const TransactionsPage = (): JSX.Element => {
	const { t } = useTranslation();

	return (
		<Page title={t("transactions")}>
			<TransactionsOverview />
		</Page>
	);
};

export default TransactionsPage;
