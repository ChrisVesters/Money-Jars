import type { JSX } from "react";

import type { IconProps } from "./Icon";
import Icon from "./Icon";

export default function IconPanelLeftClose(props: IconProps): JSX.Element {
	return (
		<Icon {...props}>
			<rect width="18" height="18" x="3" y="3" rx="2" />
			<path d="M9 3v18" />
			<path d="m16 15-3-3 3-3" />
		</Icon>
	);
}
