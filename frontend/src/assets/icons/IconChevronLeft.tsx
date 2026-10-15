import type { JSX } from "react";

import type { IconProps } from "./Icon";
import Icon from "./Icon";

const IconChevronLeft = (props: Readonly<IconProps>): JSX.Element => {
	return (
		<Icon {...props}>
			<path d="m15 18-6-6 6-6" />
		</Icon>
	);
};

export default IconChevronLeft;
