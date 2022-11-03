package rs.raf.specification;

import rs.raf.model.DirectoryHandlerConfig;

import java.io.IOException;
import java.io.InputStream;

import java.nio.file.FileAlreadyExistsException;

import java.security.GeneralSecurityException;

import java.util.List;
import java.util.Properties;

public interface IDirectoryHandlerSpecification<T> {
    void authorizeGoogleDriveClient() throws IOException, GeneralSecurityException;
    /**
     *
     */
    Object getCredentials(final InputStream inputStream, final String CREDENTIALS_FILE_PATH, final Object HTTP_TRANSPORT, final Object JSON_FACTORY, final List<String> SCOPES, final String TOKENS_DIRECTORY_PATH) throws UnsupportedOperationException, IOException;
    String getFileIdByName(final String fileName) throws IOException;
    String getFileIdByNameInRepository(final List<T> fileListInRepository, final String fileName) throws IOException;
    /**
     * Creates a repository with the specified name and default config
     *
     * @param repositoryName name of the repository to create
     * @throws IOException
     */
    void createRepository(final String repositoryName) throws IOException, FileAlreadyExistsException;
    /**
     * Creates a repository with the specified name and custom config
     *
     * @param repositoryName         name of the repository to create
     * @param directoryHandlerConfig custom config
     * @throws IOException
     */
    void createRepository(final String repositoryName, final DirectoryHandlerConfig directoryHandlerConfig) throws IOException, FileAlreadyExistsException;
    /**
     * Creates a directory at the specified slash delimited string representation of the directory path to the directory to create (/, \, \\)
     *
     * @param directoryPathString slash delimited string representation of the directory path to the directory to create (/, \, \\)
     */
    void createDirectory(final String directoryPathString) throws IOException, FileAlreadyExistsException;
    /**
     * Creates a file at the specified slash delimited string representation of the file path to the file to create (/, \, \\)
     *
     * @param filePathString slash delimited string representation of the file path to the file to create (/, \, \\)
     */
    boolean createFile(final String filePathString) throws Exception, FileAlreadyExistsException;
    void createDefaultConfig(final String repositoryName) throws IOException, FileAlreadyExistsException;
    void createConfig(final String repositoryName, final DirectoryHandlerConfig directoryHandlerConfig) throws IOException, FileAlreadyExistsException;
    /**
     * Updates the config of specified repository with the specified config
     *
     * @param repositoryName         name of the repository to create
     * @param directoryHandlerConfig custom config
     * @throws IOException
     */
    void updateConfig(final String repositoryName, final DirectoryHandlerConfig directoryHandlerConfig) throws IOException, FileAlreadyExistsException;
    /**
     * Gets the Properties Object from the specified repository
     * @param repositoryName name of the repository to get the properties from
     * @return Properties Object from the specified repository*/
    Properties getProperties(final String repositoryName) throws IOException;
    int getFileCount(final String directoryPathString);
    /**
     * Gets the size of a file at the specified slash delimited string representation of the file path to the file of which to get the size of (/, \, \\)
     * @param filePathString slash delimited string representation of the file path to the file of which to get the size of (/, \, \\)
     * @return long size in bytes
     */
    long getFileSize(final String filePathString) throws NullPointerException;
    /**
     * Gets the size of a directory at the specified slash delimited string representation of the directory path to the directory of which to get the size of (/, \, \\)
     * @param directoryPathString slash delimited string representation of the directory path to the directory of which to get the size of (/, \, \\)
     * @return long size in bytes
     */
    long getDirectorySize(final String directoryPathString) throws NullPointerException;
    void writeToFile(final String filePathString, final String textToWrite) throws IOException;
    void deleteFile(final String filePathString) throws IOException;
    void downloadFile(final String filePathString, final boolean overwrite) throws IOException;
    void downloadFile(final String filePathString, final String downloadPathString, final boolean overwrite) throws IOException;
    void moveOrRenameFile(final String oldPathString, final String newPathString) throws IOException;

    List<T> getFileListInDirectory(final String directoryPathString, final boolean recursive, final boolean includeFiles, final boolean includeDirectories) throws IOException;
    List<T> getFilesForSearchName(final String directoryPathString, final String search, final boolean recursive, final boolean includeFiles, final boolean includeDirectories) throws IOException;
    List<T> getFilesForSearchNameAndExtensions(final String directoryPathString, final String search, final String searchExtensionsString, final boolean recursive, final boolean includeFiles, final boolean includeDirectories) throws IOException;
    List<T> getFilesForSearchNameAndExcludedExtensions(final String directoryPathString, final String search, final String searchExcludedExtensionsString, final boolean recursive, final boolean includeFiles, final boolean includeDirectories) throws IOException;
    List<T> getFilesForSearchNameAndExtensionsAndExcludedExtensions(final String directoryPathString, final String search, final String searchExtensionsString, final String searchExcludedExtensionsString, final boolean recursive, final boolean includeFiles, final boolean includeDirectories) throws IOException;
    List<T> getFilesForExtensions(final String directoryPathString, final String searchExtensionsString, final boolean recursive, final boolean includeFiles, final boolean includeDirectories) throws IOException;
    List<T> getFilesForExcludedExtensions(final String directoryPathString, final String searchExcludedExtensionsString, final boolean recursive, final boolean includeFiles, final boolean includeDirectories) throws IOException;
    List<T> getFilesForExtensionsAndExcludedExtensions(final String directoryPathString, final String searchExtensionsString, final String searchExcludedExtensionsString, final boolean recursive, final boolean includeFiles, final boolean includeDirectories) throws IOException;
    List<T> getFilesWithName(final String directoryPathString, final String search, final boolean recursive, final boolean includeFiles, final boolean includeDirectories) throws IOException;
}