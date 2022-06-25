package it.polito.tdp.PremierLeague.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.PremierLeague.model.Action;
import it.polito.tdp.PremierLeague.model.Arco;
import it.polito.tdp.PremierLeague.model.Player;

public class PremierLeagueDAO {
	
	public List<Player> listAllPlayers(){
		String sql = "SELECT * FROM Players";
		List<Player> result = new ArrayList<Player>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Player player = new Player(res.getInt("PlayerID"), res.getString("Name"));
				
				result.add(player);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Action> listAllActions(){
		String sql = "SELECT * FROM Actions";
		List<Action> result = new ArrayList<Action>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Action action = new Action(res.getInt("PlayerID"),res.getInt("MatchID"),res.getInt("TeamID"),res.getInt("Starts"),res.getInt("Goals"),
						res.getInt("TimePlayed"),res.getInt("RedCards"),res.getInt("YellowCards"),res.getInt("TotalSuccessfulPassesAll"),res.getInt("totalUnsuccessfulPassesAll"),
						res.getInt("Assists"),res.getInt("TotalFoulsConceded"),res.getInt("Offsides"));
				
				result.add(action);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<Player> getVertici (double goal, Map<Integer, Player> idMap){
		String sql="SELECT p.* "
				+ "FROM actions a, players p "
				+ "WHERE a.PlayerID=p.PlayerID "
				+ "GROUP BY p.PlayerID "
				+ "HAVING AVG(a.Goals)>?";
		
		List<Player> result = new ArrayList<Player>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setDouble(1, goal);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Player player = new Player(res.getInt("PlayerID"), res.getString("Name"));
				
				result.add(player);
				idMap.put(player.getPlayerID(), player);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Arco> getArchi(Map<Integer, Player> idMap){
		String sql="SELECT a1.PlayerID AS g1, a2.PlayerID AS g2, (SUM(a1.TimePlayed)-SUM(a2.TimePlayed)) AS delta "
				+ "FROM actions a1, actions a2 "
				+ "WHERE a1.MatchID=a2.MatchID and a1.PlayerID<>a2.PlayerID AND a1.TeamID<>a2.TeamID "
				+ "	AND a1.Starts=1 AND a2.Starts=1 "
				+ "GROUP BY a1.PlayerID,a2.PlayerID";
		List<Arco> archi= new ArrayList<Arco>();
			
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				if(idMap.containsKey(res.getInt("g1")) && idMap.containsKey(res.getInt("g2"))) {
							Arco a= new Arco(res.getInt("g1"), res.getInt("g2"), res.getInt("delta"));
							archi.add(a);
					
				}
				
				
				
				
			}
			conn.close();
			return archi;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
