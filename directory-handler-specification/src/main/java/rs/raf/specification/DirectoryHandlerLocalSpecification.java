package rs.raf.specification;

import rs.raf.model.DirectoryHandlerConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public interface DirectoryHandlerLocalSpecification {
    String workingDirectory = System.getProperty("user.dir") + "\\directory-handler-project";
	/**
     * Creates a local repository with the default name and config
     */
	void createLocalRepository() throws IOException;
	/**
     * Creates a local repository with the specified name and default config
     * @param repositoryName name of the repository to create
     */
	void createLocalRepository(final String repositoryName) throws IOException;
    /**
     * Creates a local repository with the specified name and default config
     * @param repositoryName name of the repository to create
     * @param directoryHandlerConfig custom config
     */
    void createLocalRepository(final String repositoryName, final DirectoryHandlerConfig directoryHandlerConfig) throws IOException;
    /**
     * Creates a local directory with the default name in the specified repository
     * @param repositoryName name of the repository to create the directory in
     */
    void createLocalDirectory(final String repositoryName);
    /**
     * Creates a local directory with the specified name
     * @param directoryName name of directory to create
     */
    void createLocalDirectory(final String repositoryName, final String directoryName);
    /**
     * Creates a local file with the default name;
     * @param directoryName name of directory to create the file in
     * @param fileExtension file extension
     */
    void createLocalFile(final String repositoryName, final String directoryName, final String fileExtension);
    /**
     * Creates a local directory with the specified name;
     * @param directoryName name of directory to create the file in
     * @param fileName name of file to create
     * @param fileExtension file extension
     */
    void createLocalFile(final String repositoryName, final String directoryName, final String fileName, final String fileExtension);
    /**
     * Gets the size of a directory
     * @param directoryName name of the directory to get the size of
     */
    long getDirectorySize(final String directoryName) throws FileNotFoundException, IOException;
    long getFileSize(final String directoryName, final String fileName) throws FileNotFoundException, IOException;
	Properties getProperties(final String directoryName) throws IOException;
	void createDefaultConfig(final String directoryName) throws IOException;
	void createConfig(final String directoryName, final DirectoryHandlerConfig directoryHandlerConfig) throws IOException;
	String arrayToString(final String[] array);
    void writeToFile(final String directoryName, final String fileName, final String textToWrite) throws IOException;
    void deleteFile(final String directoryName, final String fileName) throws IOException;
    void renameFile(final String directoryName, final String fileName, final String newName) throws IOException;
    List<String> getFileList(final String directoryName);
    int getFileCount(final String directoryName);
    List<File> getFilesForSearchName(final String repositoryName, final String directoryName, final String search);
    List<File> getFilesForSearchNameAndExtensions(final String repositoryName, final String directoryName, final String search, final String[] searchExtensions);
    List<File> getFilesForSearchNameAndExcludedExtensions(final String repositoryName, final String directoryName, final String search, final String[] searchExcludedExtensions);
    List<File> getFilesForSearchNameAndExtensionsAndExcludedExtensions(final String repositoryName, final String directoryName, final String search, final String[] searchExtensions, final String[] searchExcludedExtensions);
    List<File> getFilesForExtensions(final String repositoryName, final String directoryName, final String[] searchExtensions);
    List<File> getFilesForExcludedExtensions(final String repositoryName, final String directoryName, final String[] searchExcludedExtensions);
    List<File> getFilesForExtensionsAndExcludedExtensions(final String repositoryName, final String directoryName, final String[] searchExtensions, final String[] searchExcludedExtensions);
    void moveFile(final String repositoryName, final String oldDirectoryName, final String newDirectoryName);

}