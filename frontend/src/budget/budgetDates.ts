const pad = (value: number): string => String(value).padStart(2, "0");

export const toIsoDate = (date: Date): string =>
	`${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())}`;

/** Returns the first and last day of the current month. */
export const currentMonthPeriod = (): { startDate: string; endDate: string } => {
	const today = new Date();

	return {
		startDate: toIsoDate(new Date(today.getFullYear(), today.getMonth(), 1)),
		endDate: toIsoDate(
			new Date(today.getFullYear(), today.getMonth() + 1, 0)
		)
	};
};

export const isCurrent = (
	budget: { startDate: string; endDate: string },
	today: string
): boolean => budget.startDate <= today && today <= budget.endDate;
