import type { JSX } from "react";

import type { IconProps } from "./Icon";
import Icon from "./Icon";

const IconPlus = (props: Readonly<IconProps>): JSX.Element => {
	return (
		<Icon {...props}>
			<path d="M5 12h14" />
			<path d="M12 5v14" />
		</Icon>
	);
};

export default IconPlus;
