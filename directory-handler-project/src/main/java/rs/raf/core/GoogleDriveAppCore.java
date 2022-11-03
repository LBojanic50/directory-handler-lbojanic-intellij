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
    }
    public void startApp() throws IOException {
        /*for(File file : allFilesList){
            System.out.println(file.getName() + " : " + file.getId());
        }
        directoryHandlerGoogleDriveImplementation.moveFileToFolder("1wZWzLTiPx13YWco_mXeQ8tAQIEjrlWG_", "1AHj-qlwaq64R_4rAC-U37VZabnuaL8Pe");*/
        /*List<File> repositoryList = directoryHandlerGoogleDriveImplementation.getFileListForRepository("customDir");
        for(File file : repositoryList){
            System.out.println(file.getName());
        }*/
        //directoryHandlerGoogleDriveImplementation.createRepository("customRep");
        //directoryHandlerGoogleDriveImplementation.createDirectory("customRep", "customDir1/customDir2/customDir3/customDir4");
        List<File> fileList = directoryHandlerGoogleDriveImplementation.getFileListInDirectory("", true, true, true);
        for (File file : fileList){
            System.out.println(file.getName());
        }
    }
}