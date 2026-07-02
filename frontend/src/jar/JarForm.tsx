import type { JSX } from "react";
import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";

import type { Jar, UpdateJar } from "@gql/graphql";

export type JarFormProps = {
	jar?: Jar;
	onClose: () => void;
	onConfirm: (jar: UpdateJar) => void;
};

const JarForm = (props: Readonly<JarFormProps>): JSX.Element => {
	const { t } = useTranslation();

	const [name, setName] = useState(props.jar?.name ?? "");
	const [description, setDescription] = useState(
		props.jar?.description ?? ""
	);

	useEffect(() => {
		setName(props.jar?.name ?? "");
		setDescription(props.jar?.description ?? "");
	}, [props.jar]);

	// const [error, setError] = useState<string | null>(null);

	// TODO: progress indicator
	const handleSubmit = async (e: React.FormEvent) => {
		e.preventDefault();

		// TODO: validation!
		// setError(null);

		// if (!name.trim()) {
		// 	setError("Jar name is required");
		// 	return;
		// }

		props.onConfirm({
			name,
			description
		});
	};

	return (
		<form onSubmit={handleSubmit}>
			<div className="form-group">
				<label htmlFor="name">{t("name")}</label>
				<input
					id="name"
					type="text"
					value={name}
					onChange={e => setName(e.target.value)}
				/>
			</div>

			<div className="form-group">
				<label htmlFor="description">{t("description")}</label>
				<textarea
					id="description"
					value={description}
					onChange={e => setDescription(e.target.value)}
					rows={3}
				/>
			</div>

			<div className="form-actions">
				<button type="button" onClick={props.onClose}>
					{t("cancel")}
				</button>
				<button type="submit">
					{props.jar ? t("update") : t("create")}
				</button>
			</div>
		</form>
	);
};

export default JarForm;
