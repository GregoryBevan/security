package com.elgregos.security;

import java.util.Date;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.elgregos.security.entities.Group;
import com.elgregos.security.entities.User;
import com.elgregos.security.service.PasswordEncryptionService;
import com.elgregos.security.service.UserBean;

@RunWith(Arquillian.class)
public class UserBeanTest {

	@Deployment
	public static JavaArchive createDeploymentPackage() {
		return ShrinkWrap.create(JavaArchive.class, "my.jar").addPackages(true, UserBean.class.getPackage())
				.addAsManifestResource("test-persistence.xml", "persistence.xml").addAsManifestResource("test-ds.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@Inject
	private UserBean userBean;

	@Inject
	private PasswordEncryptionService passwordEncryptionService;

	@Test
	public void createUserTest() {
		final User user = new User();
		user.setEmail("gregory_bevan@hotmail.com");
		user.setFirstName("Grégory");
		user.setLastname("Bévan");
		try {
			this.passwordEncryptionService.setEncryptedPassword(user, "MonMotDePasse");
		} catch (SecurityException securityException) {
			Assert.fail(securityException.getMessage());
		}
		user.setRegisteredOn(new Date());
		user.addGroup(Group.ADMINISTRATOR);

		this.userBean.createUser(user);
	}

}
