import type { JSX } from "react";
import { useTranslation } from "react-i18next";

import IconChevronLeft from "@assets/icons/IconChevronLeft";
import IconChevronRight from "@assets/icons/IconChevronRight";

import "./pagination.css";

const PAGE_SIZES = [10, 25, 50, 100];

export type PaginationProps = {
	pageNumber: number;
	pageSize: number;
	totalElements: number;
	onPageChange: (pageNumber: number) => void;
	onPageSizeChange: (pageSize: number) => void;
};

const Pagination = (props: Readonly<PaginationProps>): JSX.Element => {
	const { t } = useTranslation();

	const totalPages = Math.max(
		1,
		Math.ceil(props.totalElements / props.pageSize)
	);
	const firstElement =
		props.totalElements === 0 ? 0 : props.pageNumber * props.pageSize + 1;
	const lastElement = Math.min(
		(props.pageNumber + 1) * props.pageSize,
		props.totalElements
	);

	return (
		<div className="pagination">
			<span>
				{t("pagingRange", {
					first: firstElement,
					last: lastElement,
					total: props.totalElements
				})}
			</span>
			<div className="pagination-controls">
				<label className="pagination-page-size">
					{t("pageSize")}
					<select
						value={props.pageSize}
						onChange={e =>
							props.onPageSizeChange(Number(e.target.value))
						}>
						{PAGE_SIZES.map(size => (
							<option key={size} value={size}>
								{size}
							</option>
						))}
					</select>
				</label>
				<button
					className="icon-button"
					onClick={() => props.onPageChange(props.pageNumber - 1)}
					disabled={props.pageNumber === 0}
					title={t("previousPage")}>
					<IconChevronLeft />
				</button>
				<span>
					{t("pageOf", {
						page: props.pageNumber + 1,
						total: totalPages
					})}
				</span>
				<button
					className="icon-button"
					onClick={() => props.onPageChange(props.pageNumber + 1)}
					disabled={props.pageNumber >= totalPages - 1}
					title={t("nextPage")}>
					<IconChevronRight />
				</button>
			</div>
		</div>
	);
};

export default Pagination;
