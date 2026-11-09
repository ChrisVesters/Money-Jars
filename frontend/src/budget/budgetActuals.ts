import { BudgetLineDirection } from "@gql/graphql";

import type {
	BudgetLineItem,
	BudgetRow,
	CategoryTotalItem
} from "./budgetTypes";

const toNumber = (amount: string | undefined): number =>
	amount ? Number.parseFloat(amount) : 0;

const netAmount = (
	direction: BudgetLineDirection,
	total: CategoryTotalItem | undefined
): number => {
	const incoming = toNumber(total?.incoming);
	const outgoing = toNumber(total?.outgoing);

	return direction === BudgetLineDirection.Income
		? incoming - outgoing
		: outgoing - incoming;
};

const toRow = (
	key: string,
	direction: BudgetLineDirection,
	planned: number,
	actual: number,
	category?: BudgetRow["category"],
	line?: BudgetLineItem
): BudgetRow => ({
	key,
	direction,
	category,
	line,
	planned,
	actual,
	remaining: planned - actual,
	isOver: actual > planned
});

const byName = (a: BudgetRow, b: BudgetRow): number =>
	(a.category?.name ?? "").localeCompare(b.category?.name ?? "");

export const toBudgetRows = (
	lines: Array<BudgetLineItem>,
	totals: Array<CategoryTotalItem>
): Array<BudgetRow> => {
	const totalsByCategoryId = new Map(
		totals
			.filter(total => total.category)
			.map(total => [total.category?.id, total])
	);
	const budgetedCategoryIds = new Set(lines.map(line => line.category.id));

	const lineRows = lines
		.map(line =>
			toRow(
				line.id,
				line.direction,
				toNumber(line.amount),
				netAmount(
					line.direction,
					totalsByCategoryId.get(line.category.id)
				),
				line.category,
				line
			)
		)
		.sort(byName);

	const unbudgetedRows = totals
		.filter(
			total =>
				total.category && !budgetedCategoryIds.has(total.category.id)
		)
		.flatMap(total => {
			const net = netAmount(BudgetLineDirection.Income, total);
			if (net === 0) {
				return [];
			}

			const direction =
				net > 0
					? BudgetLineDirection.Income
					: BudgetLineDirection.Expense;
			return [
				toRow(
					`category-${total.category?.id}`,
					direction,
					0,
					Math.abs(net),
					total.category ?? undefined
				)
			];
		})
		.sort(byName);

	const uncategorised = totals.find(total => !total.category);
	const uncategorisedRows = [
		toRow(
			"uncategorised-income",
			BudgetLineDirection.Income,
			0,
			toNumber(uncategorised?.incoming)
		),
		toRow(
			"uncategorised-expense",
			BudgetLineDirection.Expense,
			0,
			toNumber(uncategorised?.outgoing)
		)
	].filter(row => row.actual !== 0);

	return [...lineRows, ...unbudgetedRows, ...uncategorisedRows];
};
