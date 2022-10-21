package rs.raf.util;

import rs.raf.localImplementation.DirectoryHandlerLocalImplementation;

import java.io.IOException;
import java.util.Properties;

public class LocalUtil {
	public static Properties properties;
	public static long folderSize = 0;
	public static DirectoryHandlerLocalImplementation directoryHandlerLocalImplementation;
	public LocalUtil() throws IOException {
		directoryHandlerLocalImplementation = new DirectoryHandlerLocalImplementation();		
	}
	public void startApp() throws IOException {
		directoryHandlerLocalImplementation.createLocalRepository("myRep", "10000B", 30, new String[] {"exe"});
		directoryHandlerLocalImplementation.createLocalFile("myRep", "testFile", "txt");
		directoryHandlerLocalImplementation.writeToFile("myRep", "testFile.txt", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
		System.out.println(directoryHandlerLocalImplementation.getFileSize("myRep", "testFile.txt"));
		//directoryHandlerLocalImplementation.renameFile("myRep", "testFile.txt", "newName");
	}
}