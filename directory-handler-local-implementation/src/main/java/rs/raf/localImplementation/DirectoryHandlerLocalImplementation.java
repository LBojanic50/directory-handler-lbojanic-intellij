package rs.raf.localImplementation;

import org.apache.commons.io.FileUtils;
import rs.raf.specification.DirectoryHandlerLocalSpecification;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class DirectoryHandlerLocalImplementation implements DirectoryHandlerLocalSpecification {
	private static String workingDirectory = System.getProperty("user.dir") + "\\directory-handler-project";

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
	public void createLocalDirectory(String directoryName) {
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
	public void createLocalFile(String directoryName, String fileExtension) {
		File file = new File(String.format(workingDirectory + "\\src\\directoryName\\defaultFile.%s", fileExtension));
		try {
			if (file.createNewFile()) {
				System.out.println("Directory Created");
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
	public void createLocalFile(String directoryName, String fileName, String fileExtension) {
		File file = new File(
				String.format(workingDirectory + "\\src\\%s\\%s.%s", directoryName, fileName, fileExtension));
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
	public void createLocalRepository() throws IOException {
		File file = new File(workingDirectory + "\\src\\defaultRepository");
		file.mkdir();
		createDefaultConfig("defaultRepository");
	}

	@Override
	public void createLocalRepository(String repositoryName) throws IOException {
		File file = new File(String.format(workingDirectory + "\\src\\%s", repositoryName));
		file.mkdir();
		createDefaultConfig(repositoryName);
	}

	@Override
	public void createLocalRepository(String repositoryName, String maxRepositorySize, int maxFileCount, String[] excludedExtensions) throws IOException {
		File file = new File(String.format(workingDirectory + "\\src\\%s", repositoryName));
		file.mkdir();
		createConfig(repositoryName, maxRepositorySize, maxFileCount, excludedExtensions);
	}

	@Override
	public long getFolderSize() throws IOException {
		Path path = Paths.get(workingDirectory + "\\src\\defaultRepository");
		File file = path.toFile();
		/*
		 * BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
		 * System.out.println(bufferedReader.readLine());
		 */
		return FileUtils.sizeOfDirectory(file);
	}

	@Override
	public Properties getProperties() throws IOException {
		Properties properties = new Properties();
		FileInputStream fileInputStream = new FileInputStream(workingDirectory + "\\src\\main\\resources\\config.properties");
		InputStream inputStream = fileInputStream;
		properties.load(inputStream);
		return properties;
	}

	@Override
	public void createDefaultConfig(String directoryName) throws IOException {
		File file = new File(String.format(workingDirectory + "\\src\\%s\\config.properties", directoryName));
		PrintWriter writer = new PrintWriter(file, "UTF-8");
		writer.println("maxRepositorySize = 1073741824");
		writer.println("maxFileCount = 20");
		writer.print("excludedExtensions = none");
		writer.close();
	}

	@Override
	public void createConfig(String directoryName, String maxRepositorySize, int maxFileCount, String[] excludedExtensions)throws IOException {
		File file = new File(String.format(workingDirectory + "\\src\\%s\\config.properties", directoryName));
		PrintWriter writer = new PrintWriter(file, "UTF-8");
		writer.println(String.format("maxRepositorySize = %s", maxRepositorySize));
		writer.println(String.format("maxFileCount = %s", maxFileCount));
		writer.print(String.format("excludedExtensions = %s", arrayToString(excludedExtensions)));
		writer.close();
	}
	@Override
	public String arrayToString(String[] array) {
		String arrayString = "";
		for (int i = 0; i < array.length; i++) {
			arrayString += array[i] + ",";
		}
		return arrayString.substring(0, arrayString.length() - 1);
	}
}