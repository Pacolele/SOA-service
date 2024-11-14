// package se.liu.ida.tdp024.account.logic.test.facade;

// import static org.junit.Assert.assertEquals;
// import static org.junit.Assert.assertFalse;
// import static org.junit.Assert.assertTrue;
// import static org.mockito.Mockito.mock;

// import java.util.List;

// import org.junit.After;
// import org.junit.Before;
// import org.junit.Test;

// import se.liu.ida.tdp024.account.data.api.entity.Transaction;
// import se.liu.ida.tdp024.account.data.api.facade.AccountEntityFacade;
// import se.liu.ida.tdp024.account.data.api.facade.TransactionEntityFacade;
// import se.liu.ida.tdp024.account.data.api.util.StorageFacade;
// import se.liu.ida.tdp024.account.data.impl.db.facade.AccountEntityFacadeDB;
// import se.liu.ida.tdp024.account.data.impl.db.facade.TransactionEntityFacadeDB;
// import se.liu.ida.tdp024.account.data.impl.db.util.StorageFacadeDB;
// import se.liu.ida.tdp024.account.logic.api.facade.AccountLogicFacade;
// import se.liu.ida.tdp024.account.logic.api.facade.TransactionLogicFacade;
// import se.liu.ida.tdp024.account.logic.impl.facade.AccountLogicFacadeImpl;
// import se.liu.ida.tdp024.account.logic.impl.facade.TransactionLogicFacadeImpl;

// public class TransactionLogicFacadeTest {
//     private TransactionLogicFacade transactionLogicFacade;
//     private AccountLogicFacade accountLogicFacade;
//     private TransactionEntityFacade transactionEntityFacadeMock;
//     private StorageFacade storageFacade;
//     private AccountEntityFacade accountEntityFacadeMock;
//     private String correctAccountType = "CHECK";
//     private String correctPersonKey = "3";
//     private String correctBankName = "HANDELSBANKEN";

//     @Before
//     public void setUp() {
//         transactionEntityFacadeMock = mock(TransactionEntityFacadeDB.class);
//         accountEntityFacadeMock = mock(AccountEntityFacadeDB.class);

//         transactionLogicFacade = new TransactionLogicFacadeImpl(transactionEntityFacadeMock);
//         accountLogicFacade = new AccountLogicFacadeImpl(accountEntityFacadeMock);
//         storageFacade = mock(StorageFacadeDB.class);
//     }

//     @After
//     public void tearDown() {
//         if (storageFacade != null)
//             storageFacade.emptyStorage();
//     }

//     @Test
//     public void testGetTransactionSuccess() {
//         List<Transaction> result = null;
//         accountLogicFacade.createAccount(correctAccountType, correctPersonKey, correctBankName);
//         accountLogicFacade.creditAccount(1, 1000);
//         result = transactionLogicFacade.getAccountTransactions(1);
//         assertFalse(result.isEmpty());
//         assertEquals(1, result.size());
//         accountLogicFacade.debitAccount(1, 500);
//         accountLogicFacade.debitAccount(1, 500);
//         result = transactionLogicFacade.getAccountTransactions(1);
//         assertEquals(3, result.size());
//         assertTrue(!result.isEmpty());
//     }

// }
