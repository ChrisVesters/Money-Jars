import type { JSX } from "react";

import { useQuery } from "@apollo/client/react";

import { GetJarBalanceHistoryDocument } from "@gql/graphql";

import BalanceChart from "@src/common/BalanceChart";

export type JarBalanceChartProps = {
	jarId: string;
};

const JarBalanceChart = (
	props: Readonly<JarBalanceChartProps>
): JSX.Element => {
	const { data } = useQuery(GetJarBalanceHistoryDocument, {
		variables: { req: { jarIds: [props.jarId] } }
	});

	return (
		<BalanceChart snapshots={data?.queryJarBalanceHistory[0]?.snapshots} />
	);
};

export default JarBalanceChart;
