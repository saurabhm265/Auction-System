package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.Client;
import db.DbConnection;

@WebServlet("/Sold")
public class Sold extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Connection connection = null ;
		PreparedStatement statement  = null;
		HttpSession session = request.getSession();
		Client c = (Client) session.getAttribute("client");
		
		if (c == null){
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return;
		}
		System.out.println(new Date() + "Sold item by:" + c.name + " \trequest:" + request.getServerName());
		
		try {
			
			connection = DbConnection.getConnection();

			String sql = "Update Item set Sold = 'Y' where ItemId = ? and ClientId = ?";
			statement = connection.prepareStatement(sql);
			
			statement.setInt(1, Integer.parseInt(request.getParameter("item_id")));
			statement.setInt(2, c.clientId);
			
			int result = statement.executeUpdate();

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
