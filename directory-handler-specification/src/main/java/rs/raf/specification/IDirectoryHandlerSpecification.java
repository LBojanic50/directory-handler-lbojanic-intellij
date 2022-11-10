package rs.raf.specification;

import rs.raf.config.ConfigUpdateTypes;
import rs.raf.exception.DirectoryHandlerExceptions;
import rs.raf.config.DirectoryHandlerConfig;
import rs.raf.model.SortingType;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.text.ParseException;
import java.util.List;
import rs.raf.exception.DirectoryHandlerExceptions.*;

public interface IDirectoryHandlerSpecification<T> {

    void copyFiles(String filePathsString, String copyDestinationDirectoryString, final boolean overwrite) throws IOException, BadPathException, NoFileAtPathException, MaxFileCountExceededException, InvalidParameterException, NonExistentRepositoryException;
    /**
     * Creates a specified config in the specified repository
     * @param repositoryName name of the repository to create the config in
     * @param configString config to create
     *                               (parse new DirectoryHandlerConfig() to create the default config with
     *                               maxRepositorySize=1073741824 (Bytes)
     *                               excludedExtensions="")
     */
    void createConfig(final String repositoryName, final String configString) throws IOException, InvalidConfigParametersException, NoFileAtPathException, BadPathException, InvalidParameterException, NonExistentRepositoryException;
    /**
     * Creates a directory at the specified forward slash delimited path to the directory to create
     * @param directoryPathsString forward slash delimited path to the directory to create
     */
    void createDirectories(String directoryPathsString) throws NoFileAtPathException, IOException, MaxFileCountExceededException, BadPathException, NonExistentRepositoryException, InvalidParameterException;
    /**
     * Creates a file at the specified forward slash delimited path to the file to create
     * @param filePathsString forward slash delimited path to the file to create
     */
    void createFiles(String filePathsString) throws IOException, BadPathException, MaxFileCountExceededException, FileExtensionException, MaxRepositorySizeExceededException, NoFileAtPathException, InvalidParameterException, NonExistentRepositoryException;
    /**
     * Creates a repository with the specified name and custom config
     * @param repositoryName name of the repository to create
     * @param configString custom config
     * @throws IOException
     */
    void createRepository(final String repositoryName, final String configString) throws NonExistentRepositoryException, InvalidParameterException, NoFileAtPathException, IOException, BadPathException, InvalidConfigParametersException;
    /**
     * Deletes a file at the specified forward slash delimited path
     * @param filePathsString forward slash delimited path to the file delete
     */
    void deleteFiles(String filePathsString) throws NoFileAtPathException, BadPathException, IOException;
    /**
     * Downloads a file at the specified forward slash delimited path to the specified forward slash delimited path to the download directory
     * @param filePathsString forward slash delimited path to the file to download
     * @param downloadDestinationDirectoryString forward slash delimited path to the download directory
     * @param overwrite if naming conflict occurs, determines if to overwrite the file or create a new one
     */
    void downloadFiles(String filePathsString, String downloadDestinationDirectoryString, boolean overwrite) throws NoFileAtPathException, IOException, MaxFileCountExceededException, BadPathException, InvalidParameterException, NonExistentRepositoryException;
    /**
     * Gets the Properties Object from the specified repository
     * @param repositoryName name of the repository to get the properties from
     * @return Properties Object from the specified repository
     */
    DirectoryHandlerConfig getConfig(final String repositoryName) throws NoFileAtPathException, IOException, MaxFileCountExceededException, BadPathException, NonExistentRepositoryException, InvalidParameterException;
    /**
     * Gets the size of a directory at the specified forward slash delimited path to the directory of which to get the size of
     * @param directoryPathString forward slash delimited string representation of the directory path to the directory of which to get the size of
     * @return long size of directory in bytes
     */
    long getDirectorySize(String directoryPathString) throws BadPathException, NoFileAtPathException, IOException;
    /**
     * Gets the size of a file at the specified forward slash delimited path to the file of which to get the size of
     * @param directoryPathString forward slash delimited string representation of the path to the directory of which to get the size of
     * @return int number of files in directory
     */
    int getFileCount(String directoryPathString) throws BadPathException, NoFileAtPathException, IOException;
    /**
     * Gets the file list for specified search parameters
     * @param directoryPathString forward slash delimited string representation of the path to the directory to search
     * @param recursive whether to search recursively
     * @param includeFiles whether to include files
     * @param includeDirectories whether to include directories
     * @param sortingType SortingType enum element of the method of sorting to apply
     * @return List<T> of files in search result
     */
    List<T> getFileListInDirectory(final String directoryPathString, final boolean recursive, final boolean includeFiles, final boolean includeDirectories, final SortingType sortingType) throws InvalidParameterException, IOException, BadPathException, NoFileAtPathException;
    /**
     * Gets the size of a file at the specified forward slash delimited path to the file of which to get the size of
     * @param filePathString forward slash delimited string representation of the path to the file of which to get the size of (/, \, \\)
     * @return long size of file in bytes
     */
    long getFileSize(String filePathString) throws MaxFileCountExceededException, BadPathException, NoFileAtPathException, IOException, InvalidParameterException, NonExistentRepositoryException;
    /**
     * Gets the file list for specified search parameters
     * @param directoryPathString forward slash delimited string representation of the path to the directory to search
     * @param startDate dd/MM/yyyy formatted string of the low date limit for search
     * @param endDate dd/MM/yyyy formatted string of the upper date limit for search
     * @param dateCreated whether to consider file creation date in search
     * @param dateModified whether to consider file modification date in search
     * @param recursive whether to search recursively
     * @param includeFiles whether to include files
     * @param includeDirectories whether to include directories
     * @param sortingType SortingType enum element of the method of sorting to apply
     * @return List<T> of files in search result
     */
    List<T> getFilesForDateRange(final String directoryPathString, final String startDate, final String endDate, final boolean dateCreated, final boolean dateModified, final boolean recursive, final boolean includeFiles, final boolean includeDirectories, final SortingType sortingType) throws InvalidParameterException, ParseException, NoFileAtPathException, IOException, BadPathException;
    /**
     * Gets the file list for specified search parameters
     * @param directoryPathString forward slash delimited string representation of the path to the directory to search
     * @param searchExcludedExtensionsString comma delimited string of the extensions to exclude from search
     * @param recursive whether to search recursively
     * @param includeFiles whether to include files
     * @param includeDirectories whether to include directories
     * @param sortingType SortingType enum element of the method of sorting to apply
     * @return List<T> of files in search result
     */
    List<T> getFilesForExcludedExtensions(final String directoryPathString, final String searchExcludedExtensionsString, final boolean recursive, final boolean includeFiles, final boolean includeDirectories, final SortingType sortingType) throws InvalidParameterException, NoFileAtPathException, IOException, BadPathException;
    /**
     * Gets the file list for specified search parameters
     * @param directoryPathString forward slash delimited string representation of the path to the directory to search
     * @param searchExtensionsString comma delimited string of the extensions to include in search
     * @param recursive whether to search recursively
     * @param includeFiles whether to include files
     * @param includeDirectories whether to include directories
     * @param sortingType SortingType enum element of the method of sorting to apply
     * @return List<T> of files in search result
     */
    List<T> getFilesForExtensions(final String directoryPathString, final String searchExtensionsString, final boolean recursive, final boolean includeFiles, final boolean includeDirectories, final SortingType sortingType) throws InvalidParameterException, NoFileAtPathException, IOException, BadPathException;
    /**
     * Gets the file list for specified search parameters
     * @param directoryPathString forward slash delimited string representation of the path to the directory to search
     * @param searchExtensionsString comma delimited string of the extensions to include in search
     * @param searchExcludedExtensionsString comma delimited string of the extensions to exclude from search
     * @param recursive whether to search recursively
     * @param includeFiles whether to include files
     * @param includeDirectories whether to include directories
     * @param sortingType SortingType enum element of the method of sorting to apply
     * @return List<T> of files in search result
     */
    List<T> getFilesForExtensionsAndExcludedExtensions(final String directoryPathString, final String searchExtensionsString, final String searchExcludedExtensionsString, final boolean recursive, final boolean includeFiles, final boolean includeDirectories, final SortingType sortingType) throws InvalidParameterException, NoFileAtPathException, IOException, BadPathException;
    /**
     * Gets the file list for specified search parameters
     * @param directoryPathString forward slash delimited string representation of the path to the directory to search
     * @param search search string for file name
     * @param recursive whether to search recursively
     * @param includeFiles whether to include files
     * @param includeDirectories whether to include directories
     * @param sortingType SortingType enum element of the method of sorting to apply
     * @return List<T> of files in search result
     */
    List<T> getFilesForSearchName(final String directoryPathString, final String search, final boolean recursive, final boolean includeFiles, final boolean includeDirectories, final SortingType sortingType) throws InvalidParameterException, NoFileAtPathException, IOException, BadPathException;
    /**
     * Gets the file list for specified search parameters
     * @param directoryPathString forward slash delimited string representation of the path to the directory to search
     * @param search search string for file name
     * @param searchExcludedExtensionsString comma delimited string of the extensions to exclude from search
     * @param recursive whether to search recursively
     * @param includeFiles whether to include files
     * @param includeDirectories whether to include directories
     * @param sortingType SortingType enum element of the method of sorting to apply
     * @return List<T> of files in search result
     */
    List<T> getFilesForSearchNameAndExcludedExtensions(final String directoryPathString, final String search, final String searchExcludedExtensionsString, final boolean recursive, final boolean includeFiles, final boolean includeDirectories, final SortingType sortingType) throws InvalidParameterException, NoFileAtPathException, IOException, BadPathException;
    /**
     * Gets the file list for specified search parameters
     * @param directoryPathString forward slash delimited string representation of the path to the directory to search
     * @param search search string for file name
     * @param searchExtensionsString comma delimited string of the extensions to include in search
     * @param recursive whether to search recursively
     * @param includeFiles whether to include files
     * @param includeDirectories whether to include directories
     * @param sortingType SortingType enum element of the method of sorting to apply
     * @return List<T> of files in search result
     */
    List<T> getFilesForSearchNameAndExtensions(final String directoryPathString, final String search, final String searchExtensionsString, final boolean recursive, final boolean includeFiles, final boolean includeDirectories, final SortingType sortingType) throws InvalidParameterException, NoFileAtPathException, IOException, BadPathException;
    /**
     * Gets the file list for specified search parameters
     * @param directoryPathString forward slash delimited string representation of the path to the directory to search
     * @param search search string for file name
     * @param searchExtensionsString comma delimited string of the extensions to include in search
     * @param searchExcludedExtensionsString comma delimited string of the extensions to exclude from search
     * @param recursive whether to search recursively
     * @param includeFiles whether to include files
     * @param includeDirectories whether to include directories
     * @param sortingType SortingType enum element of the method of sorting to apply
     * @return List<T> of files in search result
     */
    List<T> getFilesForSearchNameAndExtensionsAndExcludedExtensions(final String directoryPathString, final String search, final String searchExtensionsString, final String searchExcludedExtensionsString, final boolean recursive, final boolean includeFiles, final boolean includeDirectories, final SortingType sortingType) throws InvalidParameterException, NoFileAtPathException, IOException, BadPathException;
    /**
     * Gets the file list for specified search parameters
     * @param directoryPathString forward slash delimited string representation of the path to the directory to search
     * @param searchListString comma delimited string of the file names to search for
     * @param recursive whether to search recursively
     * @param includeFiles whether to include files
     * @param includeDirectories whether to include directories
     * @param sortingType SortingType enum element of the method of sorting to apply
     * @return List<T> of files in search result
     */
    List<T> getFilesWithNames(final String directoryPathString, final String searchListString, final boolean recursive, final boolean includeFiles, final boolean includeDirectories, final SortingType sortingType) throws InvalidParameterException, NoFileAtPathException, IOException, BadPathException;
    void moveFiles(String filePathsString, String moveDestinationDirectoryString, boolean overwrite) throws NoFileAtPathException, IOException, BadPathException, InvalidParameterException, NonExistentRepositoryException, MaxFileCountExceededException, MaxRepositorySizeExceededException;
    void printConfig(final DirectoryHandlerConfig directoryHandlerConfig) throws IOException;
    /**
     * Prints the list of files specified
     * @param fileList file list to print
     */
    void printFileList(final List<T> fileList) throws IOException;
    void renameFile(String filePathString, final String newFileName) throws NoFileAtPathException, IOException, MaxFileCountExceededException, BadPathException, InvalidParameterException, NonExistentRepositoryException, FileExtensionException;
    /**
     * Updates the config of specified repository with the specified config
     *
     * @param repositoryName         name of the repository to create
     * @param configString custom config
     * @throws IOException
     */
    void updateConfig(final String repositoryName, final String configString, final ConfigUpdateTypes updateType) throws InvalidParameterException, NoFileAtPathException, NonExistentRepositoryException, IOException, MaxFileCountExceededException, BadPathException, InvalidConfigParametersException, ValueInConfigCannotBeLessThanOneException;
    /**
     * Writes specified text to specified file path
     * @param filePathString forward slash delimited string representation of the path to the file to create
     * @param textToWrite text to write
     */
    void writeToFile(String filePathString, final String textToWrite) throws BadPathException, NoFileAtPathException, IOException, MaxFileCountExceededException, InvalidParameterException, NonExistentRepositoryException, FileExtensionException, MaxRepositorySizeExceededException;
}