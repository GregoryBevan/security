package com.elgregos.security.authentication;

import java.io.IOException;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import lombok.extern.slf4j.Slf4j;

import com.elgregos.security.service.LoginVerifier;

@Slf4j
public class PersonalLoginModule implements LoginModule {

	private Subject subject;
	private CallbackHandler callbackHandler;
	private Map<String, ?> sharedState;
	private Map<String, ?> options;
	private PersonalPrincipal personalPrincipal;

	private LoginVerifier loginVerifier;

	private boolean succeeded = false;

	@Override
	public void initialize(final Subject subject, final CallbackHandler callbackHandler, final Map<String, ?> sharedState,
	        final Map<String, ?> options) {
		System.out.println("init login module");
		this.subject = subject;
		this.callbackHandler = callbackHandler;
		this.sharedState = sharedState;
		this.options = options;

		try {
			loginVerifier = (LoginVerifier) new InitialContext().lookup("java:global/security/service/LoginVerifier");
		} catch (final NamingException e) {
			throw new IllegalStateException("Can't lookup LoginVerifier service");
		}
	}

	@Override
	public boolean login() throws LoginException {
		System.out.println("login");
		if (callbackHandler == null) {
			throw new LoginException("CallbackHandler can't be null");
		}
		final Callback[] callbacks = new Callback[2];
		callbacks[0] = new NameCallback("name:");
		callbacks[1] = new PasswordCallback("password:", false);

		try {
			callbackHandler.handle(callbacks);
		} catch (IOException | UnsupportedCallbackException e) {
			throw new LoginException("CallbackHandler error");
		}

		final String email = ((NameCallback) callbacks[0]).getName();
		final PasswordCallback passwordCallback = (PasswordCallback) callbacks[1];
		final String password = String.valueOf(passwordCallback.getPassword());
		passwordCallback.clearPassword();

		succeeded = loginVerifier.checkLogin(email, password);

		return succeeded;
	}

	private void createIdentity(final String username) {
		if (personalPrincipal == null) {
			personalPrincipal = new PersonalPrincipal(username);
		}
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
