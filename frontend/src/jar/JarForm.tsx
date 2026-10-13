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
	const [goal, setGoal] = useState(props.jar?.goal ?? "");

	useEffect(() => {
		setName(props.jar?.name ?? "");
		setDescription(props.jar?.description ?? "");
		setGoal(props.jar?.goal ?? "");
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
		const goalValue = goal.trim() === "" ? null : goal;

		props.onConfirm({
			name,
			description,
			goal: goalValue
		});

		clear();
	};

	const handleCancel = (): void => {
		props.onClose();
		clear();
	}

	const clear = (): void => {
		setName("");
		setDescription("");
		setGoal("");
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

			<div className="form-group">
				<label htmlFor="goal">{t("goal")}</label>
				<input
					id="goal"
					type="number"
					step="0.01"
					value={goal}
					onChange={e => setGoal(e.target.value)}
				/>
			</div>

			<div className="form-actions">
				<button type="button" onClick={handleCancel}>
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
