import type { JSX } from "react";
import { useTranslation } from "react-i18next";

import Overview from "@src/budget/Overview";

import Page from "./Page";

const BudgetsPage = (): JSX.Element => {
	const { t } = useTranslation();

	return (
		<Page title={t("budgets")}>
			<Overview />
		</Page>
	);
};

export default BudgetsPage;
