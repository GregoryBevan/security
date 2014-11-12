package com.elgregos.security.service;

import javax.ejb.Singleton;
import javax.inject.Inject;

import com.elgregos.security.data.crud.UserCrudService;
import com.elgregos.security.data.entities.Role;
import com.elgregos.security.data.entities.User;

@Singleton
public class LoginVerifier {

	@Inject
	private UserCrudService userCrudService;

	@Inject
	private PasswordEncryptionService passwordEncryptionService;

	public boolean checkLogin(final String email, final String password) {
		final User user = this.userCrudService.find(email);
		if (user == null) {
			return false;
		}
		final String encryptedPassword = this.passwordEncryptionService.getEncryptedPassword(password, user.getSalt());
		if (user.getPassword().equals(encryptedPassword)) {
			return true;
		}
		return false;
	}

	public Role[] getUserRoles(final String email) {
		return this.userCrudService.getRoles(email);
	}

}
