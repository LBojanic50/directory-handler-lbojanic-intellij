package rs.raf.main;

import rs.raf.config.ConfigUpdateTypes;
import rs.raf.model.SortingType;
import rs.raf.specification.DirectoryHandlerManager;
import rs.raf.specification.IDirectoryHandlerSpecification;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import rs.raf.exception.DirectoryHandlerExceptions.*;
public class Main {
	private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	public static void main(String[] args) throws Exception {
		if(args[0].equals("local")) {
			try{
				Class.forName("rs.raf.localImplementation.DirectoryHandlerLocalImplementation");
			}
			catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
		else if(args[0].equals("drive")) {
			try{
				Class.forName("rs.raf.googleDriveImplementation.DirectoryHandlerGoogleDriveImplementation");
			}
			catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
		IDirectoryHandlerSpecification directoryHandler = DirectoryHandlerManager.getDirectoryHandler();
		while(true){
			String command = reader.readLine();
			if(command.startsWith("copyFiles ")){
				directoryHandler.copyFiles("customRep/myDir1/test.txt", "customRep/myDir2", false);
				System.out.println("Done");
			}
			if(command.startsWith("createConfig ")) {
				System.out.println("test");
			}
			if(command.startsWith("createDirectories ")){
				String directoryPathString = command.split(" ")[1];
				directoryHandler.createDirectories(directoryPathString);
			}
			if(command.startsWith("createFiles ")){
				String filePathString = command.split(" ")[1];
				directoryHandler.createFiles(filePathString);
			}
			if(command.startsWith("createRepositories ")){
				String[] createRepositoryCommand = command.split(" ");
				String repositoryName;
				String configString;
				if(createRepositoryCommand.length == 2){
					repositoryName = createRepositoryCommand[1];
					directoryHandler.createRepositories(repositoryName);
				}
				else if(createRepositoryCommand.length == 3){
					repositoryName = createRepositoryCommand[1];
					configString = createRepositoryCommand[2];
					directoryHandler.createRepositories(repositoryName, configString);
				}
				else{
					throw new InvalidCommandException(command);
				}
			}
			if(command.startsWith("deleteFiles ")){
				System.out.println("test");
			}
			if(command.startsWith("downloadFiles ")){
				String filePathString = command.split(" ")[1];
				directoryHandler.downloadFiles(filePathString, null, false);
			}
			if(command.startsWith("getConfig ")){
				String filePathString = command.split(" ")[1];
				directoryHandler.downloadFiles(filePathString, null, false);
			}
			if(command.startsWith("getDirectorySize ")){
				String directoryPathString = command.split(" ")[1];
				System.out.println(directoryHandler.getDirectorySize(directoryPathString));
			}
			if(command.startsWith("getFileCount ")){
				String filePathString = command.split(" ")[1];
				System.out.println(directoryHandler.getFileSize(filePathString));
			}
			if(command.startsWith("getFileListInDirectory ")){
				String[] getFileListInDirectoryCommand = command.split(" ");
				directoryHandler.printFileList(directoryHandler.getFileListInDirectory(getFileListInDirectoryCommand[1], true, true, true, SortingType.NAME));
			}
			if(command.startsWith("getFileSize ")){
				String filePathString = command.split(" ")[1];
				System.out.println(directoryHandler.getFileSize(filePathString));
			}
			if(command.startsWith("getFilesForDateRange ")){
				String filePathString = command.split(" ")[1];
				System.out.println(directoryHandler.getFileSize(filePathString));
			}
			if(command.startsWith("getFilesForExcludedExtensions ")){
				String filePathString = command.split(" ")[1];
				System.out.println(directoryHandler.getFileSize(filePathString));
			}
			if(command.startsWith("getFilesForExtensions ")){
				String filePathString = command.split(" ")[1];
				System.out.println(directoryHandler.getFileSize(filePathString));
			}
			if(command.startsWith("getFilesForExtensionsAndExcludedExtensions ")){
				String filePathString = command.split(" ")[1];
				System.out.println(directoryHandler.getFileSize(filePathString));
			}
			if(command.startsWith("getFilesForSearchName ")){
				String filePathString = command.split(" ")[1];
				System.out.println(directoryHandler.getFileSize(filePathString));
			}
			if(command.startsWith("getFilesForSearchNameAndExcludedExtensions ")){
				String filePathString = command.split(" ")[1];
				System.out.println(directoryHandler.getFileSize(filePathString));
			}
			if(command.startsWith("getFilesForSearchNameAndExtensions ")){
				String filePathString = command.split(" ")[1];
				System.out.println(directoryHandler.getFileSize(filePathString));
			}
			if(command.startsWith("getFilesForSearchNameAndExtensionsAndExcludedExtensions ")){
				String filePathString = command.split(" ")[1];
				System.out.println(directoryHandler.getFileSize(filePathString));
			}
			if(command.startsWith("getFilesWithNames ")){
				String filePathString = command.split(" ")[1];
				System.out.println(directoryHandler.getFileSize(filePathString));
			}
			if(command.startsWith("moveFiles ")){
				String filePathString = command.split(" ")[1];
				System.out.println(directoryHandler.getFileSize(filePathString));
			}
			if(command.startsWith("printConfig ")){
				String filePathString = command.split(" ")[1];
				System.out.println(directoryHandler.getFileSize(filePathString));
			}
			if(command.startsWith("printFileList ")){
				String filePathString = command.split(" ")[1];
				System.out.println(directoryHandler.getFileSize(filePathString));
			}
			if(command.startsWith("renameFile ")){
				String[] renameFileCommand = command.split(" ");
				if(renameFileCommand.length  == 3){
					directoryHandler.renameFile(renameFileCommand[1], renameFileCommand[2]);
				}
				else{
					throw new InvalidCommandException(command);
				}
			}
			if(command.startsWith("updateConfig ")){
				String[] updateConfigCommand = command.split(" ");
				String repositoryName;
				String configString;
				ConfigUpdateTypes configUpdateType;
				if(updateConfigCommand.length == 4){
					repositoryName = updateConfigCommand[1];
					configString = updateConfigCommand[2];
					if(updateConfigCommand[3].equals("replace")){
						configUpdateType = ConfigUpdateTypes.REPLACE;
					}
					else if(updateConfigCommand[3].equals("add")){
						configUpdateType = ConfigUpdateTypes.ADD;
					}
					else{
						throw new InvalidCommandException(command);
					}
					directoryHandler.updateConfig(repositoryName, configString, configUpdateType);
				}
				else{
					throw new InvalidCommandException(command);
				}
			}
			if(command.startsWith("writeToFile ")){
				String filePathString = command.split(" ")[1];
				System.out.println(directoryHandler.getFileSize(filePathString));
			}
			if(command.equals("exit")){
				System.exit(0);
			}
		}
	}
}