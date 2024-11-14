package se.liu.ida.tdp024.account.data.test.facade;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.api.facade.AccountEntityFacade;
import se.liu.ida.tdp024.account.data.api.facade.TransactionEntityFacade;
import se.liu.ida.tdp024.account.data.api.util.StorageFacade;
import se.liu.ida.tdp024.account.data.impl.db.entity.AccountDB;
import se.liu.ida.tdp024.account.data.impl.db.facade.AccountEntityFacadeDB;
import se.liu.ida.tdp024.account.data.impl.db.facade.TransactionEntityFacadeDB;
import se.liu.ida.tdp024.account.utils.logger.AccountLogger;
import se.liu.ida.tdp024.account.utils.logger.KafkaLoggerProducer;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AccountEntityFacadeTest {

    @Mock
    private AccountLogger accountLogger; // Mock the logger

    @Mock
    private TransactionEntityFacade transactionEntityFacade;

    @InjectMocks
    private TransactionEntityFacadeDB transactionEntityFacadeDB;

    private AccountEntityFacadeDB accountEntityFacadeDB;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        transactionEntityFacadeDB = new TransactionEntityFacadeDB(accountLogger);
        accountEntityFacadeDB = new AccountEntityFacadeDB(transactionEntityFacadeDB);
    }

    @Test
    public void testCreateAccountSuccess() {
        String result = accountEntityFacadeDB.createAccount("CHECK", "1", "1");
        assertEquals("OK", result);
    }

    @Test
    public void testFindPersonAccount() {
    accountEntityFacadeDB.createAccount("CHECK", "1", "1");
    List<Account> result = accountEntityFacadeDB.findPersonAccount("1");
    assertFalse(result.isEmpty());
    Account account = result.getFirst();
    assertEquals("1", account.getBankKey());
    assertEquals("1", account.getPersonKey());
    assertEquals("CHECK", account.getAccountType());
    }

    @Test
    public void testDebitAccountFailed() {
        accountEntityFacadeDB.createAccount("CHECK", "1", "1");
        String result = accountEntityFacadeDB.debitAccount(1, 9999);
        assertEquals("FAILED", result);
    }

    @Test
    public void testCreditAndDebitAccount() {
        accountEntityFacadeDB.createAccount("CHECK", "1", "1");
        String creditResult = accountEntityFacadeDB.creditAccount(1, 500);
        assertEquals("OK", creditResult);
        String debitResult = accountEntityFacadeDB.creditAccount(1, 500);
        assertEquals("OK", debitResult);
    }

    @Test
    public void testCreditFailed() {
        accountEntityFacadeDB.createAccount("CHECK", "1", "1");
        String result = accountEntityFacadeDB.creditAccount(1, 0);
        assertEquals("FAILED", result);
    }


}
