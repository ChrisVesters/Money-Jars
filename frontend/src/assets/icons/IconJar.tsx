import type { JSX } from "react";

import type { IconProps } from "./Icon";
import Icon from "./Icon";

export default function IconJar(props: IconProps): JSX.Element {
	return (
		<Icon {...props}>
			<path d="M4.5 3h15" />
			<path d="M6 3v16a2 2 0 0 0 2 2h8a2 2 0 0 0 2-2V3" />
			<path d="M6 14h12" />
		</Icon>
	);
}
