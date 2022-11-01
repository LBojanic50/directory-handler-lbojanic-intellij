package rs.raf.core;

import org.apache.commons.io.FileUtils;
import rs.raf.localImplementation.DirectoryHandlerLocalImplementation;
import rs.raf.model.DirectoryHandlerConfig;
import rs.raf.model.DirectoryHandlerFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Properties;
import java.util.Random;

public class LocalAppCore {
	private static Properties properties;
	private static long folderSize = 0;
	private static DirectoryHandlerLocalImplementation directoryHandlerLocalImplementation = null;
	private static String[] extensions = new String[] {"txt", "json", "xml"};
	private static Random randomExtensionIndex = new Random();
	private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	public LocalAppCore() throws IOException {
		directoryHandlerLocalImplementation = new DirectoryHandlerLocalImplementation();
	}
	public void startApp() throws IOException {
		/*String initialSelection;
		String repositoryName;
		String configType;
		String maxRepositorySize;
		String[] excludedExtensions;
		System.out.println("1. Create a new repository");
		System.out.println("2. Select an existing repository");
		initialSelection = reader.readLine();

		if(initialSelection.equals("1")){
			System.out.print("Name of the new repository (leave blank to set the default name): ");
			repositoryName = reader.readLine();
			System.out.println("1. Create default config");
			System.out.println("2. Create custom config");
			configType = reader.readLine();
			if(configType.equals("1")){
				if(repositoryName.equals("")){
					directoryHandlerLocalImplementation.createRepository();
				}
				else{
					directoryHandlerLocalImplementation.createRepository(repositoryName);
				}
			}
			else if(configType.equals("2")){
				System.out.print("maxRepositorySize (number in bytes): ");
				maxRepositorySize = reader.readLine();
				System.out.print("excludedExtensions: (comma separated): ");
				excludedExtensions = reader.readLine().split(",");
				if(repositoryName.equals("")){
					directoryHandlerLocalImplementation.createRepository(new DirectoryHandlerConfig(maxRepositorySize, 30, excludedExtensions));
				}
				else{
					directoryHandlerLocalImplementation.createRepository(repositoryName, new DirectoryHandlerConfig(maxRepositorySize, 30, excludedExtensions));
				}
			}
		}
		else if(initialSelection.equals("2")){
			System.out.println("selected a repository");
		}*/
		directoryHandlerLocalImplementation.createRepository("testRep");
	}
}