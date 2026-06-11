import type { JSX } from "react";
import { useTranslation } from "react-i18next";

import Page from "./Page";

const SettingsPage = (): JSX.Element => {
	const { t } = useTranslation();

	return (
		<Page title={t("settings")}>
			<p>Configure your preferences and settings.</p>
		</Page>
	);
};

export default SettingsPage;
