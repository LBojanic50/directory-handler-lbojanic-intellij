package rs.raf.googleDriveImplementation;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.FileContent;
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
import rs.raf.model.LocalFile;
import rs.raf.model.SortingType;
import rs.raf.specification.IDirectoryHandlerSpecification;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.util.*;

public class DirectoryHandlerGoogleDriveImplementation implements IDirectoryHandlerSpecification<File> {
    private static Path workingDirectory = Paths.get("directory-handler-project");
    private static Drive googleDriveClient;
    public static List<File> allFilesList;
    private static String APPLICATION_NAME = "directory-handler-lbojanic";
    private static JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static String TOKENS_DIRECTORY_PATH = workingDirectory.resolve("tokens").toString();
    private static List<String> SCOPES = Arrays.asList(DriveScopes.DRIVE, DriveScopes.DRIVE_APPDATA, DriveScopes.DRIVE_FILE, DriveScopes.DRIVE_METADATA, DriveScopes.DRIVE_SCRIPTS);
    private static String CREDENTIALS_FILE_PATH = workingDirectory.resolve("credentials.json").toString();
    private static HttpTransport HTTP_TRANSPORT = null;
    public DirectoryHandlerGoogleDriveImplementation() throws GeneralSecurityException, IOException {
        authorizeGoogleDriveClient();
        allFilesList = getFileListInDirectory(null, true, true, true);
    }

