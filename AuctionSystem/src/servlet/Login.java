package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int result = -1 ;
		PrintWriter out = response.getWriter();
		Client c = null;
		String sql = "Select * from client where upper(LoginId) = upper(?) and Password = ?";
		try (	Connection connection = DbConnection.getConnection() ;
				PreparedStatement statement  = connection.prepareStatement(sql)){

			statement.setString(1, request.getParameter("login_id"));
			statement.setString(2, request.getParameter("login_password"));
			
			ResultSet rs = statement.executeQuery();
			
			if (rs.next()) {
				c = new Client();
				result = c.clientId = rs.getInt("ClientId");
				c.name = rs.getString("Name");

				System.out.println(new Date() + "Login by:" + c.name + " \trequest:" + request.getServerName());
			}
			
			HttpSession session = request.getSession();
			session.setAttribute("client", c);
			
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		out.print(result);
		out.flush();
	}

}
