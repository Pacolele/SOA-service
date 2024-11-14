package se.liu.ida.tdp024.account.data.api.entity;

import java.io.Serializable;

public interface Account extends Serializable {

    public long getAccountId();

    public void setAccountType(String accountType);
    public String getAccountType();

    public void setPersonKey(String personKey);
    public String getPersonKey();

    public void setBankKey(String bankId);
    public String getBankKey();

    public void setHoldings(long holdings);
    public long getHoldings();


}
