package rs.raf.util;

import rs.raf.localImplementation.DirectoryHandlerConfigLocalImplementation;
import rs.raf.localImplementation.DirectoryHandlerLocalImplementation;

import java.io.IOException;
import java.util.Properties;

public class LocalUtil {
	public static Properties properties;
	public static long folderSize = 0;
	public static DirectoryHandlerLocalImplementation directoryHandlerLocalImplementation = null;
	public static DirectoryHandlerConfigLocalImplementation directoryHandlerConfigLocalImplementation = null;
	public LocalUtil() throws IOException {
		directoryHandlerLocalImplementation = new DirectoryHandlerLocalImplementation();
		directoryHandlerConfigLocalImplementation = new DirectoryHandlerConfigLocalImplementation();
	}
	public void startApp() throws IOException {
		directoryHandlerLocalImplementation.createLocalRepository("myRep", "10000B", 30, new String[] {"exe"});
		for(int i = 0; i < 35; i++){
			directoryHandlerLocalImplementation.createLocalFile("myRep", "myRep", "file" + (i + 1), "txt");
		}
	}
}