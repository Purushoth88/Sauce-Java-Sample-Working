package com.saucelabs.test.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.jgit.api.CreateBranchCommand.SetupUpstreamMode;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.api.errors.UnmergedPathsException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.junit.Before;
import org.junit.Test;

public class JgitTest1 {

	static String fileName = "";
	static String fileParentPath = "";
	static Workbook wb = new XSSFWorkbook();
	static Sheet ws = wb.createSheet("Result");
	static int flag = 0;
	static HashMap<Integer, String> pageList = new HashMap<Integer, String>();
	static HashMap<Integer, List<String>> pageObjList = new HashMap<Integer, List<String>>();
	
	private static String localPath;
	private static String remotePath;
	private static Repository repository;
	private static Git git;
	private static FileRepositoryBuilder builder;

	@Before
	public static void pushFiles() throws IOException, GitAPIException {
		localPath = System.getProperty("user.dir");
		System.out.println("localPath" + localPath);
		remotePath = "https://github.com/Purushoth88/Sauce-Java-Sample-Working.git";
		//localRepo = new FileRepository(localPath + "/.git");
		//git = new Git(localRepo);
		builder= new FileRepositoryBuilder();
		File file = new File(localPath);
		System.out.println("localPath file" + file);
		 repository = builder.setGitDir(file).readEnvironment()
				.findGitDir().build();
		git = new Git(repository);
		CloneCommand clone = Git.cloneRepository();
		clone.setBare(false);
		clone.setCloneAllBranches(true);
		clone.setDirectory(file).setURI(remotePath);
		UsernamePasswordCredentialsProvider user = new UsernamePasswordCredentialsProvider(
				"Purushoth88", "October@12");
		clone.setCredentialsProvider(user);
		clone.call();
		testAdd(file, git);
		testCommit(localPath);
		testPush(file, localPath);
	}

	@Test
	public static void testAdd(File file, Git git) throws IOException, GitAPIException {
		try {
		System.out.println("Inside Addd file" + git);
		System.out.println("Inside filename file" + file);
		//System.out.println("Work Tree" + git.getRepository().getWorkTree());
		//System.out.println(" Directory" + git.getRepository().getDirectory());
		
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

	@Test
	public static void testCommit(String localPath) throws IOException, JGitInternalException,
			UnmergedPathsException, GitAPIException {
		RepositoryBuilder builder=new RepositoryBuilder();
		File f = new File(localPath+"/.git");
		Repository repository=builder.setGitDir(f).readEnvironment().findGitDir().build();
		
		Git git = new Git(repository);
		RevWalk walk = new RevWalk(repository);
		RevCommit commit = null;
		Iterable<RevCommit> logs = git.log().call();
		Iterator<RevCommit> i = logs.iterator();

		while (i.hasNext()) {
		    commit = walk.parseCommit( i.next() );
		    System.out.println( commit.getFullMessage() );
		}
	}

	@Test
	public static void testPush(File file, String localPath) throws IOException, JGitInternalException,
			 GitAPIException {
		try {
		      Repository localRepo = new FileRepository("https://github.com/Purushoth88/Sauce-Java-Sample-Working.git");
		      localRepo.create();
		      testAdd(file, git);
		      Git git=new Git(localRepo);
		      git.push().call();
		      localRepo.close();
		} catch (IllegalStateException ise) {
		        System.out.println("The repository already exists!");
		} catch (IOException ioe) {
		        System.out.println("Failed to create the repository!");
		} catch (NoFilepatternException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		} catch (GitAPIException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	}

	public void testTrackMaster() throws IOException, JGitInternalException,
			GitAPIException {
		git.branchCreate().setName("master")
				.setUpstreamMode(SetupUpstreamMode.SET_UPSTREAM)
				.setStartPoint("origin/master").setForce(true).call();
	}

	public void testPull() throws IOException,
			GitAPIException {
		git.pull().call();
	}
}
