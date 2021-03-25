package repository.account;

import model.Account;
import model.Client;

import java.util.List;

public interface AccountRepository {


    Boolean save(Account account);

    Boolean addAccountToClient(Account account, Client client);

    Boolean deleteAccountFromClient(Account account);

    Boolean updateAccount(Account account);

    Boolean deleteAccount(Account account);

    Account findAccountById(long accountId);

    Account findAccountByIdNumber(String accountIdNumber);

    List<Account> findClientAccount(Client client);

    Boolean removeAll();
}
