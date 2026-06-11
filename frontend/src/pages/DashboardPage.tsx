import type { JSX } from "react";
import { useTranslation } from "react-i18next";

import Page from "./Page";

const DashboardPage = (): JSX.Element => {
	const { t } = useTranslation();

	return (
		<Page title={t("dashboard")}>
			<p>Welcome to your Money Jars dashboard.</p>
		</Page>
	);
};

export default DashboardPage;
