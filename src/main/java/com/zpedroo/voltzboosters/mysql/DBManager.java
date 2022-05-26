package com.zpedroo.voltzboosters.mysql;

import com.zpedroo.voltzboosters.objects.PlayerBooster;
import com.zpedroo.voltzboosters.objects.PlayerData;
import com.zpedroo.voltzboosters.utils.serialization.BoosterSerialization;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DBManager extends BoosterSerialization {

    public void savePlayerData(PlayerData data) {
        String query = "REPLACE INTO `" + DBConnection.TABLE + "` (`uuid`, `boosters`) VALUES " +
                "('" + data.getUniqueId() + "', " +
                "'" + serializeBoosters(data.getBoosters()) + "');";
        executeUpdate(query);
    }

    public Map<UUID, PlayerData> getPlayerBoostersFromDatabase() {
        Map<UUID, PlayerData> playerData = new HashMap<>(32);

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        String query = "SELECT * FROM `" + DBConnection.TABLE + "`;";

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(query);
            result = preparedStatement.executeQuery();

            while (result.next()) {
                UUID playerUniqueId = UUID.fromString(result.getString(1));
                List<PlayerBooster> playerBoosters = deserializeBoosters(result.getString(2));

                playerData.put(playerUniqueId, new PlayerData(playerUniqueId, playerBoosters));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeConnection(connection, result, preparedStatement, null);
        }

        return playerData;
    }

    public void deletePlayerData(UUID uuid) {
        executeUpdate("DELETE FROM `" + DBConnection.TABLE + "` WHERE `uuid`='" + uuid + "';");
    }

    private void executeUpdate(String query) {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = getConnection();
            statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            closeConnection(connection, null, null, statement);
        }
    }

    private void closeConnection(Connection connection, ResultSet resultSet, PreparedStatement preparedStatement, Statement statement) {
        try {
            if (connection != null) connection.close();
            if (resultSet != null) resultSet.close();
            if (preparedStatement != null) preparedStatement.close();
            if (statement != null) statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    protected void createTable() {
        executeUpdate("CREATE TABLE IF NOT EXISTS `" + DBConnection.TABLE + "` (`uuid` VARCHAR(255), `boosters` LONGTEXT, PRIMARY KEY(`uuid`));");
    }

    private Connection getConnection() throws SQLException {
        return DBConnection.getInstance().getConnection();
    }
}