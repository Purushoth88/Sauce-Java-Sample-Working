package com.saucelabs.test.Utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
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
import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.ConcurrentRefUpdateException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.NoMessageException;
import org.eclipse.jgit.api.errors.UnmergedPathsException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.errors.UnmergedPathException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.revwalk.RevCommit;

@SuppressWarnings("unused")
public class JsonConfigFinal {
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

	public static void closeExcel() {
		
		try {
			String file = System.getProperty("user.home") + "//Result_"
					+ fileName + "_" + new Random().nextInt(50046846) + ".xlsx";
			String ResultfileToImport = "Result_"
				+ fileName + "_" + new Random().nextInt(50046846) + ".xlsx";
			//FileOutputStream out = new FileOutputStream(file, true);
			System.out.println("Result File name :" + file);
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
			

	        String name = "Purushoth88";
	        String password = "October@12";
	        String url = "https://github.com/Purushoth88/Sauce-Java-Sample-Working.git";

	        // credentials
	        CredentialsProvider cp = new UsernamePasswordCredentialsProvider(name, password);
	        // clone
	        File dir = new File(ResultfileToImport);
	        CloneCommand cc = new CloneCommand()
	                .setCredentialsProvider(cp)
	                .setDirectory(dir)
	                .setURI(url);
	        Git git = cc.call();
	        // add
	        AddCommand ac = git.add();
	        ac.addFilepattern(ResultfileToImport);
	        try {
	            ac.call();
	        } catch (NoFilepatternException e) {
	            e.printStackTrace();
	        }

	        // commit
	        CommitCommand commit = git.commit();
	        commit.setCommitter("TMall", "open@tmall.com")
	                .setMessage("push war");
	        try {
	            commit.call();
	        } catch (NoHeadException e) {
	            e.printStackTrace();
	        } catch (NoMessageException e) {
	            e.printStackTrace();
	        } catch (ConcurrentRefUpdateException e) {
	            e.printStackTrace();
	        } catch (WrongRepositoryStateException e) {
	            e.printStackTrace();
	        }
	        // push
	        PushCommand pc = git.push();
	        pc.setCredentialsProvider(cp)
	                .setForce(true)
	                .setPushAll();
	        try {
	            Iterator<PushResult> it = pc.call().iterator();
	            if(it.hasNext()){
	                System.out.println(it.next().toString());
	            }
	        } catch (InvalidRemoteException e) {
	            e.printStackTrace();
	        }

	        // cleanup
	        dir.deleteOnExit();
		} catch (IOException io) {
			System.out.println("unable to write to excel" + io);
		} catch (Exception e) {
			System.out.println("unable to write to excel" + e);
		}
	}
	
    	public static void addFile(Git git, String filename) throws IOException, GitAPIException { 
        	System.out.println("Inside Addd file" + git);
		System.out.println("Inside filename file" + filename);
		System.out.println("Work Tree" + git.getRepository().getWorkTree());
        	System.out.println(" Directory" + git.getRepository().getDirectory());
		FileWriter writer = new FileWriter(new File(git.getRepository().getWorkTree(), filename));
        	System.out.println(git.getRepository().getWorkTree());
        	System.out.println(git.getRepository().getDirectory());
        	writer.write(filename + "\n"); 
        	writer.close(); 
        	AddCommand add = git.add(); 
        	add.addFilepattern(filename).call(); 
    	} 
 
    	public static void commit(Git git, String message) throws UnmergedPathException, 
	        UnmergedPathsException, GitAPIException { 
	        CommitCommand commit = git.commit(); 
	        commit.setMessage(message).call(); 
		git.push();
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



