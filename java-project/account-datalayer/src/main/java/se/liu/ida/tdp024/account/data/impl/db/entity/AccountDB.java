package se.liu.ida.tdp024.account.data.impl.db.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


import se.liu.ida.tdp024.account.data.api.entity.Account;

@Entity
@Table(name = "ACCOUNTDB")
public class AccountDB implements Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long accountId;
    private String personKey;
    private String accountType;
    private String bankKey;
    public long holdings;


    @Override
    public long getAccountId() {
        return accountId;
    }

    @Override
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    @Override
    public String getAccountType() {
        return accountType;
    }

    @Override
    public void setPersonKey(String personKey) {
        this.personKey = personKey;
    }

    @Override
    public String getPersonKey() {
        return personKey;
    }

    @Override
    public void setBankKey(String bankKey) {
        this.bankKey = bankKey;
    }

    @Override
    public String getBankKey() {
        return bankKey;
    }

    @Override
    public void setHoldings(long holdings) {
        this.holdings = holdings;
    }

    @Override
    public long getHoldings() {
        return holdings;
    }


}
