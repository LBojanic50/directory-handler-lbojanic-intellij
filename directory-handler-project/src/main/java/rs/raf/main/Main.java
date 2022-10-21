package rs.raf.main;

import java.io.IOException;
import java.security.GeneralSecurityException;

import rs.raf.util.GoogleDriveUtil;
import rs.raf.util.LocalUtil;

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