package com.elgregos.security.authentication;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

public class PersonalCallbackHandler implements CallbackHandler {
	
	private String login;
	private String password;

	public PersonalCallbackHandler(final String login, final String password) {
		this.login = login;
		this.password = password;
	}

	@Override
	public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
		// TODO Auto-generated method stub

	}

}
