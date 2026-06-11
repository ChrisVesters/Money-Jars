import type { JSX } from "react";

export type PageProps = {
	title: string;
	children: JSX.Element;
};

const Page = (props: PageProps): JSX.Element => {
	return (
		<div>
			<h1>{props.title}</h1>
			{props.children}
		</div>
	);
};

export default Page;
