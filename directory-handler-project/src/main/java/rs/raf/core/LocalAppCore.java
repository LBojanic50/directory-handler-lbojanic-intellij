package rs.raf.core;

import rs.raf.localImplementation.DirectoryHandlerLocalImplementation;
import rs.raf.model.DirectoryHandlerFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Random;

public class LocalAppCore {
	public static Properties properties;
	public static long folderSize = 0;
	public static DirectoryHandlerLocalImplementation directoryHandlerLocalImplementation = null;
	public static String[] extensions = new String[] {"txt", "json", "xml"};
	public static Random randomExtensionIndex = new Random();
	public LocalAppCore() throws IOException {
		directoryHandlerLocalImplementation = new DirectoryHandlerLocalImplementation();
	}
	public void startApp() throws IOException {
		directoryHandlerLocalImplementation.createRepository("testRep");
		directoryHandlerLocalImplementation.createDirectory("testRep", "testDir");
		List<File> fileList = directoryHandlerLocalImplementation.getFileList("testRep", "testDir");
		for(File file : fileList){
			DirectoryHandlerFile directoryHandlerFile = new DirectoryHandlerFile(file);
			System.out.println(directoryHandlerFile.getFileMetadata().lastModifiedTime());
		}
	}
}