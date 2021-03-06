package org.orcid.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.orcid.pojo.ajaxForm.ErrorsInterface;

public class ProfileDeprecationRequest implements ErrorsInterface {
    private List<String> errors = new ArrayList<String>();
    private ProfileDetails deprecatedAccount;
    private ProfileDetails primaryAccount;
    private Date deprecatedDate;

    public ProfileDeprecationRequest() {
        this.deprecatedAccount = new ProfileDetails();
        this.primaryAccount = new ProfileDetails();
    }

    public ProfileDetails getDeprecatedAccount() {
        return deprecatedAccount;
    }

    public void setDeprecatedAccount(ProfileDetails deprecatedAccount) {
        this.deprecatedAccount = deprecatedAccount;
    }

    public ProfileDetails getPrimaryAccount() {
        return primaryAccount;
    }

    public void setPrimaryAccount(ProfileDetails primaryAccount) {
        this.primaryAccount = primaryAccount;
    }

    public Date getDeprecatedDate() {
        return deprecatedDate;
    }

    public void setDeprecatedDate(Date deprecatedDate) {
        this.deprecatedDate = deprecatedDate;
    }

    @Override
    public List<String> getErrors() {
        return this.errors;
    }

    @Override
    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
