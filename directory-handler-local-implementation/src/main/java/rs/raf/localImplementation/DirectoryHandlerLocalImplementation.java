package rs.raf.localImplementation;

import org.apache.commons.io.FileUtils;
import rs.raf.model.DirectoryHandlerConfig;
import rs.raf.specification.IDirectoryHandlerSpecification;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DirectoryHandlerLocalImplementation implements IDirectoryHandlerSpecification {
	//private static String workingDirectory = System.getProperty("user.dir") + "\\directory-handler-project";
	//private static String workingDirectory = "D:\\JavaProjects\\directory-handler-lbojanic-intellij\\directory-handler-project";
	private static String workingDirectory = System.getProperty("user.dir") + "\\directory-handler-project";

	@Override
	public void createRepository() throws IOException {

	}

	@Override
	public void createRepository(String repositoryName) throws IOException {

	}

	@Override
	public void createRepository(DirectoryHandlerConfig directoryHandlerConfig) throws IOException {

	}

	@Override
	public void createRepository(String repositoryName, DirectoryHandlerConfig directoryHandlerConfig) throws IOException {

	}

	@Override
	public void createDirectory(String repositoryName) {

	}

	@Override
	public void createDirectory(String repositoryName, String directoryName) {

	}

	@Override
	public void createFile(String repositoryName, String directoryName, String fileExtension) {

	}

	@Override
	public void createFile(String repositoryName, String directoryName, String fileName, String fileExtension) {

	}

	@Override
	public void updateConfig(String repositoryName, DirectoryHandlerConfig directoryHandlerConfig) throws IOException {

	}

	@Override
	public long getDirectorySize(String repositoryName, String directoryName) throws FileNotFoundException, IOException {
		return 0;
	}

	@Override
	public long getFileSize(String repositoryName, String directoryName, String fileName) throws FileNotFoundException, IOException {
		return 0;
	}

	@Override
	public Properties getProperties(String repositoryName, String directoryName) throws IOException {
		return null;
	}

	@Override
	public void createDefaultConfig(String repositoryName, String directoryName) throws IOException {

	}

	@Override
	public void createConfig(String repositoryName, String directoryName, DirectoryHandlerConfig directoryHandlerConfig) throws IOException {

	}

	@Override
	public String arrayToString(String repositoryName, String[] array) {
		return null;
	}

	@Override
	public void writeToFile(String repositoryName, String directoryName, String fileName, String textToWrite) throws IOException {

	}

	@Override
	public void deleteFile(String repositoryName, String directoryName, String fileName) throws IOException {

	}

	@Override
	public void renameFile(String repositoryName, String directoryName, String fileName, String newName) throws IOException {

	}

	@Override
	public List<String> getFileList(String repositoryName, String directoryName) {
		return null;
	}

	@Override
	public int getFileCount(String repositoryName, String directoryName) {
		return 0;
	}

	@Override
	public List<File> getFilesForSearchName(String repositoryName, String directoryName, String search) {
		return null;
	}

	@Override
	public List<File> getFilesForSearchNameAndExtensions(String repositoryName, String directoryName, String search, String[] searchExtensions) {
		return null;
	}

	@Override
	public List<File> getFilesForSearchNameAndExcludedExtensions(String repositoryName, String directoryName, String search, String[] searchExcludedExtensions) {
		return null;
	}

	@Override
	public List<File> getFilesForSearchNameAndExtensionsAndExcludedExtensions(String repositoryName, String directoryName, String search, String[] searchExtensions, String[] searchExcludedExtensions) {
		return null;
	}

	@Override
	public List<File> getFilesForExtensions(String repositoryName, String directoryName, String[] searchExtensions) {
		return null;
	}

	@Override
	public List<File> getFilesForExcludedExtensions(String repositoryName, String directoryName, String[] searchExcludedExtensions) {
		return null;
	}

	@Override
	public List<File> getFilesForExtensionsAndExcludedExtensions(String repositoryName, String directoryName, String[] searchExtensions, String[] searchExcludedExtensions) {
		return null;
	}

	@Override
	public void moveFile(String repositoryName, String oldDirectoryName, String newDirectoryName) {

	}
}