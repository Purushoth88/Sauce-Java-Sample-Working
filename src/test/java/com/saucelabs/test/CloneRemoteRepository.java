package com.saucelabs.test;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;



/**
 * Simple snippet which shows how to clone a repository from a remote source
 *
 * @author dominik.stadler at gmx.at
 */
public class CloneRemoteRepository {

    private static final String REMOTE_URL = "https://github.com/Purushoth88/Sauce-Java-Sample-Working.git";

    public static void main(String[] args) throws IOException, GitAPIException {
        // prepare a new folder for the cloned repository
        File localPath = File.createTempFile("Sauce-Java-Sample-Working1", "");
        if(!localPath.delete()) {
            throw new IOException("Could not delete temporary file " + localPath);
        }

        // then clone
        System.out.println("Cloning from " + REMOTE_URL + " to " + localPath);
        try {
        	Git result = Git.cloneRepository()
                    .setURI(REMOTE_URL)
                    .setDirectory(localPath)
                    .call();
	        // Note: the call() returns an opened repository already which needs to be closed to avoid file handle leaks!
	        System.out.println("Having repository: " + result.getRepository().getDirectory());
	        
	        
	        // Add
	        File myfile = new File(result.getRepository().getDirectory().getParent(), "testfile");
            if(!myfile.createNewFile()) {
                throw new IOException("Could not create file " + myfile);
            }

            // run the add-call
            result.add()
                    .addFilepattern("testfile")
                    .call();

            System.out.println("Added file " + myfile + " to repository at " + result.getRepository().getDirectory().getParent());
	        
            // Committ
            
            result.commit().setMessage("Added testfile").call();

			System.out.println("Committed file " + myfile + " to repository at " + result.getRepository().getDirectory().getParent());
			UsernamePasswordCredentialsProvider user = new UsernamePasswordCredentialsProvider("Purushoth88","October@12");

			result.push().setRemote(REMOTE_URL).setCredentialsProvider(user).call();
        } catch(Exception e) {
        	System.out.println(e);
        } 
        
    }
}
