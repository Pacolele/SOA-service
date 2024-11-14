package se.liu.ida.tdp024.account.data.api.facade;

import java.util.List;

import org.apache.derby.iapi.store.raw.Transaction;

import se.liu.ida.tdp024.account.data.api.entity.Account;

public interface AccountEntityFacade {
    public String createAccount(String accountType, String personKey, String bankKey);
    public List<Account> findPersonAccount(String personKey);
    public String debitAccount(long accountId, long debitAmount);
    public String creditAccount(long accountId, long creditAmount);
}
