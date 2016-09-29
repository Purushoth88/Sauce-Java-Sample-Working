package com.saucelabs.test.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.rules.Verifier;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.google.common.base.Verify;
import com.jayway.jsonpath.JsonPath;

@SuppressWarnings("unused")
public class JsonConfig {
	static String fileName = "";
	static String fileParentPath = "";
	static Workbook wb = new XSSFWorkbook();
	static Sheet ws = wb.createSheet("Result");
	static int flag = 0;
	static HashMap<Integer, String> pageList = new HashMap<Integer, String>();
	static HashMap<Integer, List<String>> pageObjList = new HashMap<Integer, List<String>>();
	//static String jsonFilePath = "C:\\Users\\A0717585\\Documents\\My Received Files\\recording.json";

	 public static void readAndCompareJson(String pathFirstJson, WebDriver wd) {
		File jsonFile = new File(pathFirstJson);
		fileName = jsonFile.getName().replaceAll(".json", "");
		fileParentPath = jsonFile.getAbsolutePath();
                System.out.println("Absolute Path : " + jsonFile.getAbsolutePath());
		String[] resultPathObjArray = null;

		String[] resultXpathListArray = null;

		List<String> resultXpathObjList = null;

		List<String> resultXpathList = null;

		List<String> resultConfLocList = null;

		String[] resultConfLocArray = null;

		List<String> resultObjList = null;
		List<String> pageNameList = null;

		String[] resultObjectArray = null;
		String[] pageNameArray = null;

		try {

			String url = wd.getCurrentUrl();

			url = url.substring(url.lastIndexOf('/') + 1, url.length());

			if (url.contains("?")) {
				url = url.substring(0, url.indexOf("?"));
			}

			String pathResultJson = JsonPath.parse(jsonFile).read(
					"$.inputs[0].ResultPath");

			resultXpathList = JsonPath.parse(new File(pathResultJson)).read(
					"$.Data[?(@.PageName==" + url + ")].locator.value");

			resultConfLocList = JsonPath.parse(new File(pathResultJson)).read(
					"$.Data[?(@.PageName==" + url + ")].ConfLoc");
			resultConfLocArray = resultConfLocList.toArray(new String[0]);

			resultXpathListArray = resultXpathList.toArray(new String[0]);
			int j = 0;
			for (String resultXpath : resultXpathListArray) {

				resultXpathObjList = JsonPath.parse(new File(pathResultJson))
						.read(
								"$.Data[?(@.ConfLoc==" + resultConfLocArray[j]
										+ ")].objList.Expvalue");

				resultObjList = JsonPath.parse(new File(pathResultJson)).read(
						"$.Data[?(@.ConfLoc==" + resultConfLocArray[j]
								+ ")].objName");

				pageNameList = JsonPath.parse(new File(pathResultJson)).read(
						"$.Data[?(@.ConfLoc==" + resultConfLocArray[j]
								+ ")].PageName");

				resultObjectArray = resultObjList.toArray(new String[0]);
				pageNameArray = pageNameList.toArray(new String[0]);

				resultPathObjArray = resultXpathObjList.toArray(new String[0]);

				List<WebElement> elements = wd.findElements(By
						.xpath(resultXpath));
				flag = flag + 1;
				pageList.put(flag, "Page - " + pageNameArray[0]);
				int i = 0;
				int serialNumber = 0;
				boolean counter = false;
				String jsonResult = null;
				String jsonAttribute = null;
				String resultStatus = "Fail";
				for (WebElement ele : elements) {
					serialNumber = i + 1;
					jsonAttribute = ele.getAttribute("innerText").toString();
					if (!counter && i > 0) {
						jsonResult = "JSON result not present";

						resultStatus = "Fail";

					}
					counter = false;

					if (null != jsonAttribute && i < resultPathObjArray.length
							&& null != resultPathObjArray[i]
							&& !resultPathObjArray[i].equals("")) {

						if (!jsonAttribute.equals("")
								&& StringUtils.containsIgnoreCase(
										resultPathObjArray[i], jsonAttribute)) {
							jsonResult = resultPathObjArray[i];
							resultStatus = "PASS";
						} else {
							jsonResult = resultPathObjArray[i];
							resultStatus = "Fail";

						}
						flag = flag + 1;
						List<String> str = new ArrayList<String>();
						str.add(serialNumber + "");
						str.add(resultObjectArray[0]);
						str.add(jsonResult);
						str.add(jsonAttribute.trim());
						str.add(resultStatus);
						pageObjList.put(flag, str);

						counter = true;
						i++;

					}

					if (!counter) {
						i++;
					}

				}
				j++;

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// static SoftAssert softAssert= new SoftAssert();

	public static void closeExcel(String generatedResultPath) {
		
		try {
			File Output = new File(generatedResultPath);
			System.out.println("Absolute Path gitHubRepositoryUrl: " + Output.getAbsolutePath());
			System.out.println("Close Excel" + System.getProperty("user.dir"));
			String file = Output.getAbsolutePath() + "/Result_"
					+ fileName + "_" + new Random().nextInt(50046846) + ".xlsx";
			System.out.println("Result File: " + file);
			FileOutputStream out = new FileOutputStream(file, true);
			System.out.println("out File: " + out);
			for (Entry<Integer, String> e : pageList.entrySet()) {
				Integer key = e.getKey();
				String value = e.getValue();
				Row row1 = ws.createRow(key);
				ws.addMergedRegion(new CellRangeAddress(key, key, 0, 4));
				row1.createCell(0).setCellValue(value);
				CellStyle style1 = wb.createCellStyle();
				style1.setFillForegroundColor(IndexedColors.BRIGHT_GREEN
						.getIndex());
				style1.setAlignment(CellStyle.ALIGN_CENTER);
				style1.setFillPattern(CellStyle.SOLID_FOREGROUND);
				row1.getCell(0).setCellStyle(style1);

				for (Entry<Integer, List<String>> entry : pageObjList
						.entrySet()) {
					Integer rowNum = entry.getKey();
					List<String> valuesList = entry.getValue();

					Row row = ws.createRow(rowNum);
					row.createCell(0).setCellValue(valuesList.get(0));
					row.createCell(1).setCellValue(valuesList.get(1));
					String jsonResult = valuesList.get(2);
					try {
						jsonResult = jsonResult.split("\\|\\|")[0].trim();
					} catch (Exception ex) {
						jsonResult = jsonResult.trim();
						// TODO: handle exception
					}
					row.createCell(2).setCellValue(jsonResult);
					row.createCell(3).setCellValue(valuesList.get(3).trim());
					CellStyle style = wb.createCellStyle();
					if (valuesList.get(4).contains("PASS")) {
						style.setFillForegroundColor(IndexedColors.GREEN
								.getIndex());
					} else {
						style.setFillForegroundColor(IndexedColors.RED
								.getIndex());
					}

					style.setFillPattern(CellStyle.SOLID_FOREGROUND);
					row.createCell(4).setCellValue(valuesList.get(4));
					row.getCell(4).setCellStyle(style);
				}

			}
			System.out.println("Before write to excel");
			wb.write(out);
			System.out.println("After write to excel");
			out.flush();
			out.close();
		} catch (Exception e) {
			System.out.println("unable to write to excel");
		}
	}

	public static void createExcel() throws FileNotFoundException {
		Row row = ws.createRow(ws.getPhysicalNumberOfRows());
		CellStyle style = wb.createCellStyle();
		style.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		String[] stringArray = new String[] { "Serial Number", "Object Name",
				"Expected result", "Actual Result", "Status" };

		for (int j = 0; j < stringArray.length; j++) {
			Cell cell1 = row.createCell(j);
			row.getCell(j).setCellStyle(style);
			cell1.setCellValue(stringArray[j]);
		}

	}

/*	public static AndroidDriver androidMobileChromeLauncher(String deviceName, String deviceSerialNumber) throws InterruptedException, IOException {
		*//**DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		WebDriver wd = new ChromeDriver(capabilities);**//*
		
		DesiredCapabilities caps = new DesiredCapabilities();
            caps.setCapability("automationName","Appium");
            caps.setCapability("platformName", "Android");
            caps.setCapability("deviceName", deviceName);
            caps.setCapability("newCommandTimeout", "120");
            caps.setCapability("browserName", "Chrome");
            caps.setCapability("udid", deviceSerialNumber);

       AndroidDriver wd = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), caps);
       System.out.println("session open" + wd);
          //  wd.get("https://l4dridap1273:8446/web/earth/login");
		//wd.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		//wd.get("https://l4dridap1273:8446/web/earth/login");
		
        return wd;
     
	}*/

}
