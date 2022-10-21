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
	}
}