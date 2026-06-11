import type { JSX } from "react";
import { useTranslation } from "react-i18next";

import Page from "./Page";

const ReportsPage = (): JSX.Element => {
	const { t } = useTranslation();

	return (
		<Page title={t("reports")}>
			<p>View your financial reports and analytics.</p>
		</Page>
	);
};

export default ReportsPage;
