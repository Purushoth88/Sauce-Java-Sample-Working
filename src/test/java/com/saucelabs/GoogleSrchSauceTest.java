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

public class GoogleSrchSauceTest implements SauceOnDemandSessionIdProvider {
    private String sessionId;
    public SauceOnDemandAuthentication authentication = new SauceOnDemandAuthentication("Purush_88", "e497cff7-27f0-4dbc-8d2b-49e1af75a583");
    public @Rule SauceOnDemandTestWatcher resultReportingTestWatcher = new SauceOnDemandTestWatcher(this, authentication);
    public @Rule TestName testName = new TestName();
    @Override public String getSessionId() { return sessionId; }
    RemoteWebDriver wd;
    
    @Before
    public void setUp() throws Exception {
        DesiredCapabilities caps = DesiredCapabilities.chrome();
            caps.setCapability("name", "GoogleSrchSauceTest");
        wd = new RemoteWebDriver(
            new URL("http://Purush_88:e497cff7-27f0-4dbc-8d2b-49e1af75a583@ondemand.saucelabs.com:80/wd/hub"),
            caps);
        wd.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        sessionId = wd.getSessionId().toString();
    }
    
    @Test
    public void GoogleSrchSauceTest() {
        wd.get("https://www.google.co.in/");
        wd.findElement(By.id("lst-ib")).click();
        wd.findElement(By.id("lst-ib")).clear();
        wd.findElement(By.id("lst-ib")).sendKeys("Sauce Labs");
        wd.findElement(By.id("lst-ib")).click();
        wd.findElement(By.id("lst-ib")).sendKeys("\n");
        wd.findElement(By.linkText("sauce labs wiki")).click();
        wd.findElement(By.linkText("Sauce Labs Documentation Wiki")).click();
        wd.findElement(By.linkText("Getting Started")).click();
        wd.findElement(By.linkText("Best Practices and Tips")).click();
        wd.findElement(By.linkText("Test Configuration and Annotation")).click();
        wd.findElement(By.linkText("Annotating Tests")).click();
        wd.findElement(By.xpath("//div[@id='main-content']/ul[2]/li[4]/a")).click();
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
