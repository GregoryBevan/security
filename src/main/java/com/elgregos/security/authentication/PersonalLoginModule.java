package com.elgregos.security.authentication;

import java.io.IOException;
import java.security.Principal;
import java.security.acl.Group;
import java.util.Map;
import java.util.Set;

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

import com.elgregos.security.data.entities.Role;
import com.elgregos.security.service.LoginVerifier;

@Slf4j
public class PersonalLoginModule implements LoginModule {

	private Subject subject;
	private CallbackHandler callbackHandler;
	private Map<String, ?> sharedState;
	private Map<String, ?> options;
	private UserPrincipal userPrincipal;

	private LoginVerifier loginVerifier;

	private boolean succeeded = false;

	@Override
	public void initialize(final Subject subject, final CallbackHandler callbackHandler, final Map<String, ?> sharedState,
			final Map<String, ?> options) {
		log.info("init login module");
		this.subject = subject;
		this.callbackHandler = callbackHandler;
		this.sharedState = sharedState;
		this.options = options;

		try {
			this.loginVerifier = (LoginVerifier) new InitialContext().lookup("java:global/security/service/LoginVerifier");
		} catch (final NamingException e) {
			throw new IllegalStateException("Can't lookup LoginVerifier service");
		}
	}

	@Override
	public boolean login() throws LoginException {
		System.out.println("login");
		if (this.callbackHandler == null) {
			throw new LoginException("CallbackHandler can't be null");
		}
		final Callback[] callbacks = new Callback[2];
		callbacks[0] = new NameCallback("name:");
		callbacks[1] = new PasswordCallback("password:", false);

		try {
			this.callbackHandler.handle(callbacks);
		} catch (IOException | UnsupportedCallbackException e) {
			throw new LoginException("CallbackHandler error");
		}

		final String email = ((NameCallback) callbacks[0]).getName();
		final PasswordCallback passwordCallback = (PasswordCallback) callbacks[1];
		final String password = String.valueOf(passwordCallback.getPassword());
		passwordCallback.clearPassword();

		this.succeeded = this.loginVerifier.checkLogin(email, password);

		if (this.succeeded) {
			createIdentity(email);
		}

		return this.succeeded;
	}

	private void createIdentity(final String username) {
		if (this.userPrincipal == null) {
			this.userPrincipal = new UserPrincipal(username);
		}
	}

	@Override
	public boolean commit() throws LoginException {
		System.out.println("commit");
		final Set<Principal> principals = this.subject.getPrincipals();
		if (!principals.contains(this.userPrincipal)) {
			principals.add(this.userPrincipal);
		}

		final Role[] roleList = getRoles(this.userPrincipal);

		// principals.add(new RolePrincipal(Role.USER));

		final Group roles = new PersonalGroup("Roles");
		for (final Role role : roleList) {
			final Group group = new PersonalGroup(role.toString());
			group.addMember(this.userPrincipal);
			roles.addMember(group);
		}
		principals.add(roles);
		// roles.add(Role.USER);
		// for (final Role role : roles) {
		// principals.add(new RolePrincipal(role));
		// }
		return true;
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

	private Role[] getRoles(final UserPrincipal principal) {
		return this.loginVerifier.getUserRoles(principal.getName());
	}
}
