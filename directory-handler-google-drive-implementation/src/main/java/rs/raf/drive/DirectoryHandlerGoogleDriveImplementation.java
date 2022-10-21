package rs.raf.drive;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import rs.raf.specification.DirectoryHandlerGoogleDriveSpecification;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DirectoryHandlerGoogleDriveImplementation implements DirectoryHandlerGoogleDriveSpecification {
	@Override
	public Credential getCredentials(InputStream inputStream, String CREDENTIALS_FILE_PATH,
			NetHttpTransport HTTP_TRANSPORT, JsonFactory JSON_FACTORY, List<String> SCOPES,
			String TOKENS_DIRECTORY_PATH) throws IOException {
		if (inputStream == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(inputStream));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES).setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH))).setAccessType("offline").build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        return credential;
	}
	@Override
	public String createGoogleDriveDirectory(final Drive googleDriveClient) throws IOException {
		File fileMetadata = new File();
    	fileMetadata.setName("defaultDirectoryName");
    	fileMetadata.setMimeType("application/vnd.google-apps.folder");
    	try {
    	      File file = googleDriveClient.files().create(fileMetadata)
    	          .setFields("id")
    	          .execute();
    	      System.out.println("Folder ID: " + file.getId());
    	      return file.getId();
    	    } 
    	catch (GoogleJsonResponseException e) {
    	      // TODO(developer) - handle error appropriately
    	      System.err.println("Unable to create folder: " + e.getDetails());
    	      throw e;
    	}
	}
	@Override
	public String createGoogleDriveDirectory(final Drive googleDriveClient, final String directoryName) throws IOException {
		File fileMetadata = new File();
    	fileMetadata.setName("directoryName");
    	fileMetadata.setMimeType("application/vnd.google-apps.folder");
    	try {
    	      File file = googleDriveClient.files().create(fileMetadata)
    	          .setFields("id")
    	          .execute();
    	      System.out.println("Folder ID: " + file.getId());
    	      return file.getId();
    	    } 
    	catch (GoogleJsonResponseException e) {
    	      System.err.println("Unable to create folder: " + e.getDetails());
    	      throw e;
    	}
		
	}
	@Override
	public String createGoogleDriveFile(final Drive googleDriveClient, final String directoryName) throws IOException {
		File fileMetadata = new File();
		
    	fileMetadata.setName("defaultFileName");
    	fileMetadata.setMimeType("application/octet-stream");
    	try {
    	      File file = googleDriveClient.files().create(fileMetadata)
    	          .setFields("id")
    	          .execute();
    	      System.out.println("Folder ID: " + file.getId());
    	      return file.getId();
    	    } 
    	catch (GoogleJsonResponseException e) {
    	      System.err.println("Unable to create folder: " + e.getDetails());
    	      throw e;
    	}
	}
	@Override
	public String createGoogleDriveFile(final Drive googleDriveClient, final String directoryName, final String fileExtension) throws IOException {
		File fileMetadata = new File();
		List<String> parentFolders = new ArrayList<String>();
		parentFolders.add(directoryName);
    	fileMetadata.setMimeType("application/octet-stream");
    	fileMetadata.setName("defaultFileName");
    	fileMetadata.setFileExtension(fileExtension);
    	fileMetadata.setParents(parentFolders);
    	try {
    	      File file = googleDriveClient.files().create(fileMetadata)
    	          .setFields("id")
    	          .execute();
    	      System.out.println("Folder ID: " + file.getId());
    	      return file.getId();
    	    } 
    	catch (GoogleJsonResponseException e) {
    	      // TODO(developer) - handle error appropriately
    	      System.err.println("Unable to create folder: " + e.getDetails());
    	      throw e;
    	}		
	}
	@Override
	public String createGoogleDriveFile(final Drive googleDriveClient, final String directoryName, final String fileName, final String fileExtension) throws IOException {
		File fileMetadata = new File();
		createGoogleDriveDirectory(googleDriveClient, "test2");
		List<String> parentFolders = new ArrayList<String>();
		parentFolders.add("test2");
    	fileMetadata.setMimeType("application/octet-stream");
    	fileMetadata.setParents(parentFolders);
    	fileMetadata.setName("fileName");
    	fileMetadata.setFileExtension(fileExtension);
    	try {
    	      File file = googleDriveClient.files().create(fileMetadata).setFields("id").execute();
    	      System.out.println("Folder ID: " + file.getId());
    	      return file.getId();
    	    } 
    	catch (GoogleJsonResponseException e) {
    	      System.err.println("Unable to create folder: " + e.getDetails());
    	      throw e;
    	}
	}
}