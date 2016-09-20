package com.saucelabs;

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

public class QCTest implements SauceOnDemandSessionIdProvider {
    private String sessionId;
    public SauceOnDemandAuthentication authentication = new SauceOnDemandAuthentication("Purush_88", "e497cff7-27f0-4dbc-8d2b-49e1af75a583");
    public @Rule SauceOnDemandTestWatcher resultReportingTestWatcher = new SauceOnDemandTestWatcher(this, authentication);
    public @Rule TestName testName = new TestName();
    @Override public String getSessionId() { return sessionId; }
    RemoteWebDriver wd;
    
    @Before
    public void setUp() throws Exception {
        DesiredCapabilities caps = DesiredCapabilities.chrome();
            caps.setCapability("name", "QCTest");
        wd = new RemoteWebDriver(
            new URL("http://Purush_88:e497cff7-27f0-4dbc-8d2b-49e1af75a583@ondemand.saucelabs.com:80/wd/hub"),
            caps);
        wd.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        sessionId = wd.getSessionId().toString();
    }
    
    @Test
    public void QCTest() {
        wd.get("https://leqlb100.portal.hewitt.com/web/pg/login");
        wd.findElement(By.id("_ParticipantLogon20_WAR_ahcommonauthportlet_userId")).click();
        wd.findElement(By.id("_ParticipantLogon20_WAR_ahcommonauthportlet_userId")).clear();
        wd.findElement(By.id("_ParticipantLogon20_WAR_ahcommonauthportlet_userId")).sendKeys("51241054AA");
        wd.findElement(By.id("_ParticipantLogon20_WAR_ahcommonauthportlet_password")).click();
        wd.findElement(By.id("_ParticipantLogon20_WAR_ahcommonauthportlet_password")).clear();
        wd.findElement(By.id("_ParticipantLogon20_WAR_ahcommonauthportlet_password")).sendKeys("12345678");
        wd.findElement(By.id("_ParticipantLogon20_WAR_ahcommonauthportlet_testCfgList[0].cfgValue")).click();
        wd.findElement(By.id("_ParticipantLogon20_WAR_ahcommonauthportlet_testCfgList[0].cfgValue")).clear();
        wd.findElement(By.id("_ParticipantLogon20_WAR_ahcommonauthportlet_testCfgList[0].cfgValue")).sendKeys("lv3b");
        if (!wd.findElement(By.id("_ParticipantLogon20_WAR_ahcommonauthportlet_skipAACheckbox")).isSelected()) {
            wd.findElement(By.id("_ParticipantLogon20_WAR_ahcommonauthportlet_skipAACheckbox")).click();
        }
        wd.findElement(By.id("_ParticipantLogon20_WAR_ahcommonauthportlet_logOn")).click();
   /*     if (!wd.findElement(By.tagName("html")).getText().contains("Messages")) {
            System.out.println("verifyTextPresent failed");
        }*/
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
