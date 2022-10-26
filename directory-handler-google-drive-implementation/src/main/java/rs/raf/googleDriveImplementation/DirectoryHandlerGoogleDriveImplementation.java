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
import org.apache.commons.io.FileUtils;
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
    public static List<File> fileListInRoot;
    private static String APPLICATION_NAME = "directory-handler-lbojanic";
    private static JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static String TOKENS_DIRECTORY_PATH = workingDirectory.resolve("tokens").toString();
    private static List<String> SCOPES = Arrays.asList(DriveScopes.DRIVE, DriveScopes.DRIVE_APPDATA, DriveScopes.DRIVE_FILE, DriveScopes.DRIVE_METADATA, DriveScopes.DRIVE_SCRIPTS);
    private static String CREDENTIALS_FILE_PATH = workingDirectory.resolve("credentials.json").toString();
    private static HttpTransport HTTP_TRANSPORT = null;
    @Override
    public void authorizeGoogleDriveClient() throws IOException, GeneralSecurityException {
        InputStream credentialsInputStream = new FileInputStream(CREDENTIALS_FILE_PATH);
        HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Credential credentials = (Credential) getCredentials(credentialsInputStream, CREDENTIALS_FILE_PATH, HTTP_TRANSPORT, JSON_FACTORY, SCOPES, TOKENS_DIRECTORY_PATH);
        googleDriveClient = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credentials).setApplicationName(APPLICATION_NAME).build();
        fileListInRoot = getFileListInRoot();
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
    public List<String> getRepositoryIdsByName(final String repositoryName) throws IOException {
        List<String> directoryIdsList = new ArrayList<>();

        for(File file : fileListInRoot){
            if(file.getMimeType().equals("application/vnd.google-apps.folder")) {
                directoryIdsList.add(file.getId());
            }
        }
        return directoryIdsList;
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
        createDefaultConfig(defaultRepositoryName);
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
        createConfig(defaultRepositoryName, directoryHandlerConfig);
    }

    @Override
    public void createRepository(final String repositoryName, final DirectoryHandlerConfig directoryHandlerConfig) throws IOException {
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
        createConfig(repositoryName, directoryHandlerConfig);
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
        fileMetadata.setParents(getRepositoryIdsByName(repositoryName));
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
    public long getRepositorySize(final String repositoryName) {
        return FileUtils.sizeOfDirectory(workingDirectory.resolve(repositoryName).toFile());
    }

    @Override
    public void createFile(final String repositoryName, final String directoryName, final String fileName, final String fileExtension) throws IOException {

    }

    @Override
    public void createDefaultConfig(final String repositoryName) throws IOException {

    }

    @Override
    public void createConfig(final String repositoryName, final DirectoryHandlerConfig directoryHandlerConfig) throws IOException {

    }

    @Override
    public void updateConfig(final String repositoryName, final DirectoryHandlerConfig directoryHandlerConfig) throws IOException {

    }

    @Override
    public Properties getProperties(final String repositoryName) throws IOException {
        return null;
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
        /*if (file.getDownloadUrl() != null && file.getDownloadUrl().length() > 0) {
            try {
                HttpResponse resp = service.getRequestFactory().buildGetRequest(new GenericUrl(file.getDownloadUrl())).execute();
                return resp.getContent();
            } catch (IOException e) {
                // An error occurred.
                e.printStackTrace();
                return null;
            }
        } else {
            // The file doesn't have any content stored on Drive.
            return null;
        }*/
    }

    @Override
    public List<File> getAllFiles(final String directoryName) throws IOException {
        List<File> fileList = new ArrayList<>();
        FileList result = googleDriveClient.files().list().setQ(String.format("'%s' in parents and trashed = false", directoryName)).setFields("nextPageToken, files(id, name, parents, mimeType)").execute();
        List<File> files = result.getFiles();
        if (files == null || files.isEmpty()) {
            System.out.println("No files found.");
        }
        else {
            for (File file : files) {
                fileList.add(file);
                if(file.getMimeType().equals("application/vnd.google-apps.folder")){
                    getAllFiles(file.getId());
                }
            }
        }
        return fileList;
    }

    @Override
    public List<File> getFileListForRepository(final String repositoryName) throws IOException {
        List<File> allFilesForRepository = new ArrayList<>();
        for(File repository : fileListInRoot){
            if(repository.getName().equals(repositoryName)){
                allFilesForRepository = getAllFiles(repository.getId());
                return allFilesForRepository;
            }
        }
        return allFilesForRepository;
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
        //TODO possible optional root in folder!!!!
        FileList result = googleDriveClient.files().list().setQ("'root' in parents and trashed = false").setFields("nextPageToken, files(id, name, parents, mimeType)").execute();
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
    public List<File> getFileListInDirectory(final String directoryName) throws IOException {
        List<File> fileList = new ArrayList<>();
        FileList result = googleDriveClient.files().list().setQ(String.format("'%s' in parents and trashed = false", directoryName)).setFields("nextPageToken, files(id, name, parents)").execute();
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