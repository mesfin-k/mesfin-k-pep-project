package Service;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Account;

public class AccountService {

    private AccountDAO accountDAO;
    private MessageDAO messageDAO;

    public AccountService() {
        this.accountDAO = new AccountDAO();  // Assuming AccountDAO has a no-args constructor
    }

    // Instance method for registering an account
    public Account registerAccount(Account account) {
        if (account.getUsername() != null && !account.getUsername().isBlank()
            && account.getPassword().length() >= 4
            && accountDAO.getAccountByUsername(account.getUsername()) == null) {
            return accountDAO.createAccount(account);
        }
        return null;
    }

    // Instance method for logging in
    public Account login(Account account) {
        Account existingAccount = accountDAO.getAccountByUsername(account.getUsername());
        if (existingAccount != null && existingAccount.getPassword().equals(account.getPassword())) {
            return existingAccount;
        }
        return null;
    }
}
