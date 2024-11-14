package se.liu.ida.tdp024.account.logic.test.facade;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.api.facade.AccountEntityFacade;
import se.liu.ida.tdp024.account.data.impl.db.entity.AccountDB;
import se.liu.ida.tdp024.account.logic.impl.facade.AccountLogicFacadeImpl;
import se.liu.ida.tdp024.account.logic.util.http.HTTPHelper;
import java.util.ArrayList;
import java.util.List;
@RunWith(MockitoJUnitRunner.class)
public class AccountLogicFacadeTest {


    @Mock
    private AccountEntityFacade accountEntityFacade;

    @Mock
    private HTTPHelper httpHelper;

    @InjectMocks
    private AccountLogicFacadeImpl accountLogicFacade;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateAccountSuccess() {
        when(httpHelper.get("http://project-kotlin-1:8070/find.name", "name", "SWEDBANK")).thenReturn("1");
        when(httpHelper.get("http://project-python-1:8060/find.key", "key", "3")).thenReturn("Person found");
        when(accountEntityFacade.createAccount("CHECK", "3", "1")).thenReturn("OK");

        String result = accountLogicFacade.createAccount("CHECK", "3", "SWEDBANK");
        assertEquals("OK", result);
        verify(httpHelper).get("http://project-kotlin-1:8070/find.name", "name", "SWEDBANK");
        verify(httpHelper).get("http://project-python-1:8060/find.key", "key", "3");
        verify(accountEntityFacade).createAccount("CHECK", "3", "1");
    }
    @Test
    public void testCreateAccountFailure() {
        when(httpHelper.get("http://project-kotlin-1:8070/find.name", "name", "BANK")).thenReturn("null");
        when(httpHelper.get("http://project-python-1:8060/find.key", "key", "person")).thenReturn("null");

        String result = accountLogicFacade.createAccount("CHECK", "person", "BANK");
        assertEquals("FAILED", result);
        verify(httpHelper).get("http://project-kotlin-1:8070/find.name", "name", "BANK");
        verify(httpHelper, never()).get("http://project-python-1:8060/find.key", "key", "20");
        verify(accountEntityFacade, never()).createAccount(anyString(), anyString(), anyString());
    }

    @Test
    public void testFindAccountSuccess() {
        when(httpHelper.get("http://project-python-1:8060/find.key", "key", "3")).thenReturn("Person found");
        List<Account> mockAccounts = new ArrayList<>();
        mockAccounts.add(new AccountDB());
        when(accountEntityFacade.findPersonAccount("3")).thenReturn(mockAccounts);
        List<Account> accounts = accountLogicFacade.findAccount("3");
        assertFalse(accounts.isEmpty());

        verify(httpHelper).get("http://project-python-1:8060/find.key", "key", "3");
        verify(accountEntityFacade).findPersonAccount("3");
    }
    @Test
    public void testFindAccountFailure() {
        when(httpHelper.get("http://project-python-1:8060/find.key", "key", "20")).thenReturn("null");
        List<Account> accounts = accountLogicFacade.findAccount("20");
        assertTrue(accounts.isEmpty());
        verify(httpHelper).get("http://project-python-1:8060/find.key", "key", "20");
        verify(accountEntityFacade, never()).findPersonAccount(anyString());
    }

    @Test
    public void testDebitAccountSuccess() {
        when(accountEntityFacade.debitAccount(1L, 100L)).thenReturn("OK");
        String result = accountLogicFacade.debitAccount(1L, 100L);
        assertEquals("OK", result);
        verify(accountEntityFacade).debitAccount(1L, 100L);
    }

    @Test
    public void testDebitAccountFailed() {
        when(accountEntityFacade.debitAccount(1L, 100L)).thenReturn("OK");
        String result = accountLogicFacade.debitAccount(1L, 100L);
        assertEquals("OK", result);
        verify(accountEntityFacade).debitAccount(1L, 100L);
    }

    @Test
    public void testCreditAccountSuccess() {
        when(accountEntityFacade.creditAccount(1, 100L)).thenReturn("OK");
        String result = accountLogicFacade.creditAccount(1, 100);
        assertEquals("OK", result);
    }
    @Test
    public void testCreditAccountFailed() {
        when(accountEntityFacade.creditAccount(1, 100L)).thenReturn("OK");
        String result = accountLogicFacade.creditAccount(1, 100);
        assertEquals("OK", result);
    }

}
