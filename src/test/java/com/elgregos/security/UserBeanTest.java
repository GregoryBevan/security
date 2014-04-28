package com.elgregos.security;

import java.util.Date;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
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
import com.elgregos.test.arquillian.EarDeployment;

@RunWith(Arquillian.class)
public class UserBeanTest {

	@Deployment
	public static Archive<?> createDeploymentPackage() {
		return new EarDeployment("user.ear") {
			{
				webArchive.addAsWebInfResource("test-ds.xml");
				ejbModule.addClasses(UserBean.class,PasswordEncryptionService.class).addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
				earLibraries.add(ShrinkWrap.create(JavaArchive.class, "user.jar").addPackage(User.class.getPackage())
						.addAsManifestResource("test-persistence.xml", "persistence.xml"));
			}
		}.create();
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
