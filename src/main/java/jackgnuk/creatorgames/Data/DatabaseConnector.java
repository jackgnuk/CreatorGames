package jackgnuk.creatorgames.Data;

import jackgnuk.creatorgames.CreatorGames;
import jackgnuk.creatorgames.Model.PlayerStats;
import jackgnuk.creatorgames.Model.PlayerWrapper;
import jackgnuk.creatorgames.Model.TeamRegister;
import org.bukkit.Bukkit;

import java.sql.*;
import java.util.UUID;

public class DatabaseConnector implements Connector {
    final CreatorGames instance;

    private String connString;

    public DatabaseConnector(CreatorGames instance) {
        this.instance = instance;

        this.connString = String.format("jdbc:mysql://%s:%d/%s", instance.Config.getHost(), instance.Config.getPort(), instance.Config.getDatabase());

        if (instance.Config.isMySQLEnabled()) {
            ValidateSchema();
        }
    }

    private void ValidateSchema() {
        try (Connection conn = DriverManager.getConnection(connString, instance.Config.getUsername(), instance.Config.getPassword())) {
            String playerTable = "CREATE TABLE IF NOT EXISTS players(" +
                    "uuid varchar(255) NOT NULL PRIMARY KEY," +
                    "username varchar(255) NOT NULL" +
                    ")";

            String teamTable = "CREATE TABLE IF NOT EXISTS teams(" +
                    "uuid varchar(255) NOT NULL," +
                    "teamname varchar(255) NOT NULL)";

            try (Statement stmt = conn.createStatement()) {
                try {
                    stmt.executeUpdate(teamTable);
                    stmt.executeUpdate(playerTable);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public PlayerStats GetPlayer(String id) {
        boolean isUUID = true;
        try {
            UUID uuid = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            isUUID = false;
        }


        String sql = "SELECT * FROM players WHERE " + ((isUUID) ? "uuid = " : "username = ");
        sql += "'" + id + "'";

        try (Connection conn = DriverManager.getConnection(connString, instance.Config.getUsername(), instance.Config.getPassword())) {
            try (Statement stmt = conn.createStatement()) {
                try (ResultSet set = stmt.executeQuery(sql)) {
                    if (set.first()) {
                        PlayerStats stats = new PlayerStats();
                        stats.Name = set.getString("username");
                        stats.UUID = set.getString("uuid");
                        return stats;
                    }

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void UpdatePlayer(String uuid, PlayerStats stats) {

        String sql = "UPDATE players SET username = '" + stats.Name + "' WHERE uuid = '" + stats.UUID + "'";

        try (Connection conn = DriverManager.getConnection(connString, instance.Config.getUsername(), instance.Config.getPassword())) {
            try (Statement stmt = conn.createStatement()) {
                try  {
                    stmt.executeUpdate(sql);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void AddPlayer(PlayerStats stats) {
        String sql = String.format("INSERT INTO players VALUES ('%s','%s')", stats.UUID, stats.Name);

        try (Connection conn = DriverManager.getConnection(connString, instance.Config.getUsername(), instance.Config.getPassword())) {
            try (Statement stmt = conn.createStatement()) {
                try {
                    stmt.executeUpdate(sql);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public TeamRegister GetTeam(String teamID) {
        TeamRegister register = instance.Config.getTeamFromID(teamID);
        String sql = "SELECT players.* FROM players WHERE uuid = (SELECT uuid FROM teams WHERE teamname = '" + register.TeamName + "')";

        try (Connection conn = DriverManager.getConnection(connString, instance.Config.getUsername(), instance.Config.getPassword())) {
            try (Statement stmt = conn.createStatement()) {
                try (ResultSet set = stmt.executeQuery(sql)) {
                    if (set.first()) {
                        do {
                            PlayerStats stats = new PlayerStats();
                            stats.Name = set.getString("username");
                            stats.UUID = set.getString("uuid");
                            if (register.Players.stream().noneMatch(p -> p.UUID.equals(stats.UUID))) register.Players.add(stats);
                        } while (set.next());
                        return register;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void AddTeammate(PlayerStats stats, String teamName) {
        String sql = String.format("INSERT INTO teams VALUES ('%s','%s')", stats.UUID, teamName);

        try (Connection conn = DriverManager.getConnection(connString, instance.Config.getUsername(), instance.Config.getPassword())) {
            try (Statement stmt = conn.createStatement()) {
                try {
                    stmt.executeUpdate(sql);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void RemoveTeammate(PlayerStats stats, String teamName) {
        String sql = String.format("DELETE FROM teams WHERE uuid = '%s' AND teamname = '%s'", stats.UUID, teamName);

        try (Connection conn = DriverManager.getConnection(connString, instance.Config.getUsername(), instance.Config.getPassword())) {
            try (Statement stmt = conn.createStatement()) {
                try {
                    stmt.executeUpdate(sql);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
