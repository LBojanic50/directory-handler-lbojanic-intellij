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
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.*;

public class DirectoryHandlerGoogleDriveImplementation implements IDirectoryHandlerSpecification<File> {
    private static Path workingDirectory = Paths.get("directory-handler-project");
    private static Drive googleDriveClient;
    private static String defaultRepositoryName = "defaultRepository";
    private static String defaultDirectoryName = "defaultDirectory";
    private static String defaultFileName = "defaultFile";
    private static String propertiesFileName = "config.properties";
    public static List<File> allFilesList;
    private static String APPLICATION_NAME = "directory-handler-lbojanic";
    private static JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static String TOKENS_DIRECTORY_PATH = workingDirectory.resolve("tokens").toString();
    private static List<String> SCOPES = Arrays.asList(DriveScopes.DRIVE, DriveScopes.DRIVE_APPDATA, DriveScopes.DRIVE_FILE, DriveScopes.DRIVE_METADATA, DriveScopes.DRIVE_SCRIPTS);
    private static String CREDENTIALS_FILE_PATH = workingDirectory.resolve("credentials.json").toString();
    private static HttpTransport HTTP_TRANSPORT = null;
    public DirectoryHandlerGoogleDriveImplementation() throws GeneralSecurityException, IOException {
        authorizeGoogleDriveClient();
    }
    @Override
    public void authorizeGoogleDriveClient() throws IOException, GeneralSecurityException {
        InputStream credentialsInputStream = new FileInputStream(CREDENTIALS_FILE_PATH);
        HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Credential credentials = (Credential) getCredentials(credentialsInputStream, CREDENTIALS_FILE_PATH, HTTP_TRANSPORT, JSON_FACTORY, SCOPES, TOKENS_DIRECTORY_PATH);
        googleDriveClient = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credentials).setApplicationName(APPLICATION_NAME).build();
        allFilesList = getFileListInDirectory("", true, true, true);
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
    public String getFileIdByName(String fileName) throws IOException {
        return null;
    }
    @Override
    public String getFileIdByNameInRepository(List<File> fileListInRepository, String fileName) throws IOException {
        return null;
    }
    @Override
    public void createRepository(final String repositoryName) throws IOException {
        File fileMetadata = new File();
        fileMetadata.setName(repositoryName);
        fileMetadata.setMimeType("application/vnd.google-apps.folder");
        try {
            File file = googleDriveClient.files().create(fileMetadata).setFields("id, name, mimeType").execute();
            System.out.println("Folder ID: " + file.getId());
        }
        catch (GoogleJsonResponseException e) {
            // TODO(developer) - handle error appropriately
            System.err.println("Unable to create folder: " + e.getDetails());
            throw e;
        }
        createDefaultConfig(repositoryName);
    }
    @Override
    public void createRepository(final String repositoryName, final DirectoryHandlerConfig directoryHandlerConfig) throws IOException {
        File fileMetadata = new File();
        fileMetadata.setName(repositoryName);
        fileMetadata.setMimeType("application/vnd.google-apps.folder");
        try {
            File file = googleDriveClient.files().create(fileMetadata).setFields("id, name, mimeType").execute();
            System.out.println("Folder ID: " + file.getId());
        }
        catch (GoogleJsonResponseException e) {
            // TODO(developer) - handle error appropriately
            System.err.println("Unable to create folder: " + e.getDetails());
            throw e;
        }
        createConfig(repositoryName, directoryHandlerConfig);

    }
    @Override
    public void createDirectory(final String directoryPathString) throws IOException, FileAlreadyExistsException {

    }
    @Override
    public boolean createFile(final String filePathString) throws Exception, FileAlreadyExistsException {
        return false;
    }
    @Override
    public void createDefaultConfig(final String repositoryName) throws IOException, FileAlreadyExistsException {

    }
    @Override
    public void createConfig(final String repositoryName, final DirectoryHandlerConfig directoryHandlerConfig) throws IOException, FileAlreadyExistsException {

    }
    @Override
    public void updateConfig(final String repositoryName, final DirectoryHandlerConfig directoryHandlerConfig) throws IOException, FileAlreadyExistsException {

    }
    @Override
    public Properties getProperties(final String repositoryName) throws IOException {
        return null;
    }
    @Override
    public void moveOrRenameFile(final String oldPathString, final String newPathString) throws IOException {
        File file = googleDriveClient.files().get(oldPathString)
                .setFields("id, parents")
                .execute();
        StringBuilder previousParents = new StringBuilder();
        for (String parent : file.getParents()) {
            previousParents.append(parent);
            previousParents.append(',');
        }
        try {
            // Move the file to the new folder
            file = googleDriveClient.files().update(oldPathString, null)
                    .setAddParents(newPathString)
                    .setRemoveParents(previousParents.toString())
                    .setFields("id, parents")
                    .execute();
        } catch (GoogleJsonResponseException e) {
            System.err.println("Unable to move file: " + e.getDetails());
            throw e;
        }
    }
    @Override
    public int getFileCount(String directoryPathString) {
        return 0;
    }
    @Override
    public long getFileSize(final String filePathString) throws NullPointerException {
        return 0;
    }
    @Override
    public long getDirectorySize(final String directoryPathString) throws NullPointerException {
        return 0;
    }
    @Override
    public void writeToFile(final String filePathString, final String textToWrite) throws IOException {

    }
    @Override
    public void deleteFile(final String filePathString) throws IOException {

    }
    @Override
    public void downloadFile(final String filePathString, final boolean overwrite) throws IOException {

    }
    @Override
    public void downloadFile(final String filePathString, final String downloadPathString, final boolean overwrite) throws IOException {

    }

