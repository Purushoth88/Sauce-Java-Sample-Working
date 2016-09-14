package com.saucelabs;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import static org.testng.Assert.*;
import java.net.URL;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.concurrent.TimeUnit;
import java.util.Date;
import java.io.File;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.*;
import static org.openqa.selenium.OutputType.*;

public class GoogleSearchJU {
    RemoteWebDriver wd;
    
    @BeforeMethod
    public void setUp() throws Exception {
        DesiredCapabilities caps = DesiredCapabilities.firefox();
            caps.setCapability("name", "GoogleSearchJU");
        wd = new RemoteWebDriver(
            new URL("http://Purush_12:3b1c6846-0b29-40e3-a87a-70d93820d78a@ondemand.saucelabs.com:80/wd/hub"),
            caps);
        wd.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
    }
    
    @Test
    public void GoogleSearchJU() {
        wd.get("https://www.google.co.in/?gfe_rd=cr&ei=fUzSV9R06cDyB5_cisgF&gws_rd=ssl");
        wd.findElement(By.id("lst-ib")).click();
        wd.findElement(By.id("lst-ib")).clear();
        wd.findElement(By.id("lst-ib")).sendKeys("Sauce Labs blogs");
        wd.findElement(By.id("lst-ib")).click();
        wd.findElement(By.id("lst-ib")).sendKeys("\n");
        wd.findElement(By.linkText("Blog | Sauce Labs")).click();
        wd.findElement(By.linkText("JUnit")).click();
        wd.findElement(By.linkText("READ MORE")).click();
        wd.findElement(By.linkText("API documentation")).click();
        wd = (FirefoxDriver) wd.switchTo().window(Windows 10 Firefox 48);
    }
    
    @AfterMethod
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
