package se.liu.ida.tdp024.account.data.api.facade;

import java.util.List;
import javax.persistence.EntityManager;

import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.api.entity.Transaction;
import se.liu.ida.tdp024.account.utils.logger.AccountLogger;

public interface TransactionEntityFacade {

    public List<Transaction> getTransactions(long accountId);

    public AccountLogger getLogger();

    public String createTransaction(Account account, EntityManager em, String type, long amount, String status);
}
