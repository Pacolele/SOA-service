package se.liu.ida.tdp024.account.logic.impl.facade;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.api.facade.AccountEntityFacade;
import se.liu.ida.tdp024.account.logic.api.facade.AccountLogicFacade;
import se.liu.ida.tdp024.account.logic.util.http.HTTPHelper;
import se.liu.ida.tdp024.account.logic.util.http.HTTPHelperImpl;

public class AccountLogicFacadeImpl implements AccountLogicFacade {

    private AccountEntityFacade accountEntityFacade;
    private HTTPHelper httpHelper;

    // private static final Logger LOGGER =
    // Logger.getLogger(AccountLogicFacadeImpl.class.g);
    public AccountLogicFacadeImpl(AccountEntityFacade accountEntityFacade) {
        this.accountEntityFacade = accountEntityFacade;
        this.httpHelper = new HTTPHelperImpl();
    }

    /* check api values -> get results and send to data layer */
    @Override
    public String createAccount(String accountType, String personKey, String bankName) {
        System.out.println("CREATING ACCOUNT.... in AccountLogicFacadeImpl");
        String bankRes = httpHelper.get("http://project-kotlin-1:8070/find.name", "name", bankName);
        String personRes = httpHelper.get("http://project-python-1:8060/find.key", "key", personKey);
        System.out.println(personRes);
        if (((personRes == null || personRes.equals("null")) || (bankRes == null || bankRes.equals("null")))
                || (!accountType.contentEquals("CHECK") && !accountType.contentEquals("SAVINGS"))) {
            return "FAILED";
        } else {
            return accountEntityFacade.createAccount(accountType, personKey, bankRes);
        }
    }

    @Override
    public List<Account> findAccount(String personKey) {
        List<Account> accounts = new ArrayList<>();
        try {
            String personRes = httpHelper.get("http://project-python-1:8060/find.key", "key", personKey);
            if (personRes.equals("null")) {
                return accounts;
            }
            accounts = accountEntityFacade.findPersonAccount(personKey);

        } catch (Exception e) {
            System.out.println(e.getStackTrace());
            return accounts;
        }
        return accounts;
    }

    @Override
    public String debitAccount(long accountId, long debitAmount) {
        String res = accountEntityFacade.debitAccount(accountId, debitAmount);
        return res;
    }

    @Override
    public String creditAccount(long accountID, long creditAmount) {
        String res = accountEntityFacade.creditAccount(accountID, creditAmount);
        return res;
    }

}
