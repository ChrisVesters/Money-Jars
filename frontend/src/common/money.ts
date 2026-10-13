export const MonitaryUnit = {
	EURO: "€"
} as const;

export type MonitaryUnit = typeof MonitaryUnit[keyof typeof MonitaryUnit];

export class Money {

	static toLabel(amount: string): string {
		return `${MonitaryUnit.EURO}${amount}`;
	}
}