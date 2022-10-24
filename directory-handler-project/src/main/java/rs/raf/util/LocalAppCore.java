package rs.raf.util;

import rs.raf.localImplementation.DirectoryHandlerConfigLocalImplementation;
import rs.raf.localImplementation.DirectoryHandlerLocalImplementation;

import java.io.IOException;
import java.util.Properties;

public class LocalAppCore {
	public static Properties properties;
	public static long folderSize = 0;
	public static DirectoryHandlerLocalImplementation directoryHandlerLocalImplementation = null;
	public static DirectoryHandlerConfigLocalImplementation directoryHandlerConfigLocalImplementation = null;
	public LocalAppCore() throws IOException {
		directoryHandlerLocalImplementation = new DirectoryHandlerLocalImplementation();
		directoryHandlerConfigLocalImplementation = new DirectoryHandlerConfigLocalImplementation();
	}
	public void startApp() throws IOException {
		/*DirectoryHandlerConfig directoryHandlerConfig = new DirectoryHandlerConfig("1024B", 32, new String[]{ "exe", "bat"});
		directoryHandlerLocalImplementation.createLocalRepository("myRep", directoryHandlerConfig);
		directoryHandlerLocalImplementation.createLocalDirectory("myRep", "myDir");*/

	}
}