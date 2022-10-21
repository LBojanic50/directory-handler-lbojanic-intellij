package rs.raf.util;

import java.io.IOException;
import java.util.Properties;
import rs.raf.local.DirectoryHandlerLocalImplementation;

public class LocalUtil {
	public static Properties properties;
	public static long folderSize = 0;
	public static DirectoryHandlerLocalImplementation directoryHandlerLocalImplementation;
	public LocalUtil() throws IOException {
		directoryHandlerLocalImplementation = new DirectoryHandlerLocalImplementation();		
	}
	public void startApp() throws IOException {
		directoryHandlerLocalImplementation.createLocalDirectory();
		directoryHandlerLocalImplementation.createConfig("1000B", 30, new String[] {"exe", "jar"});
		//System.out.println(folderSize + "B " + properties.getProperty("maxFileSize"));
	}
}