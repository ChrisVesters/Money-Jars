import type { JSX } from "react";
import { useEffect, useState } from "react";

import type { Jar, UpdateJar } from "@gql/graphql";

export type JarFormProps = {
	jar?: Jar;
	onSave: (jar: UpdateJar) => void;
	onClose: () => void;
};

export default function JarForm(props: JarFormProps): JSX.Element {
	const [name, setName] = useState(props.jar?.name ?? "");
	const [description, setDescription] = useState(
		props.jar?.description ?? ""
	);

	useEffect(() => {
		if (props.jar) {
			setName(props.jar.name);
			setDescription(props.jar.description);
		} else {
			setName("");
			setDescription("");
		}
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

		props.onSave({
			name,
			description
		});
	};

	return (
		<form onSubmit={handleSubmit} className="jar-form">
			<div className="form-group">
				<label htmlFor="name">Name</label>
				<input
					id="name"
					type="text"
					value={name}
					onChange={e => setName(e.target.value)}
				/>
			</div>

			<div className="form-group">
				<label htmlFor="description">Description</label>
				<textarea
					id="description"
					value={description}
					onChange={e => setDescription(e.target.value)}
					rows={3}
				/>
			</div>

			<div
				style={{
					display: "flex",
					justifyContent: "center",
					gap: "1rem"
				}}>
				<button type="button" onClick={props.onClose}>
					Cancel
				</button>
				<button type="submit">{props.jar ? "Update" : "Create"}</button>
			</div>
		</form>
	);
}
