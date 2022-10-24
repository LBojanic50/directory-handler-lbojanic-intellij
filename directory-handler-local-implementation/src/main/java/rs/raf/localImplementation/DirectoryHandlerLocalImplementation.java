package rs.raf.localImplementation;

import org.apache.commons.io.FileUtils;

import rs.raf.model.DirectoryHandlerConfig;
import rs.raf.specification.IDirectoryHandlerSpecification;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DirectoryHandlerLocalImplementation implements IDirectoryHandlerSpecification {
	//private static final String workingDirectory = System.getProperty("user.dir") + "\\directory-handler-project";
	//private static final String workingDirectory = "D:\\JavaProjects\\directory-handler-lbojanic-intellij\\directory-handler-project";
	//private static final String workingDirectory = System.getProperty("user.dir") + "\\directory-handler-project";
	public static Path workingDirectory = Paths.get("directory-handler-project");
	public static Path homeDirectory = Paths.get(System.getProperty("user.home"));
	public static String defaultRepositoryName = "defaultRepository";
	public static String defaultDirectoryName = "defaultDirectory";
	public static String defaultFileName = "defaultFile";
	public static String propertiesFileName = "config.properties";
	@Override
	public void createRepository() throws IOException {
		File file = workingDirectory.resolve(defaultRepositoryName).toFile();
		try {
			if (file.mkdir()) {
				System.out.println("Repository Created");
				createDefaultConfig(defaultRepositoryName);

			}
			else {
				System.out.println("Repository Not Created");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void createRepository(final String repositoryName) throws IOException {
		File file = workingDirectory.resolve(repositoryName).toFile();
		try {
			if (file.mkdir()) {
				System.out.println("Repository Created");
				createDefaultConfig(repositoryName);

			}
			else {
				System.out.println("Repository Not Created");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void createRepository(final DirectoryHandlerConfig directoryHandlerConfig) throws IOException {
		File file = workingDirectory.resolve(defaultRepositoryName).toFile();
		try {
			if (file.mkdir()) {
				System.out.println("Repository Created");
				createConfig(defaultRepositoryName, directoryHandlerConfig);

			}
			else {
				System.out.println("Repository Not Created");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void createRepository(final String repositoryName, DirectoryHandlerConfig directoryHandlerConfig) throws IOException {
		File file = workingDirectory.resolve(defaultRepositoryName).toFile();
		try {
			if (file.mkdir()) {
				System.out.println("Repository Created");
				createConfig(defaultRepositoryName, directoryHandlerConfig);

			}
			else {
				System.out.println("Repository Not Created");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void createDirectory(final String repositoryName) {
		File file = workingDirectory.resolve(defaultRepositoryName).resolve(defaultDirectoryName).toFile();
		try {
			if (file.mkdir()) {
				System.out.println("Directory Created");
			}
			else {
				System.out.println("Directory Not Created");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void createDirectory(final String repositoryName, final String directoryName) {
		File file = workingDirectory.resolve(repositoryName).resolve(directoryName).toFile();
		try {
			if (file.mkdir()) {
				System.out.println("Directory Created");
			}
			else {
				System.out.println("Directory Not Created");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void createFile(final String repositoryName, final String directoryName, final String fileExtension) {
		File file = workingDirectory.resolve(repositoryName).resolve(directoryName).resolve(defaultFileName).resolve(".").resolve(fileExtension).toFile();
		try {
			if (file.createNewFile()) {
				System.out.println("File Created");
			}
			else {
				System.out.println("File Not Created");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void createFile(final String repositoryName, final String directoryName, final String fileName, final String fileExtension) {
		File file = workingDirectory.resolve(repositoryName).resolve(directoryName).resolve(fileName + "." + fileExtension).toFile();
		try {
			if (file.createNewFile()) {
				System.out.println("File Created");
			}
			else {
				System.out.println("File Not Created");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void updateConfig(final String repositoryName, final DirectoryHandlerConfig directoryHandlerConfig) throws IOException {
		Properties properties = new Properties();
		FileInputStream fileInputStream = new FileInputStream(workingDirectory.resolve(repositoryName).resolve(propertiesFileName).toAbsolutePath().toString());
		InputStream inputStream = fileInputStream;
		properties.load(inputStream);
		properties.setProperty("maxRepositorySize", directoryHandlerConfig.getMaxRepositorySize());
		properties.setProperty("maxFileCount", Integer.toString(directoryHandlerConfig.getMaxFileCount()));
		properties.setProperty("excludedExtensions", arrayToString(directoryHandlerConfig.getExcludedExtensions()));
		OutputStream outputStream = new FileOutputStream(workingDirectory.resolve(repositoryName).resolve(propertiesFileName).toAbsolutePath().toString());
		properties.store(outputStream, "updatedConfig");
	}
	@Override
	public long getDirectorySize(final String repositoryName, final String directoryName) throws FileNotFoundException, IOException {
		return FileUtils.sizeOfDirectory(workingDirectory.resolve(repositoryName).resolve(directoryName).toFile());
	}
	@Override
	public long getFileSize(final String repositoryName, final String directoryName, final String fileName) throws FileNotFoundException, IOException {
		return FileUtils.sizeOf(workingDirectory.resolve(repositoryName).resolve(directoryName).resolve(fileName).toFile());
	}
	@Override
	public void createDefaultConfig(final String repositoryName) throws IOException {
		File file = workingDirectory.resolve(repositoryName).resolve(propertiesFileName).toFile();
		file.createNewFile();
		updateConfig(repositoryName, new DirectoryHandlerConfig());
	}
	@Override
	public void createConfig(final String repositoryName, final DirectoryHandlerConfig directoryHandlerConfig) throws IOException {
		File file = workingDirectory.resolve(repositoryName).resolve(propertiesFileName).toFile();
		file.createNewFile();
		updateConfig(repositoryName, directoryHandlerConfig);
	}
	@Override
	public Properties getProperties(final String repositoryName) throws IOException {
		Properties properties = new Properties();
		FileInputStream fileInputStream = new FileInputStream(workingDirectory.resolve(repositoryName).resolve(propertiesFileName).toFile());
		InputStream inputStream = fileInputStream;
		properties.load(inputStream);
		return properties;
	}
	@Override
	public final String arrayToString(final String[] array) {
		String arrayString = "";
		for (int i = 0; i < array.length; i++) {
			arrayString += array[i] + ",";
		}
		return arrayString.substring(0, arrayString.length() - 1);
	}
	@Override
	public void writeToFile(final String repositoryName, final String directoryName, final String fileName, final String textToWrite) throws IOException {
		FileUtils.writeStringToFile(workingDirectory.resolve(repositoryName).resolve(directoryName).resolve(fileName).toFile(), textToWrite, "UTF-8");
	}
	@Override
	public void deleteFile(final String repositoryName, final String directoryName, final String fileName) throws IOException {
		workingDirectory.resolve(repositoryName).resolve(directoryName).resolve(fileName).toFile().delete();
	}

	@Override
	public void downloadFile(final String repositoryName, final String directoryName, final String fileName, final boolean overwrite) throws IOException {
		if(overwrite){
			Files.copy(workingDirectory.resolve(repositoryName).resolve(directoryName).resolve(fileName), homeDirectory.resolve("Downloads").resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
		}
		else{
			try {
				Files.copy(workingDirectory.resolve(repositoryName).resolve(directoryName).resolve(fileName), homeDirectory.resolve("Downloads").resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
			}
			catch (FileAlreadyExistsException e){
				System.out.println("File already exists");
			}
		}
	}

	@Override
	public void downloadFile(final String repositoryName, final String directoryName, final String fileName, final String downloadPathString, final boolean overwrite) throws IOException {
		Path downloadPath = Paths.get(downloadPathString);
		if(overwrite){
			Files.copy(workingDirectory.resolve(repositoryName).resolve(directoryName).resolve(fileName), downloadPath, StandardCopyOption.REPLACE_EXISTING);
		}
		else{
			try {
				Files.copy(workingDirectory.resolve(repositoryName).resolve(directoryName).resolve(fileName), downloadPath, StandardCopyOption.REPLACE_EXISTING);
			}
			catch (FileAlreadyExistsException e){
				System.out.println("File already exists");
			}
		}

	}

	@Override
	public void moveOrRenameFile(final String repositoryName, final String directoryName, final String fileName, final String newName) throws IOException {
		try {
			Files.move(workingDirectory.resolve(repositoryName).resolve(directoryName).resolve(fileName), workingDirectory.resolve(repositoryName).resolve(directoryName).resolve(fileName).resolveSibling(newName));
		}
		catch (FileAlreadyExistsException e){
			System.out.println("File already exists");
		}
	}
	@Override
	public List<File> getFileList(final String repositoryName, final String directoryName) {
		List<File> fileList = new ArrayList<>();
		File directory = workingDirectory.resolve(repositoryName).resolve(directoryName).toFile();
		if(directory.listFiles() != null) {
			for (File file : directory.listFiles()) {
				fileList.add(file);
			}
		}
		return fileList;
	}
	@Override
	public int getFileCount(final String repositoryName, final String directoryName) {
		int fileCount = 0;
		File directory = workingDirectory.resolve(repositoryName).resolve(directoryName).toFile();
		if(directory.listFiles() != null) {
			for (File file : directory.listFiles()) {
				fileCount++;
			}
		}
		return fileCount;
	}
	@Override
	public List<File> getFilesForSearchName(final String repositoryName, final String directoryName, final String search) {
		List<File> fileList = new ArrayList<>();
		File directoryToSearch = workingDirectory.resolve(repositoryName).resolve(directoryName).toFile();
		File[] directoryToSearchList = directoryToSearch.listFiles();
		for(File file : directoryToSearchList){
			if(file.getName().toLowerCase().contains(search.toLowerCase())){
				fileList.add(file);
			}
		}
		return fileList;
	}
	@Override
	public List<File> getFilesForSearchNameAndExtensions(final String repositoryName, final String directoryName, final String search, final String[] searchExtensions) {
		List<File> fileList = new ArrayList<>();
		File directoryToSearch = workingDirectory.resolve(repositoryName).resolve(directoryName).toFile();
		File[] directoryToSearchList = directoryToSearch.listFiles();
		for(File file : directoryToSearchList){
			for(String extension : searchExtensions){
				if(file.getName().toLowerCase().endsWith(extension.toLowerCase())){
					if(file.getName().toLowerCase().contains(search.toLowerCase())){
						fileList.add(file);
					}
				}
			}
		}
		return fileList;
	}
	@Override
	public List<File> getFilesForSearchNameAndExcludedExtensions(final String repositoryName, final String directoryName, final String search, final String[] searchExcludedExtensions) {
		List<File> fileList = new ArrayList<>();
		File directoryToSearch = workingDirectory.resolve(repositoryName).resolve(directoryName).toFile();
		File[] directoryToSearchList = directoryToSearch.listFiles();
		for(File file : directoryToSearchList){
			for(String excludedExtension : searchExcludedExtensions){
				if(!file.getName().toLowerCase().endsWith(excludedExtension.toLowerCase())){
					if(file.getName().toLowerCase().contains(search.toLowerCase())){
						fileList.add(file);
					}
				}
			}
		}
		return fileList;
	}
	@Override
	public List<File> getFilesForSearchNameAndExtensionsAndExcludedExtensions(final String repositoryName, final String directoryName, final String search, final String[] searchExtensions, final String[] searchExcludedExtensions) {
		List<File> fileList = new ArrayList<>();
		File directoryToSearch = workingDirectory.resolve(repositoryName).resolve(directoryName).toFile();
		File[] directoryToSearchList = directoryToSearch.listFiles();
		for(File file : directoryToSearchList){
			for(String excludedExtension : searchExcludedExtensions){
				if(!file.getName().toLowerCase().endsWith(excludedExtension.toLowerCase())){
					for(String extension : searchExtensions){
						if(file.getName().toLowerCase().endsWith(extension.toLowerCase())){
							if(file.getName().toLowerCase().contains(search.toLowerCase())){
								fileList.add(file);
							}
						}
					}
				}
			}
		}
		return fileList;
	}
	@Override
	public List<File> getFilesForExtensions(final String repositoryName, final String directoryName, final String[] searchExtensions) {
		List<File> fileList = new ArrayList<>();
		File directoryToSearch = workingDirectory.resolve(repositoryName).resolve(directoryName).toFile();
		File[] directoryToSearchList = directoryToSearch.listFiles();
		for(File file : directoryToSearchList){
			for(String extension : searchExtensions){
				if(file.getName().toLowerCase().endsWith(extension.toLowerCase())){
					fileList.add(file);
				}
			}
		}
		return fileList;
	}
	@Override
	public List<File> getFilesForExcludedExtensions(final String repositoryName, final String directoryName, final String[] searchExcludedExtensions) {
		List<File> fileList = new ArrayList<>();
		File directoryToSearch = workingDirectory.resolve(repositoryName).resolve(directoryName).toFile();
		File[] directoryToSearchList = directoryToSearch.listFiles();
		for(File file : directoryToSearchList){
			for(String excludedExtension : searchExcludedExtensions){
				if(!file.getName().toLowerCase().endsWith(excludedExtension.toLowerCase())){
					fileList.add(file);
				}
			}
		}
		return fileList;
	}
	@Override
	public List<File> getFilesForExtensionsAndExcludedExtensions(final String repositoryName, final String directoryName, final String[] searchExtensions, final String[] searchExcludedExtensions) {
		List<File> fileList = new ArrayList<>();
		File directoryToSearch = workingDirectory.resolve(repositoryName).resolve(directoryName).toFile();
		File[] directoryToSearchList = directoryToSearch.listFiles();
		for(File file : directoryToSearchList){
			for(String excludedExtension : searchExcludedExtensions){
				if(!file.getName().toLowerCase().endsWith(excludedExtension.toLowerCase())){
					for(String extension : searchExtensions){
						if(file.getName().toLowerCase().endsWith(extension.toLowerCase())){
							fileList.add(file);
						}
					}
				}
			}
		}
		return fileList;
	}
}