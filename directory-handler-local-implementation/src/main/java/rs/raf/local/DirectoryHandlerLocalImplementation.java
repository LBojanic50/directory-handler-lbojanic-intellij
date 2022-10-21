package rs.raf.local;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import org.apache.commons.io.FileUtils;
import rs.raf.specification.DirectoryHandlerLocalSpecification;

public class DirectoryHandlerLocalImplementation implements DirectoryHandlerLocalSpecification {
	private static String workingDirectory = System.getProperty("user.dir");

	@Override
	public void createLocalDirectory() {
		File file = new File(workingDirectory + "\\src\\customDirectory");
		try {
			if (file.mkdir()) {
				createDefaultConfig();
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
		File file = new File(String.format(workingDirectory + "\\src\\directoryName\\customFile.%s", fileExtension));
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
	public void createLocalRepository() {
		File file = new File(workingDirectory + "\\src\\customRepository");
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
	public void createLocalRepository(String repositoryName) {
		File file = new File(workingDirectory + "\\src\\%s", repositoryName);
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
	public long getFolderSize() throws IOException {
		Path path = Paths.get(workingDirectory + "\\src\\customDirectory");
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
		FileInputStream fileInputStream = new FileInputStream(
				workingDirectory + "\\src\\main\\resources\\config.properties");
		InputStream inputStream = fileInputStream;
		properties.load(inputStream);
		return properties;
	}

	@Override
	public void createDefaultConfig() throws IOException {
		File file = new File(workingDirectory + "\\src\\customDirectory\\config.properties");
		PrintWriter writer = new PrintWriter(file, "UTF-8");
		writer.println("maxRepositorySize = 1073741824");
		writer.println("maxFileCount = 20");
		writer.println("excludedExtensions = none");
		writer.close();
	}

	@Override
	public void createConfig(String maxRepositorySize, int maxFileCount, String[] excludedExtensions)
			throws IOException {
		File file = new File(workingDirectory + "\\src\\customDirectory\\config.properties");
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