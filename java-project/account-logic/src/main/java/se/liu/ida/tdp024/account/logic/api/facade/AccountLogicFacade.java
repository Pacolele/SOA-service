package se.liu.ida.tdp024.account.logic.api.facade;

import java.util.List;

import se.liu.ida.tdp024.account.data.api.entity.Account;

public interface AccountLogicFacade {

    public String createAccount(String accountType, String personKey, String bankName);
    public List<Account> findAccount(String personKey);
    public String debitAccount(long accountId, long debitAmount);
    public String creditAccount(long accountID, long creditAmount);
}
