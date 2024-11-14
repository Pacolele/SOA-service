package se.liu.ida.tdp024.account.data.impl.db.facade;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.LockModeType;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Service;

import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.api.facade.AccountEntityFacade;
import se.liu.ida.tdp024.account.data.impl.db.entity.AccountDB;
import se.liu.ida.tdp024.account.data.impl.db.util.EMF;
import se.liu.ida.tdp024.account.utils.logger.FinalConstants;
import se.liu.ida.tdp024.account.data.api.entity.Transaction;
import se.liu.ida.tdp024.account.data.impl.db.entity.TransactionDB;
import se.liu.ida.tdp024.account.data.api.facade.TransactionEntityFacade;
// import se.liu.ida.tdp024.account.utils.logger.AccountLogger;

public class AccountEntityFacadeDB implements AccountEntityFacade {

    // private EntityManager em;
    private TransactionEntityFacade transactionEntityFacade;

    public AccountEntityFacadeDB(TransactionEntityFacade transactionEntityFacade) {
        // this.em = EMF.getEntityManager();
        this.transactionEntityFacade = transactionEntityFacade;
    }

    @Override
    public String createAccount(String accountType, String personKey, String bankKey) {
        System.out.println("adding account to db in AccountEntityFacadeDB");
        EntityManager em = EMF.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        Account account = null;

        try {
            transaction.begin();
            account = setupAccount(accountType, personKey, bankKey);
            em.persist(account);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            transactionEntityFacade.getLogger().logMessages(FinalConstants.TOPIC_TRANSACTION, "Account creation fail due unexpected error.");
            return "FAILED";
        } finally {
            em.close();
        }
        transactionEntityFacade.getLogger().logMessages(FinalConstants.TOPIC_REST_REQUEST, "Account has successfully been created.");
        return "OK";
    }

    @Override
    public List<Account> findPersonAccount(String personKey) {
        List<Account> accounts = new ArrayList<>();
        EntityManager em = EMF.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            TypedQuery<AccountDB> query = em
                    .createQuery(String.format("SELECT a FROM AccountDB a WHERE a.personKey = :personKey"),
                            AccountDB.class)
                    .setParameter("personKey", personKey);
            List<AccountDB> accountDbs = query.getResultList();
            accounts.addAll(accountDbs);

        } catch (Exception e) {
            e.printStackTrace();
            if (transaction.isActive()) {
                transaction.rollback();
            }
        } finally {
            em.close();
        }
        return accounts;
    }

    @Override
    public String debitAccount(long accountId, long debitAmount) {
        EntityManager em = EMF.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        String status = null;
        try {
            transaction.begin();
            AccountDB account = getAccount(accountId, em);
            if (account == null) {
                throw new IllegalArgumentException("Account not found with ID: " + accountId);
            }
            em.lock(account, LockModeType.PESSIMISTIC_WRITE);
            if (account.getHoldings() < debitAmount || debitAmount == 0) {
                status = "FAILED";
            } else {
                status = "OK";
                account.setHoldings(account.getHoldings() - debitAmount);
                System.out.println(account.getHoldings());
            }
            transactionEntityFacade.createTransaction(account, em, "DEBIT", debitAmount, status);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction.isActive()) {
                transaction.rollback();
            }
            status = "FAILED";
        } finally {
            em.close();
        }
        transactionEntityFacade.getLogger().logMessages(FinalConstants.TOPIC_TRANSACTION,
                "Debit account with status: " + status);

        return status;
    }

    @Override
    public String creditAccount(long accountId, long creditAmount) {
        System.out.println("credit account in facade");
        EntityManager em = EMF.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        String status = null;
        try {
            transaction.begin();
            AccountDB account = getAccount(accountId, em);
            if (account == null) {
                throw new IllegalArgumentException("Account not found with ID: " + accountId);
            }
            em.lock(account, LockModeType.PESSIMISTIC_WRITE);
            if (creditAmount <= 0) {
                status = "FAILED";
            } else {
                status = "OK";
                account.setHoldings(account.getHoldings() + creditAmount);
                System.out.println(account.getHoldings());
            }
            transactionEntityFacade.createTransaction(account, em, "CREDIT", creditAmount, status);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            status = "FAILED";
        } finally {
            em.close();

        }
        transactionEntityFacade.getLogger().logMessages(FinalConstants.TOPIC_TRANSACTION,
                "Credit account with status: " + status);
        return status;
    }

    private Account setupAccount(String accountType, String personKey, String bankKey) {
        Account account = new AccountDB();
        account.setAccountType(accountType);
        account.setPersonKey(personKey);
        account.setBankKey(bankKey);
        return account;
    }

    private AccountDB getAccount(long accountId, EntityManager em) {
        AccountDB account = em.find(AccountDB.class, accountId);
        return account;
    }
}
