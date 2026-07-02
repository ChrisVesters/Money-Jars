import type { JSX } from "react";

import Icon, { type IconProps } from "./Icon";

const IconAccount = (props: Readonly<IconProps>): JSX.Element => {
	return (
		<Icon {...props}>
			<rect width="20" height="14" x="2" y="5" rx="2" />
			<line x1="2" x2="22" y1="10" y2="10" />
		</Icon>
	);
};

export default IconAccount;
