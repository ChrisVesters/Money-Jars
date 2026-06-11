import type { JSX } from "react";

import type { IconProps } from "./Icon";
import Icon from "./Icon";

const IconTransaction = (props: Readonly<IconProps>): JSX.Element => {
	return (
		<Icon {...props}>
			<path d="M3.85 8.62a4 4 0 0 1 4.78-4.77 4 4 0 0 1 6.74 0 4 4 0 0 1 4.78 4.78 4 4 0 0 1 0 6.74 4 4 0 0 1-4.77 4.78 4 4 0 0 1-6.75 0 4 4 0 0 1-4.78-4.77 4 4 0 0 1 0-6.76Z" />
			<path d="M7 12h5" />
			<path d="M15 9.4a4 4 0 1 0 0 5.2" />
		</Icon>
	);
};

export default IconTransaction;
