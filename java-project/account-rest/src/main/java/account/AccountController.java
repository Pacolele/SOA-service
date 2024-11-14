package account;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import se.liu.ida.tdp024.account.logic.impl.facade.AccountLogicFacadeImpl;
import se.liu.ida.tdp024.account.logic.impl.facade.TransactionLogicFacadeImpl;
import se.liu.ida.tdp024.account.logic.api.facade.AccountLogicFacade;
import se.liu.ida.tdp024.account.logic.api.facade.TransactionLogicFacade;
import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.api.entity.Transaction;
import se.liu.ida.tdp024.account.data.impl.db.facade.AccountEntityFacadeDB;
import se.liu.ida.tdp024.account.data.impl.db.facade.TransactionEntityFacadeDB;
import se.liu.ida.tdp024.account.utils.logger.AccountLogger;
import se.liu.ida.tdp024.account.utils.logger.KafkaLoggerProducer;

/**
 * @return "OK" eller "FAILED"
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/account-rest")
public class AccountController {

    private final AccountLogger loggerProducer = new KafkaLoggerProducer();
    private AccountLogicFacade accountLogicFacade;
    private TransactionLogicFacade transactionLogicFacade;

    public AccountController() {
        this.accountLogicFacade = new AccountLogicFacadeImpl(
                new AccountEntityFacadeDB(new TransactionEntityFacadeDB(loggerProducer)));
        this.transactionLogicFacade = new TransactionLogicFacadeImpl(
                new TransactionEntityFacadeDB(loggerProducer));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleMissingParams(MissingServletRequestParameterException e) {
        return new ResponseEntity<>("FAILED", HttpStatus.OK);
    }

    @PostMapping(value = "/account/create")
    public ResponseEntity<String> createAccount(@RequestParam(required = false) String person,
            @RequestParam(required = false) String bank, @RequestParam(required = false) String accounttype) {

        if (person == null || bank == null || accounttype == null) {
            return new ResponseEntity<>("FAILED", HttpStatus.OK);
        }
        String res = accountLogicFacade.createAccount(accounttype, person, bank);
        if (!res.equals("OK")) {
            return new ResponseEntity<>(res, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(res, HttpStatus.OK);
        }
    }

    /**
     * @return Lista på konton i JSON (se specifikation 4.1). Om inga konton
     *         hittades skall en tom JSON array returneras (dvs []).
     */
    @GetMapping(value = "/account/find/person", produces = "application/json")
    public ResponseEntity<List<Account>> findPerson(@RequestParam(value = "person") String person) {
        List<Account> res = accountLogicFacade.findAccount(person);
        if (res.isEmpty()) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    /**
     * @return"OK" eller "FAILED"
     */
    @PostMapping("/account/debit")
    public ResponseEntity<String> debitAccount(@RequestParam long id, @RequestParam long amount) {
        String res = accountLogicFacade.debitAccount(id, amount);
        if (res.equals("OK")) {
            return new ResponseEntity<>(res, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(res, HttpStatus.OK);
        }
    }

    /**
     * @return "OK" eller "FAILED"
     */
    @PostMapping("/account/credit")
    public ResponseEntity<String> creditAccount(@RequestParam long id, @RequestParam long amount) {
        String res = accountLogicFacade.creditAccount(id, amount);
        if (res.equals("OK")) {
            return new ResponseEntity<>(res, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(res, HttpStatus.OK);
        }
    }

    /**
     * @return Lista på transaktioner i JSON (se specifikation 4.2). Om inga
     *         transaktioner hittades skall en tom JSON array returneras (dvs []).
     */
    @GetMapping(value = "/account/transactions", produces = "application/json")
    public ResponseEntity<List<Transaction>> getTransactions(@RequestParam int id) {
        List<Transaction> transactions = transactionLogicFacade.getAccountTransactions(id);
        if (transactions.isEmpty()) {
            return new ResponseEntity<>(transactions, HttpStatus.OK);
        }
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

}
