import type { JSX } from "react";

import type { IconProps } from "./Icon";
import Icon from "./Icon";

const IconChevronRight = (props: Readonly<IconProps>): JSX.Element => {
	return (
		<Icon {...props}>
			<path d="m9 18 6-6-6-6" />
		</Icon>
	);
};

export default IconChevronRight;
