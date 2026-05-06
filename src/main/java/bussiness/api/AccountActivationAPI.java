package business.api;

import business.service.AccountActivationService;
import model.account.ActivationEmployeeInfo;

public class AccountActivationAPI {
    private final AccountActivationService service = new AccountActivationService();

    public ActivationEmployeeInfo check(String code) throws Exception {
        return service.checkAndFetchActivation(code);
    }

    public void activate(String code, String username, String passwordPlain) throws Exception {
        service.activateAccountByCode(code, username, passwordPlain);
    }
}