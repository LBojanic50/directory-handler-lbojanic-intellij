package rs.raf.specification;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public interface DirectoryHandlerLocalSpecification {
	/**
     * Creates a local repository with the default name and config;
     */
	void createLocalRepository() throws IOException;
	/**
     * Creates a local repository with the specified name and default config;
     * @param repositoryName name of the repository to create
     */
	void createLocalRepository(final String repositoryName) throws IOException;
    /**
     * Creates a local repository with the specified name and default config;
     * @param repositoryName name of the repository to create
     * @param maxRepositorySize max repository size (stored in config file, 1GB by default)
     * @param maxFileCount max file count in repository (stored in config file, 20 by default)
     * @param excludedExtensions string array of extensions that can't be stored in repository (stored in config file, "none" by default)
     */
    void createLocalRepository(final String repositoryName, String maxRepositorySize, int maxFileCount, String[] excludedExtensions) throws IOException;
    /**
     * Creates a local directory with the default name;
     */
    void createLocalDirectory();
    /**
     * Creates a local directory with the specified name;
     * @param directoryName name of directory to create
     */
    void createLocalDirectory(final String directoryName);
    /**
     * Creates a local file with the default name;
     * @param directoryName name of directory to create the file in
     * @param fileExtension file extension
     */
    void createLocalFile(final String directoryName, final String fileExtension);
    /**
     * Creates a local directory with the specified name;
     * @param directoryName name of directory to create the file in
     * @param fileName name of file to create
     * @param fileExtension file extension
     */
    void createLocalFile(final String directoryName, final String fileName, final String fileExtension);
    
    long getFolderSize(final String directoryName) throws FileNotFoundException, IOException;
    long getFileSize(final String directoryName, final String fileName) throws FileNotFoundException, IOException;
	Properties getProperties(final String directoryName) throws IOException;
	void createDefaultConfig(final String directoryName) throws IOException;
	void createConfig(final String directoryName, final String maxRepositorySize, final int maxFileCount, final String[] excludedExtensions) throws IOException;
	String arrayToString(final String[] array);
    void writeToFile(final String directoryName, final String fileName, final String textToWrite) throws IOException;
    void deleteFile(final String directoryName, final String fileName) throws IOException;
    void renameFile(final String directoryName, final String fileName, final String newName) throws IOException;
}