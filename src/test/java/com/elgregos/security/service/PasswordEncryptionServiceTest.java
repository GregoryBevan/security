package com.elgregos.security.service;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.elgregos.security.data.entities.Role;
import com.elgregos.security.data.entities.UserProfile;
import com.elgregos.test.arquillian.EarDeployment;

@RunWith(Arquillian.class)
public class PasswordEncryptionServiceTest {

	@Deployment
	public static Archive<?> createDeploymentPackage() {
		return new EarDeployment("security.ear") {
			{
				this.ejbModule.addClasses(PasswordEncryptionService.class, SecurityException.class, UserProfile.class, Role.class);
			}
		}.create();
	}

	@Inject
	private PasswordEncryptionService passwordEncryptionService;

	@Test
	public void testPasswordEncryptionServiceInjection() {
		Assert.assertNotNull(this.passwordEncryptionService);
	}

	@Test
	public void testSetEncryptedPassword() {
		final UserProfile user = new UserProfile();
		user.setPassword("MyPassword");
		try {
			this.passwordEncryptionService.setEncryptedPassword(user);
			final String password = user.getPassword();
			Assert.assertNotNull(password);
			Assert.assertEquals(128, password.length());
			final String salt = user.getSalt();
			Assert.assertNotNull(salt);
			Assert.assertEquals(12, salt.length());
		} catch (final SecurityException se) {
			Assert.fail(se.getMessage());
		}
	}

}