    protected void authorizeGoogleDriveClient() throws IOException, GeneralSecurityException {
        InputStream credentialsInputStream = new FileInputStream(CREDENTIALS_FILE_PATH);
        HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Credential credentials = (Credential) getCredentials(credentialsInputStream, CREDENTIALS_FILE_PATH, HTTP_TRANSPORT, JSON_FACTORY, SCOPES, TOKENS_DIRECTORY_PATH);
        googleDriveClient = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credentials).setApplicationName(APPLICATION_NAME).build();
    }

    protected Object getCredentials(final InputStream inputStream, final String CREDENTIALS_FILE_PATH, final Object HTTP_TRANSPORT, final Object JSON_FACTORY, final List SCOPES, final String TOKENS_DIRECTORY_PATH) throws IOException {
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
        createConfig(repositoryName, new DirectoryHandlerConfig());
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
        List<String> directories = new LinkedList<>(Arrays.asList(directoryPathString.split("/")));
        String directoryName = directories.get(directories.size() - 1);
        directories.remove(directories.size() - 1);
        List<String> listOfIds = new ArrayList<>();
        List<File> currentFileList = allFilesList;
        for(String directory : directories){
            for(File file : currentFileList){
                if(file.getName().equals(directory)){
                    listOfIds.add(file.getId());
                    currentFileList =  getFileListInDirectory(file.getId(),true, true, true);
                    break;
                }
            }
        }
        //TODO
        String directoryId = listOfIds.get(listOfIds.size() - 1);
        File fileMetadata = new File();
        System.out.println(directoryName);
        fileMetadata.setName(directoryName);
        fileMetadata.setParents(Collections.singletonList(directoryId));
        fileMetadata.setMimeType("application/vnd.google-apps.folder");

        File file = googleDriveClient.files().create(fileMetadata)
                .setFields("id, name, parents, mimeType")
                .execute();
        System.out.println("Folder ID: " + file.getId());
    }
    public String getFileIdByName(final String filePathString) throws IOException {
        List<String> directories = new LinkedList<>(Arrays.asList(filePathString.split("/")));
        if(directories.size() > 1){
            directories.remove(directories.size() - 1);
        }
        List<String> listOfIds = new ArrayList<>();
        List<File> currentFileList;
        String currentDirectoryToSearch = null;
        boolean found = false;
        int i = 0;/*
        while(i < directories.size()){
            currentFileList = getFileListInDirectory(currentDirectoryToSearch,true, true, true);
            found = false;
            for(File file : currentFileList){
                if(file.getName().equals(directories.get(i))){
                    currentDirectoryToSearch = file.getId();
                    found = true;
                    break;
                }
            }
            if(found == false){
                throw new FileNotFoundException();
            }
            i++;
        }*/
        List<String> listOfFilePaths = new ArrayList<>();
        for(File file : allFilesList){
            listOfFilePaths.add(getFilePath(file));
        }
        for(String filePath : listOfFilePaths){
            System.out.println(filePath);
        }
        return listOfIds.get(listOfIds.size() - 1);
    }
    protected String getFilePath(File file) throws IOException {
        String parentId = "";
        int i = 0;
        String path = "";
        while(true){
            try {
                if (i > 0) {
                    file = googleDriveClient.files().get(file.getParents().toString()).execute();
                }
                path = path + "/" + file.getName();
                i++;
            }
            catch (NullPointerException | GoogleJsonResponseException e) {
                break;
            }
        }
        return path;
    }

    @Override
    public boolean createFile(final String filePathString) throws Exception, FileAlreadyExistsException {
        File fileMetadata = new File();
        List<String> directories = new LinkedList<>(Arrays.asList(filePathString.split("/")));
        String filename = directories.get(directories.size() - 1);
        fileMetadata.setName(filename);
        fileMetadata.setMimeType("application/octet-stream");
        try {
            File file = googleDriveClient
                    .files()
                    .create(fileMetadata)
                    .setFields("id, name, parents, mimeType")
                    .execute();
        }
        catch (GoogleJsonResponseException e) {
            System.err.println("Unable to create folder: " + e.getDetails());
            throw e;
        }
        return true;
    }
    @Override
    public void createConfig(final String repositoryName, final DirectoryHandlerConfig directoryHandlerConfig) throws IOException, FileAlreadyExistsException {
        updateConfig(repositoryName, directoryHandlerConfig, null);
    }
    protected void createConfig(){

    }
    @Override
    public void updateConfig(final String repositoryName, final DirectoryHandlerConfig directoryHandlerConfig, final String directoriesWithMaxFileCountString) throws IOException {



        String propertiesPathString;
        Properties properties = getConfig(repositoryName);
        if(directoryHandlerConfig == null && directoriesWithMaxFileCountString == null){
            System.out.println("Config not updated");
            return;
        }
        if(directoryHandlerConfig != null && directoriesWithMaxFileCountString == null){
            properties.setProperty("maxRepositorySize", directoryHandlerConfig.getMaxRepositorySize());
            properties.setProperty("excludedExtensions", directoryHandlerConfig.getExcludedExtensionsString());
        }
        if(directoryHandlerConfig == null && directoriesWithMaxFileCountString != null){
            List<String> directoriesWithMaxFileCount = List.of(directoriesWithMaxFileCountString.split(","));
            for(String directoryWithMaxFileCount : directoriesWithMaxFileCount){
                String directory = directoryWithMaxFileCount.substring(0, directoryWithMaxFileCount.indexOf("-"));
                String maxFileCount = directoryWithMaxFileCount.substring(directoryWithMaxFileCount.indexOf("-"));
                properties.setProperty(directory, maxFileCount);
            }
        }
        if(directoryHandlerConfig != null && directoriesWithMaxFileCountString != null){
            properties.setProperty("maxRepositorySize", directoryHandlerConfig.getMaxRepositorySize());
            properties.setProperty("excludedExtensions", directoryHandlerConfig.getExcludedExtensionsString());
            List<String> directoriesWithMaxFileCount = List.of(directoriesWithMaxFileCountString.split(","));
            for(String directoryWithMaxFileCount : directoriesWithMaxFileCount){
                String directory = directoryWithMaxFileCount.substring(0, directoryWithMaxFileCount.indexOf("-"));
                String maxFileCount = directoryWithMaxFileCount.substring(directoryWithMaxFileCount.indexOf("-"));
                properties.setProperty(directory, maxFileCount);
            }
        }
        OutputStream outputStream = new FileOutputStream(workingDirectory.resolve("temp").resolve("config.properties").toAbsolutePath().toString());
        properties.store(outputStream, "updatedConfig");





        Path tempPath = workingDirectory.resolve("temp");
        Path configPath = tempPath.resolve("config.properties");
        if(!Files.exists(tempPath)){
            tempPath.toFile().mkdir();
            configPath.toFile().createNewFile();
        }
        else{
            if(!Files.exists(configPath)){
                configPath.toFile().createNewFile();
            }
        }


        File fileMetadata = new File();
        fileMetadata.setName("config.properties");
        fileMetadata.setParents(Collections.singletonList(getFileIdByName(repositoryName)));

        java.io.File localConfigFile = configPath.toFile();
        FileContent mediaContent = new FileContent("text/plain", localConfigFile);
        try {
            File file = googleDriveClient
                    .files()
                    .create(fileMetadata, mediaContent)
                    .setFields("id, name, parents, mimeType")
                    .execute();
        }
        catch (GoogleJsonResponseException e) {
            System.err.println("Unable to upload file: " + e.getDetails());
            throw e;
        }
    }

    @Override
    public Properties getConfig(final String repositoryName) throws IOException {
        String downloadPathString = "temp";
        downloadFile(repositoryName + "/config.properties", downloadPathString, true);
        Properties properties = new Properties();
        FileInputStream fileInputStream = new FileInputStream(workingDirectory.resolve(downloadPathString).resolve("config.properties").toFile());
        InputStream inputStream = fileInputStream;
        properties.load(inputStream);
        return properties;

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
                    .setFields("id, name, parents, mimeType")
                    .execute();
        } catch (GoogleJsonResponseException e) {
            System.err.println("Unable to move file: " + e.getDetails());
            throw e;
        }
    }
    @Override
    public int getFileCount(final String directoryPathString) {
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
    public void downloadFile(final String filePathString, final String downloadPathString, final boolean overwrite) throws IOException {
        Path downloadPathParent;
        if(downloadPathString == null){
            downloadPathParent = workingDirectory.resolve("Downloads");
        }
        else{
            downloadPathParent = workingDirectory.resolve(Paths.get(downloadPathString));
        }
        if(!Files.exists(downloadPathParent)){
            downloadPathParent.toFile().mkdir();
        }
        OutputStream outputStream = new ByteArrayOutputStream();
        googleDriveClient
                .files()
                .get(getFileIdByName(filePathString))
                //.get("15jRbciq-HNvY8xzN-hN2lJM2rhCR4c6_")
                .executeMediaAndDownloadTo(outputStream);
        java.io.File file = downloadPathParent.toFile();
        InputStream inputStream = new ByteArrayInputStream(((ByteArrayOutputStream)outputStream).toByteArray());
        FileUtils.copyInputStreamToFile(inputStream, file);
    }

    @Override
    public List<File> getFileListInDirectory(final String directoryPathString, final boolean recursive, final boolean includeFiles, final boolean includeDirectories) throws IOException {
        List<File> fileList = new ArrayList<>();
        if(directoryPathString == null){
            if(recursive){
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
            else{
                FileList result = googleDriveClient
                        .files()
                        .list()
                        .setQ("'root' in parents and trashed = false")
                        .setFields("files(id, name, parents, mimeType)")
                        .execute();
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
        }
        else{
            FileList result = googleDriveClient
                    .files()
                    .list()
                    .setQ(String.format("'%s' in parents and trashed = false", directoryPathString))
                    .setFields("files(id, name, parents, mimeType)")
                    .execute();
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
    public List<File> getAllFiles(String directoryPathString, boolean recursive, boolean includeFiles, boolean includeDirectories, SortingType sortingType) throws IOException {
        return null;
    }

    @Override
    public List<File> getFilesForSearchName(String directoryPathString, String search, boolean recursive, boolean includeFiles, boolean includeDirectories, SortingType sortingType) throws IOException {
        return null;
    }

    @Override
    public List<File> getFilesForSearchNameAndExtensions(String directoryPathString, String search, String searchExtensionsString, boolean recursive, boolean includeFiles, boolean includeDirectories, SortingType sortingType) throws IOException {
        return null;
    }

    @Override
    public List<File> getFilesForSearchNameAndExcludedExtensions(String directoryPathString, String search, String searchExcludedExtensionsString, boolean recursive, boolean includeFiles, boolean includeDirectories, SortingType sortingType) throws IOException {
        return null;
    }

    @Override
    public List<File> getFilesForSearchNameAndExtensionsAndExcludedExtensions(String directoryPathString, String search, String searchExtensionsString, String searchExcludedExtensionsString, boolean recursive, boolean includeFiles, boolean includeDirectories, SortingType sortingType) throws IOException {
        return null;
    }

    @Override
    public List<File> getFilesForExtensions(String directoryPathString, String searchExtensionsString, boolean recursive, boolean includeFiles, boolean includeDirectories, SortingType sortingType) throws IOException {
        return null;
    }

    @Override
    public List<File> getFilesForExcludedExtensions(String directoryPathString, String searchExcludedExtensionsString, boolean recursive, boolean includeFiles, boolean includeDirectories, SortingType sortingType) throws IOException {
        return null;
    }

    @Override
    public List<File> getFilesForExtensionsAndExcludedExtensions(String directoryPathString, String searchExtensionsString, String searchExcludedExtensionsString, boolean recursive, boolean includeFiles, boolean includeDirectories, SortingType sortingType) throws IOException {
        return null;
    }

    @Override
    public List<File> getFilesWithName(String directoryPathString, String search, boolean recursive, boolean includeFiles, boolean includeDirectories, SortingType sortingType) throws IOException {
        return null;
    }

    @Override
    public List<File> getFilesForDateRange(String directoryPathString, String startDate, String endDate, boolean dateCreated, boolean dateModified, boolean recursive, boolean includeFiles, boolean includeDirectories, SortingType sortingType) throws IOException, ParseException {
        return null;
    }

    @Override
    public String getParentDirectoryForFile(String directoryPathString, String fileName) throws IOException {
        return null;
    }
}