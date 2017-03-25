package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import utility.BidUtility;
import beans.Client;
import beans.Item;

import com.fasterxml.jackson.databind.ObjectMapper;

import db.DbConnection;

@WebServlet("/BidWins")
public class BidWins extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession();
		Client c = (Client) session.getAttribute("client");
		
		if (c == null){
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return;
		}
		System.out.println(new Date() + "Bid won items by:" + c.name + " \trequest:" + request.getServerName());
		
		int clientId = c.clientId;
		Connection connection = null ;
		PreparedStatement statement  = null;
		ObjectMapper mapper = new ObjectMapper();
		ArrayList<Item> items = new ArrayList<Item>();
		try {
			
			connection = DbConnection.getConnection();
			
			String sql = "Select i.ItemId as ItemId, i.Name as Name, "
					+ "i.Description as Description, i.MinBid as MinBid from item i "
					+ "inner join bid b on i.ItemId = b.ItemId "
					+ "inner join client c on c.ClientId = b.ClientId "
					+ "where Sold = 'Y' and b.LogicalCounter = "
					+ "(select max(LogicalCounter) from bid where ItemId = i.ItemId) "
					+ "and c.ClientId = " + clientId;

			statement = connection.prepareStatement(sql);
			
			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				Item item = new Item();
				item.id = rs.getInt("ItemId");
				item.name = rs.getString("Name");
				item.description = rs.getString("Description");
				item.minBid = rs.getDouble("MinBid");
				item = BidUtility.getCurrentBid(item);
				items.add(item);
			}
			rs.close();
			
			response.setContentType("application/json");
			
			PrintWriter out = response.getWriter();
			out.print(mapper.writeValueAsString(items));
			out.flush();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				connection.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		
	}

}
