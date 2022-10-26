package rs.raf.main;

import rs.raf.core.GoogleDriveAppCore;
import rs.raf.core.LocalAppCore;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class Main {
	public static void main(String[] args) throws GeneralSecurityException, IOException {
		if(args[0].equals("local")) {
			LocalAppCore localAppCore = new LocalAppCore();
			localAppCore.startApp();
		}
		else if(args[0].equals("drive")) {
			GoogleDriveAppCore googleDriveAppCore = new GoogleDriveAppCore();
			googleDriveAppCore.startApp();
		}
	}
}