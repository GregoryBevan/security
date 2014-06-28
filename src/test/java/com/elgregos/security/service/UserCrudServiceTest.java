package com.elgregos.security.service;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.elgregos.security.data.crud.UserCrudService;
import com.elgregos.security.data.entities.Group;
import com.elgregos.security.data.entities.User;
import com.elgregos.test.arquillian.EarDeployment;

@RunWith(Arquillian.class)
public class UserCrudServiceTest {

	@Deployment
	public static Archive<?> createDeploymentPackage() {
		return new EarDeployment("user.ear") {
			{
				webArchive.addAsWebInfResource("test-ds.xml");
				ejbModule.addClasses(UserManagementService.class, UserCrudService.class, PasswordEncryptionService.class).addAsManifestResource(
						EmptyAsset.INSTANCE, "beans.xml");
				earLibraries.add(ShrinkWrap.create(JavaArchive.class, "user.jar").addPackage(User.class.getPackage())
						.addAsManifestResource("test-persistence.xml", "persistence.xml"));
				earLibraries.add(ShrinkWrap.createFromZipFile(JavaArchive.class, getJarFile(UserCrudService.class)));
			}
		}.create();
	}

	private static File getJarFile(final Class<?> childClass) {
		try {
			return new File(URLDecoder.decode(childClass.getSuperclass().getProtectionDomain().getCodeSource().getLocation().getFile(), "UTF-8"));
		} catch (final UnsupportedEncodingException e) {
			Assert.fail(e.getMessage());
			return null;
		}
	}

	@Inject
	private UserManagementService userManagementService;

	@Test
	@Ignore
	public void testFailed() {
		Assert.fail();
	}

	@Test
	public void createUserTest() {
		final User user = new User();
		user.setEmail("fanny.duhem@gmail.com");
		user.setFirstname("Fanny");
		user.setLastname("Duhem");
		user.setPassword("MyPassword");
		user.setRegisteredOn(new Date());
		user.addGroup(Group.ADMINISTRATOR);
		userManagementService.createUser(user);
		Assert.assertNotNull(user.getSalt());
		Assert.assertEquals(128, user.getPassword().length());
	}
}
