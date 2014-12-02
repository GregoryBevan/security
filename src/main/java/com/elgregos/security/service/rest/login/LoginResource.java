package com.elgregos.security.service.rest.login;

import javax.enterprise.context.RequestScoped;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RequestScoped
@Path("/login")
public class LoginResource {

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(@FormParam("email") final String email, @FormParam("password") final String password, @Context final HttpServletRequest req) {
		final String username = email;
		if (req.getUserPrincipal() == null) {
			try {
				req.login(username, password);
				System.out.println(req.getUserPrincipal().toString());
			} catch (final ServletException e) {
				e.printStackTrace();
				return Response.status(Response.Status.UNAUTHORIZED).build();
			}
		} else {
			System.out.println(req.getUserPrincipal().toString());
			req.getServletContext().log("Skip logged because already logged in: " + username);
		}

		req.getServletContext().log("Authentication Demo: successfully retrieved User Profile from DB for " + username);
		return Response.ok().build();
	}

}
