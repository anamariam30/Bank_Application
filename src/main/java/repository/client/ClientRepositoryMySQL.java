package repository.client;

import model.Client;
import model.builder.ClientBuilder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static database.Constants.Tables.CLIENT;


public class ClientRepositoryMySQL implements ClientRepository {

    private final Connection connection;

    public ClientRepositoryMySQL(Connection connection) {
        this.connection = connection;
    }


    @Override
    public Boolean save(Client client) {
        try {
            PreparedStatement insertUserStatement = connection
                    .prepareStatement("INSERT INTO `" + CLIENT + "` values (null, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            insertUserStatement.setString(1, client.getName());
            insertUserStatement.setString(2, client.getCardNumber());
            insertUserStatement.setString(3, client.getCNP());
            insertUserStatement.setString(4, client.getAddress());
            insertUserStatement.executeUpdate();

            ResultSet rs = insertUserStatement.getGeneratedKeys();
            rs.next();
            long userId = rs.getLong(1);
            client.setId(userId);


            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public Boolean updateClient(Client client) {
        String sql = "UPDATE `" + CLIENT + "` SET `name`=? , cardNumber = ?, address = ? WHERE (id = ?)";

        try {
            PreparedStatement insertStatement = connection
                    .prepareStatement(sql);
            insertStatement.setString(1, client.getName());
            insertStatement.setString(2, client.getCardNumber());
            insertStatement.setString(3, client.getAddress());
            insertStatement.setLong(4, client.getId());
            insertStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public Client findClientByName(String name) {
        String sql = "Select * from `" + CLIENT + "` where `name` =\'" + name + "\'";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {

                return getClientFromResultSet(resultSet);

            } else {
                return null;
            }

        } catch (SQLException throwables) {
            return null;
        }


    }

    @Override
    public Client findClientById(Long Id) {
        String sql = "Select * from `" + CLIENT + "` WHERE id = " + Id;

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                return getClientFromResultSet(resultSet);
            } else {
                return null;
            }

        } catch (SQLException throwables) {
            return null;
        }


    }

    @Override
    public List<Client> findAllClient() {
        String sql = "Select * from `" + CLIENT + "`";

        List<Client> clients = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                clients.add(getClientFromResultSet(resultSet));
            }
        } catch (SQLException throwables) {
            return null;
        }

        return clients;
    }

    @Override
    public Boolean removeAll() {
        String sql = "DELETE FROM `" + CLIENT + "` where id>=0";
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            return true;
        } catch (SQLException throwables) {
            return false;
        }

    }

    @Override
    public Client findClientByCNP(String cnp) {
        String sql = "Select * from `" + CLIENT + "` WHERE CNP= " + cnp;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                return getClientFromResultSet(resultSet);
            } else {
                return null;
            }

        } catch (SQLException throwables) {
            return null;
        }
    }

    private Client getClientFromResultSet(ResultSet rs) throws SQLException {
        return new ClientBuilder()
                .setId(rs.getLong("id"))
                .setName(rs.getString("name"))
                .setCardNumber(rs.getString("cardNumber"))
                .setCNP(rs.getString("CNP"))
                .setAddress(rs.getString("address"))
                .build();
    }
}
