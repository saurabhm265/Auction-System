package servlet;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import utility.BidUtility;
import utility.Sync;
import beans.Client;
import db.DbConnection;

@WebServlet("/Bid")
public class Bid extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession();
		Client c = (Client) session.getAttribute("client");
		
		ServletContext context = getServletConfig().getServletContext();
		
		Integer counter_i = (Integer) context.getAttribute("BidCounter");
		
		int counter;
		
		if (counter_i != null) {
			counter = counter_i;
		} else {
			counter = BidUtility.getLogicalCounter();
		}
		
		if (c == null){
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return;
		}
		
		System.out.println(new Date() + "Bid by :" + c.name + " request:" + request.getServerName());
		
		int clientId = c.clientId;
		Connection connection = null ;
		PreparedStatement statement  = null;
		try {
			
			connection = DbConnection.getConnection();
			
			String sql = "Insert into bid(LogicalCounter, ItemId, ClientId, Amount) values(?,?,?,?)";
			statement = connection.prepareStatement(sql);
			
			
			statement.setInt(1, counter);
			statement.setInt(2, Integer.parseInt(request.getParameter("itemId")));
			statement.setInt(3, clientId);
			statement.setDouble(4, Double.parseDouble(request.getParameter("amount")));
			
			int result = statement.executeUpdate();
			
			if (result > 0) {
				context.setAttribute("BidCounter", ++counter);
				Sync.notifyCounter(counter);
			}
			
			PrintWriter out = response.getWriter();
			out.print(result);
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
