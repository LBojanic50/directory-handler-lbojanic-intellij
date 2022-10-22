package rs.raf.localImplementation;

import org.apache.commons.io.FileUtils;
import rs.raf.specification.DirectoryHandlerLocalSpecification;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DirectoryHandlerLocalImplementation implements DirectoryHandlerLocalSpecification {
	//private static String workingDirectory = System.getProperty("user.dir") + "\\directory-handler-project";
	//private static String workingDirectory = "D:\\JavaProjects\\directory-handler-lbojanic-intellij\\directory-handler-project";

	@Override
	public void createLocalDirectory() {
		File file = new File(workingDirectory + "\\src\\defaultDirectory");
		try {
			if (file.mkdir()) {
				createDefaultConfig("defaultDirectory");
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
	public void createLocalDirectory(final String directoryName) {
		File file = new File(String.format(workingDirectory + "\\src\\%s", directoryName));
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
	public void createLocalFile(final String repositoryName, final String directoryName, final String fileExtension) {
		File file = new File(String.format(workingDirectory + "\\src\\directoryName\\defaultFile.%s", fileExtension));
		try {
			if(getFileCount(directoryName) <= Integer.valueOf(getProperties(repositoryName).getProperty("maxFileCount"))){
				if (file.createNewFile()) {
					System.out.println("Directory Created");
				}
				else {
					System.out.println("File Not Created");
				}
			}
			else {
				System.out.println("File count limit exceeded");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void createLocalFile(final String repositoryName, final String directoryName, final String fileName, final String fileExtension) {
		File file = new File(String.format(workingDirectory + "\\src\\%s\\%s.%s", directoryName, fileName, fileExtension));
		try {
			if(getFileCount(directoryName) <= Integer.valueOf(getProperties(repositoryName).getProperty("maxFileCount"))){
				if (file.createNewFile()) {
					System.out.println("Directory Created");
				}
				else {
					System.out.println("File Not Created");
				}
			}
			else {
				System.out.println("File count limit exceeded");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void createLocalRepository() throws IOException {
		File file = new File(workingDirectory + "\\src\\defaultRepository");
		file.mkdir();
		createDefaultConfig("defaultRepository");
	}

	@Override
	public void createLocalRepository(final String repositoryName) throws IOException {
		File file = new File(String.format(workingDirectory + "\\src\\%s", repositoryName));
		file.mkdir();
		createDefaultConfig(repositoryName);
	}

	@Override
	public void createLocalRepository(final String repositoryName, final String maxRepositorySize, final int maxFileCount, String[] excludedExtensions) throws IOException {
		File file = new File(String.format(workingDirectory + "\\src\\%s", repositoryName));
		file.mkdir();
		createConfig(repositoryName, maxRepositorySize, maxFileCount, excludedExtensions);
	}

	@Override
	public long getFolderSize(final String directoryName) throws IOException {
		Path path = Paths.get(String.format(workingDirectory + "\\src\\%s", directoryName));
		File file = path.toFile();
		return FileUtils.sizeOfDirectory(file);
	}

	@Override
	public long getFileSize(final String directoryName, final String fileName) throws FileNotFoundException, IOException {
		Path path = Paths.get(String.format(workingDirectory + "\\src\\%s\\%s", directoryName, fileName));
		File file = path.toFile();
		return FileUtils.sizeOf(file);
	}

	@Override
	public void renameFile(final String directoryName, final String fileName, final String newName) throws IOException {
		Path path = Paths.get(String.format(workingDirectory + "\\src\\%s\\%s", directoryName, fileName));
		Files.move(path, path.resolveSibling(newName));
	}

	@Override
	public Properties getProperties(final String directoryName) throws IOException {
		Properties properties = new Properties();
		FileInputStream fileInputStream = new FileInputStream(String.format(workingDirectory + "\\src\\%s\\config.properties", directoryName));
		InputStream inputStream = fileInputStream;
		properties.load(inputStream);
		return properties;
	}

	@Override
	public void deleteFile(final String directoryName, final String fileName) throws IOException {
		Path path = Paths.get(String.format(workingDirectory + "\\src\\%s\\%s", directoryName, fileName));
		File file = path.toFile();
		file.delete();
	}

	@Override
	public List<String> getFileList(final String directoryName) {
		List<String> fileList = new ArrayList<>();
		File directory = new File(String.format(workingDirectory + "\\src\\%s", directoryName));
		if(directory.listFiles() != null) {
			for (File file : directory.listFiles()) {
				fileList.add(file.getName());
			}
		}
		return fileList;
	}

	@Override
	public int getFileCount(String directoryName) {
		int fileCount = 0;
		File directory = new File(String.format(workingDirectory + "\\src\\%s", directoryName));
		if(directory.listFiles() != null) {
			for (File file : directory.listFiles()) {
				fileCount++;
			}
		}
		return fileCount;
	}

	@Override
	public void createDefaultConfig(final String directoryName) throws IOException {
		File file = new File(String.format(workingDirectory + "\\src\\%s\\config.properties", directoryName));
		PrintWriter writer = new PrintWriter(file, "UTF-8");
		writer.println("maxRepositorySize = 1073741824");
		writer.println("maxFileCount = 20");
		writer.print("excludedExtensions = none");
		writer.close();
	}

	@Override
	public void createConfig(final String directoryName, final String maxRepositorySize, final int maxFileCount, final String[] excludedExtensions)throws IOException {
		File file = new File(String.format(workingDirectory + "\\src\\%s\\config.properties", directoryName));
		PrintWriter writer = new PrintWriter(file, "UTF-8");
		writer.println(String.format("maxRepositorySize = %s", maxRepositorySize));
		writer.println(String.format("maxFileCount = %s", maxFileCount));
		writer.print(String.format("excludedExtensions = %s", arrayToString(excludedExtensions)));
		writer.close();
	}
	@Override
	public String arrayToString(final String[] array) {
		String arrayString = "";
		for (int i = 0; i < array.length; i++) {
			arrayString += array[i] + ",";
		}
		return arrayString.substring(0, arrayString.length() - 1);
	}

	@Override
	public void writeToFile(final String directoryName, final String fileName, final String textToWrite) throws IOException {
		Path path = Paths.get(String.format(workingDirectory + "\\src\\%s\\%s", directoryName, fileName));
		File file = path.toFile();
		FileUtils.writeStringToFile(file, textToWrite, "UTF-8");
	}
}