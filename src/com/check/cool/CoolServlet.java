package com.check.cool;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.check.cool_model.Stamp;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;

@SuppressWarnings("serial")
public class CoolServlet extends HttpServlet {

	private DatastoreService ds;

	public CoolServlet() {
		ds = DatastoreServiceFactory.getDatastoreService();
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		// User user = null;
		// try {
		// OAuthService oauth = OAuthServiceFactory.getOAuthService();
		RequestDispatcher rd;
		// if (oauth.isUserAdmin()) {
		// rd = req.getRequestDispatcher("/_ah/OAuthAuthorizeToken");
		// } else {
		// user = oauth.getCurrentUser();
		req.setAttribute("user", UUID.randomUUID());
//		ArrayList<Stamp> list = getList();
		req.setAttribute("stampList", ds.prepare(new Query("LIST").addSort("date", SortDirection.DESCENDING)).asIterator());
		rd = req.getRequestDispatcher("/WEB-INF/view/list.jsp");
		// }
		try {
			rd.forward(req, resp);
		} catch (ServletException e) {
			e.printStackTrace();
			resp.getWriter().println("SYSTEM IS BUSY.");
		}
		// } catch (OAuthRequestException e) {
		// resp.getWriter().println("THERE ARE SOME ERROR.");
		// }
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		Entity e = new Entity("LIST");
		e.setProperty("name", req.getParameter("name"));
		e.setProperty("date",
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		e.setProperty("alt", req.getParameter("alt"));
		e.setProperty("lon", req.getParameter("lon"));
 		ds.put(e);
		resp.sendRedirect("/");
	}

	private ArrayList<Stamp> getList() {
		ArrayList<Stamp> list = new ArrayList<Stamp>();
		Query q = new Query("LIST");
		PreparedQuery pq = ds.prepare(q);
		for (Entity result : pq.asIterable()) {
			Stamp stamp = Stamp.build(result);
			list.add(stamp);
		}
		return list;
	}

}
