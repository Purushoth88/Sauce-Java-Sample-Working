package com.saucelabs.test.Utils;

import java.io.File;
import java.util.Iterator;

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
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

class Main {
	public static void main(String args[]) throws TransportException, GitAPIException {
		String name = "Purushoth88";
		String password = "October@12";
		String url = "https://github.com/Purushoth88/Sauce-Java-Sample-Working.git";

		// credentials
		CredentialsProvider cp = new UsernamePasswordCredentialsProvider(name, password);
		// clone
		File dir = new File("/tmp/abc");
		CloneCommand cc = new CloneCommand().setCredentialsProvider(cp).setDirectory(dir).setURI(url);
		Git git = cc.call();
		// add
		AddCommand ac = git.add();
		ac.addFilepattern("text.txt");
		try {
			ac.call();
		} catch (NoFilepatternException e) {
			e.printStackTrace();
		}

		// commit
		CommitCommand commit = git.commit();
		commit.setCommitter("TMall", "open@tmall.com").setMessage("push war");
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
