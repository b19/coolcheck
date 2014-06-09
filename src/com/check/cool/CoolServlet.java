package com.check.cool;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;


@SuppressWarnings("serial")
public class CoolServlet extends HttpServlet {

	private DatastoreService ds;

	public CoolServlet() {
		ds = DatastoreServiceFactory.getDatastoreService();
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String pathInfo = req.getServletPath().toString();
		
		if(pathInfo.equals("/signin")) {
			getSignin(req, resp);
			return;
		}
		
		RequestDispatcher rd;
		req.setAttribute("user", UUID.randomUUID());
		req.setAttribute("stampList", ds.prepare(new Query("LIST").addSort("date", SortDirection.DESCENDING)).asIterator());
		rd = req.getRequestDispatcher("/WEB-INF/view/list.jsp");
		
		try {
			rd.forward(req, resp);
		} catch (ServletException e) {
			e.printStackTrace();
			resp.getWriter().println("SYSTEM IS BUSY.");
		}
	}

	private void getSignin(HttpServletRequest req, HttpServletResponse resp) {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		resp.setContentType("text/html");
		try {
			resp.getWriter().println("<h2>GAE - Integrating Google user account</h2>");
		
			if (user != null) {
				resp.getWriter().println("Welcome, " + user.getNickname());
				resp.getWriter().println(
					"<a href='"
						+ userService.createLogoutURL(req.getRequestURI())
						+ "'> LogOut </a>");
			} else {
	 
				resp.getWriter().println(
					"Please <a href='"
						+ userService.createLoginURL(req.getRequestURI())
						+ "'> LogIn </a>");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void deleteAll() {
		Query q = new Query("LIST");
		PreparedQuery pq = ds.prepare(q);
		for (Entity result : pq.asIterable()) {
			ds.delete(result.getKey());
		}
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
		req.setCharacterEncoding("UTF-8");
		Entity e = new Entity("LIST");
		e.setProperty("name", req.getParameter("name"));
		e.setProperty("date",
				sdf.format(new Date()));
		e.setProperty("lat", req.getParameter("lat"));
		e.setProperty("lon", req.getParameter("lon"));
 		ds.put(e);
		resp.sendRedirect("/");
	}

}
