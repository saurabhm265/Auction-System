package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.Client;
import db.DbConnection;

@WebServlet("/CreateAccount")
public class CreateAccount extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int result = -1 ;
		PrintWriter out = response.getWriter();
		Connection connection = null ;
		PreparedStatement statement  = null;
		try {
			
			connection = DbConnection.getConnection();

			String sql = "Insert into client(Name, LoginId, Password) values(?,?,?)";
			statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			statement.setString(1, request.getParameter("create_name"));
			statement.setString(2, request.getParameter("login_id"));
			statement.setString(3, request.getParameter("create_password"));
			
			result = statement.executeUpdate();

			ResultSet rs = statement.getGeneratedKeys();
			
			if (rs.next()) {
				result = rs.getInt(1);
			}
			
			Client client = new Client();
			client.clientId = result;
			client.name = request.getParameter("create_name");
			
			System.out.println(new Date() + "Create Account by :" + client.name + " request:" + request.getServerName());

			HttpSession session = request.getSession();
			session.setAttribute("client", client);
			
			
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
		out.print(result);
		out.flush();


	}

}
