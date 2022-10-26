package rs.raf.core;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.drive.Drive;

import com.google.api.services.drive.model.File;
import rs.raf.googleDriveImplementation.DirectoryHandlerGoogleDriveImplementation;

import java.io.IOException;
import java.security.GeneralSecurityException;

import java.util.List;

public class GoogleDriveAppCore {
    private static DirectoryHandlerGoogleDriveImplementation directoryHandlerGoogleDriveImplementation = null;
    private static Credential credentials = null;
    private static Drive googleDriveClient = null;

    public GoogleDriveAppCore() throws GeneralSecurityException, IOException {
        directoryHandlerGoogleDriveImplementation = new DirectoryHandlerGoogleDriveImplementation();
        directoryHandlerGoogleDriveImplementation.authorizeGoogleDriveClient();
    }
    public void startApp() throws IOException {
        for(File file : directoryHandlerGoogleDriveImplementation.fileListInRoot){
            System.out.println(file.getName());
        }
        /*List<File> repositoryList = directoryHandlerGoogleDriveImplementation.getFileListForRepository("customDir");
        for(File file : repositoryList){
            System.out.println(file.getName());
        }*/
    }
}