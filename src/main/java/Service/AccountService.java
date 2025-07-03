package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService() {
        this.accountDAO = new AccountDAO();
    }

    public Account register(Account account) {
        if (account.getUsername() == null || account.getUsername().isBlank() || account.getPassword().length() <= 4) {
            return null;
        }
        return accountDAO.insertAccount(account);
    }

    public Account login(String username, String password) {
        return accountDAO.getAccountByUsernameAndPassword(username, password);
    }

    public Account getAccountById(int id) {
        return accountDAO.getAccountById(id);
    }
}
