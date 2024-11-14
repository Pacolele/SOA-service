package account;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.impl.db.entity.AccountDB;
import se.liu.ida.tdp024.account.logic.api.facade.AccountLogicFacade;
import se.liu.ida.tdp024.account.logic.api.facade.TransactionLogicFacade;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(MockitoJUnitRunner.class)
public class AccountControllerTest {

    @Mock
    private AccountLogicFacade accountLogicFacade;

    @Mock
    private TransactionLogicFacade transactionLogicFacade;

    private AccountController accountController;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        accountController = new AccountController();
    }

    @Test
    public void testCreateAccountSuccess() {
        ResponseEntity<String> response = accountController.createAccount("1", "SWEDBANK", "SAVINGS");
        assertEquals("OK", response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void testCreateAccountFailure() {
        ResponseEntity<String> response = accountController.createAccount("person", "bank", "accounttype");
        assertEquals("FAILED", response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void testFindPersonNoAccounts() {

        ResponseEntity<List<Account>> response = accountController.findPerson("person");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(0, response.getBody().size());
    }

    @Test
    public void testCreditAndDebitFailed() {
        ResponseEntity<String> responseCredit = accountController.creditAccount(1000000000000L, 200L);
        assertEquals("FAILED", responseCredit.getBody());
        assertEquals(200, responseCredit.getStatusCodeValue());
        ResponseEntity<String> responseDebit = accountController.debitAccount(1000000000000L, 100L);
        assertEquals("FAILED", responseDebit.getBody());
        assertEquals(200, responseDebit.getStatusCodeValue());
    }

}
