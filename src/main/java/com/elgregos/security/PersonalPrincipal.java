package com.elgregos.security;

import java.security.Principal;

import javax.security.auth.Subject;

public class PersonalPrincipal implements Principal {

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean implies(Subject subject) {
		// TODO Auto-generated method stub
		return false;
	}

}
