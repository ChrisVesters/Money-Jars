import { useState, type JSX } from "react";

import { useMutation, useQuery } from "@apollo/client/react";

import IconPlus from "@assets/icons/IconPlus";

import {
	CreateJarDocument,
	DeleteJarDocument,
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
	const [deleteJar] = useMutation(DeleteJarDocument);

	const [JarFormVisible, setJarFormVisible] = useState<boolean>(false);
	const [selectedJar, setSelectedJar] = useState<Jar | undefined>(undefined);
	const [selectedCardId, setSelectedCardId] = useState<string | undefined>(
		undefined
	);

	function openCreateJarForm(): void {
		setSelectedJar(undefined);
		setSelectedCardId(undefined);
		setJarFormVisible(true);
	}

	function openEditJarForm(jar: Jar): void {
		setSelectedJar(jar);
		setJarFormVisible(true);
	}

	function handleCardSelect(jarId: string | undefined): void {
		if (selectedCardId === jarId) {
			setSelectedCardId(undefined);
		} else {
			setSelectedCardId(jarId);
		}
	}

	function closeJarForm(): void {
		setJarFormVisible(false);
		setSelectedJar(undefined);
		setSelectedCardId(undefined);
	}

	async function handleSubmitJar(jar: UpdateJar): Promise<void> {
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

	async function handleDeleteJar(): Promise<void> {
		try {
			if (!selectedJar) {
				return;
			}

			const result = await deleteJar({
				variables: {
					id: selectedJar.id
				},
				refetchQueries: [{ query: GetJarsDocument }]
			});
			console.log("Delete jar result:", result);
		} catch (error) {
			console.error("Error deleting jar:", error);
		}

		closeJarForm();
	}

	return (
		<div>
			<div className="jar-grid">
				{data?.getJars.map((jar: Jar) => (
					<JarView
						key={jar.id}
						jar={jar}
						onEdit={openEditJarForm}
						isSelected={selectedCardId === jar.id}
						onSelect={() => handleCardSelect(jar.id)}
					/>
				))}
				<div
					className="jar-card jar-add-card"
					onClick={openCreateJarForm}>
					<IconPlus />
				</div>
			</div>

			<Modal isOpen={JarFormVisible} onClose={closeJarForm}>
				<JarForm
					jar={selectedJar}
					onClose={closeJarForm}
					onSave={handleSubmitJar}
					onDelete={handleDeleteJar}
				/>
			</Modal>
		</div>
	);
}
