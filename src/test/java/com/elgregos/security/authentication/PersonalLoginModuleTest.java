package com.elgregos.security.authentication;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
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
		final URL configureSecurityScriptURL = PersonalLoginModuleTest.class.getClassLoader().getResource(
				PersonalLoginModuleTest.CONFIGURE_SECURITY_SCRIPT);
		PersonalLoginModuleTest.configureSecurityScript = new File(configureSecurityScriptURL.getFile());
		final URL removeSecurityScriptURL = PersonalLoginModuleTest.class.getClassLoader()
				.getResource(PersonalLoginModuleTest.REMOVE_SECURITY_SCRIPT);
		PersonalLoginModuleTest.removeSecurityScript = new File(removeSecurityScriptURL.getPath());
	}

	@Deployment
	public static Archive<?> createDeployment() {
		PersonalLoginModuleTest.setUp();
		WildFlyCliInvoker.newInstance().processCliScript(PersonalLoginModuleTest.configureSecurityScript);
		return new EarDeployment("security.ear") {
			{
				webArchive.addClass(LoginServlet.class).addAsWebInfResource("jboss-web.xml");
				ejbModule.addClasses(PasswordEncryptionService.class, SampleEJB.class).addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
				        .addAsManifestResource("jboss-ejb3.xml");
				earLibraries.add(ShrinkWrap.create(JavaArchive.class).addClasses(PersonalPrincipal.class, PersonalLoginModule.class)
				        .addPackage(User.class.getPackage()).addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml"));
				addGradleDependency("org.apache.httpcomponents:httpclient:4.3.4", true);
			}
		}.create();
	}

	@Test
	public void test() {
		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			final URI loginURI = new URIBuilder().setScheme("http").setHost("localhost").setPort(8080).setPath("/web/login")
					.setParameter("username", "gregory").setParameter("password", "mypassword").build();
			final HttpGet httpGet = new HttpGet(loginURI);
			final CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
			Assert.assertEquals(200, httpResponse.getStatusLine().getStatusCode());
		}
		// assertTrue(httpResponse.getEntity()getText().contains("principal=" +
		// CustomPrincipal.class.getSimpleName()));
		// assertTrue(response.getText().contains("username=username"));
		// assertTrue(response.getText().contains("description=An user description!"));
		catch (final IOException | URISyntaxException e) {
			Assert.fail();
		}
	}

	@AfterClass
	public static void tearDown() {
		WildFlyCliInvoker.newInstance().processCliScript(PersonalLoginModuleTest.removeSecurityScript);
	}

}
