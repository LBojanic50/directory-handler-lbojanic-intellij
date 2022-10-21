package rs.raf.specification;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

public interface DirectoryHandlerLocalSpecification {
	/**
     * Creates a local repository with the default name;
     */
	void createLocalRepository();
	/**
     * Creates a local repository with the specified name;
     * @param repositoryName name of the repository to create
     */
	void createLocalRepository(String repositoryName);
    /**
     * Creates a local directory with the default name;
     */
    void createLocalDirectory();
    /**
     * Creates a local directory with the specified name;
     * @param directoryName name of directory to create
     */
    void createLocalDirectory(String directoryName);
    /**
     * Creates a local file with the default name;
     * @param directoryName name of directory to create the file in
     * @param fileExtension file extension
     */
    void createLocalFile(String directoryName, String fileExtension);
    /**
     * Creates a local directory with the specified name;
     * @param directoryName name of directory to create the file in
     * @param fileName name of file to create
     * @param fileExtension file extension
     */
    void createLocalFile(String directoryName, String fileName, String fileExtension);
    
    long getFolderSize() throws FileNotFoundException, IOException;
	Properties getProperties() throws IOException;
	void createDefaultConfig() throws IOException;
	void createConfig(String maxRepositorySize, int maxFileCount, String[] excludedExtensions) throws IOException;
	String arrayToString(String[] array);
}