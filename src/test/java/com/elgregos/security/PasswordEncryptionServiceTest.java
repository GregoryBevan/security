package com.elgregos.security;

import static org.junit.Assert.assertNotNull;

import javax.ejb.EJB;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.elgregos.security.entities.Group;
import com.elgregos.security.entities.User;
import com.elgregos.security.service.PasswordEncryptionService;

@RunWith(Arquillian.class)
public class PasswordEncryptionServiceTest {

	@Deployment
	public static JavaArchive createDeploymentPackage() {
		return ShrinkWrap
				.create(JavaArchive.class, "test.jar")
				.addClasses(PasswordEncryptionService.class, User.class,
						Group.class, SecurityException.class);
	}

	@EJB
	private PasswordEncryptionService passwordEncryptionService;

	@Test
	public void testGetNewEncryptor() {
		assertNotNull(this.passwordEncryptionService);
	}

	@Test
	public void testSetEncryptedPassword() {
		final User user = new User();
		final String inputPassword = "MyPassword";
		try {
			this.passwordEncryptionService.setEncryptedPassword(user,
					inputPassword);
			String password = user.getPassword();
			assertNotNull(password);
			Assert.assertEquals(128, password.length());
			String salt = user.getSalt();
			assertNotNull(salt);
			Assert.assertEquals(12, salt.length());
		} catch (SecurityException se) {
			Assert.fail(se.getMessage());
		}
	}

}
