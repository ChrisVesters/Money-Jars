import { useState, type JSX } from "react";

import { useMutation, useQuery } from "@apollo/client/react";

import IconPlus from "@assets/icons/IconPlus";

import {
	CreateJarDocument,
	GetJarsDocument,
	UpdateJarDocument,
	type Jar,
	type UpdateJar
} from "@gql/graphql";
import JarView from "./JarView";

import Modal from "../common/Modal";
import "./jar.css";
import JarForm from "./JarForm";

export default function Overview(): JSX.Element {
	const { data } = useQuery(GetJarsDocument);

	const [createJar] = useMutation(CreateJarDocument);
	const [updateJar] = useMutation(UpdateJarDocument);

	const [JarFormVisible, setJarFormVisible] = useState<boolean>(false);
	const [selectedJar, setSelectedJar] = useState<Jar | undefined>(undefined);

	function openCreateJarForm(): void {
		setSelectedJar(undefined);
		setJarFormVisible(true);
	}

	function openEditJarForm(jar: Jar): void {
		setSelectedJar(jar);
		setJarFormVisible(true);
	}

	// TODO: saveJar
	function closeJarForm(): void {
		setJarFormVisible(false);
		setSelectedJar(undefined);
	}

	async function submitJarForm(jar: UpdateJar): Promise<void> {
		try {
			if (selectedJar) {
				const result = await updateJar({
					variables: {
						id: selectedJar.id,
						req: jar
					},
					refetchQueries: [{ query: GetJarsDocument }]
				});
				console.log("Update jar result:", result);
			} else {
				const result = await createJar({
					variables: {
						req: jar
					},
					refetchQueries: [{ query: GetJarsDocument }]
				});
				console.log("Create jar result:", result);
			}
		} catch (error) {
			console.error("Error creating jar:", error);
		}

		closeJarForm();
	}

	return (
		<div>
			<div className="jar-grid">
				{data?.getJars.map((jar: Jar) => (
					<div key={jar.id} className="jar-card">
						<JarView jar={jar} onEdit={openEditJarForm} />
					</div>
				))}
				<div className="jar-card">
					<button className="icon-button" onClick={openCreateJarForm}>
						<IconPlus />
					</button>
				</div>
			</div>

			<Modal
				isOpen={JarFormVisible}
				onClose={closeJarForm}
				hasCloseBtn={true}>
				<JarForm
					jar={selectedJar}
					onSave={submitJarForm}
					onClose={closeJarForm}
				/>
			</Modal>
		</div>
	);
}
