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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static rs.raf.exception.DirectoryHandlerExceptions.MaxFileCountExceededException;
public class DirectoryHandlerLocalImplementation implements IDirectoryHandlerSpecification<LocalFile> {
    private static final Path workingDirectory = Paths.get(System.getProperty("user.dir")).resolve("LocalRepositories");
    private static DirectoryHandlerLocalImplementation instance;
    static{
        if(!Files.exists(workingDirectory)){
            workingDirectory.toFile().mkdir();
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
    public void createRepository(final String repositoryName) throws IOException, FileAlreadyExistsException, MaxFileCountExceededException {
        Files.createDirectory(workingDirectory.resolve(repositoryName));
        createConfig(repositoryName, new DirectoryHandlerConfig());
    }
    @Override
    public void createRepository(final String repositoryName, final DirectoryHandlerConfig directoryHandlerConfig) throws IOException, FileAlreadyExistsException, MaxFileCountExceededException {
        Files.createDirectory(workingDirectory.resolve(repositoryName));
        createConfig(repositoryName, directoryHandlerConfig);
    }
    @Override
    public void createDirectory(final String directoryPathString) throws IOException, FileAlreadyExistsException, DirectoryHandlerExceptions.MaxFileCountExceededException {
        String parentDirectoryPathString = directoryPathString.substring(0, directoryPathString.lastIndexOf("/"));
        Properties config = getConfig(directoryPathString);
        if(getFileCount(parentDirectoryPathString) + 1 > Integer.parseInt(config.getProperty(parentDirectoryPathString))){
            throw new MaxFileCountExceededException(parentDirectoryPathString);
        }
        Files.createDirectories(workingDirectory.resolve(Paths.get(directoryPathString)));
    }
    @Override
    public void createFile(final String filePathString) throws IOException, MaxFileCountExceededException {
        String parentDirectoryPathString = filePathString.substring(0, filePathString.lastIndexOf("/"));
        Properties config = getConfig(filePathString);
        if(getFileCount(parentDirectoryPathString) + 1 > Integer.parseInt(config.getProperty(parentDirectoryPathString))){
            throw new MaxFileCountExceededException(parentDirectoryPathString);
        }
        Path filePath = workingDirectory.resolve(Paths.get(filePathString));
        filePath.toFile().createNewFile();
    }
    @Override
    public void createConfig(final String repositoryName, final DirectoryHandlerConfig directoryHandlerConfig) throws IOException, MaxFileCountExceededException {
        createFile(repositoryName + "/config.properties");
        Properties config = getConfig(repositoryName);
        config.setProperty("maxRepositorySize", directoryHandlerConfig.getMaxRepositorySize());
        config.setProperty("excludedExtensions", directoryHandlerConfig.getExcludedExtensionsString());
        OutputStream outputStream = new FileOutputStream(workingDirectory.resolve(repositoryName).resolve("config.properties").toAbsolutePath().toString());
        config.store(outputStream, "updatedConfig");
        outputStream.close();
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
    public Properties getConfig(final String repositoryName) throws IOException {
        Properties config = new Properties();
        FileInputStream fileInputStream = new FileInputStream(workingDirectory.resolve(repositoryName).resolve("config.properties").toFile());
        InputStream inputStream = fileInputStream;
        config.load(inputStream);
        inputStream.close();
        return config;
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
        if (getDirectorySize(repositoryName) + textToWrite.getBytes().length > Integer.parseInt(getConfig(repositoryName).getProperty("maxRepositorySize"))) {
            System.out.println("Max Repository Size Exceeded");
        }
        else {
            FileUtils.writeStringToFile(workingDirectory.resolve(Paths.get(filePathString)).toFile(), textToWrite, "UTF-8", true);
        }
    }
    @Override
    public void deleteFile(final String filePathString) throws IOException {
        workingDirectory.resolve(Paths.get(filePathString)).toFile().delete();
    }
    //TODO if directory, copy all subdirectories recursively
    @Override
    public void downloadFile(final String filePathString, final String downloadAbsolutePathString, final boolean overwrite) throws IOException {
        Path downloadPath;
        if(downloadAbsolutePathString == null){
            downloadPath = workingDirectory.resolve("Downloads");
            if(!Files.exists(downloadPath)){
                downloadPath.toFile().mkdir();
            }
        }
        else{
            downloadPath = Paths.get(downloadAbsolutePathString);
        }
        String fileName = String.valueOf(Paths.get(filePathString).getFileName());
        Path originalPath = workingDirectory.resolve(Paths.get(filePathString));
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
    public List<LocalFile> getAllFiles(final SortingType sortingType) throws IOException {
        return getFileListInDirectory(null, true, true, true, sortingType);
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
    public void printFileList(final List<LocalFile> fileList) throws IOException {
        for(LocalFile file : fileList){
            System.out.println(file.getFile().getName());
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