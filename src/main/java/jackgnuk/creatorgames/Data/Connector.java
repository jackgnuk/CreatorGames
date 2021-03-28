package jackgnuk.creatorgames.Data;

import jackgnuk.creatorgames.Model.PlayerStats;
import jackgnuk.creatorgames.Model.TeamRegister;

public interface Connector {
    PlayerStats GetPlayer(String id);

    void UpdatePlayer(String uuid, PlayerStats stats);

    void AddPlayer(PlayerStats stats);

    TeamRegister GetTeam(String teamID);

    void AddTeammate(PlayerStats stats, String teamName);

    void RemoveTeammate(PlayerStats stats, String teamName);
}
