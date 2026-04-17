import type { JSX } from "react";

import type { Jar } from "@gql/graphql";

import IconEdit from "@assets/icons/IconEdit";

export type JarViewProps = {
	jar: Jar;
	onEdit: (jar: Jar) => void;
};

export default function JarView(props: JarViewProps): JSX.Element {
	return (
		<div>
			<h3>{props.jar.name}</h3>
			<p>{props.jar.description}</p>
			<p>Balance: {props.jar.balance}</p>

			<button className="icon-button" onClick={() => props.onEdit(props.jar)}>
				<IconEdit />
			</button>
		</div>
	);
}
