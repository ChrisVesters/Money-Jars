import type { ComposeOption } from "echarts/core";
import * as echarts from "echarts/core";
import type { LineSeriesOption } from "echarts/charts";
import { LineChart } from "echarts/charts";
import type {
	DataZoomComponentOption,
	GridComponentOption,
	LegendComponentOption,
	TooltipComponentOption,
} from "echarts/components";
import {
	DataZoomComponent,
	GridComponent,
	LegendComponent,
	TooltipComponent,
} from "echarts/components";
import { SVGRenderer } from "echarts/renderers";

// Register only the parts we use, so the bundle stays small.
// Add new chart types (e.g. BarChart, PieChart) and their option types here.
echarts.use([
	LineChart,
	GridComponent,
	TooltipComponent,
	LegendComponent,
	DataZoomComponent,
	SVGRenderer,
]);

export type ChartOption = ComposeOption<
	| LineSeriesOption
	| GridComponentOption
	| TooltipComponentOption
	| LegendComponentOption
	| DataZoomComponentOption
>;

export { echarts };
