package rs.raf.googleDriveImplementation;

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
import com.google.api.services.drive.model.FileList;
import rs.raf.specification.DirectoryHandlerGoogleDriveSpecification;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
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
        fileMetadata.setName(directoryName);
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
    public String createGoogleDriveFile(Drive googleDriveClient, String directoryId) throws IOException {
        File fileMetadata = new File();
        fileMetadata.setName("testFile.txt");
        fileMetadata.setParents(Collections.singletonList(getDirectoryIdByName(googleDriveClient, "defaultFileName")));
        try {
            File file = googleDriveClient.files().create(fileMetadata).setFields("id, parents").execute();
            System.out.println("File ID: " + file.getId());
            return file.getId();
        }
        catch (GoogleJsonResponseException e) {
            System.err.println("Unable to upload file: " + e.getDetails());
            throw e;
        }
    }

    @Override
    public String createGoogleDriveFile(Drive googleDriveClient, String directoryName, String fileName) throws IOException {
        File fileMetadata = new File();
        fileMetadata.setName(String.format("%s.txt", fileName));
        fileMetadata.setParents(Collections.singletonList(getDirectoryIdByName(googleDriveClient, directoryName)));
        try {
            File file = googleDriveClient.files().create(fileMetadata).setFields("id, parents").execute();
            System.out.println("File ID: " + file.getId());
            return file.getId();
        }
        catch (GoogleJsonResponseException e) {
            System.err.println("Unable to upload file: " + e.getDetails());
            throw e;
        }
    }

    @Override
    public String createGoogleDriveFile(final Drive googleDriveClient, final String directoryName, final String fileName, final String fileExtension) throws IOException {
        File fileMetadata = new File();
        fileMetadata.setName(String.format("%s.%s", fileName, fileExtension));
        fileMetadata.setParents(Collections.singletonList(getDirectoryIdByName(googleDriveClient, directoryName)));
        //System.out.println(fileMetadata.size());
        try {
            Drive.Files.Create fileCreate = googleDriveClient.files().create(fileMetadata).setFields("id, name, size, parents");
            System.out.println(fileCreate.size());
            File file = fileCreate.execute();
            System.out.println("File ID: " + file.getId() + "Size: " + file.size());
            return file.getId();
        }
        catch (GoogleJsonResponseException e) {
            System.err.println("Unable to upload file: " + e.getDetails());
            throw e;
        }
    }

    @Override
    public List<String> getFileList(final Drive googleDriveClient, final String directoryName) throws IOException {
        List<String> fileList = new ArrayList<>();
        FileList resultSpecific = googleDriveClient.files().list()
                .setQ(String.format("'%s' in parents and trashed = false", getDirectoryIdByName(googleDriveClient, directoryName)))
                .setFields("nextPageToken, files(id, name, parents)").execute();
        //FileList result = googleDriveClient.files().list().setPageSize(10).setFields("nextPageToken, files(id, name)").execute();
        List<File> files = resultSpecific.getFiles();
        if (files == null || files.isEmpty()) {
            System.out.println("No files found.");
        }
        else {
            for (File file : files) {
                fileList.add(file.getName());
            }
        }
        return fileList;
    }

    @Override
    public String getDirectoryIdByName(final Drive googleDriveClient, final String directoryName) throws IOException {
        FileList result = googleDriveClient.files().list().setQ("mimeType='application/vnd.google-apps.folder' and trashed=false").setFields("nextPageToken, files(id, name)").setSpaces("drive").execute();
        List<File> files = result.getFiles();
        if (files == null || files.isEmpty()) {
            System.out.println("No files found.");
        }
        else {
            for (File file : files) {
                if(file.getName().equals(directoryName)){
                    return file.getId();
                }
            }
        }
        return null;
    }
}