package se.liu.ida.tdp024.account.data.impl.db.facade;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Service;

import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.api.entity.Transaction;
import se.liu.ida.tdp024.account.data.api.facade.TransactionEntityFacade;
import se.liu.ida.tdp024.account.data.impl.db.entity.AccountDB;
import se.liu.ida.tdp024.account.data.impl.db.entity.TransactionDB;
import se.liu.ida.tdp024.account.data.impl.db.util.EMF;
import se.liu.ida.tdp024.account.utils.logger.AccountLogger;

public class TransactionEntityFacadeDB implements TransactionEntityFacade {
    AccountLogger accountLogger;

    public TransactionEntityFacadeDB(AccountLogger accountLogger) {
        this.accountLogger = accountLogger;
    }

    @Override
    public AccountLogger getLogger() {
        return accountLogger;
    }

    @Override
    public List<Transaction> getTransactions(long accountId) {
        EntityManager em = EMF.getEntityManager();
        List<Transaction> transactions = new ArrayList<>();
        TypedQuery<TransactionDB> query = em
                .createQuery("SELECT t FROM TransactionDB t WHERE t.account.accountId = :accountId",
                        TransactionDB.class)
                .setParameter("accountId", accountId);
        List<TransactionDB> dbTransactions = query.getResultList();
        transactions.addAll(dbTransactions);
        return transactions;
    }

    @Override
    public String createTransaction(Account account, EntityManager em, String type, long amount,
            String status) {

        Transaction transaction = new TransactionDB();
        transaction.setAccount((AccountDB) account);
        transaction.setAmount(amount);
        transaction.setStatus(status);
        transaction.setType(type);
        em.persist(transaction);
        return "OK";
    }

}
