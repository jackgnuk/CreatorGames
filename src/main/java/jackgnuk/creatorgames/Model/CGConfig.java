package jackgnuk.creatorgames.Model;

import jackgnuk.creatorgames.CreatorGames;
import jackgnuk.creatorgames.Data.Connector;
import jackgnuk.creatorgames.Data.DatabaseConnector;
import jackgnuk.creatorgames.Data.StorageConnector;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class CGConfig {
    public CGConfig(CreatorGames instance) {
        this.instance = instance;
    }

    private final CreatorGames instance;
    private HashMap<EntityType, Integer> coinsToEntity = new HashMap<EntityType, Integer>() {{
        put(EntityType.PLAYER, 10); put(EntityType.BAT, 1);
    }};

    //region MySQL_Settings
    private boolean mySQLEnabled = false;
    private String host = "localhost";
    private int port = 3306;
    private String database = "creatorgames";
    private String username = "root";
    private String password = "root";
    //endregion

    private List<TeamRegister> teams = new ArrayList<TeamRegister>(
        Arrays.asList(
            new TeamRegister("Incredible Irons", "II", ChatColor.WHITE),
            new TeamRegister("Dazzling Diamonds", "DD", ChatColor.AQUA),
            new TeamRegister("Rowdy Redstones", "RR", ChatColor.DARK_RED),
            new TeamRegister("Luxurious Lapis", "LL", ChatColor.BLUE),
            new TeamRegister("Glistering Golds", "GG", ChatColor.GOLD),
            new TeamRegister("Nimble Netherites", "NN", ChatColor.DARK_GRAY),
            new TeamRegister("Exquisite Emeralds", "EE", ChatColor.GREEN),
            new TeamRegister("Cool Coals", "CC", ChatColor.GRAY)
        )
    );

    public boolean coinAvailable(EntityType e) {
        return coinsToEntity.containsKey(e);
    }

    public List<String> getCoinsAsStringMap() {
        List<String> tmp = new ArrayList<>();

        for(Map.Entry<EntityType, Integer> entry : coinsToEntity.entrySet()) {
            String a = entry.getKey().name();
            Integer b = entry.getValue();

            tmp.add(a + " : " + b);
        }

        return tmp;
    }

    public void setCoinsFromStrings(List<String> strings) {
        coinsToEntity = new HashMap<EntityType, Integer>();
        Iterator<String> it = strings.iterator();

        String current;
        while(it.hasNext()) {
            current = it.next();
            String[] sections = current.split(" : ");
            this.coinsToEntity.put(EntityType.valueOf(sections[0]), Integer.parseInt(sections[1]));
        }
    }

    public List<TeamRegister> getTeams() {
        return teams;
    }

    public TeamRegister getTeamFromID(String id) {
        for (TeamRegister r : teams) {
            if (r.ID.equals(id)) {
                return r;
            }
        }
        return null;
    }

    public List<String> getTeamStrings() {
        List<String> tmp = new ArrayList<>();
        for (TeamRegister r : teams) {
            tmp.add(r.TeamName);
        }

        return tmp;
    }

    public void setTeams(List<TeamRegister> teams) {
        this.teams = teams;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isMySQLEnabled() {
        return mySQLEnabled;
    }

    public void setMySQLEnabled(boolean mySQLEnabled) {
        this.mySQLEnabled = mySQLEnabled;
    }

    public int CoinsToDrop(EntityType entityType) {
        Integer coin = coinsToEntity.get(entityType);

        if (coin == null) return 0;
        return coin;
    }

    public Connector CreateConnector() {
        Connector conn;
        if (isMySQLEnabled()) {
            conn = new DatabaseConnector(instance);
        } else {
            conn = new StorageConnector(instance);
        }
        return conn;
    }
}
