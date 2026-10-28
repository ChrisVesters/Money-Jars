import type { JSX } from "react";
import { useMemo, useState } from "react";
import { useTranslation } from "react-i18next";

import { useMutation, useQuery } from "@apollo/client/react";

import IconDelete from "@assets/icons/IconDelete";
import IconEdit from "@assets/icons/IconEdit";
import IconPlus from "@assets/icons/IconPlus";

import type { UpdateCategory } from "@gql/graphql";
import {
	CreateCategoryDocument,
	DeleteCategoryDocument,
	GetCategoriesDocument,
	UpdateCategoryDocument
} from "@gql/graphql";

import Modal from "@src/common/Modal";

import "../transaction/transaction.css";
import CategoryForm from "./CategoryForm";
import type { CategoryItem } from "./categoryTypes";

const Overview = (): JSX.Element => {
	const { t } = useTranslation();
	const { data } = useQuery(GetCategoriesDocument);

	const [createCategory] = useMutation(CreateCategoryDocument);
	const [updateCategory] = useMutation(UpdateCategoryDocument);
	const [deleteCategory] = useMutation(DeleteCategoryDocument);

	const [categoryFormVisible, setCategoryFormVisible] = useState(false);
	const [selectedCategory, setSelectedCategory] = useState<
		CategoryItem | undefined
	>(undefined);

	const categories = useMemo(
		() =>
			[...(data?.getCategorys ?? [])].sort((a, b) =>
				a.name.localeCompare(b.name)
			),
		[data?.getCategorys]
	);

	const handleSelectCategory = (category: CategoryItem): void => {
		if (selectedCategory?.id === category.id) {
			setSelectedCategory(undefined);
		} else {
			setSelectedCategory(category);
		}
	};

	const openCreateCategoryForm = (): void => {
		setSelectedCategory(undefined);
		setCategoryFormVisible(true);
	};

	const openEditCategoryForm = (): void => {
		setCategoryFormVisible(true);
	};

	const closeCategoryForm = (): void => {
		setCategoryFormVisible(false);
		setSelectedCategory(undefined);
	};

	const handleSubmitCategory = async (
		category: UpdateCategory
	): Promise<void> => {
		try {
			if (selectedCategory) {
				await updateCategory({
					variables: {
						id: selectedCategory.id,
						req: category
					},
					refetchQueries: [{ query: GetCategoriesDocument }]
				});
			} else {
				await createCategory({
					variables: {
						req: category
					},
					refetchQueries: [{ query: GetCategoriesDocument }]
				});
			}
		} catch (error) {
			console.info(t("errorCreateCategory"), error);
		}

		closeCategoryForm();
	};

	const handleDeleteCategory = async (): Promise<void> => {
		if (
			!selectedCategory ||
			!globalThis.confirm(t("confirmDeleteCategory"))
		) {
			return;
		}

		try {
			await deleteCategory({
				variables: {
					id: selectedCategory.id
				},
				refetchQueries: [{ query: GetCategoriesDocument }]
			});
		} catch (error) {
			console.info(t("errorDeleteCategory"), error);
		}

		setSelectedCategory(undefined);
	};

	return (
		<div className="transactions-overview">
			<div className="transactions-actions">
				{selectedCategory ? (
					<>
						<button
							className="icon-button"
							onClick={openEditCategoryForm}
							title={t("editCategory")}>
							<IconEdit />
						</button>
						<button
							className="icon-button"
							onClick={handleDeleteCategory}
							title={t("deleteCategory")}>
							<IconDelete />
						</button>
					</>
				) : (
					<button
						className="icon-button"
						onClick={openCreateCategoryForm}
						title={t("createCategory")}>
						<IconPlus />
					</button>
				)}
			</div>

			<table className="transactions-table">
				<thead>
					<tr>
						<th>{t("name")}</th>
						<th>{t("description")}</th>
					</tr>
				</thead>
				<tbody>
					{categories.map(category => (
						<tr
							key={category.id}
							className={
								selectedCategory?.id === category.id
									? "selected"
									: ""
							}
							onClick={() => handleSelectCategory(category)}>
							<td>{category.name}</td>
							<td>{category.description}</td>
						</tr>
					))}
				</tbody>
			</table>

			<Modal isOpen={categoryFormVisible} onClose={closeCategoryForm}>
				<CategoryForm
					category={selectedCategory}
					onClose={closeCategoryForm}
					onConfirm={handleSubmitCategory}
				/>
			</Modal>
		</div>
	);
};

export default Overview;
