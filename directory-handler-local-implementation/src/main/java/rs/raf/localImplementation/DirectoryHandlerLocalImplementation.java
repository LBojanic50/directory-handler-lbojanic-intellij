package rs.raf.localImplementation;

import org.apache.commons.io.FileUtils;

import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import rs.raf.model.DirectoryHandlerConfig;
import rs.raf.model.LocalFile;
import rs.raf.specification.IDirectoryHandlerSpecification;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DirectoryHandlerLocalImplementation implements IDirectoryHandlerSpecification<LocalFile> {

    //private static final String workingDirectory = System.getProperty("user.dir") + "\\directory-handler-project";
    //private static final String workingDirectory = "D:\\JavaProjects\\directory-handler-lbojanic-intellij\\directory-handler-project";
    //private static final String workingDirectory = System.getProperty("user.dir") + "\\directory-handler-project";
    private static Path workingDirectory = Paths.get("directory-handler-project");
    private static Path homeDirectory = Paths.get(System.getProperty("user.home"));
    private static String defaultRepositoryName = "defaultRepository";
    private static String defaultDirectoryName = "defaultDirectory";
    private static String defaultFileName = "defaultFile";
    private static String propertiesFileName = "config";
    private static String propertiesFileExtension = "properties";
    @Override
    public Object getCredentials(final InputStream inputStream, final String CREDENTIALS_FILE_PATH, final Object HTTP_TRANSPORT, final Object JSON_FACTORY, final List SCOPES, final String TOKENS_DIRECTORY_PATH) throws UnsupportedOperationException, FileNotFoundException {
        throw new UnsupportedOperationException();
    }
    @Override
    public void authorizeGoogleDriveClient() throws IOException, GeneralSecurityException {
        throw new UnsupportedOperationException();
    }
    @Override
    public void createRepository(final String repositoryName) throws IOException, FileAlreadyExistsException {
        Files.createDirectory(workingDirectory.resolve(repositoryName));
        createDefaultConfig(repositoryName);
    }
    @Override
    public void createRepository(final String repositoryName, final DirectoryHandlerConfig directoryHandlerConfig) throws IOException, FileAlreadyExistsException{
        Files.createDirectory(workingDirectory.resolve(repositoryName));
        createConfig(repositoryName, directoryHandlerConfig);
    }
    @Override
    public void createDirectory(final String directoryPathString) throws IOException, FileAlreadyExistsException {
        Files.createDirectories(workingDirectory.resolve(Paths.get(directoryPathString)));
    }
    @Override
    public boolean createFile(final String filePathString) throws IOException {
        Path filePath = workingDirectory.resolve(Paths.get(filePathString));
        return filePath.toFile().createNewFile();
    }
    @Override
    public void createDefaultConfig(final String repositoryName) throws IOException {
        createFile(repositoryName + "/config.properties");
        updateConfig(repositoryName, new DirectoryHandlerConfig());
    }
    @Override
    public void createConfig(final String repositoryName, final DirectoryHandlerConfig directoryHandlerConfig) throws IOException {
        createFile(repositoryName + "/config.properties");
        updateConfig(repositoryName, directoryHandlerConfig);
    }
    @Override
    public void updateConfig(final String repositoryName, final DirectoryHandlerConfig directoryHandlerConfig) throws IOException {
        Properties properties = getProperties(repositoryName);
        properties.setProperty("maxRepositorySize", directoryHandlerConfig.getMaxRepositorySize());
        properties.setProperty("maxFileCount", Integer.toString(directoryHandlerConfig.getMaxFileCount()));
        properties.setProperty("excludedExtensions", directoryHandlerConfig.getExcludedExtensionsString());
        OutputStream outputStream = new FileOutputStream(workingDirectory.resolve(repositoryName).resolve("config.properties").toAbsolutePath().toString());
        properties.store(outputStream, "updatedConfig");
    }
    @Override
    public Properties getProperties(final String repositoryName) throws IOException {
        Properties properties = new Properties();
        FileInputStream fileInputStream = new FileInputStream(workingDirectory.resolve(repositoryName).resolve("config.properties").toFile());
        InputStream inputStream = fileInputStream;
        properties.load(inputStream);
        return properties;
    }
    @Override
    public int getFileCount(final String directoryPathString) {
        int fileCount = 0;
        File directory = workingDirectory.resolve(Paths.get(directoryPathString)).toFile();
        if (directory.listFiles() != null) {
            for (File file : directory.listFiles()) {
                fileCount++;
            }
        }
        return fileCount;
    }
    @Override
    public long getFileSize(final String filePathString) throws NullPointerException{
        return FileUtils.sizeOf(workingDirectory.resolve(Paths.get(filePathString)).toFile());
    }
    @Override
    public long getDirectorySize(final String directoryPathString) throws NullPointerException {
        return FileUtils.sizeOfDirectory(workingDirectory.resolve(Paths.get(directoryPathString)).toFile());
    }


    @Override
    public void writeToFile(final String filePathString, final String textToWrite) throws IOException {
        String repositoryName = filePathString.split("/")[0];
        if (getDirectorySize(repositoryName) + textToWrite.getBytes().length > Integer.valueOf(getProperties(repositoryName).getProperty("maxRepositorySize"))) {
            System.out.println("Max Repository Size Exceeded");
        }
        else {
            FileUtils.writeStringToFile(workingDirectory.resolve(Paths.get(filePathString)).toFile(), textToWrite, "UTF-8");
        }
    }
    @Override
    public void deleteFile(final String filePathString) throws IOException {
        workingDirectory.resolve(Paths.get(filePathString)).toFile().delete();
    }
    @Override
    public void downloadFile(final String filePathString, final boolean overwrite) throws IOException {
        String fileName = String.valueOf(Paths.get(filePathString).getFileName());
        Path originalPath = workingDirectory.resolve(Paths.get(filePathString));
        Path downloadPath = workingDirectory.resolve("Downloads");
        if (overwrite) {
            Files.copy(originalPath, downloadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
        }
        else {
            String suffix = "";
            int i = 1;
            while(true){
                try{
                    Files.copy(originalPath, downloadPath.resolve(fileName.substring(0, fileName.indexOf(".")) + suffix + fileName.substring(fileName.indexOf("."))));
                    break;
                }
                catch(FileAlreadyExistsException e){
                    suffix += i;
                }
            }
        }
    }
    @Override
    public void downloadFile(final String filePathString, final String downloadPathString, final boolean overwrite) throws IOException {
        String fileName = String.valueOf(Paths.get(filePathString).getFileName());
        Path originalPath = workingDirectory.resolve(Paths.get(filePathString));
        Path downloadPath = Paths.get(downloadPathString);
        if (overwrite) {
            Files.copy(originalPath, downloadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
        }
        else {
            String suffix = "";
            int i = 1;
            while(true){
                try{
                    Files.copy(originalPath, downloadPath.resolve(fileName.substring(0, fileName.indexOf(".")) + suffix + fileName.substring(fileName.indexOf("."))));
                    break;
                }
                catch(FileAlreadyExistsException e){
                    suffix += i;
                }
            }
        }
    }
    @Override
    public void moveOrRenameFile(final String oldPathString, final String newPathString) throws IOException {
        try {
            Files.move(workingDirectory.resolve(Paths.get(oldPathString)), workingDirectory.resolve(newPathString));
        }
        catch (FileAlreadyExistsException e) {
            System.out.println("File already exists");
        }
    }
    @Override
    public List<LocalFile> getFileListInDirectory(final String directoryPathString, final boolean recursive, final boolean includeFiles, final boolean includeDirectories) throws IOException {
        File directory;
        if(directoryPathString.equals("")){
            directory = workingDirectory.toFile();
        }
        else{
            directory = workingDirectory.resolve(Paths.get(directoryPathString)).toFile();
        }
        List<File> fileList = new ArrayList<>();
        List<LocalFile> localFileList = new ArrayList<>();
        if(includeFiles == false && includeDirectories == true){
            if(recursive){
                fileList = (List<File>) FileUtils.listFilesAndDirs(directory, DirectoryFileFilter.DIRECTORY, TrueFileFilter.INSTANCE);
            }
            else{
                fileList = (List<File>) FileUtils.listFilesAndDirs(directory, DirectoryFileFilter.DIRECTORY, null);
            }
        }
        if(includeFiles == true && includeDirectories == false){
            fileList = (List<File>) FileUtils.listFiles(directory, null, recursive);
        }
        if(includeFiles == true && includeDirectories == true){
            if(recursive){
                fileList = (List<File>) FileUtils.listFilesAndDirs(directory, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
            }
            else{
                fileList = (List<File>) FileUtils.listFilesAndDirs(directory, TrueFileFilter.INSTANCE, null);
            }
        }
        if(includeFiles == false && includeDirectories == false){
            System.out.println("You must specify what type of file to include");
            return null;
        }
        for(File file : fileList){
            LocalFile localFile = new LocalFile(file);
            localFileList.add(localFile);
        }
        return localFileList;
    }

    @Override
    public List<LocalFile> getFilesForSearchName(final String directoryPathString, final String search, final boolean recursive, final boolean includeFiles, final boolean includeDirectories) throws IOException {
        List<LocalFile> fileList = new ArrayList<>();
        List<LocalFile> directoryToSearchList = getFileListInDirectory(directoryPathString, recursive, includeFiles, includeDirectories);
        for (LocalFile file : directoryToSearchList) {
            if (file.getFile().getName().toLowerCase().contains(search.toLowerCase())) {
                fileList.add(file);
            }
        }
        return fileList;
    }
    @Override
    public List<LocalFile> getFilesForSearchNameAndExtensions(final String directoryPathString, final String search, final String searchExtensionsString, final boolean recursive, final boolean includeFiles, final boolean includeDirectories) throws IOException {
        List<String> searchExtensionsList = List.of(searchExtensionsString.split(","));
        List<LocalFile> fileList = new ArrayList<>();
        List<LocalFile> directoryToSearchList = getFileListInDirectory(directoryPathString, recursive, includeFiles, includeDirectories);
        for (LocalFile file : directoryToSearchList) {
            for (String extension : searchExtensionsList) {
                if (file.getFile().getName().toLowerCase().endsWith(extension.toLowerCase())) {
                    if (file.getFile().getName().toLowerCase().contains(search.toLowerCase())) {
                        fileList.add(file);
                    }
                }
            }
        }
        return fileList;
    }
    @Override
    public List<LocalFile> getFilesForSearchNameAndExcludedExtensions(final String directoryPathString, final String search, final String searchExcludedExtensionsString, final boolean recursive, final boolean includeFiles, final boolean includeDirectories) throws IOException {
        List<String> searchExcludedExtensionsList = List.of(searchExcludedExtensionsString.split(","));
        List<LocalFile> fileList = new ArrayList<>();
        List<LocalFile> directoryToSearchList = getFileListInDirectory(directoryPathString, recursive, includeFiles, includeDirectories);
        for (LocalFile file : directoryToSearchList) {
            for (String excludedExtension : searchExcludedExtensionsList) {
                if (!file.getFile().getName().toLowerCase().endsWith(excludedExtension.toLowerCase())) {
                    if (file.getFile().getName().toLowerCase().contains(search.toLowerCase())) {
                        fileList.add(file);
                    }
                }
            }
        }
        return fileList;
    }
    @Override
    public List<LocalFile> getFilesForSearchNameAndExtensionsAndExcludedExtensions(final String directoryPathString, final String search, final String searchExtensionsString, final String searchExcludedExtensionsString, final boolean recursive, final boolean includeFiles, final boolean includeDirectories) throws IOException {
        List<String> searchExtensionsList = List.of(searchExtensionsString.split(","));
        List<String> searchExcludedExtensionsList = List.of(searchExcludedExtensionsString.split(","));
        List<LocalFile> fileList = new ArrayList<>();
        List<LocalFile> directoryToSearchList = getFileListInDirectory(directoryPathString, recursive, includeFiles, includeDirectories);
        for (LocalFile file : directoryToSearchList) {
            for (String excludedExtension : searchExcludedExtensionsList) {
                if (!file.getFile().getName().toLowerCase().endsWith(excludedExtension.toLowerCase())) {
                    for (String extension : searchExtensionsList) {
                        if (file.getFile().getName().toLowerCase().endsWith(extension.toLowerCase())) {
                            if (file.getFile().getName().toLowerCase().contains(search.toLowerCase())) {
                                fileList.add(file);
                            }
                        }
                    }
                }
            }
        }
        return fileList;
    }
    @Override
    public List<LocalFile> getFilesForExtensions(final String directoryPathString, final String searchExtensionsString, final boolean recursive, final boolean includeFiles, final boolean includeDirectories) throws IOException {
        List<String> searchExtensionsList = List.of(searchExtensionsString.split(","));
        List<LocalFile> fileList = new ArrayList<>();
        List<LocalFile> directoryToSearchList = getFileListInDirectory(directoryPathString, recursive, includeFiles, includeDirectories);
        for (LocalFile file : directoryToSearchList) {
            for (String extension : searchExtensionsList) {
                if (file.getFile().getName().toLowerCase().endsWith(extension.toLowerCase())) {
                    fileList.add(file);
                }
            }
        }
        return fileList;
    }
    @Override
    public List<LocalFile> getFilesForExcludedExtensions(final String directoryPathString, final String searchExcludedExtensionsString, final boolean recursive, final boolean includeFiles, final boolean includeDirectories) throws IOException {
        List<String> searchExcludedExtensionsList = List.of(searchExcludedExtensionsString.split(","));
        List<LocalFile> fileList = new ArrayList<>();
        List<LocalFile> directoryToSearchList = getFileListInDirectory(directoryPathString, recursive, includeFiles, includeDirectories);
        for (LocalFile file : directoryToSearchList) {
            for (String excludedExtension : searchExcludedExtensionsList) {
                if (!file.getFile().getName().toLowerCase().endsWith(excludedExtension.toLowerCase())) {
                    fileList.add(file);
                }
            }
        }
        return fileList;
    }
    @Override
    public List<LocalFile> getFilesForExtensionsAndExcludedExtensions(final String directoryPathString, final String searchExtensionsString, final String searchExcludedExtensionsString, final boolean recursive, final boolean includeFiles, final boolean includeDirectories) throws IOException {
        List<String> searchExtensionsList = List.of(searchExtensionsString.split(","));
        List<String> searchExcludedExtensionsList = List.of(searchExcludedExtensionsString.split(","));
        List<LocalFile> fileList = new ArrayList<>();
        List<LocalFile> directoryToSearchList = getFileListInDirectory(directoryPathString, recursive, includeFiles, includeDirectories);
        for (LocalFile file : directoryToSearchList) {
            for (String excludedExtension : searchExcludedExtensionsList) {
                if (!file.getFile().getName().toLowerCase().endsWith(excludedExtension.toLowerCase())) {
                    for (String extension : searchExtensionsList) {
                        if (file.getFile().getName().toLowerCase().endsWith(extension.toLowerCase())) {
                            fileList.add(file);
                        }
                    }
                }
            }
        }
        return fileList;
    }

    @Override
    public List<LocalFile> getFilesWithName(final String directoryPathString, final String searchListString, final boolean recursive, final boolean includeFiles, final boolean includeDirectories) throws IOException {
        List<String> searchList = List.of(searchListString.split(","));
        List<LocalFile> directoryToSearchList = getFileListInDirectory(directoryPathString, recursive, includeFiles, includeDirectories);
        List<LocalFile> foundFiles = new ArrayList<>();
        for (LocalFile file : directoryToSearchList) {
            for(String search : searchList){
                if(file.getFile().getName().equals(search)){
                    foundFiles.add(file);
                }
            }
        }
        return foundFiles;
    }

    @Override
    public String getFileIdByName(final String fileName) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getFileIdByNameInRepository(List<LocalFile> fileListInRepository, String fileName) throws IOException {
        return null;
    }
}