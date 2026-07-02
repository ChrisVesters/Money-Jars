import type { JSX } from "react";
import { useTranslation } from "react-i18next";

import type { Jar } from "@gql/graphql";

import IconEdit from "@assets/icons/IconEdit";
import IconDelete from "@assets/icons/IconDelete";

export type JarViewProps = {
	jar: Jar;
	isSelected: boolean;
	onSelect: () => void;
	onEdit: (jar: Jar) => void;
	onDelete: () => void;
};

const JarView = (props: Readonly<JarViewProps>): JSX.Element => {
	const { t } = useTranslation();

	const handleEdit = (e: React.MouseEvent<HTMLButtonElement>) => {
		e.stopPropagation();

		props.onEdit(props.jar);
	};

	const handleDelete = (e: React.MouseEvent<HTMLButtonElement>) => {
		e.stopPropagation();

		props.onDelete();
	};

	return (
		<div
			className={`jar-card ${props.isSelected ? "selected" : ""}`}
			onClick={props.onSelect}>
			<div className="card-content">
				<div className="card-title">
					<div>{props.jar.name}</div>
				</div>
				<div className="card-description">
					<div>{props.jar.description}</div>
				</div>
				<div className="card-footer">
					<div>
						{t("balance")}: {props.jar.balance}
					</div>
				</div>
			</div>

			{props.isSelected && (
				<div className="card-actions">
					<button className="icon-button" onClick={handleEdit}>
						<IconEdit size="1rem" />
					</button>
					<button
						className="icon-button danger"
						onClick={handleDelete}>
						<IconDelete size="1rem" />
					</button>
				</div>
			)}
		</div>
	);
};

export default JarView;
