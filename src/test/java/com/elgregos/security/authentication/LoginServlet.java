package com.elgregos.security.authentication;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/login")
public class LoginServlet extends HttpServlet {

	@Inject
	private SampleEJB sampleEJB;

	@Override
	protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
		processRequest(req, resp);
	}

	@Override
	protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
		processRequest(req, resp);
	}

	private void processRequest(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		try {
			final String username = request.getParameter("username");
			final String password = request.getParameter("password");

			if (username != null && password != null) {
				request.login(username, password);
			}

			final PersonalPrincipal principal = (PersonalPrincipal) request.getUserPrincipal();
			response.getWriter().println("principal=" + request.getUserPrincipal().getClass().getSimpleName());
			response.getWriter().println("username=" + sampleEJB.getPrincipalName());

		} catch (final ServletException e) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
		}
	}

}
