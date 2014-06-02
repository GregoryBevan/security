package com.elgregos.security.authentication;

import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

public class PersonalLoginModule implements LoginModule {

	private Subject subject;
	private CallbackHandler callbackHandler;
	private Map<String, ?> sharedState;
	private Map<String, ?> options;
	private PersonalPrincipal personalPrincipal;

	@Override
	public void initialize(final Subject subject, final CallbackHandler callbackHandler, final Map<String, ?> sharedState,
			final Map<String, ?> options) {
		System.out.println("init login module");
		this.subject = subject;
		this.callbackHandler = callbackHandler;
		this.sharedState = sharedState;
		this.options = options;
	}

	@Override
	public boolean login() throws LoginException {
		System.out.println("login");
		return false;
	}

	@Override
	public boolean commit() throws LoginException {
		System.out.println("commit");
		return false;
	}

	@Override
	public boolean abort() throws LoginException {
		System.out.println("abort");
		return false;
	}

	@Override
	public boolean logout() throws LoginException {
		System.out.println("abort");
		return false;
	}

}
