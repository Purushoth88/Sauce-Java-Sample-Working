package com.saucelabs.test.Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.UnmergedPathException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

/**
 * Simple snippet which shows how to clone a repository from a remote source
 * 
 * @author dominik.stadler at gmx.at
 */
public class PushToRemoteRepository {

	private static final String REMOTE_URL = "https://github.com/Purushoth88/Sauce-Java-Sample-Working.git";

	public static void main(String[] args) throws Exception {
		// prepare a new folder for the cloned repository
		File localPath = File.createTempFile("SauceTest", "");
		localPath.delete();

		// then clone
		System.out.println("Cloning from " + REMOTE_URL + " to " + localPath);
		Git.cloneRepository().setURI(REMOTE_URL).setDirectory(localPath).call();

		// now open the created repository
		UsernamePasswordCredentialsProvider user = new UsernamePasswordCredentialsProvider("Purushoth88", "October@12");
		Git git = Git.cloneRepository().setURI(REMOTE_URL).setDirectory(new File(localPath + "test123/.git"))
				.setCredentialsProvider(user).call();
		File gitDir = git.getRepository().getDirectory();
		//git.close();
		// ...
		// FileRepositoryBuilder builder = new FileRepositoryBuilder();

		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		Repository repository = builder.setGitDir(gitDir).setMustExist(true).build();
		/*
		 * Repository repository = builder.setGitDir(localPath)
		 * .readEnvironment() // scan environment GIT_* variables .findGitDir()
		 * // scan up the file system tree .build();
		 */
		git = new Git(repository);
		addFile(git, "testfile.txt");
		commit(git, "initial commit");
		git.pull().setCredentialsProvider(user);
		git.pull().call();
		git.push().setCredentialsProvider(user);
		git.push().call();

		System.out.println("Pulled from remote repository to local repository at " + repository.getDirectory());

		repository.close();
	}

	public static void addFile(Git git, String filename) throws IOException, Exception {
		FileWriter writer = new FileWriter(new File(git.getRepository().getWorkTree(), filename));
		writer.write(filename + "\n");
		writer.close();
		AddCommand add = git.add();
		add.addFilepattern(filename).call();
	}

	public static void commit(Git git, String message)
			throws UnmergedPathException, Exception, GitAPIException {
		CommitCommand commit = git.commit();
		commit.setMessage(message).call();
	}
}
