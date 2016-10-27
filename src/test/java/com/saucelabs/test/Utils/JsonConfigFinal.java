package com.saucelabs.test.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedWriter;
import org.eclipse.jgit.lib.Repository;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.RemoteRefUpdate;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.transport.PushResult;

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
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.errors.ConcurrentRefUpdateException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.NoMessageException;
import org.eclipse.jgit.api.errors.UnmergedPathsException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.errors.TransportException;
import org.eclipse.jgit.errors.UnmergedPathException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;

@SuppressWarnings("unused")
public class JsonConfigFinal {
	static String fileName = "";
	static String fileParentPath = "";
	static Workbook wb = new XSSFWorkbook();
	static Sheet ws = wb.createSheet("Result");
	static int flag = 0;
	static HashMap<Integer, String> pageList = new HashMap<Integer, String>();
	static HashMap<Integer, List<String>> pageObjList = new HashMap<Integer, List<String>>();
	// static String jsonFilePath = "C:\\Users\\A0717585\\Documents\\My Received
	// Files\\recording.json";
	private static String localPath;
	private String remotePath;
	private static Repository repository;
	private static Git git;
	private static FileRepositoryBuilder builder;

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

			String pathResultJson = JsonPath.parse(jsonFile).read("$.inputs[0].ResultPath");

			resultXpathList = JsonPath.parse(new File(pathResultJson))
					.read("$.Data[?(@.PageName==" + url + ")].locator.value");

			resultConfLocList = JsonPath.parse(new File(pathResultJson))
					.read("$.Data[?(@.PageName==" + url + ")].ConfLoc");
			resultConfLocArray = resultConfLocList.toArray(new String[0]);

