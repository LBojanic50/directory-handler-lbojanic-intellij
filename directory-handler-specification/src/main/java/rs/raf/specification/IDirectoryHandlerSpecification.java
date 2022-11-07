package rs.raf.specification;

import rs.raf.exception.DirectoryHandlerExceptions;
import rs.raf.model.DirectoryHandlerConfig;
import rs.raf.model.SortingType;

import java.io.IOException;

import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Path;

import java.text.ParseException;
import java.util.List;
import java.util.Properties;

public interface IDirectoryHandlerSpecification<T> {
    /**
     * Creates a repository with the specified name and default config
     * @param repositoryName name of the repository to create
     * @throws IOException
     */
    void createRepository(final String repositoryName) throws Exception;
    /**
     * Creates a repository with the specified name and default config ()
     * @param repositoryName name of the repository to create
     * @param directoryHandlerConfig custom config
     * @throws IOException
     */
    void createRepository(final String repositoryName, final DirectoryHandlerConfig directoryHandlerConfig) throws Exception;
    /**
     * Creates a directory at the specified forward slash delimited path to the directory to create
     * @param directoryPathString forward slash delimited path to the directory to create
     */
    void createDirectory(final String directoryPathString) throws IOException, FileAlreadyExistsException, DirectoryHandlerExceptions.MaxFileCountExceededException;
    /**
     * Creates a file at the specified forward slash delimited path to the file to create
     * @param filePathString forward slash delimited path to the file to create
     */
    void createFile(final String filePathString) throws Exception, FileAlreadyExistsException;
    /**
     * Creates a specified config in the specified repository
     * @param repositoryName name of the repository to create the config in
     * @param directoryHandlerConfig config to create
     *                               (parse new DirectoryHandlerConfig() to create the default config with
     *                               maxRepositorySize=1073741824 (Bytes)
     *                               excludedExtensions="")
     */
    void createConfig(final String repositoryName, final DirectoryHandlerConfig directoryHandlerConfig) throws Exception;
    /**
     * Updates the config of specified repository with the specified config
     *
     * @param repositoryName         name of the repository to create
     * @param directoryHandlerConfig custom config
     * @throws IOException
     */
    void updateConfig(final String repositoryName, final DirectoryHandlerConfig directoryHandlerConfig, final String directoriesWithMaxFileCountString) throws IOException;
    /**
     * Gets the Properties Object from the specified repository
     * @param repositoryName name of the repository to get the properties from
     * @return Properties Object from the specified repository
     */
    Properties getConfig(final String repositoryName) throws IOException;
    /**
     * Writes specified text to specified file path
     * @param filePathString forward slash delimited string representation of the path to the file to create
     * @param textToWrite text to write
     */
    void writeToFile(final String filePathString, final String textToWrite) throws IOException;
    /**
     * Moves or renames a file from the specified forward slash delimited source path to the specified forward slash delimited destination path
     * @param oldPathString forward slash delimited path to the file which move or rename
     * @param newPathString forward slash delimited destination path
     */
    void moveOrRenameFile(final String oldPathString, final String newPathString) throws IOException;
    /**
     * Deletes a file at the specified forward slash delimited path
     * @param filePathString forward slash delimited path to the file delete
     */
    void deleteFile(final String filePathString) throws IOException;
    /**
     * Downloads a file at the specified forward slash delimited path to the specified forward slash delimited path to the download directory
     * @param filePathString forward slash delimited path to the file to download
     * @param downloadAbsolutePathString forward slash delimited path to the download directory
     * @param overwrite if naming conflict occurs, determines if to overwrite the file or create a new one
     */
    void downloadFile(final String filePathString, final String downloadAbsolutePathString, final boolean overwrite) throws IOException;
    /**
     * Gets the file list for specified search parameters
     * @param directoryPathString forward slash delimited string representation of the path to the directory to search
     * @param recursive whether to search recursively
     * @param includeFiles whether to include files
     * @param includeDirectories whether to include directories
     * @param sortingType SortingType enum element of the method of sorting to apply
     * @return List<T> of files in search result
     */
    List<T> getFileListInDirectory(final String directoryPathString, final boolean recursive, final boolean includeFiles, final boolean includeDirectories, final SortingType sortingType) throws IOException;
    /**
     * Gets all files in the working directory
     * @param sortingType SortingType enum element of the method of sorting to apply
     * @return List<T> of files in search result
     */
    List<T> getAllFiles(final SortingType sortingType) throws IOException;
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
    List<T> getFilesForSearchName(final String directoryPathString, final String search, final boolean recursive, final boolean includeFiles, final boolean includeDirectories, final SortingType sortingType) throws IOException;
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
    List<T> getFilesForSearchNameAndExtensions(final String directoryPathString, final String search, final String searchExtensionsString, final boolean recursive, final boolean includeFiles, final boolean includeDirectories, final SortingType sortingType) throws IOException;
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
    List<T> getFilesForSearchNameAndExcludedExtensions(final String directoryPathString, final String search, final String searchExcludedExtensionsString, final boolean recursive, final boolean includeFiles, final boolean includeDirectories, final SortingType sortingType) throws IOException;
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
    List<T> getFilesForSearchNameAndExtensionsAndExcludedExtensions(final String directoryPathString, final String search, final String searchExtensionsString, final String searchExcludedExtensionsString, final boolean recursive, final boolean includeFiles, final boolean includeDirectories, final SortingType sortingType) throws IOException;
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
    List<T> getFilesForExtensions(final String directoryPathString, final String searchExtensionsString, final boolean recursive, final boolean includeFiles, final boolean includeDirectories, final SortingType sortingType) throws IOException;
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
    List<T> getFilesForExcludedExtensions(final String directoryPathString, final String searchExcludedExtensionsString, final boolean recursive, final boolean includeFiles, final boolean includeDirectories, final SortingType sortingType) throws IOException;
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
    List<T> getFilesForExtensionsAndExcludedExtensions(final String directoryPathString, final String searchExtensionsString, final String searchExcludedExtensionsString, final boolean recursive, final boolean includeFiles, final boolean includeDirectories, final SortingType sortingType) throws IOException;
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
    List<T> getFilesWithNames(final String directoryPathString, final String searchListString, final boolean recursive, final boolean includeFiles, final boolean includeDirectories, final SortingType sortingType) throws IOException;
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
    List<T> getFilesForDateRange(final String directoryPathString, final String startDate, final String endDate, final boolean dateCreated, final boolean dateModified, final boolean recursive, final boolean includeFiles, final boolean includeDirectories, final SortingType sortingType) throws IOException, ParseException;
    /**
     * Gets the size of a directory at the specified forward slash delimited path to the directory of which to get the size of
     * @param directoryPathString forward slash delimited string representation of the directory path to the directory of which to get the size of
     * @return long size of directory in bytes
     */
    long getDirectorySize(final String directoryPathString) throws NullPointerException, IOException;
    /**
     * Gets the size of a file at the specified forward slash delimited path to the file of which to get the size of
     * @param filePathString forward slash delimited string representation of the path to the file of which to get the size of (/, \, \\)
     * @return long size of file in bytes
     */
    long getFileSize(final String filePathString) throws NullPointerException, IOException;
    /**
     * Gets the size of a file at the specified forward slash delimited path to the file of which to get the size of
     * @param directoryPathString forward slash delimited string representation of the path to the directory of which to get the size of
     * @return int number of files in directory
     */
    int getFileCount(final String directoryPathString) throws IOException;
    /**
     * Prints the list of files specified
     * @param fileList file list to print
     */
    void printFileList(final List<T> fileList) throws IOException;
}