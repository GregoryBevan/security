package com.elgregos.security.authentication;

import java.io.File;
import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.elgregos.security.data.entities.User;
import com.elgregos.security.service.PasswordEncryptionService;
import com.elgregos.test.arquillian.EarDeployment;
import com.elgregos.test.arquillian.wildfly.WildFlyCliInvoker;

@RunWith(Arquillian.class)
public class PersonalLoginModuleTest {

	private static final String REMOVE_SECURITY_SCRIPT = "remove-security.cli";

	private static final String CONFIGURE_SECURITY_SCRIPT = "configure-security.cli";

	private static File configureSecurityScript;

	private static File removeSecurityScript;

	public static void setUp() {
		final URL configureSecurityScriptURL = PersonalLoginModuleTest.class.getClassLoader().getResource(CONFIGURE_SECURITY_SCRIPT);
		configureSecurityScript = new File(configureSecurityScriptURL.getFile());
		final URL removeSecurityScriptURL = PersonalLoginModuleTest.class.getClassLoader().getResource(REMOVE_SECURITY_SCRIPT);
		removeSecurityScript = new File(removeSecurityScriptURL.getPath());
	}

	@Deployment
	public static Archive<?> createDeployment() {
		setUp();
		WildFlyCliInvoker.newInstance().processCliScript(configureSecurityScript);
		return new EarDeployment("security.ear") {
			{
				this.webArchive.addClass(LoginServlet.class).addAsWebInfResource("jboss-web.xml");
				this.ejbModule.addClasses(PasswordEncryptionService.class, SampleEJB.class).addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
						.addAsManifestResource("jboss-ejb3.xml");
				this.earLibraries.add(ShrinkWrap.create(JavaArchive.class).addClasses(PersonalPrincipal.class, PersonalLoginModule.class)
						.addPackage(User.class.getPackage()).addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml"));
			}
		}.create();
	}

	@Test
	public void test() {
		Assert.fail();
	}

	@AfterClass
	public static void tearDown() {
		WildFlyCliInvoker.newInstance().processCliScript(removeSecurityScript);

	}

}
