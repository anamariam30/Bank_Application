package repository.account;

import model.Account;
import model.Client;
import model.builder.AccountBuilder;
import repository.client.ClientRepository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static database.Constants.Tables.*;

public class AccountRepositoryMySQL implements AccountRepository {

    private final ClientRepository clientRepository;

    private final Connection connection;

    public AccountRepositoryMySQL(ClientRepository clientRepository, Connection connection) {
        this.clientRepository = clientRepository;
        this.connection = connection;
    }


    @Override
    public Boolean save(Account account) {

        try {
            PreparedStatement insertUserStatement = connection
                    .prepareStatement("INSERT INTO " + ACCOUNT +" values (null, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            insertUserStatement.setString(1, account.getIdentificationNumber());
            insertUserStatement.setString(2, account.getType());
            insertUserStatement.setLong(3, account.getAmountOfMoney());
            insertUserStatement.setDate(4, java.sql.Date.valueOf(account.getDateCreation()));

            insertUserStatement.executeUpdate();

            ResultSet rs = insertUserStatement.getGeneratedKeys();
            rs.next();
            long accountId = rs.getLong(1);
            account.setId(accountId);

            return true;
        } catch (SQLException e) {

            return false;
        }
    }

    @Override
    public Boolean addAccountToClient(Account account, Client client) {


        try {
            PreparedStatement insertUserStatement = connection
                    .prepareStatement("INSERT INTO `" + CLIENT_ACCOUNT + "` values (null, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            insertUserStatement.setLong(1, client.getId());
            insertUserStatement.setLong(2, account.getId());
            insertUserStatement.executeUpdate();
            ResultSet rs = insertUserStatement.getGeneratedKeys();
            rs.next();
            return true;
        } catch (SQLException e) {

            return false;
        }
    }

    @Override
    public Boolean deleteAccountFromClient(Account account) {
        try {
            Statement statement = connection.createStatement();

            String sqlDelete = "DELETE from `" + CLIENT_ACCOUNT + "` where `account_id` =\'" + account.getId() + "\'";
            statement.executeUpdate(sqlDelete);
            return true;
        } catch (SQLException e) {

            return false;
        }
    }

    @Override
    public Boolean updateAccount(Account account) {
        String sql = "UPDATE `" + ACCOUNT + "` SET amountOfMoney=? WHERE (id = ?)";

        try {
            PreparedStatement updateStatement = connection
                    .prepareStatement(sql);
            updateStatement.setLong(1, account.getAmountOfMoney());
            updateStatement.setLong(2, account.getId());
            updateStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }

    }


    @Override
    public Boolean deleteAccount(Account account) {
        try {
            Statement statement = connection.createStatement();
            String sql = "DELETE from `" + ACCOUNT + "` where `id` =\'" + account.getId() + "\'";
            statement.executeUpdate(sql);

            return true;
        } catch (SQLException e) {

            return false;
        }
    }

    @Override
    public Account findAccountById(long accountId) {

        String sql = "Select * from `" + ACCOUNT + "` WHERE id = " + accountId;

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                return getAccountFromResultSet(resultSet);
            }

        } catch (SQLException throwables) {
            return null;
        }

        return null;
    }

    @Override
    public Account findAccountByIdNumber(String accountIdNumber) {
        String sql = "Select * from `" + ACCOUNT + "` WHERE identificationNumber = " + accountIdNumber;

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                return getAccountFromResultSet(resultSet);
            }

        } catch (SQLException throwable) {
            return null;
        }

        return null;
    }

    @Override
    public List<Account> findClientAccount(Client client) {
        String sql = "SELECT * FROM `" + ACCOUNT + "` inner join client_account " +
                "on account.id= client_account.account_id where client_account.client_id=" + client.getId();

        List<Account> accounts = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                accounts.add(getAccountFromResultSet(resultSet));
            }
        } catch (SQLException throwables) {
            return null;
        }

        return accounts;
    }

    @Override
    public Boolean removeAll() {
        String sql = "DELETE FROM `" + ACCOUNT + "` where id>=0";
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            return true;
        } catch (SQLException throwables) {

            return false;
        }
    }

    private Account getAccountFromResultSet(ResultSet rs) throws SQLException {
        return new AccountBuilder().setId(rs.getLong("id"))
                .setType(rs.getString("type"))
                .setAmountOfMoney(rs.getLong("amountOfMoney"))
                .setIdentificationNumber(rs.getString("identificationNumber"))
                .setDateCreation(rs.getDate("dateCreation").toLocalDate())
                .build();
    }
}
