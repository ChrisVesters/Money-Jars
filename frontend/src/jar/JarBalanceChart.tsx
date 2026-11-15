import type { JSX } from "react";
import { useEffect, useRef } from "react";
import { useTranslation } from "react-i18next";

import { useQuery } from "@apollo/client/react";

import type { GetJarBalanceHistoryQuery } from "@gql/graphql";
import { GetJarBalanceHistoryDocument } from "@gql/graphql";

import type { ChartOption } from "@src/common/echarts";
import { echarts } from "@src/common/echarts";
import { Money } from "@src/common/money";

const NO_SNAPSHOTS: GetJarBalanceHistoryQuery["queryJarBalanceHistory"][number]["snapshots"] =
	[];

export type JarBalanceChartProps = {
	jarId: string;
};

const JarBalanceChart = (
	props: Readonly<JarBalanceChartProps>
): JSX.Element => {
	const { t, i18n } = useTranslation();
	const chartRef = useRef<HTMLDivElement>(null);

	const { data } = useQuery(GetJarBalanceHistoryDocument, {
		variables: { req: { jarIds: [props.jarId] } }
	});
	const snapshots =
		data?.queryJarBalanceHistory[0]?.snapshots ?? NO_SNAPSHOTS;

	useEffect(() => {
		const element = chartRef.current;
		if (!element || snapshots.length === 0) {
			return;
		}

		const style = getComputedStyle(element);
		const textColor = style.getPropertyValue("--color-text").trim();
		const lineColor = style.getPropertyValue("--color-accent").trim();
		const backgroundColor = getComputedStyle(
			document.documentElement
		).backgroundColor;
		// Snapshot dates are ISO dates (yyyy-MM-dd), format them in UTC so
		// the month doesn't shift in time zones behind UTC.
		const monthFormat = new Intl.DateTimeFormat(i18n.language, {
			month: "short",
			year: "numeric",
			timeZone: "UTC"
		});

		const option: ChartOption = {
			grid: {
				left: 16,
				right: 16,
				top: 16,
				bottom: 8,
				containLabel: true
			},
			tooltip: {
				trigger: "axis",
				backgroundColor,
				borderColor: lineColor,
				textStyle: { color: textColor },
				formatter: params => {
					const [point] = Array.isArray(params) ? params : [params];
					const snapshot = snapshots[point.dataIndex];
					return `${point.name}<br/><b>${Money.toLabel(snapshot.balance)}</b>`;
				}
			},
			xAxis: {
				type: "category",
				boundaryGap: false,
				data: snapshots.map(snapshot =>
					monthFormat.format(new Date(snapshot.date))
				),
				axisLabel: { color: textColor }
			},
			yAxis: {
				type: "value",
				// Fit the axis to the data instead of starting at 0, so
				// changes in the balance are visible.
				scale: true,
				axisLabel: {
					color: textColor,
					formatter: (value: number) => Money.toLabel(String(value))
				},
				splitLine: { lineStyle: { opacity: 0.2 } }
			},
			series: [
				{
					type: "line",
					name: t("balance"),
					data: snapshots.map(snapshot => Number(snapshot.balance)),
					animation: false,
					symbolSize: 6,
					itemStyle: { color: lineColor },
					lineStyle: { width: 3 },
					areaStyle: {
						color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
							{
								offset: 0,
								color: echarts.color.modifyAlpha(lineColor, 0.4)
							},
							{
								offset: 1,
								color: echarts.color.modifyAlpha(lineColor, 0)
							}
						])
					}
				}
			]
		};

		const chart = echarts.init(element);
		chart.setOption(option);

		const observer = new ResizeObserver(() => chart.resize());
		observer.observe(element);

		return () => {
			observer.disconnect();
			chart.dispose();
		};
	}, [snapshots, i18n.language, t]);

	return (
		<div className="jar-balance-history">
			<h3>{t("balanceHistory")}</h3>
			{data && snapshots.length === 0 ? (
				<p>{t("noBalanceHistory")}</p>
			) : (
				<div ref={chartRef} className="jar-balance-chart" />
			)}
		</div>
	);
};

export default JarBalanceChart;
