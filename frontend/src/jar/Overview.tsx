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

const Overview = (): JSX.Element => {
	const { data } = useQuery(GetJarsDocument);

	const [createJar] = useMutation(CreateJarDocument);
	const [updateJar] = useMutation(UpdateJarDocument);
	const [deleteJar] = useMutation(DeleteJarDocument);

	const [JarFormVisible, setJarFormVisible] = useState(false);
	const [selectedJar, setSelectedJar] = useState<Jar | undefined>(undefined);

	// TODO: Can't we get rid of this?
	const [selectedCardId, setSelectedCardId] = useState<string | undefined>(
		undefined
	);

	const openCreateJarForm = (): void => {
		setSelectedJar(undefined);
		setSelectedCardId(undefined);
		setJarFormVisible(true);
	};

	const openEditJarForm = (jar: Jar): void => {
		setSelectedJar(jar);
		setJarFormVisible(true);
	};

	const handleSelectJar = (jarId: string | undefined): void => {
		if (selectedCardId === jarId) {
			setSelectedCardId(undefined);
		} else {
			setSelectedCardId(jarId);
		}
	};

	const closeJarForm = (): void => {
		setJarFormVisible(false);
		setSelectedJar(undefined);
		setSelectedCardId(undefined);
	};

	const handleSubmitJar = async (jar: UpdateJar): Promise<void> => {
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
	};

	const handleDeleteJar = async (jarId: string): Promise<void> => {
		if (!globalThis.confirm("Are you sure you want to delete this jar?")) {
			return;
		}

		try {
			const result = await deleteJar({
				variables: {
					id: jarId
				},
				refetchQueries: [{ query: GetJarsDocument }]
			});
			console.log("Delete jar result:", result);
		} catch (error) {
			console.error("Error deleting jar:", error);
		}

		closeJarForm();
	};

	return (
		<>
			<div className="jar-grid">
				{data?.getJars.map((jar: Jar) => (
					<JarView
						key={jar.id}
						jar={jar}
						onEdit={openEditJarForm}
						isSelected={selectedCardId === jar.id}
						onSelect={() => handleSelectJar(jar.id)}
						onDelete={() => handleDeleteJar(jar.id)}
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
					onConfirm={handleSubmitJar}
				/>
			</Modal>
		</>
	);
};

export default Overview;
