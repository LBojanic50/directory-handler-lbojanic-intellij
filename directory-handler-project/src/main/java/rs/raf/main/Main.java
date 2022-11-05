package rs.raf.main;

import rs.raf.specification.DirectoryHandlerManager;
import rs.raf.specification.IDirectoryHandlerSpecification;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;

public class Main {

	public static void main(String[] args) throws Exception {
		if(args[0].equals("local")) {
			try{
				Class.forName("rs.raf.localImplementation.DirectoryHandlerLocalImplementation");
			}
			catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}


		}
		else if(args[0].equals("drive")) {
			try{
				Class.forName("rs.raf.googleDriveImplementation.DirectoryHandlerGoogleDriveImplementation");
			}
			catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
		IDirectoryHandlerSpecification directoryHandler = DirectoryHandlerManager.getDirectoryHandler();
		directoryHandler.createFile("customRep/dir1/dir2/dir3/testFile.txt");

	}
}