    @Override
    public List<File> getFileListInDirectory(final String directoryPathString, final boolean recursive, final boolean includeFiles, final boolean includeDirectories) throws IOException {
        List<File> fileList = new ArrayList<>();
        //TODO possible sqtQ root in folder!!!!
        if(directoryPathString.equals("")){
            FileList result = googleDriveClient.files().list().setFields("nextPageToken, files(id, name, parents, mimeType)").execute();
            List<File> files = result.getFiles();
            if (files == null || files.isEmpty()) {
                System.out.println("No files found.");
            }
            else {
                for (File file : files) {
                    fileList.add(file);
                }
            }
        }
        return fileList;
    }
    @Override
    public List<File> getFilesForSearchName(final String directoryPathString, final String search, final boolean recursive, final boolean includeFiles, final boolean includeDirectories) throws IOException {
        return null;
    }

    @Override
    public List<File> getFilesForSearchNameAndExtensions(final String directoryPathString, final String search, final String searchExtensionsString, final boolean recursive, final boolean includeFiles, final boolean includeDirectories) throws IOException {
        return null;
    }

    @Override
    public List<File> getFilesForSearchNameAndExcludedExtensions(final String directoryPathString, final String search, final String searchExcludedExtensionsString, final boolean recursive,final  boolean includeFiles, final boolean includeDirectories) throws IOException {
        return null;
    }

    @Override
    public List<File> getFilesForSearchNameAndExtensionsAndExcludedExtensions(final String directoryPathString, final String search, final String searchExtensionsString, final String searchExcludedExtensionsString, final boolean recursive, final boolean includeFiles, final boolean includeDirectories) throws IOException {
        return null;
    }

    @Override
    public List<File> getFilesForExtensions(final String directoryPathString, final String searchExtensionsString, final boolean recursive, final boolean includeFiles, final boolean includeDirectories) throws IOException {
        return null;
    }

    @Override
    public List<File> getFilesForExcludedExtensions(final String directoryPathString, final String searchExcludedExtensionsString, final boolean recursive, final boolean includeFiles, final boolean includeDirectories) throws IOException {
        return null;
    }

    @Override
    public List<File> getFilesForExtensionsAndExcludedExtensions(final String directoryPathString, final String searchExtensionsString, final String searchExcludedExtensionsString, final boolean recursive, final boolean includeFiles, final boolean includeDirectories) throws IOException {
        return null;
    }

    @Override
    public List<File> getFilesWithName(final String directoryPathString, final String search, final boolean recursive, final boolean includeFiles, final boolean includeDirectories) throws IOException {
        return null;
    }
}