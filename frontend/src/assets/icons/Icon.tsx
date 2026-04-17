import type { JSX } from "react";

export type IconProps = {
	size?: string;
	className?: string;
};

export type IconRenderProps = IconProps & {
	children: React.ReactNode;
};

export default function Icon(props: IconRenderProps): JSX.Element {
	return (
		<svg
			xmlns="http://www.w3.org/2000/svg"
			width={props.size ?? "1.5rem"}
			height={props.size ?? "1.5rem"}
			viewBox="0 0 24 24"
			fill="none"
			stroke="currentColor"
			stroke-width="2"
			stroke-linecap="round"
			stroke-linejoin="round"
			className={props.className}>
			{props.children}
		</svg>
	);
}
