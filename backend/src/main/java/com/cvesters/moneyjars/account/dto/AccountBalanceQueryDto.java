package com.cvesters.moneyjars.account.dto;

import java.util.List;

public record AccountBalanceQueryDto(List<Long> accountIds) {
}
