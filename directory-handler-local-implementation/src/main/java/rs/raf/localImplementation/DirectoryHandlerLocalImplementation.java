package rs.raf.localImplementation;

import org.apache.commons.io.FileUtils;

import rs.raf.model.DirectoryHandlerConfig;
import rs.raf.specification.IDirectoryHandlerSpecification;

import java.io.*;
import java.nio.file.*;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DirectoryHandlerLocalImplementation implements IDirectoryHandlerSpecification<File> {
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
    public void createDirectory(final String repositoryName, final String directoryName) throws IOException, FileAlreadyExistsException {
        Files.createDirectories(workingDirectory.resolve(repositoryName).resolve(Paths.get(directoryName)));
    }
    @Override
    public boolean createFile(final String repositoryName, final String fileName, final String fileExtension) throws IOException {
        Path filePath = workingDirectory.resolve(repositoryName).resolve(String.format("%s.%s", fileName, fileExtension));
        return filePath.toFile().createNewFile();
    }
    @Override
    public boolean createFile(final String repositoryName, final String directoryName, final String fileName, final String fileExtension) throws IOException {
        Path filePath = workingDirectory.resolve(repositoryName).resolve(Paths.get(directoryName)).resolve(String.format("%s.%s", fileName, fileExtension));
        return filePath.toFile().createNewFile();
    }
    @Override
    public void createDefaultConfig(final String repositoryName) throws IOException {
        createFile(repositoryName, "config", "properties");
        updateConfig(repositoryName, new DirectoryHandlerConfig());
    }
    @Override
    public void createConfig(final String repositoryName, final DirectoryHandlerConfig directoryHandlerConfig) throws IOException {
        createFile(repositoryName, "config", "properties");
        updateConfig(repositoryName, directoryHandlerConfig);
    }
    @Override
    public void updateConfig(final String repositoryName, final DirectoryHandlerConfig directoryHandlerConfig) throws IOException {
        Properties properties = getProperties(repositoryName);
        properties.setProperty("maxRepositorySize", directoryHandlerConfig.getMaxRepositorySize());
        properties.setProperty("maxFileCount", Integer.toString(directoryHandlerConfig.getMaxFileCount()));
        properties.setProperty("excludedExtensions", arrayToString(directoryHandlerConfig.getExcludedExtensions()));
        OutputStream outputStream = new FileOutputStream(workingDirectory.resolve(repositoryName).resolve(propertiesFileName + "." + propertiesFileExtension).toAbsolutePath().toString());
        properties.store(outputStream, "updatedConfig");
    }
    @Override
    public Properties getProperties(final String repositoryName) throws IOException {
        Properties properties = new Properties();
        FileInputStream fileInputStream = new FileInputStream(workingDirectory.resolve(repositoryName).resolve(propertiesFileName + "." + propertiesFileExtension).toFile());
        InputStream inputStream = fileInputStream;
        properties.load(inputStream);
        return properties;
    }
    @Override
    public long getRepositorySize(final String repositoryName) throws NullPointerException{
        return FileUtils.sizeOfDirectory(workingDirectory.resolve(repositoryName).toFile());
    }
    @Override
    public long getDirectorySize(final String repositoryName, final String directoryName) throws NullPointerException {
        return FileUtils.sizeOfDirectory(workingDirectory.resolve(repositoryName).resolve(directoryName).toFile());
    }
    @Override
    public long getFileSize(final String repositoryName, final String directoryName, final String fileName) throws NullPointerException{
        return FileUtils.sizeOf(workingDirectory.resolve(repositoryName).resolve(directoryName).resolve(fileName).toFile());
    }
    @Override
    public List<File> getFileListInDirectory(final String directoryName) throws IOException {
        return null;
    }
    @Override
    public final String arrayToString(final String[] array) {
        String arrayString = "";
        for (int i = 0; i < array.length; i++) {
            arrayString += array[i] + ",";
        }
        return arrayString.substring(0, arrayString.length() - 1);
    }
    @Override
    public void writeToFile(final String repositoryName, final String directoryName, final String fileName, final String textToWrite) throws IOException {
        if (getRepositorySize(repositoryName) + textToWrite.getBytes().length > Integer.valueOf(getProperties(repositoryName).getProperty("maxRepositorySize"))) {
            System.out.println("Max Repository Size Exceeded");
        }
        else {
            FileUtils.writeStringToFile(workingDirectory.resolve(repositoryName).resolve(directoryName).resolve(fileName).toFile(), textToWrite, "UTF-8");
        }
    }
    @Override
    public void deleteFile(final String repositoryName, final String directoryName, final String fileName) throws FileAlreadyExistsException {
        workingDirectory.resolve(repositoryName).resolve(directoryName).resolve(fileName).toFile().delete();
    }
    @Override
    public void downloadFile(final String repositoryName, final String directoryName, final String fileName, final boolean overwrite) throws IOException {
        if (overwrite) {
            Files.copy(workingDirectory.resolve(repositoryName).resolve(directoryName).resolve(fileName), homeDirectory.resolve("Downloads").resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
        }
        else {
            String suffix = "";
            int i = 1;
            while(true){
                try{
                    Files.copy(workingDirectory
                            .resolve(repositoryName)
                            .resolve(directoryName)
                            .resolve(fileName), homeDirectory
                            .resolve("Downloads")
                            .resolve(fileName.substring(0, fileName.indexOf(".")) + suffix + fileName.substring(fileName.indexOf("."))));
                }
                catch(FileAlreadyExistsException e){
                    suffix += i;
                }
            }
        }
    }
    @Override
    public void downloadFile(final String repositoryName, final String directoryName, final String fileName, final String downloadPathString, final boolean overwrite) throws IOException {
        Path downloadPath = Paths.get(downloadPathString);
        if (overwrite) {
            Files.copy(workingDirectory.resolve(repositoryName).resolve(directoryName).resolve(fileName), downloadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
        }
        else {
            String suffix = "";
            int i = 1;
            while(true){
                try{
                    Files.copy(workingDirectory
                            .resolve(repositoryName)
                            .resolve(directoryName)
                            .resolve(fileName), homeDirectory
                            .resolve("Downloads")
                            .resolve(fileName.substring(0, fileName.indexOf(".")) + suffix + fileName.substring(fileName.indexOf("."))));
                }
                catch(FileAlreadyExistsException e){
                    suffix += i;
                }
            }
        }
    }
    @Override
    public void moveOrRenameFile(final String repositoryName, final String directoryName, final String fileName, final String newName) throws IOException {
        try {
            Files.move(workingDirectory.resolve(repositoryName).resolve(directoryName).resolve(fileName), workingDirectory.resolve(repositoryName).resolve(directoryName).resolve(fileName).resolveSibling(newName));
        }
        catch (FileAlreadyExistsException e) {
            System.out.println("File already exists");
        }
    }
    @Override
    public List<File> getAllFilesList() {
        List<File> fileList = new ArrayList<>();
        File directory = workingDirectory.toFile();
        if (directory.listFiles() != null) {
            for (File file : directory.listFiles()) {
                fileList.add(file);
            }
        }
        return fileList;
    }
    @Override
    public List<File> getFileList(final String repositoryName, final String directoryName) {
        List<File> fileList = new ArrayList<>();
        File directory = workingDirectory.resolve(repositoryName).resolve(directoryName).toFile();
        if (directory.listFiles() != null) {
            for (File file : directory.listFiles()) {
                fileList.add(file);
            }
        }
        return fileList;
    }
    @Override
    public int getFileCount(final String repositoryName, final String directoryName) {
        int fileCount = 0;
        File directory = workingDirectory.resolve(repositoryName).resolve(directoryName).toFile();
        if (directory.listFiles() != null) {
            for (File file : directory.listFiles()) {
                fileCount++;
            }
        }
        return fileCount;
    }
    @Override
    public List<File> getFilesForSearchName(final String repositoryName, final String directoryName, final String search) {
        List<File> fileList = new ArrayList<>();
        File directoryToSearch = workingDirectory.resolve(repositoryName).resolve(directoryName).toFile();
        File[] directoryToSearchList = directoryToSearch.listFiles();
        for (File file : directoryToSearchList) {
            if (file.getName().toLowerCase().contains(search.toLowerCase())) {
                fileList.add(file);
            }
        }
        return fileList;
    }
    @Override
    public List<File> getFilesForSearchNameAndExtensions(final String repositoryName, final String directoryName, final String search, final String[] searchExtensions) {
        List<File> fileList = new ArrayList<>();
        File directoryToSearch = workingDirectory.resolve(repositoryName).resolve(directoryName).toFile();
        File[] directoryToSearchList = directoryToSearch.listFiles();
        for (File file : directoryToSearchList) {
            for (String extension : searchExtensions) {
                if (file.getName().toLowerCase().endsWith(extension.toLowerCase())) {
                    if (file.getName().toLowerCase().contains(search.toLowerCase())) {
                        fileList.add(file);
                    }
                }
            }
        }
        return fileList;
    }
    @Override
    public List<File> getFilesForSearchNameAndExcludedExtensions(final String repositoryName, final String directoryName, final String search, final String[] searchExcludedExtensions) {
        List<File> fileList = new ArrayList<>();
        File directoryToSearch = workingDirectory.resolve(repositoryName).resolve(directoryName).toFile();
        File[] directoryToSearchList = directoryToSearch.listFiles();
        for (File file : directoryToSearchList) {
            for (String excludedExtension : searchExcludedExtensions) {
                if (!file.getName().toLowerCase().endsWith(excludedExtension.toLowerCase())) {
                    if (file.getName().toLowerCase().contains(search.toLowerCase())) {
                        fileList.add(file);
                    }
                }
            }
        }
        return fileList;
    }
    @Override
    public List<File> getFilesForSearchNameAndExtensionsAndExcludedExtensions(final String repositoryName, final String directoryName, final String search, final String[] searchExtensions, final String[] searchExcludedExtensions) {
        List<File> fileList = new ArrayList<>();
        File directoryToSearch = workingDirectory.resolve(repositoryName).resolve(directoryName).toFile();
        File[] directoryToSearchList = directoryToSearch.listFiles();
        for (File file : directoryToSearchList) {
            for (String excludedExtension : searchExcludedExtensions) {
                if (!file.getName().toLowerCase().endsWith(excludedExtension.toLowerCase())) {
                    for (String extension : searchExtensions) {
                        if (file.getName().toLowerCase().endsWith(extension.toLowerCase())) {
                            if (file.getName().toLowerCase().contains(search.toLowerCase())) {
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
    public List<File> getFilesForExtensions(final String repositoryName, final String directoryName, final String[] searchExtensions) {
        List<File> fileList = new ArrayList<>();
        File directoryToSearch = workingDirectory.resolve(repositoryName).resolve(directoryName).toFile();
        File[] directoryToSearchList = directoryToSearch.listFiles();
        for (File file : directoryToSearchList) {
            for (String extension : searchExtensions) {
                if (file.getName().toLowerCase().endsWith(extension.toLowerCase())) {
                    fileList.add(file);
                }
            }
        }
        return fileList;
    }
    @Override
    public List<File> getFilesForExcludedExtensions(final String repositoryName, final String directoryName, final String[] searchExcludedExtensions) {
        List<File> fileList = new ArrayList<>();
        File directoryToSearch = workingDirectory.resolve(repositoryName).resolve(directoryName).toFile();
        File[] directoryToSearchList = directoryToSearch.listFiles();
        for (File file : directoryToSearchList) {
            for (String excludedExtension : searchExcludedExtensions) {
                if (!file.getName().toLowerCase().endsWith(excludedExtension.toLowerCase())) {
                    fileList.add(file);
                }
            }
        }
        return fileList;
    }
    @Override
    public List<File> getFilesForExtensionsAndExcludedExtensions(final String repositoryName, final String directoryName, final String[] searchExtensions, final String[] searchExcludedExtensions) {
        List<File> fileList = new ArrayList<>();
        File directoryToSearch = workingDirectory.resolve(repositoryName).resolve(directoryName).toFile();
        File[] directoryToSearchList = directoryToSearch.listFiles();
        for (File file : directoryToSearchList) {
            for (String excludedExtension : searchExcludedExtensions) {
                if (!file.getName().toLowerCase().endsWith(excludedExtension.toLowerCase())) {
                    for (String extension : searchExtensions) {
                        if (file.getName().toLowerCase().endsWith(extension.toLowerCase())) {
                            fileList.add(file);
                        }
                    }
                }
            }
        }
        return fileList;
    }

    @Override
    public String getFileIdByName(String fileName) throws IOException {
        return null;
    }
}