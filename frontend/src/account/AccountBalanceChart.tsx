import type { JSX } from "react";

import { useQuery } from "@apollo/client/react";

import { GetAccountBalanceHistoryDocument } from "@gql/graphql";

import BalanceChart from "@src/common/BalanceChart";

export type AccountBalanceChartProps = {
	accountId: string;
};

const AccountBalanceChart = (
	props: Readonly<AccountBalanceChartProps>
): JSX.Element => {
	const { data } = useQuery(GetAccountBalanceHistoryDocument, {
		variables: { req: { accountIds: [props.accountId] } }
	});

	return (
		<BalanceChart
			snapshots={data?.queryAccountBalanceHistory[0]?.snapshots}
		/>
	);
};

export default AccountBalanceChart;
