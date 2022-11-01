package rs.raf.specification;

import rs.raf.model.DirectoryHandlerConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Path;
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
     * Creates a directory with the specified name in the specified repository
     *
     * @param repositoryName name of the repository to create the directory in
     * @param directoryName  name of the directory to create
     * @apiNote directoryName can be directory path with / delimiter
     */
    void createDirectory(final String repositoryName, final String directoryName) throws IOException, FileAlreadyExistsException;

    /**
     * Creates a file with the default name and specified extension in the specified repository and directory
     *
     * @param repositoryName name of the repository to create the directory in
     * @param fileName  name of directory to create the file in
     * @param fileExtension  file extension
     * @apiNote pass directoryName as empty string to target the repository root
     */
    boolean createFile(final String repositoryName, final String fileName, final String fileExtension) throws Exception, FileAlreadyExistsException;

    /**
     * Creates a file with the specified name;
     *
     * @param repositoryName name of the repository to create the directory in
     * @param directoryName  name of directory to create the file in
     * @param fileName       name of file to create
     * @param fileExtension  file extension
     */
    boolean createFile(final String repositoryName, final String directoryName, final String fileName, final String fileExtension) throws Exception, FileAlreadyExistsException;

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

    /**
     * Gets the size of a repository
     *
     * @param repositoryName name of the repository to get the size of
     * @return long size of the repository
     */
    long getRepositorySize(final String repositoryName);

    long getDirectorySize(final String repositoryName, final String directoryName) throws FileNotFoundException, IOException;

    /**
     * Gets the size of a directory
     *
     * @param repositoryName name of the repository the directory is in
     * @param directoryName  name of the directory the file is in
     * @param fileName       name of the file to get the size of
     * @return long size of the file
     */
    long getFileSize(final String repositoryName, final String directoryName, final String fileName) throws FileNotFoundException, IOException;

    String arrayToString(final String[] array);

    void writeToFile(final String repositoryName, final String directoryName, final String fileName, final String textToWrite) throws IOException;

    void deleteFile(final String repositoryName, final String directoryName, final String fileName) throws IOException;

    void downloadFile(final String repositoryName, final String directoryName, final String fileName, final boolean overwrite) throws IOException;

    void downloadFile(final String repositoryName, final String directoryName, final String fileName, final String downloadPathString, final boolean overwrite) throws IOException;

    void moveOrRenameFile(final String repositoryName, final String directoryName, final String fileName, final String newName) throws IOException;

    int getFileCount(final String repositoryName, final String directoryName);

    List<T> getFileList(final String repositoryName, final String directoryName);

    List<T> getAllFilesList() throws IOException;

    List<T> getFileListInDirectory(final String directoryName) throws IOException;

    List<T> getFilesForSearchName(final String repositoryName, final String directoryName, final String search);

    List<T> getFilesForSearchNameAndExtensions(final String repositoryName, final String directoryName, final String search, final String[] searchExtensions);

    List<T> getFilesForSearchNameAndExcludedExtensions(final String repositoryName, final String directoryName, final String search, final String[] searchExcludedExtensions);

    List<T> getFilesForSearchNameAndExtensionsAndExcludedExtensions(final String repositoryName, final String directoryName, final String search, final String[] searchExtensions, final String[] searchExcludedExtensions);

    List<T> getFilesForExtensions(final String repositoryName, final String directoryName, final String[] searchExtensions);

    List<T> getFilesForExcludedExtensions(final String repositoryName, final String directoryName, final String[] searchExcludedExtensions);

    List<T> getFilesForExtensionsAndExcludedExtensions(final String repositoryName, final String directoryName, final String[] searchExtensions, final String[] searchExcludedExtensions);

}