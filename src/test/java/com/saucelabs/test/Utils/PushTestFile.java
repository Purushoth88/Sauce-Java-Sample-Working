package com.saucelabs.test.Utils;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Random;

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
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

public class PushTestFile {
	public static void pushFiles(File file, String localRepo) throws GitAPIException, URISyntaxException {
		String name = "Purushoth88";
		String password = "October@12";
		String url = "https://github.com/Purushoth88/Sauce-Java-Sample-Working.git";

		// credentials
		CredentialsProvider cp = new UsernamePasswordCredentialsProvider(name, password);
		// clone
		localRepo = Paths.get(PushTestFile.class.getClassLoader().getResource(".").toURI()).getParent().getParent().toString();

		File dir = new File(localRepo + "/OutputFolder/Results/" + "//Result_"
				+ file.getName() +"_" + new Random().nextInt(50046846) + ".xlsx");
		CloneCommand cc = new CloneCommand().setCredentialsProvider(cp).setDirectory(dir).setURI(url);
		Git git = cc.call();
		// add
		try {
		AddCommand ac = git.add();
		ac.addFilepattern(file.getPath());
		ac.call();
		} catch (NoFilepatternException e) {
			e.printStackTrace();
		}

		// commit
		CommitCommand commit = git.commit();
		commit.setCommitter("TMall", "open@tmall.com").setMessage("TrestPAth");
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
		pc.setCredentialsProvider(cp).setForce(true).setPushAll();
		try {
			Iterator<PushResult> it = pc.call().iterator();
			if (it.hasNext()) {
				System.out.println(it.next().toString());
			}
		} catch (InvalidRemoteException e) {
			e.printStackTrace();
		}

		// cleanup
		dir.deleteOnExit();
	}
}
