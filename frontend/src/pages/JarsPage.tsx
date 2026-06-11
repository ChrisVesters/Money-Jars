import type { JSX } from "react";
import { useTranslation } from "react-i18next";

import Overview from "@src/jar/Overview";

import Page from "./Page";

const JarsPage = (): JSX.Element => {
	const { t } = useTranslation();

	return (
		<Page title={t("jars")}>
			<Overview />
		</Page>
	);
};

export default JarsPage;
