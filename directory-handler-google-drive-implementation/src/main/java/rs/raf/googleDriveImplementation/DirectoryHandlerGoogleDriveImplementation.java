package rs.raf.googleDriveImplementation;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import rs.raf.model.DirectoryHandlerConfig;
import rs.raf.specification.IDirectoryHandlerSpecification;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.*;

public class DirectoryHandlerGoogleDriveImplementation implements IDirectoryHandlerSpecification<File>{
    private static Path workingDirectory = Paths.get("directory-handler-project");
    private static Drive googleDriveClient;
    private static String defaultRepositoryName = "defaultRepository";
    private static String defaultDirectoryName = "defaultDirectory";
    private static String defaultFileName = "defaultFile";
    private static String propertiesFileName = "config.properties";
    @Override
    public void authorizeGoogleDriveClient() throws IOException, GeneralSecurityException {
        String APPLICATION_NAME = "directory-handler-lbojanic";
        JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
        String TOKENS_DIRECTORY_PATH = workingDirectory.resolve("tokens").toString();
        List<String> SCOPES = null;
        String CREDENTIALS_FILE_PATH = workingDirectory.resolve("credentials.json").toString();
        SCOPES = Arrays.asList(DriveScopes.DRIVE, DriveScopes.DRIVE_APPDATA, DriveScopes.DRIVE_FILE, DriveScopes.DRIVE_METADATA, DriveScopes.DRIVE_SCRIPTS);
        InputStream credentialsInputStream = new FileInputStream(CREDENTIALS_FILE_PATH);
        HttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        DirectoryHandlerGoogleDriveImplementation directoryHandlerGoogleDriveImplementation = new DirectoryHandlerGoogleDriveImplementation();
        Credential credentials = (Credential) getCredentials(credentialsInputStream, CREDENTIALS_FILE_PATH, HTTP_TRANSPORT, JSON_FACTORY, SCOPES, TOKENS_DIRECTORY_PATH);
        googleDriveClient = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credentials).setApplicationName(APPLICATION_NAME).build();
    }


    @Override
    public Object getCredentials(final InputStream inputStream, final String CREDENTIALS_FILE_PATH, final Object HTTP_TRANSPORT, final Object JSON_FACTORY, final List SCOPES, final String TOKENS_DIRECTORY_PATH) throws IOException {
        if (inputStream == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load((JsonFactory) JSON_FACTORY, new InputStreamReader(inputStream));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder((HttpTransport) HTTP_TRANSPORT, (JsonFactory) JSON_FACTORY, clientSecrets, SCOPES).setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH))).setAccessType("offline").build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        return credential;
    }

    @Override
    public String getRepositoryIdByName(final String directoryName) throws IOException {
        FileList result = googleDriveClient.files().list().setQ("'root' in parents and mimeType='application/vnd.google-apps.folder' and trashed=false").setFields("nextPageToken, files(id, name)").setSpaces("drive").execute();
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

    @Override
    public void createRepository() throws IOException {
        File fileMetadata = new File();
        fileMetadata.setName(defaultRepositoryName);
        fileMetadata.setMimeType("application/vnd.google-apps.folder");
        try {
            File file = googleDriveClient.files().create(fileMetadata).setFields("id").execute();
            System.out.println("Folder ID: " + file.getId());
        }
        catch (GoogleJsonResponseException e) {
            // TODO(developer) - handle error appropriately
            System.err.println("Unable to create folder: " + e.getDetails());
            throw e;
        }
        createDefaultConfig(defaultRepositoryName);
    }

    @Override
    public void createRepository(final String repositoryName) throws IOException {
        File fileMetadata = new File();
        fileMetadata.setName(repositoryName);
        fileMetadata.setMimeType("application/vnd.google-apps.folder");
        try {
            File file = googleDriveClient.files().create(fileMetadata).setFields("id").execute();
            System.out.println("Folder ID: " + file.getId());
        }
        catch (GoogleJsonResponseException e) {
            // TODO(developer) - handle error appropriately
            System.err.println("Unable to create folder: " + e.getDetails());
            throw e;
        }
    }

    @Override
    public void createRepository(final DirectoryHandlerConfig directoryHandlerConfig) throws IOException {
        File fileMetadata = new File();
        fileMetadata.setName(defaultRepositoryName);
        fileMetadata.setMimeType("application/vnd.google-apps.folder");
        try {
            File file = googleDriveClient.files().create(fileMetadata).setFields("id").execute();
            System.out.println("Folder ID: " + file.getId());
        }
        catch (GoogleJsonResponseException e) {
            // TODO(developer) - handle error appropriately
            System.err.println("Unable to create folder: " + e.getDetails());
            throw e;
        }
    }

    @Override
    public void createRepository(final String repositoryName, final DirectoryHandlerConfig directoryHandlerConfig) throws IOException {

    }

    @Override
    public void createDirectory(final String repositoryName) {

    }

    @Override
    public void createDirectory(final String repositoryName, final String directoryName) {

    }

    @Override
    public void createFile(final String repositoryName, final String directoryName, final String fileExtension) throws IOException {
        File fileMetadata = new File();
        fileMetadata.setName(defaultFileName + "." + fileExtension);
        fileMetadata.setParents(Collections.singletonList(getRepositoryIdByName(repositoryName)));
        try {
            File file = googleDriveClient.files().create(fileMetadata).setFields("id, name, parents").execute();
            System.out.println("File ID: " + file.getId());

        }
        catch (GoogleJsonResponseException e) {
            System.err.println("Unable to upload file: " + e.getDetails());
            throw e;
        }
    }

    @Override
    public void createFile(final String repositoryName, final String directoryName, final String fileName, final String fileExtension) throws IOException {
        File fileMetadata = new File();
        fileMetadata.setName(fileName + "." + fileExtension);
        fileMetadata.setParents(Collections.singletonList(getRepositoryIdByName(repositoryName)));
        try {
            File file = googleDriveClient.files().create(fileMetadata).setFields("id, name, parents").execute();
            System.out.println("File ID: " + file.getId());

        }
        catch (GoogleJsonResponseException e) {
            System.err.println("Unable to upload file: " + e.getDetails());
            throw e;
        }
    }

    @Override
    public void createDefaultConfig(final String repositoryName) throws IOException {
        createFile(repositoryName, "", "config", "properties");
        updateConfig(repositoryName, new DirectoryHandlerConfig());
    }

    @Override
    public void createConfig(final String repositoryName, final DirectoryHandlerConfig directoryHandlerConfig) throws IOException {
        /*File file = workingDirectory.resolve(repositoryName).resolve(propertiesFileName).toFile();
        file.createNewFile();*/
        updateConfig(repositoryName, new DirectoryHandlerConfig());
    }

    @Override
    public void updateConfig(final String repositoryName, final DirectoryHandlerConfig directoryHandlerConfig) throws IOException {
        Properties properties = getProperties(repositoryName);
        properties.setProperty("maxRepositorySize", directoryHandlerConfig.getMaxRepositorySize());
        properties.setProperty("maxFileCount", Integer.toString(directoryHandlerConfig.getMaxFileCount()));
        properties.setProperty("excludedExtensions", arrayToString(directoryHandlerConfig.getExcludedExtensions()));
        OutputStream outputStream = new FileOutputStream(workingDirectory.resolve(repositoryName).resolve(propertiesFileName).toAbsolutePath().toString());
        properties.store(outputStream, "updatedConfig");
    }

    @Override
    public Properties getProperties(final String repositoryName) throws IOException {
        Properties properties = new Properties();
        FileInputStream fileInputStream = new FileInputStream(workingDirectory.resolve(repositoryName).resolve(propertiesFileName).toFile());
        InputStream inputStream = fileInputStream;
        properties.load(inputStream);
        return properties;
    }

    @Override
    public long getDirectorySize(final String repositoryName, final String directoryName) throws FileNotFoundException, IOException {
        return 0;
    }

    @Override
    public long getFileSize(final String repositoryName, final String directoryName, final String fileName) throws FileNotFoundException, IOException {
        return 0;
    }

    @Override
    public String arrayToString(final String[] array) {
        return null;
    }

    @Override
    public void writeToFile(final String repositoryName, final String directoryName, final String fileName, final String textToWrite) throws IOException {

    }

    @Override
    public void deleteFile(final String repositoryName, final String directoryName, final String fileName) throws IOException {

    }

    @Override
    public void downloadFile(final String repositoryName, final String directoryName, final String fileName, boolean overwrite) throws IOException {

    }

    @Override
    public void downloadFile(final String repositoryName, final String directoryName, final String fileName, final String downloadPathString, boolean overwrite) throws IOException {

    }

    @Override
    public void moveOrRenameFile(final String repositoryName, final String directoryName, final String fileName, final String newName) throws IOException {

    }
    @Override
    public int getFileCount(final String repositoryName, final String directoryName) {
        return 0;
    }
    @Override
    public List<File> getFileList(final String repositoryName, final String directoryName) throws IOException {
        return null;
    }

    @Override
    public List<File> getFileListInRoot() throws IOException {
        List<File> fileList = new ArrayList<>();
        FileList result = googleDriveClient.files().list().setQ("'root' in parents and trashed = false").setFields("nextPageToken, files(id, name, parents)").execute();
        List<File> files = result.getFiles();
        if (files == null || files.isEmpty()) {
            System.out.println("No files found.");
        }
        else {
            for (File file : files) {
                fileList.add(file);
            }
        }
        return fileList;
    }

    @Override
    public List<File> getFilesForSearchName(final String repositoryName, final String directoryName, final String search) {
        return null;
    }

    @Override
    public List<File> getFilesForSearchNameAndExtensions(final String repositoryName, final String directoryName, final String search, final String[] searchExtensions) {
        return null;
    }

    @Override
    public List<File> getFilesForSearchNameAndExcludedExtensions(final String repositoryName, final String directoryName, final String search, final String[] searchExcludedExtensions) {
        return null;
    }

    @Override
    public List<File> getFilesForSearchNameAndExtensionsAndExcludedExtensions(final String repositoryName, final String directoryName, final String search, final String[] searchExtensions, final String[] searchExcludedExtensions) {
        return null;
    }

    @Override
    public List<File> getFilesForExtensions(final String repositoryName, final String directoryName, final String[] searchExtensions) {
        return null;
    }

    @Override
    public List<File> getFilesForExcludedExtensions(final String repositoryName, final String directoryName, final String[] searchExcludedExtensions) {
        return null;
    }

    @Override
    public List<File> getFilesForExtensionsAndExcludedExtensions(final String repositoryName, final String directoryName, final String[] searchExtensions, final String[] searchExcludedExtensions) {
        return null;
    }
}