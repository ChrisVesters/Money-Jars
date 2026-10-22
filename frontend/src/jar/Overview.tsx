import type { JSX } from "react";
import { useMemo, useState } from "react";
import { useTranslation } from "react-i18next";

import { useMutation, useQuery } from "@apollo/client/react";

import IconIncoming from "@assets/icons/IconIncoming";
import IconOutgoing from "@assets/icons/IconOutgoing";
import IconPlus from "@assets/icons/IconPlus";

import {
	CreateJarDocument,
	DeleteJarDocument,
	Direction,
	GetJarsDocument,
	UpdateJarDocument,
	type UpdateJar
} from "@gql/graphql";
import JarView from "./JarView";

import Modal from "@src/common/Modal";
import { Money } from "@src/common/money";
import { TransactionType } from "@src/transaction/transactionTypes";

import "./jar.css";
import JarForm from "./JarForm";
import type { JarItem } from "./jarTypes";

const Overview = (): JSX.Element => {
	const { t } = useTranslation();

	const { data } = useQuery(GetJarsDocument);

	const [createJar] = useMutation(CreateJarDocument);
	const [updateJar] = useMutation(UpdateJarDocument);
	const [deleteJar] = useMutation(DeleteJarDocument);

	const [JarFormVisible, setJarFormVisible] = useState(false);
	const [selectedJar, setSelectedJar] = useState<JarItem | undefined>(
		undefined
	);

	// TODO: Can't we get rid of this?
	const [selectedCardId, setSelectedCardId] = useState<string | undefined>(
		undefined
	);

	const jars = useMemo(
		() =>
			[...(data?.getJars ?? [])].sort((a, b) =>
				a.name.localeCompare(b.name)
			),
		[data?.getJars]
	);

	const openCreateJarForm = (): void => {
		setSelectedJar(undefined);
		setSelectedCardId(undefined);
		setJarFormVisible(true);
	};

	const openEditJarForm = (jar: JarItem): void => {
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
				{jars.map(jar => (
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

			<table className="transactions-table">
				<thead>
					<tr>
						<th>{t("date")}</th>
						<th>{t("amount")}</th>
						<th>{t("direction")}</th>
						<th>{t("counterparty")}</th>
						<th>{t("description")}</th>
					</tr>
				</thead>
				<tbody>
					{jars
						.find(jar => jar.id === selectedCardId)
						?.transactions.map(transaction => (
							<tr
								key={transaction.id}
								// className={`${selectedTransaction?.id === transaction.id ? "selected" : ""}`}
								// onClick={() => selectTransaction(transaction)}
							>
								<td>{transaction.date}</td>
								<td>{Money.toLabel(transaction.amount)}</td>
								{transaction.__typename ===
								TransactionType.Payment ? (
									<>
										<td>
											{transaction.direction ===
											Direction.Outgoing ? (
												<IconOutgoing />
											) : (
												<IconIncoming />
											)}
										</td>
										<td>{transaction.counterparty}</td>
									</>
								) : (
									<>
										<td>
											{transaction.sourceJar.id ===
											selectedCardId ? (
												<IconOutgoing />
											) : (
												<IconIncoming />
											)}
										</td>
										<td>
											{transaction.sourceJar.id ===
											selectedCardId
												? transaction.targetJar.name
												: transaction.sourceJar.name}
										</td>
									</>
								)}
								<td>{transaction.description}</td>
							</tr>
						))}
				</tbody>
			</table>

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
