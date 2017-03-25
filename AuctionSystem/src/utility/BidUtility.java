package utility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import beans.Item;
import db.DbConnection;

public class BidUtility {

	public static Item getCurrentBid(Item item){
		String sql = "Select amount, c.Name as clientName from bid b "
				+ "left outer join client c on b.ClientId = c.ClientId "
				+ "where LogicalCounter = "
				+ "(select max(LogicalCounter) from bid where ItemId=?)";
		try (	Connection connection = DbConnection.getConnection() ;
				PreparedStatement statement  = connection.prepareStatement(sql)){

			statement.setInt(1, item.id);
			
			ResultSet rs = statement.executeQuery();
			
			if (rs.next()) {
				item.currentbid = rs.getDouble("amount");
				item.soldTo = rs.getString("clientName");
			}
			
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return item;
	}

	public static int getLogicalCounter() {
		int counter = -1;
		String sql = "select max(LogicalCounter) + 1 from bid";
		try (	Connection connection = DbConnection.getConnection() ;
				PreparedStatement statement  = connection.prepareStatement(sql)){

			ResultSet rs = statement.executeQuery();
			
			if (rs.next())
				counter = rs.getInt(1);
			
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return counter;
	}
	
}
