package com.elgregos.security.service;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.elgregos.security.data.crud.UserCrudService;
import com.elgregos.security.data.entities.User;

@Stateless
public class UserManagementService {

	@Inject
	private UserCrudService userCrudService;

	@Inject
	private PasswordEncryptionService passwordEncryptionService;

	public void createUser(final User user) {
		this.passwordEncryptionService.setEncryptedPassword(user);
		this.userCrudService.create(user);
	}

}
