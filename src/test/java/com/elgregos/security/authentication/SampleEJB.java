package com.elgregos.security.authentication;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;

@Stateless
@RolesAllowed("USER")
public class SampleEJB {

	@Resource
	private EJBContext ejbContext;

	public String getPrincipalName() {
		return this.ejbContext.getCallerPrincipal().getName();
	}

}
