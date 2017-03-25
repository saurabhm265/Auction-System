package utility;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DbConnection;

public class Sync {
	
	static List<String> hosts = new ArrayList<String>();
	
	static{
		
		String sql = "select * from clustersystem";
		try (	Connection connection = DbConnection.getConnection() ;
				PreparedStatement statement  = connection.prepareStatement(sql)){

			ResultSet rs = statement.executeQuery();
			
			while (rs.next())
				hosts.add(rs.getString(1));
			
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
	}
	
	public static void notifyCounter(int c) {
		for (String string : hosts) {
			try {
				URL url = new URL(string + "SetBidCounter?n=" + c);
				URLConnection connection = url.openConnection();
				
				BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
				
				String s = br.readLine();
				
				System.out.println(string + " notified " + s);
			} catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public static void loginUser(int clientId) {
		for (String string : hosts) {
			try {
				URL url = new URL(string + "LoginUser?clientId=" + clientId + "&passcode=lamarpass");
				URLConnection connection = url.openConnection();
				
				BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
				
				String s = br.readLine();
				
				System.out.println(string + " notified " + s);
			} catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
