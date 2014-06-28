package com.elgregos.security.service;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.elgregos.security.data.entities.Group;
import com.elgregos.security.data.entities.User;
import com.elgregos.test.arquillian.EarDeployment;

@RunWith(Arquillian.class)
public class PasswordEncryptionServiceTest {

	@Deployment
	public static Archive<?> createDeploymentPackage() {
		return new EarDeployment("security.ear") {
			{
				ejbModule.addClasses(PasswordEncryptionService.class, SecurityException.class, User.class, Group.class);
			}
		}.create();
	}

	@Inject
	private PasswordEncryptionService passwordEncryptionService;

	@Test
	public void testPasswordEncryptionServiceInjection() {
		Assert.assertNotNull(passwordEncryptionService);
	}

	@Test
	public void testSetEncryptedPassword() {
		final User user = new User();
		user.setPassword("MyPassword");
		try {
			passwordEncryptionService.setEncryptedPassword(user);
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
