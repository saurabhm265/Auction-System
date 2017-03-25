package servlet;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/SetBidCounter")
public class SetBidCounter extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext context = getServletConfig().getServletContext();
		int c = Integer.parseInt(request.getParameter(("n")));
		context.setAttribute("BidCounter", c);
		
		System.out.println("Counter received: " + c + " from " + request.getRemoteHost());
		response.getWriter().println("Set!");
	}
}
