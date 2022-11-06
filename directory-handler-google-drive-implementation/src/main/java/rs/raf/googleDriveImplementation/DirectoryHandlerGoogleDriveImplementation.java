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
import rs.raf.specification.DirectoryHandlerManager;
import rs.raf.specification.IDirectoryHandlerSpecification;
import rs.raf.util.GoogleDriveComparators;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    static{
        try {
            DirectoryHandlerManager.registerDirectoryHandler(DirectoryHandlerGoogleDriveImplementation.getInstance());
        }
        catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public DirectoryHandlerGoogleDriveImplementation() throws GeneralSecurityException, IOException {
        super();
        authorizeGoogleDriveClient();
        allFilesList = getFileListInDirectory(null, true, true, true);
    }
    private static DirectoryHandlerGoogleDriveImplementation instance;
    public static DirectoryHandlerGoogleDriveImplementation getInstance() throws GeneralSecurityException, IOException {
        if(instance == null){
            instance = new DirectoryHandlerGoogleDriveImplementation();
        }
        return instance;
    }

    @Override
    public Path getWorkingDirectory(){
        return null;
    }
    @Override
    public void printFileList(final List<File> list) throws IOException {
        for(File file : list){
            System.out.println(file.getName() + " : " + file.getId());
        }
    }

    protected void authorizeGoogleDriveClient() throws IOException, GeneralSecurityException {
        InputStream credentialsInputStream = new FileInputStream(CREDENTIALS_FILE_PATH);
        HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Credential credentials = getCredentials(credentialsInputStream, CREDENTIALS_FILE_PATH, HTTP_TRANSPORT, JSON_FACTORY, SCOPES, TOKENS_DIRECTORY_PATH);
        googleDriveClient = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credentials).setApplicationName(APPLICATION_NAME).build();
    }

    protected Credential getCredentials(final InputStream inputStream, final String CREDENTIALS_FILE_PATH, final Object HTTP_TRANSPORT, final Object JSON_FACTORY, final List SCOPES, final String TOKENS_DIRECTORY_PATH) throws IOException {
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
    public String getFileIdByPath(final String filePathString) throws IOException {
        List<String> directories = new LinkedList<>(Arrays.asList(filePathString.split("/")));
        List<File> currentFileList;
        String currentDirectoryToSearch = "root";
        boolean found = false;
        int i = 0;
        while(i < directories.size()){
            currentFileList = getCurrentDirectoryFiles(currentDirectoryToSearch);
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
        }
        return currentDirectoryToSearch;
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
        String parentDirectoriesPathString = directoryPathString.substring(0, directoryPathString.lastIndexOf("/"));
        String directoryName = directoryPathString.substring(directoryPathString.lastIndexOf("/") + 1);
        File fileMetadata = new File();
        fileMetadata.setName(directoryName);
        fileMetadata.setParents(Collections.singletonList(getFileIdByPath(parentDirectoriesPathString)));
        fileMetadata.setMimeType("application/vnd.google-apps.folder");
        try {
            File file = googleDriveClient
                    .files()
                    .create(fileMetadata)
                    .setFields("id, name, parents, mimeType")
                    .execute();
        }
        catch (GoogleJsonResponseException e) {
            System.err.println("Unable to create directory: " + e.getDetails());
            throw e;
        }
    }
    @Override
    public boolean createFile(final String filePathString) throws Exception, FileAlreadyExistsException {
        String parentDirectoriesPathString = filePathString.substring(0, filePathString.lastIndexOf("/"));
        String fileName = filePathString.substring(filePathString.lastIndexOf("/") + 1);
        File fileMetadata = new File();
        fileMetadata.setName(fileName);
        fileMetadata.setParents(Collections.singletonList(getFileIdByPath(parentDirectoriesPathString)));
        fileMetadata.setMimeType("application/octet-stream");
        try {
            File file = googleDriveClient
                    .files()
                    .create(fileMetadata)
                    .setFields("id, name, parents, mimeType")
                    .execute();
        }
        catch (GoogleJsonResponseException e) {
            System.err.println("Unable to create file: " + e.getDetails());
            throw e;
        }
        return true;
    }
    @Override
    public void createConfig(final String repositoryName, final DirectoryHandlerConfig directoryHandlerConfig) throws IOException, FileAlreadyExistsException {
        updateConfig(repositoryName, directoryHandlerConfig, null);
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
        fileMetadata.setParents(Collections.singletonList(getFileIdByPath(repositoryName)));

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
                .setFields("id, name, parents, mimeType")
                .execute();
        StringBuilder previousParents = new StringBuilder();
        for (String parent : file.getParents()) {
            previousParents.append(parent);
            previousParents.append(',');
        }
        try {
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
    public int getFileCount(final String directoryPathString) throws IOException {
        FileList result = googleDriveClient.files().list().setQ(String.format("'%s' in parents and trashed = false", getFileIdByPath(directoryPathString))).setFields("files(id, name, parents, mimeType)").setSpaces("drive").execute();
        List<File> files = result.getFiles();
        int count = 0;
        if(files != null && !files.isEmpty()){
            for(File file : files){
                count++;
            }
        }
        return count;
    }
    @Override
    public long getFileSize(final String filePathString) throws NullPointerException, IOException {
        File file = googleDriveClient.files().get(getFileIdByPath(filePathString)).setFields("files(id, name, parents, mimeType)").execute();
        return file.getSize();
    }
    @Override
    public long getDirectorySize(final String directoryPathString) throws NullPointerException, IOException {
        long directorySize = 0;
        if(directoryPathString == null){
            for(File file : allFilesList){
                if(!file.getMimeType().equals("application/vnd.google-apps.folder")){
                    directorySize += file.getSize();
                }
            }
        }
        else{
            FileList result = googleDriveClient.files().list().setQ(String.format("'%s' in parents and mimeType != 'application/vnd.google-apps.folder' and trashed = false", getFileIdByPath(directoryPathString))).setFields("files(id, name, parents, mimeType)").setSpaces("drive").execute();
            List<File> files = result.getFiles();
            if(files != null && !files.isEmpty()){
                for(File file : files){
                    directorySize += file.getSize();
                }
            }
        }
        return directorySize;
    }
    @Override
    public void writeToFile(final String filePathString, final String textToWrite) throws IOException {

    }
    @Override
    public void deleteFile(final String filePathString) throws IOException {
        googleDriveClient.files().delete(getFileIdByPath(filePathString));
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
                .get(getFileIdByPath(filePathString))
                //.get("15jRbciq-HNvY8xzN-hN2lJM2rhCR4c6_")
                .executeMediaAndDownloadTo(outputStream);
        java.io.File file = downloadPathParent.toFile();
        InputStream inputStream = new ByteArrayInputStream(((ByteArrayOutputStream)outputStream).toByteArray());
        FileUtils.copyInputStreamToFile(inputStream, file);
    }

    @Override
    public List<File> getFileListInDirectory(final String directoryPathString, final boolean recursive, final boolean includeFiles, final boolean includeDirectories) throws IOException {
        List<File> fileList = new ArrayList<>();
        boolean doingRecursiveSearch = false;

        if(!includeFiles && !includeDirectories){
            System.out.println("error");
        }
        if(directoryPathString == null){
            FileList result = null;
            if(recursive){
                if(includeFiles && !includeDirectories){
                    result = googleDriveClient.files().list().setQ("mimeType != 'application/vnd.google-apps.folder' and trashed = false").setFields("nextPageToken, files(id, name, parents, mimeType)").setSpaces("drive").execute();
                }
                if(!includeFiles && includeDirectories){
                    result = googleDriveClient.files().list().setQ("mimeType = 'application/vnd.google-apps.folder' and trashed = false").setFields("nextPageToken, files(id, name, parents, mimeType)").setSpaces("drive").execute();
                }
                if(includeFiles && includeDirectories){
                    result = googleDriveClient.files().list().setFields("nextPageToken, files(id, name, parents, mimeType)").setSpaces("drive").execute();
                }
                
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
                if(includeFiles && !includeDirectories){
                    result = googleDriveClient.files().list().setQ("'root' in parents and mimeType != 'application/vnd.google-apps.folder' and trashed = false").setFields("files(id, name, parents, mimeType)").setSpaces("drive").execute();
                }
                if(!includeFiles && includeDirectories){
                    result = googleDriveClient.files().list().setQ("'root' in parents and mimeType = 'application/vnd.google-apps.folder' and trashed = false").setFields("files(id, name, parents, mimeType)").setSpaces("drive").execute();
                }
                if(includeFiles && includeDirectories){
                    result = googleDriveClient.files().list().setQ("'root' in parents and trashed = false").setFields("files(id, name, parents, mimeType)").setSpaces("drive").execute();
                }
                List<File> files = result.getFiles();
                if (files == null || files.isEmpty()) {
                    return null;
                }
                else {
                    for (File file : files) {
                        fileList.add(file);
                    }
                }
            }
        }
        else{
            if(recursive){
                List<File> currentFileList = new ArrayList<>();
                String currentDirectoryToSearch = null;
                while(currentFileList != null){
                    currentFileList = getFileListInDirectory(currentDirectoryToSearch,false, includeFiles, includeDirectories);
                    for(File file : currentFileList) {
                        fileList.add(file);
                        if(file.getMimeType().equals("application/vnd.google-apps.folder")){
                            currentDirectoryToSearch = file.getId();
                        }
                    }
                }
            }
            else{
                FileList result = null;
                if(includeFiles && !includeDirectories){
                    result = googleDriveClient.files().list().setQ(String.format("'%s' in parents and mimeType != 'application/vnd.google-apps.folder' and trashed = false", getFileIdByPath(directoryPathString))).setFields("files(id, name, parents, mimeType)").setSpaces("drive").execute();
                }
                if(!includeFiles && includeDirectories){
                    result = googleDriveClient.files().list().setQ(String.format("'%s' in parents and mimeType = 'application/vnd.google-apps.folder' and trashed = false", getFileIdByPath(directoryPathString))).setFields("files(id, name, parents, mimeType)").setSpaces("drive").execute();
                }
                if(includeFiles && includeDirectories){
                    result = googleDriveClient.files().list().setQ(String.format("'%s' in parents and trashed = false", getFileIdByPath(directoryPathString))).setFields("files(id, name, parents, mimeType)").setSpaces("drive").execute();
                }
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
        return fileList;
    }
    protected List<File> getCurrentDirectoryFiles(final String currentDirectoryId) throws IOException {
        List<File> fileList = new ArrayList<>();
        FileList result = googleDriveClient.files().list().setQ(String.format("'%s' in parents and trashed = false", currentDirectoryId)).setFields("files(id, name, parents, mimeType)").setSpaces("drive").execute();
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
    public List<File> getAllFiles(final String directoryPathString, final boolean recursive, final boolean includeFiles, final boolean includeDirectories, final SortingType sortingType) throws IOException {
        return sortList(getFileListInDirectory(directoryPathString, recursive, includeFiles, includeDirectories), sortingType);
    }

    @Override
    public List<File> getFilesForSearchName(final String directoryPathString, final String search, final boolean recursive, final boolean includeFiles, final boolean includeDirectories, final SortingType sortingType) throws IOException {
        List<File> directoryToSearchList = getFileListInDirectory(directoryPathString, recursive, includeFiles, includeDirectories);
        List<File> fileList = new ArrayList<>();
        for(File file : directoryToSearchList){
            if(file.getName().contains(search)){
                fileList.add(file);
            }
        }
        return sortList(fileList, sortingType);
    }

    @Override
    public List<File> getFilesForSearchNameAndExtensions(final String directoryPathString, final String search, final String searchExtensionsString, final boolean recursive, final boolean includeFiles, final boolean includeDirectories, final SortingType sortingType) throws IOException {
        List<String> searchExtensionsList = List.of(searchExtensionsString.split(","));
        List<File> directoryToSearchList = getFileListInDirectory(directoryPathString, recursive, includeFiles, includeDirectories);
        List<File> fileList = new ArrayList<>();
        for (File file : directoryToSearchList) {
            for (String extension : searchExtensionsList) {
                if (file.getName().toLowerCase().endsWith(extension.toLowerCase())) {
                    if (file.getName().toLowerCase().contains(search.toLowerCase())) {
                        fileList.add(file);
                    }
                }
            }
        }
        return sortList(fileList, sortingType);
    }

    @Override
    public List<File> getFilesForSearchNameAndExcludedExtensions(final String directoryPathString, final String search, final String searchExcludedExtensionsString, final boolean recursive, final boolean includeFiles, final boolean includeDirectories, final SortingType sortingType) throws IOException {
        List<String> searchExcludedExtensionsList = List.of(searchExcludedExtensionsString.split(","));
        List<File> directoryToSearchList = getFileListInDirectory(directoryPathString, recursive, includeFiles, includeDirectories);
        List<File> fileList = new ArrayList<>();
        for (File file : directoryToSearchList) {
            for (String extension : searchExcludedExtensionsList) {
                if (file.getName().toLowerCase().endsWith(extension.toLowerCase())) {
                    if (file.getName().toLowerCase().contains(search.toLowerCase())) {
                        fileList.add(file);
                    }
                }
            }
        }
        return sortList(fileList, sortingType);
    }

    @Override
    public List<File> getFilesForSearchNameAndExtensionsAndExcludedExtensions(final String directoryPathString, final String search, final String searchExtensionsString, final String searchExcludedExtensionsString, final boolean recursive, final boolean includeFiles, final boolean includeDirectories, final SortingType sortingType) throws IOException {
        List<String> searchExtensionsList = List.of(searchExtensionsString.split(","));
        List<String> searchExcludedExtensionsList = List.of(searchExcludedExtensionsString.split(","));
        List<File> fileList = new ArrayList<>();
        List<File> directoryToSearchList = getFileListInDirectory(directoryPathString, recursive, includeFiles, includeDirectories);
        for (File file : directoryToSearchList) {
            for (String excludedExtension : searchExcludedExtensionsList) {
                if (!file.getName().toLowerCase().endsWith(excludedExtension.toLowerCase())) {
                    for (String extension : searchExtensionsList) {
                        if (file.getName().toLowerCase().endsWith(extension.toLowerCase())) {
                            if (file.getName().toLowerCase().contains(search.toLowerCase())) {
                                fileList.add(file);
                            }
                        }
                    }
                }
            }
        }
        return sortList(fileList, sortingType);
    }

    @Override
    public List<File> getFilesForExtensions(final String directoryPathString, final String searchExtensionsString, final boolean recursive, final boolean includeFiles, final boolean includeDirectories, final SortingType sortingType) throws IOException {
        List<String> searchExtensionsList = List.of(searchExtensionsString.split(","));
        List<File> fileList = new ArrayList<>();
        List<File> directoryToSearchList = getFileListInDirectory(directoryPathString, recursive, includeFiles, includeDirectories);
        for (File file : directoryToSearchList) {
            for (String extension : searchExtensionsList) {
                if (file.getName().toLowerCase().endsWith(extension.toLowerCase())) {
                    fileList.add(file);
                }
            }
        }
        return sortList(fileList, sortingType);
    }

    @Override
    public List<File> getFilesForExcludedExtensions(final String directoryPathString, final String searchExcludedExtensionsString, final boolean recursive, final boolean includeFiles, final boolean includeDirectories, final SortingType sortingType) throws IOException {
        List<String> searchExcludedExtensionsList = List.of(searchExcludedExtensionsString.split(","));
        List<File> fileList = new ArrayList<>();
        List<File> directoryToSearchList = getFileListInDirectory(directoryPathString, recursive, includeFiles, includeDirectories);
        for (File file : directoryToSearchList) {
            for (String excludedExtension : searchExcludedExtensionsList) {
                if (!file.getName().toLowerCase().endsWith(excludedExtension.toLowerCase())) {
                    fileList.add(file);
                }
            }
        }
        return sortList(fileList, sortingType);
    }

    @Override
    public List<File> getFilesForExtensionsAndExcludedExtensions(final String directoryPathString, final String searchExtensionsString, final String searchExcludedExtensionsString, final boolean recursive, final boolean includeFiles, final boolean includeDirectories, final SortingType sortingType) throws IOException {
        List<String> searchExtensionsList = List.of(searchExtensionsString.split(","));
        List<String> searchExcludedExtensionsList = List.of(searchExcludedExtensionsString.split(","));
        List<File> fileList = new ArrayList<>();
        List<File> directoryToSearchList = getFileListInDirectory(directoryPathString, recursive, includeFiles, includeDirectories);
        for (File file : directoryToSearchList) {
            for (String excludedExtension : searchExcludedExtensionsList) {
                if (!file.getName().toLowerCase().endsWith(excludedExtension.toLowerCase())) {
                    for (String extension : searchExtensionsList) {
                        if (file.getName().toLowerCase().endsWith(extension.toLowerCase())) {
                            fileList.add(file);
                        }
                    }
                }
            }
        }
        return sortList(fileList, sortingType);
    }

    @Override
    public List<File> getFilesWithName(final String directoryPathString, final String searchListString, final boolean recursive, final boolean includeFiles, final boolean includeDirectories, final SortingType sortingType) throws IOException {
        List<String> searchList = List.of(searchListString.split(","));
        List<File> directoryToSearchList = getFileListInDirectory(directoryPathString, recursive, includeFiles, includeDirectories);
        List<File> foundFiles = new ArrayList<>();
        for (File file : directoryToSearchList) {
            for(String search : searchList){
                if(file.getName().equals(search)){
                    foundFiles.add(file);
                }
            }
        }
        return sortList(foundFiles, sortingType);
    }

    @Override
    public List<File> getFilesForDateRange(final String directoryPathString, final String startDate, final String endDate, final boolean dateCreated, final boolean dateModified, final boolean recursive, final boolean includeFiles, final boolean includeDirectories, final SortingType sortingType) throws IOException, ParseException {
        Date rangeStartDate = new SimpleDateFormat("dd/MM/yyyy").parse(startDate);
        Date rangeEndDate = new SimpleDateFormat("dd/MM/yyyy").parse(endDate);
        if(rangeStartDate.compareTo(rangeEndDate) > 0){
            System.out.println("Invalid range");
        }
        List<File> fileList = new ArrayList<>();
        List<File> directoryToSearchList = getFileListInDirectory(directoryPathString, recursive, includeFiles, includeDirectories);
        for (File file : directoryToSearchList) {
            if(dateCreated && !dateModified){
                Date fileCreationDate = new Date(file.getCreatedTime().getValue());
                if(fileCreationDate.compareTo(rangeStartDate) >= 0 && fileCreationDate.compareTo(rangeEndDate) <= 0){
                    fileList.add(file);
                }
            }
            if(!dateCreated && dateModified){
                Date fileModificationDate = new Date(file.getModifiedTime().getValue());
                if(fileModificationDate.compareTo(rangeStartDate) >= 0 && fileModificationDate.compareTo(rangeEndDate) <= 0){
                    fileList.add(file);
                }
            }
            if(dateCreated && dateModified){
                Date fileCreationDate = new Date(file.getCreatedTime().getValue());
                Date fileModificationDate = new Date(file.getModifiedTime().getValue());
                if((fileCreationDate.compareTo(rangeStartDate) >= 0 && fileCreationDate.compareTo(rangeEndDate) <= 0) || (fileModificationDate.compareTo(rangeStartDate) >= 0 && fileModificationDate.compareTo(rangeEndDate) <= 0)){
                    fileList.add(file);
                }
            }
            if(!dateCreated && !dateModified){
                System.out.println("error");
            }
        }
        return sortList(fileList, sortingType);
    }

    protected List<File> sortList(List<File> listToSort, final SortingType sortingType){
        if(sortingType == SortingType.NAME){
            listToSort.sort(new GoogleDriveComparators.NameComparator());
        }
        else if(sortingType == SortingType.SIZE){
            listToSort.sort(new GoogleDriveComparators.SizeComparator());
        }
        else if(sortingType == SortingType.DATE_CREATED){
            listToSort.sort(new GoogleDriveComparators.CreationDateComparator());
        }
        else if(sortingType == SortingType.DATE_MODIFIED){
            listToSort.sort(new GoogleDriveComparators.ModificationDateComparator());
        }
        else{
            return listToSort;
        }
        return listToSort;
    }
}