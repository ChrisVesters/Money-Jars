import type { JSX } from "react";
import { useTranslation } from "react-i18next";

import Page from "./Page";

const SettingsPage = (): JSX.Element => {
	const { t } = useTranslation();

	return (
		<Page title={t("settings")}>
			<p>{t("settingsIntro")}</p>
		</Page>
	);
};

export default SettingsPage;
