package rs.raf.specification;

import rs.raf.model.DirectoryHandlerConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public interface IDirectoryHandlerSpecification {
    /**
     * Creates a repository with the default name and config
     * @throws IOException
     */
    void createRepository() throws IOException;
    /**
     * Creates a repository with the specified name and default config
     * @param repositoryName name of the repository to create
     * @throws IOException
     */
    void createRepository(final String repositoryName) throws IOException;
    /**
     * Creates a repository with the default name and custom config
     * @param directoryHandlerConfig custom config
     * @throws IOException
     */

    void createRepository(final DirectoryHandlerConfig directoryHandlerConfig) throws IOException;
    /**
     * Creates a repository with the specified name and custom config
     * @param repositoryName name of the repository to create
     * @param directoryHandlerConfig custom config
     * @throws IOException
     */
    void createRepository(final String repositoryName, final DirectoryHandlerConfig directoryHandlerConfig) throws IOException;
    /**
     * Creates a directory with the default name in the specified repository
     * @param repositoryName name of the repository to create the directory in
     */
    void createDirectory(final String repositoryName);
    /**
     * Creates a directory with the specified name in the specified repository
     * @param repositoryName name of the repository to create the directory in
     * @param directoryName name of the directory to create
     * @apiNote directoryName can be directory path with / delimiter
     */
    void createDirectory(final String repositoryName, final String directoryName);
    /**
     * Creates a file with the default name and specified extension in the specified repository and directory
     * @param repositoryName name of the repository to create the directory in
     * @param directoryName name of directory to create the file in
     * @param fileExtension file extension
     * @apiNote pass directoryName as empty string to target the repository root
     */
    void createFile(final String repositoryName, final String directoryName, final String fileExtension);
    /**
     * Creates a file with the specified name;
     * @param repositoryName name of the repository to create the directory in
     * @param directoryName name of directory to create the file in
     * @param fileName name of file to create
     * @param fileExtension file extension
     */
    void createFile(final String repositoryName, final String directoryName, final String fileName, final String fileExtension);
    /**
     * Updates the config of specified repository with the specified config
     * @param repositoryName name of the repository to create
     * @param directoryHandlerConfig custom config
     * @throws IOException
     */
    void updateConfig(final String repositoryName, final DirectoryHandlerConfig directoryHandlerConfig) throws IOException;
    /**
     * Gets the size of a directory
     * @param repositoryName name of the repository the directory is in
     * @param directoryName name of the directory to get the size of
     * @return long size of the directory
     */
    long getDirectorySize(final String repositoryName, final String directoryName) throws FileNotFoundException, IOException;
    /**
     * Gets the size of a directory
     * @param repositoryName name of the repository the directory is in
     * @param directoryName name of the directory the file is in
     * @param fileName name of the file to get the size of
     * @return long size of the file
     */
    long getFileSize(final String repositoryName, final String directoryName, final String fileName) throws FileNotFoundException, IOException;
    Properties getProperties(final String repositoryName, final String directoryName) throws IOException;
    void createDefaultConfig(final String repositoryName, final String directoryName) throws IOException;
    void createConfig(final String repositoryName, final String directoryName, final DirectoryHandlerConfig directoryHandlerConfig) throws IOException;
    String arrayToString(final String repositoryName, final String[] array);
    void writeToFile(final String repositoryName, final String directoryName, final String fileName, final String textToWrite) throws IOException;
    void deleteFile(final String repositoryName, final String directoryName, final String fileName) throws IOException;
    void renameFile(final String repositoryName, final String directoryName, final String fileName, final String newName) throws IOException;
    List<String> getFileList(final String repositoryName, final String directoryName);
    int getFileCount(final String repositoryName, final String directoryName);
    List<File> getFilesForSearchName(final String repositoryName, final String directoryName, final String search);
    List<File> getFilesForSearchNameAndExtensions(final String repositoryName, final String directoryName, final String search, final String[] searchExtensions);
    List<File> getFilesForSearchNameAndExcludedExtensions(final String repositoryName, final String directoryName, final String search, final String[] searchExcludedExtensions);
    List<File> getFilesForSearchNameAndExtensionsAndExcludedExtensions(final String repositoryName, final String directoryName, final String search, final String[] searchExtensions, final String[] searchExcludedExtensions);
    List<File> getFilesForExtensions(final String repositoryName, final String directoryName, final String[] searchExtensions);
    List<File> getFilesForExcludedExtensions(final String repositoryName, final String directoryName, final String[] searchExcludedExtensions);
    List<File> getFilesForExtensionsAndExcludedExtensions(final String repositoryName, final String directoryName, final String[] searchExtensions, final String[] searchExcludedExtensions);
    void moveFile(final String repositoryName, final String oldDirectoryName, final String newDirectoryName);

}