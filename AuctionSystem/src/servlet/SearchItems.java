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

@WebServlet("/SearchItems")
public class SearchItems extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		Client c = (Client) session.getAttribute("client");
		
		if (c == null){
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return;
		}
		System.out.println(new Date() + "Search items by:" + c.name + " \trequest:" + request.getServerName());
		
		int clientId = c.clientId;
		Connection connection = null ;
		PreparedStatement statement  = null;
		ObjectMapper mapper = new ObjectMapper();
		ArrayList<Item> items = new ArrayList<Item>();
		try {
			
			connection = DbConnection.getConnection();
			String searchStr = request.getParameter("searchStr");
			
			String sql = "Select * from item where Sold <> 'Y' and ClientId <> " + clientId;
			if (searchStr != null && !searchStr.trim().equals("")) {
				sql += " and INSTR(lower(Name), lower(?)) > 0 ";
			}

			statement = connection.prepareStatement(sql);

			if (searchStr != null && !searchStr.trim().equals("")) {
				statement.setString(1, searchStr);
			}
			
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
