import org.junit.After;
import org.junit.Before;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.net.URL;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.saucelabs.common.SauceOnDemandAuthentication;
import com.saucelabs.common.SauceOnDemandSessionIdProvider;
import com.saucelabs.junit.SauceOnDemandTestWatcher;
import org.junit.Rule;
import org.junit.rules.TestName;
import java.util.concurrent.TimeUnit;
import java.util.Date;
import java.io.File;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.*;
import static org.openqa.selenium.OutputType.*;

public class PremierQCRetscriptTest implements SauceOnDemandSessionIdProvider {
    private String sessionId;
    public SauceOnDemandAuthentication authentication = new SauceOnDemandAuthentication("Purush_88", "e497cff7-27f0-4dbc-8d2b-49e1af75a583");
    public @Rule SauceOnDemandTestWatcher resultReportingTestWatcher = new SauceOnDemandTestWatcher(this, authentication);
    public @Rule TestName testName = new TestName();
    @Override public String getSessionId() { return sessionId; }
    RemoteWebDriver wd;
    
    @Before
    public void setUp() throws Exception {
        DesiredCapabilities caps = DesiredCapabilities.chrome();
            caps.setCapability("name", "PremierQCRetscriptTest");
        wd = new RemoteWebDriver(
            new URL("http://Purush_88:e497cff7-27f0-4dbc-8d2b-49e1af75a583@ondemand.saucelabs.com:80/wd/hub"),
            caps);
        wd.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        sessionId = wd.getSessionId().toString();
    }
    
    @Test
    public void PremierQCRetscriptTest() {
        wd.get("https://leqlb030.portal.hewitt.com/web/premieruat/login");
        if (!wd.findElement(By.id("_ParticipantLogon20_WAR_ahcommonauthportlet_skipAACheckbox")).isSelected()) {
            wd.findElement(By.id("_ParticipantLogon20_WAR_ahcommonauthportlet_skipAACheckbox")).click();
        }
        wd.findElement(By.id("_ParticipantLogon20_WAR_ahcommonauthportlet_userId")).click();
        wd.findElement(By.id("_ParticipantLogon20_WAR_ahcommonauthportlet_userId")).clear();
        wd.findElement(By.id("_ParticipantLogon20_WAR_ahcommonauthportlet_userId")).sendKeys("000HEW00044020");
        wd.findElement(By.id("_ParticipantLogon20_WAR_ahcommonauthportlet_password")).click();
        wd.findElement(By.id("_ParticipantLogon20_WAR_ahcommonauthportlet_password")).clear();
        wd.findElement(By.id("_ParticipantLogon20_WAR_ahcommonauthportlet_password")).sendKeys("99999999");
        wd.findElement(By.id("_ParticipantLogon20_WAR_ahcommonauthportlet_testCfgList[0].cfgValue")).click();
        wd.findElement(By.id("_ParticipantLogon20_WAR_ahcommonauthportlet_testCfgList[0].cfgValue")).clear();
        wd.findElement(By.id("_ParticipantLogon20_WAR_ahcommonauthportlet_testCfgList[0].cfgValue")).sendKeys("L77B");
        wd.findElement(By.id("_ParticipantLogon20_WAR_ahcommonauthportlet_testCfgList[1].cfgValue")).click();
        wd.findElement(By.id("_ParticipantLogon20_WAR_ahcommonauthportlet_testCfgList[1].cfgValue")).clear();
        wd.findElement(By.id("_ParticipantLogon20_WAR_ahcommonauthportlet_testCfgList[1].cfgValue")).sendKeys("M6hb");
        wd.findElement(By.id("_ParticipantLogon20_WAR_ahcommonauthportlet_logOn")).click();
        try { Thread.sleep(4000l); } catch (Exception e) { throw new RuntimeException(e); }
        wd.findElement(By.id("ahDialogCloseBtn")).click();
        wd.findElement(By.linkText("Savings & Retirement")).click();
        try { Thread.sleep(4000l); } catch (Exception e) { throw new RuntimeException(e); }
        if (!wd.findElement(By.tagName("html")).getText().contains("Summary")) {
            System.out.println("verifyTextPresent failed");
        }
        wd.findElement(By.linkText("401(k) Savings Plan")).click();
        wd.findElement(By.linkText("Investments")).click();
        try { Thread.sleep(4000l); } catch (Exception e) { throw new RuntimeException(e); }
        if (!wd.findElement(By.tagName("html")).getText().contains("Portfolio for")) {
            System.out.println("verifyTextPresent failed");
        }
        if (!wd.findElement(By.tagName("html")).getText().contains("Pension")) {
            System.out.println("verifyTextPresent failed");
        }
        wd.findElement(By.linkText("Pension")).click();
        wd.findElement(By.linkText("Retirement Hub")).click();
        try { Thread.sleep(4000l); } catch (Exception e) { throw new RuntimeException(e); }
        if (!wd.findElement(By.tagName("html")).getText().contains("Your Retirement Hub")) {
            System.out.println("verifyTextPresent failed");
        }
        if (!wd.findElement(By.tagName("html")).getText().contains("Financial Education")) {
            System.out.println("verifyTextPresent failed");
        }
        wd.findElement(By.linkText("Financial Education")).click();
        wd.findElement(By.linkText("Getting to Retirement")).click();
        try { Thread.sleep(4000l); } catch (Exception e) { throw new RuntimeException(e); }
        if (!wd.findElement(By.tagName("html")).getText().contains("Getting to Retirement")) {
            System.out.println("verifyTextPresent failed");
        }
        wd.findElement(By.linkText("Log Off")).click();
    }
    
    @After
    public void tearDown() {
        wd.quit();
    }
    
    public static boolean isAlertPresent(FirefoxDriver wd) {
        try {
            wd.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }
}
