import type { JSX } from "react";
import { useTranslation } from "react-i18next";

import "./transaction.css";

export type TransactionFilterValue = {
	from: string;
	to: string;
};

export type TransactionFilterProps = {
	filter: TransactionFilterValue;
	onChange: (filter: TransactionFilterValue) => void;
};

const TransactionFilter = (
	props: Readonly<TransactionFilterProps>
): JSX.Element => {
	const { t } = useTranslation();

	const { from, to } = props.filter;

	return (
		<div className="transactions-filters">
			<label className="transactions-filter">
				{t("from")}
				<input
					type="date"
					value={from}
					max={to || undefined}
					onChange={e =>
						props.onChange({
							...props.filter,
							from: e.target.value
						})
					}
				/>
			</label>
			<label className="transactions-filter">
				{t("to")}
				<input
					type="date"
					value={to}
					min={from || undefined}
					onChange={e =>
						props.onChange({ ...props.filter, to: e.target.value })
					}
				/>
			</label>
			{(from || to) && (
				<button
					className="text-button"
					onClick={() => props.onChange({ from: "", to: "" })}>
					{t("clearFilter")}
				</button>
			)}
		</div>
	);
};

export default TransactionFilter;
