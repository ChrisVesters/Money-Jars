import type { JSX } from "react";
import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";

import type { UpdateCategory } from "@gql/graphql";

import type { CategoryItem } from "./categoryTypes";

export type CategoryFormProps = {
	category?: CategoryItem;
	onClose: () => void;
	onConfirm: (category: UpdateCategory) => void;
};

const CategoryForm = (props: Readonly<CategoryFormProps>): JSX.Element => {
	const { t } = useTranslation();

	const [name, setName] = useState(props.category?.name ?? "");
	const [description, setDescription] = useState(
		props.category?.description ?? ""
	);

	useEffect(() => {
		setName(props.category?.name ?? "");
		setDescription(props.category?.description ?? "");
	}, [props.category]);

	const handleSubmit = async (event: React.FormEvent): Promise<void> => {
		event.preventDefault();

		if (!name.trim()) {
			return;
		}

		props.onConfirm({ name: name.trim(), description: description.trim() });
		clear();
	};

	const handleCancel = (): void => {
		props.onClose();
		clear();
	};

	const clear = (): void => {
		setName("");
		setDescription("");
	};

	return (
		<form onSubmit={handleSubmit}>
			<div className="form-group">
				<label htmlFor="category-name">{t("name")}</label>
				<input
					id="category-name"
					type="text"
					value={name}
					onChange={event => setName(event.target.value)}
				/>
			</div>
			<div className="form-group">
				<label htmlFor="category-description">
					{t("description")}
				</label>
				<textarea
					id="category-description"
					value={description}
					onChange={event => setDescription(event.target.value)}
					rows={3}
				/>
			</div>

			<div className="form-actions">
				<button type="button" onClick={handleCancel}>
					{t("cancel")}
				</button>
				<button type="submit">
					{props.category ? t("update") : t("create")}
				</button>
			</div>
		</form>
	);
};

export default CategoryForm;
