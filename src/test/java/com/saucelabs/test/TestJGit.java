import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.*;
import org.eclipse.jgit.api.CreateBranchCommand.SetupUpstreamMode;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;

import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("unused")
public class TestJGit {

    private String localPath, remotePath;
    private Repository localRepo;
    private Git git;

    @SuppressWarnings("resource")
	@Before
    public void init() throws IOException, InvalidRemoteException, TransportException, GitAPIException {
        localPath = System.getProperty("user.dir");
        System.out.println("localPath----" + localPath);
        remotePath = "http://github.com/Purushoth88/Test.git";
        System.out.println("remotePath---" + remotePath);
        localRepo = new FileRepository(localPath + "/.git");
        System.out.println("localRepo----" + localRepo);
        git = new Git(localRepo);
        System.out.println("git----" + git);
        
        Repository newRepo = new FileRepository(localPath + ".git");
        System.out.println("newRepo----" + newRepo);
        newRepo.create();
        System.out.println("-------------");
        Git.cloneRepository().setURI(remotePath).setDirectory(new File(localPath)).call();
        System.out.println("==================");
        File myfile = new File(localPath + "/myfile");
        System.out.println("myfile------" + myfile);
        myfile.createNewFile();
        System.out.println("-------------------");
        git.add().addFilepattern("myfile").call();
        git.commit().setMessage("Added myfile").call();
        git.push().call();
        git.branchCreate().setName("master")
        .setUpstreamMode(SetupUpstreamMode.SET_UPSTREAM)
        .setStartPoint("origin/master").setForce(true).call();
        git.pull().call();
    }
}