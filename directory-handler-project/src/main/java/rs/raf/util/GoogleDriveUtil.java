package rs.raf.util;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import rs.raf.drive.DirectoryHandlerGoogleDriveImplementation;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

public class GoogleDriveUtil {
    private static final String APPLICATION_NAME = "directory-handler-lbojanic";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static List<String> SCOPES = null;
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    private static NetHttpTransport HTTP_TRANSPORT = null;
    private static DirectoryHandlerGoogleDriveImplementation directoryHandlerGoogleDriveImplementation = null;
    private static Credential credentials = null;
    private static Drive googleDriveClient = null;
    
    public void initScopes() {
    	SCOPES = Arrays.asList(DriveScopes.DRIVE, DriveScopes.DRIVE_APPDATA, DriveScopes.DRIVE_FILE, DriveScopes.DRIVE_METADATA, DriveScopes.DRIVE_SCRIPTS);
    	System.out.println(SCOPES);
    }

    public GoogleDriveUtil() throws GeneralSecurityException, IOException {
    	initScopes();
        HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        directoryHandlerGoogleDriveImplementation = new DirectoryHandlerGoogleDriveImplementation();
        credentials = directoryHandlerGoogleDriveImplementation.getCredentials(
        		GoogleDriveUtil.class.getResourceAsStream(CREDENTIALS_FILE_PATH), CREDENTIALS_FILE_PATH, HTTP_TRANSPORT, JSON_FACTORY, SCOPES, TOKENS_DIRECTORY_PATH);
        googleDriveClient = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credentials).setApplicationName(APPLICATION_NAME).build();
    }
    public void startApp() throws IOException {
        /*FileList result = googleDriveClient.files().list().setPageSize(10).setFields("nextPageToken, files(id, name)").execute();
        List<File> files = result.getFiles();
        if (files == null || files.isEmpty()) {
            System.out.println("No files found.");
        } else {
            System.out.println("Files:");
            for (File file : files) {
                System.out.printf("%s (%s)\n", file.getName(), file.getId());
            }
        }*/
    	System.out.println(directoryHandlerGoogleDriveImplementation.createGoogleDriveFile(googleDriveClient, "test", "testFile", "txt"));
    }
}