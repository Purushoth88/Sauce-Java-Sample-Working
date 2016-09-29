package com.saucelabs.test;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.saucelabs.test.Utils.JsonConfig;
import com.saucelabs.test.Utils.SauceRunner;

public class PremierQCScriptTest {
	
	static String jsonFilePath = "RecordedScripts/PremierQCScript.json";
	static String generatedResultPath = "OutputFolder/Results";
	
/*    public static void main(String[] aa) throws InvalidFormatException,
    InterruptedException, IOException {

    JsonConfig.createExcel();
    try {
    sumitascript();
    } catch (Exception e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
    }
    JsonConfig.closeExcel();
    }*/
    
    @Test
    public void sumitascript() throws Exception {
		JsonConfig.createExcel();
		try {
    	RemoteWebDriver wd;
    	wd = (RemoteWebDriver) SauceRunner.sauceCapabilities();
        wd.get("https://leqlb030.portal.hewitt.com/web/premieruat/login");
        if (!wd.findElement(By.tagName("html")).getText().contains("Log On")) {
            System.out.println("verifyTextPresent failed");
        }
        String LogOnUrl = wd.getCurrentUrl();
        JsonConfig.readAndCompareJson(jsonFilePath, wd);
        wd.findElement(By.id("_ParticipantLogon20_WAR_ahcommonauthportlet_userId")).click();
        wd.findElement(By.id("_ParticipantLogon20_WAR_ahcommonauthportlet_userId")).clear();
        wd.findElement(By.id("_ParticipantLogon20_WAR_ahcommonauthportlet_userId")).sendKeys("000HEW00044020");
        wd.findElement(By.id("_ParticipantLogon20_WAR_ahcommonauthportlet_password")).click();
        wd.findElement(By.id("_ParticipantLogon20_WAR_ahcommonauthportlet_password")).clear();
        wd.findElement(By.id("_ParticipantLogon20_WAR_ahcommonauthportlet_password")).sendKeys("99999999");
        wd.findElement(By.id("_ParticipantLogon20_WAR_ahcommonauthportlet_testCfgList[0].cfgValue")).click();
        wd.findElement(By.id("_ParticipantLogon20_WAR_ahcommonauthportlet_testCfgList[0].cfgValue")).clear();
        wd.findElement(By.id("_ParticipantLogon20_WAR_ahcommonauthportlet_testCfgList[0].cfgValue")).sendKeys("l77b");
        wd.findElement(By.id("_ParticipantLogon20_WAR_ahcommonauthportlet_testCfgList[1].cfgValue")).click();
        wd.findElement(By.id("_ParticipantLogon20_WAR_ahcommonauthportlet_testCfgList[1].cfgValue")).clear();
        wd.findElement(By.id("_ParticipantLogon20_WAR_ahcommonauthportlet_testCfgList[1].cfgValue")).sendKeys("m6hb");
        if (!wd.findElement(By.id("_ParticipantLogon20_WAR_ahcommonauthportlet_skipAACheckbox")).isSelected()) {
            wd.findElement(By.id("_ParticipantLogon20_WAR_ahcommonauthportlet_skipAACheckbox")).click();
        }
        wd.findElement(By.id("_ParticipantLogon20_WAR_ahcommonauthportlet_logOn")).click();
        try { Thread.sleep(4000l); } catch (Exception e) { throw new RuntimeException(e); }
        String HomeUrl = wd.getCurrentUrl();
        JsonConfig.readAndCompareJson(jsonFilePath, wd);
        wd.findElement(By.id("ahDialogCloseBtn")).click();
        if (!wd.findElement(By.tagName("html")).getText().contains("Highlights for You")) {
            System.out.println("verifyTextPresent failed");
        }
        if (!wd.findElement(By.tagName("html")).getText().contains("Health & Insurance")) {
            System.out.println("verifyTextPresent failed");
        }
        wd.findElement(By.linkText("Health & Insurance")).click();
        try { Thread.sleep(4000l); } catch (Exception e) { throw new RuntimeException(e); }
        String HealthlangingUrl = wd.getCurrentUrl();
        JsonConfig.readAndCompareJson(jsonFilePath, wd);
        if (!wd.findElement(By.tagName("html")).getText().contains("Benefits Coverage")) {
            System.out.println("verifyTextPresent failed");
        }
        if (!wd.findElement(By.tagName("html")).getText().contains("Log Off")) {
            System.out.println("verifyTextPresent failed");
        }
        wd.findElement(By.linkText("Log Off")).click();
			System.out.println("Log Off before Quit---------");
        wd.quit();
			System.out.println("Log Off After Quit---------");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			System.out.println("finally before Quit---------");
			JsonConfig.closeExcel(generatedResultPath);
			System.out.println("finally After Quit---------");
		}
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
