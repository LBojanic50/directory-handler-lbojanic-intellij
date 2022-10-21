package rs.raf.util;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import rs.raf.googleDriveImplementation.DirectoryHandlerGoogleDriveImplementation;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

public class GoogleDriveUtil {
    private static final String APPLICATION_NAME = "directory-handler-lbojanic";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = System.getProperty("user.dir") + "\\tokens";
    private static List<String> SCOPES = null;
    private static final String CREDENTIALS_FILE_PATH = System.getProperty("user.dir") + "\\directory-handler-project\\credentials.json";
    private static InputStream credentialsInputStream = null;
    private static NetHttpTransport HTTP_TRANSPORT = null;
    private static DirectoryHandlerGoogleDriveImplementation directoryHandlerGoogleDriveImplementation = null;
    private static Credential credentials = null;
    private static Drive googleDriveClient = null;
    
    public void initVariables() throws IOException {
    	SCOPES = Arrays.asList(DriveScopes.DRIVE, DriveScopes.DRIVE_APPDATA, DriveScopes.DRIVE_FILE, DriveScopes.DRIVE_METADATA, DriveScopes.DRIVE_SCRIPTS);
        credentialsInputStream = new FileInputStream(CREDENTIALS_FILE_PATH);
    }

    public GoogleDriveUtil() throws GeneralSecurityException, IOException {
        initVariables();
        HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        directoryHandlerGoogleDriveImplementation = new DirectoryHandlerGoogleDriveImplementation();
        credentials = directoryHandlerGoogleDriveImplementation.getCredentials(credentialsInputStream, CREDENTIALS_FILE_PATH, HTTP_TRANSPORT, JSON_FACTORY, SCOPES, TOKENS_DIRECTORY_PATH);
        googleDriveClient = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credentials).setApplicationName(APPLICATION_NAME).build();
    }
    public void startApp() throws IOException {
        directoryHandlerGoogleDriveImplementation.createGoogleDriveFile(googleDriveClient, "testDir", "testFile", "txt");
        List<String> fileList = directoryHandlerGoogleDriveImplementation.getFileList(googleDriveClient, "testDir");
        System.out.println(fileList);
    	//System.out.println(directoryHandlerGoogleDriveImplementation.createGoogleDriveFile(googleDriveClient, "test", "testFile", "txt"));
    }
}