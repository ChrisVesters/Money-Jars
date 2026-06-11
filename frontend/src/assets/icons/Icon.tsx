import type { JSX } from "react";

export type IconProps = {
	size?: string;
	className?: string;
};

export type IconRenderProps = IconProps & {
	children: React.ReactNode;
};

const Icon = (props: Readonly<IconRenderProps>): JSX.Element => {
	return (
		<svg
			xmlns="http://www.w3.org/2000/svg"
			width={props.size ?? "1.5rem"}
			height={props.size ?? "1.5rem"}
			viewBox="0 0 24 24"
			fill="none"
			stroke="currentColor"
			strokeWidth="2"
			strokeLinecap="round"
			strokeLinejoin="round"
			className={props.className}>
			{props.children}
		</svg>
	);
};

export default Icon;
