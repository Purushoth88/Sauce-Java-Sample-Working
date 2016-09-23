package com.saucelabs.test.Utils;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.junit.Rule;
import org.junit.rules.TestName;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.saucelabs.common.SauceOnDemandAuthentication;
import com.saucelabs.common.SauceOnDemandSessionIdProvider;
import com.saucelabs.junit.SauceOnDemandTestWatcher;

/**
 * @author Ah0144719
 *
 */
public class SauceRunner implements SauceOnDemandSessionIdProvider {
	
    private static String sessionId;
    public static String username = "Purush_88";
    public static String accesskey = "e497cff7-27f0-4dbc-8d2b-49e1af75a583";
    public SauceOnDemandAuthentication authentication = new SauceOnDemandAuthentication(username, accesskey);
    public @Rule SauceOnDemandTestWatcher resultReportingTestWatcher = new SauceOnDemandTestWatcher(this, authentication);
    public @Rule TestName testName = new TestName();
    public String getSessionId() { return sessionId; }
    static RemoteWebDriver wd;
	
	public static Object sauceCapabilities() throws Exception {

		DesiredCapabilities caps = DesiredCapabilities.chrome();
		caps.setCapability("name", "PremierQCScriptTestJU");
		String seleniumURI = buildSauceUri();
		wd =  new RemoteWebDriver(
                new URL("https://" + username+ ":" + accesskey + seleniumURI +"/wd/hub"),
                caps);
		wd.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		sessionId = wd.getSessionId().toString();

		System.out.println("Appium SetUp for Android is successful and Appium Driver is launched successfully");
		return wd;
	}
	
	public static String buildSauceUri() {
        String seleniumURI = "@ondemand.saucelabs.com:443";
        String seleniumPort = System.getenv("SELENIUM_PORT");
        String seleniumHost = System.getenv("SELENIUM_HOST");
        if (seleniumPort != null &&
                seleniumHost != null &&
                !seleniumHost.contentEquals("ondemand.saucelabs.com")) {
            //While running in CI, if Sauce Connect is running the SELENIUM_PORT env var will be set.
            //use SC relay port
            seleniumURI = String.format("@localhost:%s", seleniumPort);

        }
        return seleniumURI;
    }

}
