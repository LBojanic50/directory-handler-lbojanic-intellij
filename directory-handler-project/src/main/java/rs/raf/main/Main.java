package rs.raf.main;

import rs.raf.util.GoogleDriveUtil;
import rs.raf.util.LocalUtil;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class Main {
	public static void main(String[] args) throws GeneralSecurityException, IOException {
		if(args[0].equals("local")) {
			LocalUtil localUtil = new LocalUtil();
			localUtil.startApp();
		}
		else if(args[0].equals("drive")) {
			GoogleDriveUtil googleDriveUtil = new GoogleDriveUtil();
			googleDriveUtil.startApp();		
		}	
	}
}