			resultXpathListArray = resultXpathList.toArray(new String[0]);
			int j = 0;
			for (String resultXpath : resultXpathListArray) {

				resultXpathObjList = JsonPath.parse(new File(pathResultJson))
						.read("$.Data[?(@.ConfLoc==" + resultConfLocArray[j] + ")].objList.Expvalue");

				resultObjList = JsonPath.parse(new File(pathResultJson))
						.read("$.Data[?(@.ConfLoc==" + resultConfLocArray[j] + ")].objName");

				pageNameList = JsonPath.parse(new File(pathResultJson))
						.read("$.Data[?(@.ConfLoc==" + resultConfLocArray[j] + ")].PageName");

				resultObjectArray = resultObjList.toArray(new String[0]);
				pageNameArray = pageNameList.toArray(new String[0]);

				resultPathObjArray = resultXpathObjList.toArray(new String[0]);

				List<WebElement> elements = wd.findElements(By.xpath(resultXpath));
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

					if (null != jsonAttribute && i < resultPathObjArray.length && null != resultPathObjArray[i]
							&& !resultPathObjArray[i].equals("")) {

						if (!jsonAttribute.equals("")
								&& StringUtils.containsIgnoreCase(resultPathObjArray[i], jsonAttribute)) {
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

	public static void closeExcel() throws URISyntaxException, IOException {


		System.out.println("Close Excel" + System.getProperty("user.dir"));
		String localRepo = "CloneRepo/TempLocation/";
		File file = new File(localRepo + "//Result_" + fileName + "_"
				+ new Random().nextInt(50046846) + ".xlsx");
		System.out.println("Result File name :" + file);
		
		String remoteSeconPath = "https://github.com/Purushoth88/Sauce-Java-Sample-Working.git";
		UsernamePasswordCredentialsProvider upcp = new UsernamePasswordCredentialsProvider("Purushoth88",
				"October@12");
		builder = new FileRepositoryBuilder();
		repository = builder.setGitDir(file).readEnvironment().findGitDir().build();
		git = new Git(repository);
		CloneCommand clone = git.cloneRepository();
		clone.setBare(false);
		clone.setCloneAllBranches(true);
		clone.setDirectory(file).setURI(remoteSeconPath);
		clone.setCredentialsProvider(upcp);
		clone.call();
		
		try {
/*			System.out.println("file.toString() : " + file.toString());
			System.out.println("file.toString() : " + file.getPath());
			System.out.println("file.length() : " + file.length());
			System.out.println("file.exists() : " + file.exists());
			//Git git = Git.init().setDirectory(new File(localRepo, file.toString())).setBare(false).call();
			CloneCommand cc = new CloneCommand().setCredentialsProvider(upcp).setDirectory(file).setURI(remoteSeconPath);
			Git git = cc.call();
			// System.out.println("repository : " + repository);
			// Repository repo = (Repository) github.repos();
			// Git git = new Git(repository);
			System.out.println("Git Repository : " + git);
			System.out.println("Before Getting into Add file : ");
			System.out.println("Work Tree" + git.getRepository());
			System.out.println(" Directory" + git.getRepository().getDirectory());*/
			addFile(git, file);
			commit(git, file);
			System.out.println("Result File: " + file);
			StoredConfig config = git.getRepository().getConfig();
			config.setString("remote", "origin", "fetch", "+refs/*:refs/*");
			config.save();
			testPush(git);
			testPull(git, upcp);
			System.out.println("push");
		} catch (IOException io) {
			System.out.println("unable to write to excel" + io);
		} catch (Exception e) {
			System.out.println("unable to write to excel" + e);
		}
	}

/*	private static void printPushResult(Iterable<PushResult> pr) {
		for (PushResult p : pr) {
			System.out.println("Pushresult: " + p.getMessages());
			for (RemoteRefUpdate rru : p.getRemoteUpdates()) {
				System.out.println("  RemoteRefUpdate: ");
				System.out.println("    srcRef: " + rru.getSrcRef());
				System.out.println("    message: " + rru.getMessage());
				System.out.println("    status: " + rru.getStatus());
			}
		}
	}*/
	
	
	public static void addFile(Git git, File file) throws IOException, GitAPIException {
		try {
		System.out.println("Inside Addd file" + git);
		System.out.println("Inside filename file" + file);
		System.out.println("Work Tree" + git.getRepository().getWorkTree());
		System.out.println(" Directory" + git.getRepository().getDirectory());
		
		FileOutputStream out = new FileOutputStream(file, true);
		System.out.println("out File: " + out);
		System.out.println("file.toString() : " + file.length());
		for (Entry<Integer, String> e : pageList.entrySet()) {
			Integer key = e.getKey();
			String value = e.getValue();
			Row row1 = ws.createRow(key);
			ws.addMergedRegion(new CellRangeAddress(key, key, 0, 4));
			row1.createCell(0).setCellValue(value);
			CellStyle style1 = wb.createCellStyle();
			style1.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.getIndex());
			style1.setAlignment(CellStyle.ALIGN_CENTER);
			style1.setFillPattern(CellStyle.SOLID_FOREGROUND);
			row1.getCell(0).setCellStyle(style1);

			for (Entry<Integer, List<String>> entry : pageObjList.entrySet()) {
				Integer rowNum = entry.getKey();
				List<String> valuesList = entry.getValue();

				Row row = ws.createRow(rowNum);
				row.createCell(0).setCellValue(valuesList.get(0));
				row.createCell(1).setCellValue(valuesList.get(1));
				String jsonResult = valuesList.get(2);
				System.out.println("Inside writing xls file");

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
					style.setFillForegroundColor(IndexedColors.GREEN.getIndex());
				} else {
					style.setFillForegroundColor(IndexedColors.RED.getIndex());
				}
				System.out.println("Inside writing xls file1-------");
				style.setFillPattern(CellStyle.SOLID_FOREGROUND);
				row.createCell(4).setCellValue(valuesList.get(4));
				row.getCell(4).setCellStyle(style);
			}

		}
		System.out.println("file.toString() : " + file.toString());
		System.out.println("file.toString() : " + file.getPath());
		System.out.println("file.length() : " + file.length());
		System.out.println("file.exists() : " + file.exists());
		System.out.println(git.getRepository().getWorkTree());
		System.out.println(git.getRepository().getDirectory());
		AddCommand add = git.add();
		wb.write(out);
		out.flush();
		out.close();
		//add.addFilepattern(file.getPath()).call();
		 repository = builder.setGitDir(file).readEnvironment()
					.findGitDir().build();
		git = new Git(repository);
		git.add().addFilepattern(localPath).call();
		git.commit().setCommitter("chenzhirong", "253494709@qq.com")
				.setMessage("jonee commit").call();

		// 
		for (RevCommit revCommit : git.log().call()) {
			System.out.println(revCommit);
			System.out.println(revCommit.getFullMessage());
			System.out.println(revCommit.getCommitterIdent().getName()
					+ "======="
					+ revCommit.getCommitterIdent().getEmailAddress());
		}
		
		} catch (NoFilepatternException e) {
			throw new IOException(e.getMessage());
		}
	}

	public static void commit(Git git, File file)
			throws UnmergedPathsException, GitAPIException, IOException {
		RepositoryBuilder builder=new RepositoryBuilder();
		File f = new File(localPath+"/.git");
		Repository repository=builder.setGitDir(f).readEnvironment().findGitDir().build();
		
		git = new Git(repository);
		RevWalk walk = new RevWalk(repository);
		RevCommit commit = null;
		Iterable<RevCommit> logs = git.log().call();
		Iterator<RevCommit> i = logs.iterator();

		while (i.hasNext()) {
		    commit = walk.parseCommit( i.next() );
		    System.out.println( commit.getFullMessage() );
		}
	}

	public static void testPush(Git git) throws IOException, JGitInternalException, GitAPIException {
		try {
		      Repository localRepo = new FileRepository("https://github.com/chenzhirong/rcp_view.git");
		      localRepo.create();
		      //testAdd();
		      git=new Git(localRepo);
		      git.push().call();
		      localRepo.close();
		} catch (IllegalStateException ise) {
		        System.out.println("The repository already exists!");
		} catch (IOException ioe) {
		        System.out.println("Failed to create the repository!");
		} catch (GitAPIException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
		
	}
	
	public static void testPull(Git git, UsernamePasswordCredentialsProvider upcp) throws IOException, GitAPIException {
		try {
/*            PullCommand pull = git.pull();
            pull.setCredentialsProvider(upcp);
            pull.setRemote("Sauce-Java-Sample-Working");
            pull.call();*/
			git.pull().setCredentialsProvider(upcp).call();
			System.out.println("pull");
		} catch (Exception e) {
			System.out.println("Exception in pull");
			e.printStackTrace();
		}
	}

	public static void createExcel() throws FileNotFoundException {
		Row row = ws.createRow(ws.getPhysicalNumberOfRows());
		CellStyle style = wb.createCellStyle();
		style.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		String[] stringArray = new String[] { "Serial Number", "Object Name", "Expected result", "Actual Result",
				"Status" };

		for (int j = 0; j < stringArray.length; j++) {
			Cell cell1 = row.createCell(j);
			row.getCell(j).setCellStyle(style);
			cell1.setCellValue(stringArray[j]);
		}

	}

	/*
	 * public static AndroidDriver androidMobileChromeLauncher(String
	 * deviceName, String deviceSerialNumber) throws InterruptedException,
	 * IOException {
	 *//**
		 * DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		 * WebDriver wd = new ChromeDriver(capabilities);
		 **//*
			 * 
			 * DesiredCapabilities caps = new DesiredCapabilities();
			 * caps.setCapability("automationName","Appium");
			 * caps.setCapability("platformName", "Android");
			 * caps.setCapability("deviceName", deviceName);
			 * caps.setCapability("newCommandTimeout", "120");
			 * caps.setCapability("browserName", "Chrome");
			 * caps.setCapability("udid", deviceSerialNumber); AndroidDriver wd
			 * = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"),
			 * caps); System.out.println("session open" + wd); //
			 * wd.get("https://l4dridap1273:8446/web/earth/login");
			 * //wd.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
			 * //wd.get("https://l4dridap1273:8446/web/earth/login");
			 * 
			 * return wd;
			 * 
			 * }
			 */

}
