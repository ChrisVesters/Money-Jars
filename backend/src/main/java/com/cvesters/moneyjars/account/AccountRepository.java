package com.cvesters.moneyjars.account;

import org.springframework.data.repository.ListCrudRepository;

import com.cvesters.moneyjars.account.dao.AccountDao;

public interface AccountRepository
		extends ListCrudRepository<AccountDao, Long> {
}
