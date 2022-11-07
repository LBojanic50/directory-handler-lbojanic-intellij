package rs.raf.localImplementation;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import rs.raf.exception.DirectoryHandlerExceptions;
import rs.raf.model.DirectoryHandlerConfig;
import rs.raf.model.LocalFile;
import rs.raf.model.SortingType;
import rs.raf.specification.DirectoryHandlerManager;
import rs.raf.specification.IDirectoryHandlerSpecification;
import rs.raf.util.LocalComparators;
import java.io.*;
import java.nio.file.*;
import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import rs.raf.exception.DirectoryHandlerExceptions.*;
public class DirectoryHandlerLocalImplementation implements IDirectoryHandlerSpecification<LocalFile> {
    private static final Path workingDirectory = Paths.get(System.getProperty("user.dir")).resolve("LocalRepositories");
    private static DirectoryHandlerLocalImplementation instance;
    static{
        DirectoryHandlerManager.registerDirectoryHandler(DirectoryHandlerLocalImplementation.getInstance());
        if(!Files.exists(workingDirectory)){
            try {
                Files.createDirectory(workingDirectory);
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public DirectoryHandlerLocalImplementation(){
        super();
    }
    public static DirectoryHandlerLocalImplementation getInstance(){
        if(instance == null){
            instance = new DirectoryHandlerLocalImplementation();
        }
        return instance;
    }
    @Override
    public void createConfig(final String repositoryName, final DirectoryHandlerConfig directoryHandlerConfig) throws IOException {
        Path configPath = workingDirectory.resolve(repositoryName).resolve("config.properties");
        Files.createFile(configPath);
        Properties config = new Properties();
        InputStream inputStream = new FileInputStream(configPath.toFile());
        config.load(inputStream);
        config.setProperty("maxRepositorySize", directoryHandlerConfig.getMaxRepositorySize());
        config.setProperty("excludedExtensions", directoryHandlerConfig.getExcludedExtensionsString());
        OutputStream outputStream = new FileOutputStream(configPath.toAbsolutePath().toString());
        config.store(outputStream, "updatedConfig");
        inputStream.close();
        outputStream.close();
    }
    @Override
    public void createDirectory(final String directoryPathsString) throws IOException, FileAlreadyExistsException, DirectoryHandlerExceptions.MaxFileCountExceededException {
        List<String> directoryPathsList = List.of(directoryPathsString.split("-more-"));
        for(String directoryPathString : directoryPathsList){
            String parentDirectoryPathString = directoryPathString.substring(0, directoryPathString.lastIndexOf("/"));
            String repositoryName = directoryPathString.substring(0, directoryPathString.indexOf("/"));
            Properties config = getConfig(repositoryName);
            if(config.getProperty(parentDirectoryPathString) != null){
                if(getFileCount(parentDirectoryPathString) + 1 > Integer.parseInt(config.getProperty(parentDirectoryPathString))){
                    throw new MaxFileCountExceededException(parentDirectoryPathString);
                }
                else{
                    Files.createDirectories(workingDirectory.resolve(Paths.get(directoryPathString)));
                }
            }
        }
    }
    protected boolean maxFileCountExceededCheck(final Properties config, final String parentDirectoryPathString) throws BadPathException {
        if (config.getProperty(parentDirectoryPathString) != null) {
            return getFileCount(parentDirectoryPathString) + 1 > Integer.parseInt(config.getProperty(parentDirectoryPathString));
        }
        return false;
    }
    protected boolean excludedExtensionsCheck(final Properties config, final String filePathString){
        String excludedExtensionsString = config.getProperty("excludedExtensions");
        if (excludedExtensionsString != null) {
            List<String> excludedExtensionsList = List.of(excludedExtensionsString.split(","));
            for(String excludedExtension : excludedExtensionsList){
                if(filePathString.endsWith(String.format(".%s", excludedExtension))){
                    return true;
                }
            }
        }
        return false;
    }
    protected boolean maxRepositorySizeExceededCheck(final Properties config, final String repositoryName, final String textToWrite){
        if (config.getProperty("maxRepositorySize") != null) {
            return getDirectorySize(repositoryName) + textToWrite.getBytes().length > Integer.parseInt(config.getProperty("maxRepositorySize"));
        }
        return false;
    }
    protected boolean badPathCheck(final String filePathString){
        try{
            workingDirectory.resolve(Paths.get(filePathString));
        }
        catch(InvalidPathException e){
            return true;
        }
        return false;
    }
    protected boolean noFileAtPathCheck(final String filePathString){
        if(!Files.exists(Paths.get(filePathString))){
            return true;
        }
        return false;
    }
    @Override
    public void createFile(final String filePathsString) throws IOException, MaxFileCountExceededException, MaxRepositorySizeExceededException, BadPathException, FileExtensionException {
        List<String> filePathsList = List.of(filePathsString.split("-more-"));
        for (String filePathString : filePathsList) {
            String repositoryName = filePathsString.substring(0, filePathsString.indexOf("/"));
            Properties config = getConfig(repositoryName);
            String parentDirectoryPathString = filePathString.substring(0, filePathString.lastIndexOf("/"));
            Path filePath;
            String textToWrite = "sampleText";
            if(!filePathString.contains("/") || badPathCheck(filePathString)){
                throw new BadPathException(filePathString);
            }
            if(maxFileCountExceededCheck(config, parentDirectoryPathString)){
                throw new MaxFileCountExceededException(parentDirectoryPathString);
            }
            if(excludedExtensionsCheck(config, filePathString)){
                throw new FileExtensionException(filePathString.substring(filePathString.lastIndexOf("/") + 1));
            }
            filePath = workingDirectory.resolve(Paths.get(filePathString));
            Files.createFile(filePath);
            if(maxRepositorySizeExceededCheck(config, repositoryName, textToWrite)){
                throw new MaxRepositorySizeExceededException(repositoryName);
            }
            writeToFile(filePathString, textToWrite);
        }
    }
    @Override
    public void createRepository(final String repositoryNames) throws IOException {
        List<String> repositoryNameList = List.of(repositoryNames.split("-more-"));
        for(String repositoryName : repositoryNameList){
            Files.createDirectory(workingDirectory.resolve(repositoryName));
            createConfig(repositoryName, new DirectoryHandlerConfig());
        }
    }
    @Override
    public void createRepository(final String repositoryNames, final DirectoryHandlerConfig directoryHandlerConfig) throws IOException {
        List<String> repositoryNameList = List.of(repositoryNames.split("-more-"));
        for(String repositoryName : repositoryNameList){
            Files.createDirectory(workingDirectory.resolve(repositoryName));
            createConfig(repositoryName, directoryHandlerConfig);
        }
    }
    @Override
    public void deleteFile(final String filePathsString) throws IOException, BadPathException {
        List<String> filePathsList = List.of(filePathsString.split("-more-"));
        for(String filePathString : filePathsList){
            if(badPathCheck(filePathString)){
                throw new BadPathException(filePathString);
            }
            File file = workingDirectory.resolve(Paths.get(filePathString)).toFile();
            if(file.isFile()){
                FileUtils.delete(file);
            }
            else{
                FileUtils.deleteDirectory(file);
            }
        }
    }

    //TODO exceptions
    //TODO if directory, copy all subdirectories recursively
    @Override
    public void downloadFile(final String filePathsString, final String downloadAbsolutePathString, final boolean overwrite) throws IOException, NoFileAtPathException {
        Path downloadPath;
        if(downloadAbsolutePathString == null){
            downloadPath = workingDirectory.resolve("Downloads");
            if(!Files.exists(downloadPath)){
                Files.createDirectory(downloadPath);
            }
        }
        else{
            downloadPath = Paths.get(downloadAbsolutePathString);
        }
        List<String> filePathsList = List.of(filePathsString.split("-more-"));
        for(String filePathString : filePathsList){
            if(noFileAtPathCheck(filePathString)){
                throw new NoFileAtPathException(filePathString);
            }
            String fileName = String.valueOf(Paths.get(filePathString).getFileName());
            Path originalPath = workingDirectory.resolve(Paths.get(filePathString));
            if (overwrite) {
                Files.copy(originalPath, downloadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
            }
            else {
                String suffix = "";
                int i = 0;
                while(true){
                    try{
                        Files.copy(originalPath, downloadPath.resolve(fileName.substring(0, fileName.indexOf(".")) + suffix + fileName.substring(fileName.indexOf("."))));
                        break;
                    }
                    catch(FileAlreadyExistsException e){
                        i++;
                        suffix = String.valueOf(i);
                    }
                }
            }
        }
    }
    @Override
    public List<LocalFile> getAllFiles(final SortingType sortingType) throws IOException {
        return getFileListInDirectory(null, true, true, true, sortingType);
    }
    @Override
    public Properties getConfig(final String repositoryName) throws IOException {
        Properties config = new Properties();
        FileInputStream fileInputStream = new FileInputStream(workingDirectory.resolve(repositoryName).resolve("config.properties").toFile());
        InputStream inputStream = fileInputStream;
        config.load(inputStream);
        inputStream.close();
        return config;
    }
    @Override
    public long getDirectorySize(final String directoryPathString) throws NullPointerException {
        return FileUtils.sizeOfDirectory(workingDirectory.resolve(Paths.get(directoryPathString)).toFile());
    }
    @Override
    public int getFileCount(final String directoryPathString) {
        int fileCount = 0;
        File directory = workingDirectory.resolve(Paths.get(directoryPathString)).toFile();
        List<File> fileList = null;
        if(directory.listFiles() != null) {
            fileList = List.of(directory.listFiles());
        }
        for(int i = 0; i < fileList.size(); i++){
            fileCount++;
        }
        return fileCount;
    }
    @Override
    public List<LocalFile> getFileListInDirectory(final String directoryPathString, final boolean recursive, final boolean includeFiles, final boolean includeDirectories, final SortingType sortingType) throws IOException {
        File directory;
        if(directoryPathString == null){
            directory = workingDirectory.toFile();
        }
        else{
            directory = workingDirectory.resolve(Paths.get(directoryPathString)).toFile();
        }
        List<File> fileList = new ArrayList<>();
        List<LocalFile> localFileList = new ArrayList<>();
        if(!includeFiles && includeDirectories){
            if(recursive){
                fileList = (List<File>) FileUtils.listFilesAndDirs(directory, DirectoryFileFilter.DIRECTORY, TrueFileFilter.INSTANCE);
            }
            else{
                fileList = (List<File>) FileUtils.listFilesAndDirs(directory, DirectoryFileFilter.DIRECTORY, null);
            }
        }
        if(includeFiles && !includeDirectories){
            fileList = (List<File>) FileUtils.listFiles(directory, null, recursive);
        }
        if(includeFiles && includeDirectories){
            if(recursive){
                fileList = (List<File>) FileUtils.listFilesAndDirs(directory, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
            }
            else{
                fileList = (List<File>) FileUtils.listFilesAndDirs(directory, TrueFileFilter.INSTANCE, null);
            }
        }
        if(!includeFiles && !includeDirectories){
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
    public long getFileSize(final String filePathString) throws NullPointerException{
        return FileUtils.sizeOf(workingDirectory.resolve(Paths.get(filePathString)).toFile());
    }
    @Override
    public List<LocalFile> getFilesForDateRange(final String directoryPathString, final String startDate, final String endDate, final boolean dateCreated, final boolean dateModified, final boolean recursive, final boolean includeFiles, final boolean includeDirectories, final SortingType sortingType) throws IOException, ParseException {
        Date rangeStartDate = new SimpleDateFormat("dd/MM/yyyy").parse(startDate);
        Date rangeEndDate = new SimpleDateFormat("dd/MM/yyyy").parse(endDate);
        if(rangeStartDate.compareTo(rangeEndDate) > 0){
            System.out.println("Invalid range");
        }
        List<LocalFile> fileList = new ArrayList<>();
        List<LocalFile> directoryToSearchList = getFileListInDirectory(directoryPathString, recursive, includeFiles, includeDirectories, SortingType.NONE);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        for (LocalFile file : directoryToSearchList) {
            if(dateCreated && !dateModified){
                Date fileCreationDate = dateFormat.parse(dateFormat.format(file.getFileMetadata().creationTime().toMillis()));
                if(fileCreationDate.compareTo(rangeStartDate) >= 0 && fileCreationDate.compareTo(rangeEndDate) <= 0){
                    fileList.add(file);
                }
            }
            if(!dateCreated && dateModified){
                Date fileModificationDate = dateFormat.parse(dateFormat.format(file.getFileMetadata().lastModifiedTime().toMillis()));
                if(fileModificationDate.compareTo(rangeStartDate) >= 0 && fileModificationDate.compareTo(rangeEndDate) <= 0){
                    fileList.add(file);
                }
            }
            if(dateCreated && dateModified){
                Date fileCreationDate = dateFormat.parse(dateFormat.format(file.getFileMetadata().creationTime().toMillis()));
                Date fileModificationDate = dateFormat.parse(dateFormat.format(file.getFileMetadata().lastModifiedTime().toMillis()));
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
    @Override
    public List<LocalFile> getFilesForExcludedExtensions(final String directoryPathString, final String searchExcludedExtensionsString, final boolean recursive, final boolean includeFiles, final boolean includeDirectories, final SortingType sortingType) throws IOException {
        List<String> searchExcludedExtensionsList = List.of(searchExcludedExtensionsString.split(","));
        List<LocalFile> fileList = new ArrayList<>();
        List<LocalFile> directoryToSearchList = getFileListInDirectory(directoryPathString, recursive, includeFiles, includeDirectories, SortingType.NONE);
        for (LocalFile file : directoryToSearchList) {
            for (String excludedExtension : searchExcludedExtensionsList) {
                if (!file.getFile().getName().toLowerCase().endsWith(excludedExtension.toLowerCase())) {
                    fileList.add(file);
                }
            }
        }
        return sortList(fileList, sortingType);
    }
    @Override
    public List<LocalFile> getFilesForExtensions(final String directoryPathString, final String searchExtensionsString, final boolean recursive, final boolean includeFiles, final boolean includeDirectories, final SortingType sortingType) throws IOException {
        List<String> searchExtensionsList = List.of(searchExtensionsString.split(","));
        List<LocalFile> fileList = new ArrayList<>();
        List<LocalFile> directoryToSearchList = getFileListInDirectory(directoryPathString, recursive, includeFiles, includeDirectories, SortingType.NONE);
        for (LocalFile file : directoryToSearchList) {
            for (String extension : searchExtensionsList) {
                if (file.getFile().getName().toLowerCase().endsWith(extension.toLowerCase())) {
                    fileList.add(file);
                }
            }
        }
        return sortList(fileList, sortingType);
    }
    @Override
    public List<LocalFile> getFilesForExtensionsAndExcludedExtensions(final String directoryPathString, final String searchExtensionsString, final String searchExcludedExtensionsString, final boolean recursive, final boolean includeFiles, final boolean includeDirectories, final SortingType sortingType) throws IOException {
        List<String> searchExtensionsList = List.of(searchExtensionsString.split(","));
        List<String> searchExcludedExtensionsList = List.of(searchExcludedExtensionsString.split(","));
        List<LocalFile> fileList = new ArrayList<>();
        List<LocalFile> directoryToSearchList = getFileListInDirectory(directoryPathString, recursive, includeFiles, includeDirectories, SortingType.NONE);
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
        return sortList(fileList, sortingType);
    }
    @Override
    public List<LocalFile> getFilesForSearchName(final String directoryPathString, final String search, final boolean recursive, final boolean includeFiles, final boolean includeDirectories, final SortingType sortingType) throws IOException {
        List<LocalFile> fileList = new ArrayList<>();
        List<LocalFile> directoryToSearchList = getFileListInDirectory(directoryPathString, recursive, includeFiles, includeDirectories, SortingType.NONE);
        for (LocalFile file : directoryToSearchList) {
            if (file.getFile().getName().toLowerCase().contains(search.toLowerCase())) {
                fileList.add(file);
            }
        }
        return sortList(fileList, sortingType);
    }
    @Override
    public List<LocalFile> getFilesForSearchNameAndExcludedExtensions(final String directoryPathString, final String search, final String searchExcludedExtensionsString, final boolean recursive, final boolean includeFiles, final boolean includeDirectories, final SortingType sortingType) throws IOException {
        List<String> searchExcludedExtensionsList = List.of(searchExcludedExtensionsString.split(","));
        List<LocalFile> fileList = new ArrayList<>();
        List<LocalFile> directoryToSearchList = getFileListInDirectory(directoryPathString, recursive, includeFiles, includeDirectories, SortingType.NONE);
        for (LocalFile file : directoryToSearchList) {
            for (String excludedExtension : searchExcludedExtensionsList) {
                if (!file.getFile().getName().toLowerCase().endsWith(excludedExtension.toLowerCase())) {
                    if (file.getFile().getName().toLowerCase().contains(search.toLowerCase())) {
                        fileList.add(file);
                    }
                }
            }
        }
        return sortList(fileList, sortingType);
    }
    @Override
    public List<LocalFile> getFilesForSearchNameAndExtensions(final String directoryPathString, final String search, final String searchExtensionsString, final boolean recursive, final boolean includeFiles, final boolean includeDirectories, final SortingType sortingType) throws IOException {
        List<String> searchExtensionsList = List.of(searchExtensionsString.split(","));
        List<LocalFile> fileList = new ArrayList<>();
        List<LocalFile> directoryToSearchList = getFileListInDirectory(directoryPathString, recursive, includeFiles, includeDirectories, SortingType.NONE);
        for (LocalFile file : directoryToSearchList) {
            for (String extension : searchExtensionsList) {
                if (file.getFile().getName().toLowerCase().endsWith(extension.toLowerCase())) {
                    if (file.getFile().getName().toLowerCase().contains(search.toLowerCase())) {
                        fileList.add(file);
                    }
                }
            }
        }
        return sortList(fileList, sortingType);
    }
    @Override
    public List<LocalFile> getFilesForSearchNameAndExtensionsAndExcludedExtensions(final String directoryPathString, final String search, final String searchExtensionsString, final String searchExcludedExtensionsString, final boolean recursive, final boolean includeFiles, final boolean includeDirectories, final SortingType sortingType) throws IOException {
        List<String> searchExtensionsList = List.of(searchExtensionsString.split(","));
        List<String> searchExcludedExtensionsList = List.of(searchExcludedExtensionsString.split(","));
        List<LocalFile> fileList = new ArrayList<>();
        List<LocalFile> directoryToSearchList = getFileListInDirectory(directoryPathString, recursive, includeFiles, includeDirectories, SortingType.NONE);
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
        return sortList(fileList, sortingType);
    }
    @Override
    public List<LocalFile> getFilesWithNames(final String directoryPathString, final String searchListString, final boolean recursive, final boolean includeFiles, final boolean includeDirectories, final SortingType sortingType) throws IOException {
        List<String> searchList = List.of(searchListString.split(","));
        List<LocalFile> directoryToSearchList = getFileListInDirectory(directoryPathString, recursive, includeFiles, includeDirectories, SortingType.NONE);
        List<LocalFile> foundFiles = new ArrayList<>();
        for (LocalFile file : directoryToSearchList) {
            for(String search : searchList){
                if(file.getFile().getName().equals(search)){
                    foundFiles.add(file);
                }
            }
        }
        return sortList(foundFiles, sortingType);
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
    public void printFileList(final List<LocalFile> fileList) throws IOException {
        for(LocalFile file : fileList){
            System.out.println(file.getFile().getName());
        }
    }
    @Override
    public void updateConfig(final String repositoryName, final DirectoryHandlerConfig directoryHandlerConfig, final String directoriesWithMaxFileCountString) throws IOException {
        Properties config = getConfig(repositoryName);
        if(directoryHandlerConfig == null && directoriesWithMaxFileCountString == null){
            System.out.println("Config not updated");
            return;
        }
        if(directoryHandlerConfig != null && directoriesWithMaxFileCountString == null){
            config.setProperty("maxRepositorySize", directoryHandlerConfig.getMaxRepositorySize());
            config.setProperty("excludedExtensions", directoryHandlerConfig.getExcludedExtensionsString());
        }
        if(directoryHandlerConfig == null && directoriesWithMaxFileCountString != null){
            List<String> directoriesWithMaxFileCount = List.of(directoriesWithMaxFileCountString.split(","));
            for(String directoryWithMaxFileCount : directoriesWithMaxFileCount){
                String directory = directoryWithMaxFileCount.substring(0, directoryWithMaxFileCount.indexOf("-"));
                String maxFileCount = directoryWithMaxFileCount.substring(directoryWithMaxFileCount.indexOf("-") + 1);
                config.setProperty(directory, maxFileCount);
            }
        }
        if(directoryHandlerConfig != null && directoriesWithMaxFileCountString != null){
            config.setProperty("maxRepositorySize", directoryHandlerConfig.getMaxRepositorySize());
            config.setProperty("excludedExtensions", directoryHandlerConfig.getExcludedExtensionsString());
            List<String> directoriesWithMaxFileCount = List.of(directoriesWithMaxFileCountString.split(","));
            for(String directoryWithMaxFileCount : directoriesWithMaxFileCount){
                String directory = directoryWithMaxFileCount.substring(0, directoryWithMaxFileCount.indexOf("-"));
                String maxFileCount = directoryWithMaxFileCount.substring(directoryWithMaxFileCount.indexOf("-") + 1);
                config.setProperty(directory, maxFileCount);
            }
        }
        OutputStream outputStream = new FileOutputStream(workingDirectory.resolve(repositoryName).resolve("config.properties").toAbsolutePath().toString());
        config.store(outputStream, "updatedConfig");
        outputStream.close();
    }
    @Override
    public void writeToFile(final String filePathString, final String textToWrite) throws IOException {
        String repositoryName = filePathString.split("/")[0];
        if (getDirectorySize(repositoryName) + textToWrite.getBytes().length > Integer.parseInt(getConfig(repositoryName).getProperty("maxRepositorySize"))) {
            System.out.println("Max Repository Size Exceeded");
        }
        else {
            FileUtils.writeStringToFile(workingDirectory.resolve(Paths.get(filePathString)).toFile(), textToWrite, "UTF-8", true);
        }
    }
    protected List<LocalFile> sortList(List<LocalFile> listToSort, final SortingType sortingType){
        if(sortingType == SortingType.NONE){
            return listToSort;
        }
        if(sortingType == SortingType.NAME){
            listToSort.sort(new LocalComparators.NameComparator());
        }
        else if(sortingType == SortingType.SIZE){
            listToSort.sort(new LocalComparators.SizeComparator());
        }
        else if(sortingType == SortingType.DATE_CREATED){
            listToSort.sort(new LocalComparators.CreationDateComparator());
        }
        else if(sortingType == SortingType.DATE_MODIFIED){
            listToSort.sort(new LocalComparators.ModificationDateComparator());
        }
        else{
            System.out.println("specify sorting type");
        }
        return listToSort;
    }

}