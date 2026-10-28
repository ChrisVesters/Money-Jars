import type { JSX } from "react";
import { useTranslation } from "react-i18next";

import Overview from "@src/category/Overview";

import Page from "./Page";

const CategoriesPage = (): JSX.Element => {
	const { t } = useTranslation();

	return (
		<Page title={t("categories")}>
			<Overview />
		</Page>
	);
};

export default CategoriesPage;
