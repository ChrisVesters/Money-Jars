package com.cvesters.moneyjars.transaction.bdo;

import java.time.LocalDate;

public record TransactionFilter(LocalDate from, LocalDate to) {

}
