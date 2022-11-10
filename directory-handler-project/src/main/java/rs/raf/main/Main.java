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
			String[] splitCommand = command.split(" ");
			if(splitCommand[0].equals("copyFiles")){
				if(splitCommand.length == 4){
					if(splitCommand[2].equals("default")){
						if(splitCommand[3].equals("true")){
							directoryHandler.copyFiles(splitCommand[1], null, true);
						}
						else if(splitCommand[3].equals("false")){
							directoryHandler.copyFiles(splitCommand[1], null, false);
						}
						else{
							throw new InvalidCommandException(command);
						}
					}
					else{
						if(splitCommand[3].equals("true")){
							directoryHandler.copyFiles(splitCommand[1], splitCommand[2], true);
						}
						else if(splitCommand[3].equals("false")){
							directoryHandler.copyFiles(splitCommand[1], splitCommand[2], false);
						}
						else{
							throw new InvalidCommandException(command);
						}
					}
				}
				System.out.println("Done");
			}
			else if(splitCommand[0].equals("createConfig")) {
				if(splitCommand.length == 3){
					if(splitCommand[2].equals("default")){
						directoryHandler.createConfig(splitCommand[1], null);
					}
					else{
						directoryHandler.createConfig(splitCommand[1], splitCommand[2]);
					}
				}
				else{
					throw new InvalidCommandException(command);
				}
			}
			else if(splitCommand[0].equals("createDirectories")){
				if(splitCommand.length == 2){
					directoryHandler.createDirectories(splitCommand[1]);
				}
				else{
					throw new InvalidCommandException(command);
				}
			}
			else if(splitCommand[0].equals("createFiles")){
				if(splitCommand.length == 2){
					directoryHandler.createFiles(splitCommand[1]);
				}
				else{
					throw new InvalidCommandException(command);
				}
			}
			else if(splitCommand[0].equals("createRepository")){
				if(splitCommand.length == 3){
					if(splitCommand[2].equals("default")){
						directoryHandler.createRepository(splitCommand[1], null);
					}
					else{
						directoryHandler.createRepository(splitCommand[1], splitCommand[2]);
					}
				}
				else{
					throw new InvalidCommandException(command);
				}
			}
			else if(splitCommand[0].equals("deleteFiles")){
				if(splitCommand.length == 2){
					directoryHandler.deleteFiles(splitCommand[1]);
				}
				else{
					throw new InvalidCommandException(command);
				}
			}
			else if(splitCommand[0].equals("downloadFiles")){
				if(splitCommand.length == 3){
					if(splitCommand[3].equals("true")){
						directoryHandler.downloadFiles(splitCommand[1], splitCommand[2], true);
					}
					else if(splitCommand[3].equals("false")){
						directoryHandler.downloadFiles(splitCommand[1], splitCommand[2], false);
					}
					else{
						throw new InvalidCommandException(command);
					}
				}
				else{
					throw new InvalidCommandException(command);
				}
			}
			else if(splitCommand[0].equals("getConfig")){
				if(splitCommand.length == 2){
					directoryHandler.printConfig(directoryHandler.getConfig(splitCommand[1]));
				}
				else{
					throw new InvalidCommandException(command);
				}
			}
			else if(splitCommand[0].equals("getDirectorySize")){
				if(splitCommand.length == 2){
					System.out.println(directoryHandler.getDirectorySize(splitCommand[1]));
				}
				else{
					throw new InvalidCommandException(command);
				}
			}
			else if(splitCommand[0].equals("getFileCount")){
				if(splitCommand.length == 2){
					System.out.println(directoryHandler.getFileCount(splitCommand[1]));
				}
				else{
					throw new InvalidCommandException(command);
				}
			}
			else if(splitCommand[0].equals("getFileListInDirectory")){
				if(splitCommand.length == 6){
					boolean recursive;
					boolean includeFiles;
					boolean includeDirectories;
					SortingType sortingType;
					if(splitCommand[2].equals("true")){
						recursive = true;
					}
					else if(splitCommand[2].equals("false")){
						recursive = false;
					}
					else{
						throw new InvalidCommandException(command);
					}
					if(splitCommand[3].equals("true")){
						includeFiles = true;
					}
					else if(splitCommand[3].equals("false")){
						includeFiles = false;
					}
					else{
						throw new InvalidCommandException(command);
					}
					if(splitCommand[4].equals("true")){
						includeDirectories = true;
					}
					else if(splitCommand[4].equals("false")){
						includeDirectories = false;
					}
					else{
						throw new InvalidCommandException(command);
					}
					if(splitCommand[5].equals("none")) {
						sortingType = SortingType.NONE;
					}
					else if(splitCommand[5].equals("name")) {
						sortingType = SortingType.NAME;
					}
					else if(splitCommand[5].equals("dateCreated")) {
						sortingType = SortingType.DATE_CREATED;
					}
					else if(splitCommand[5].equals("dateModified")) {
						sortingType = SortingType.DATE_MODIFIED;
					}
					else if(splitCommand[5].equals("size")) {
						sortingType = SortingType.SIZE;
					}
					else{
						throw new InvalidCommandException(command);
					}
					directoryHandler.printFileList(directoryHandler.getFileListInDirectory(splitCommand[1], recursive, includeFiles, includeDirectories, sortingType));
				}
				else{
					throw new InvalidCommandException(command);
				}

			}
			else if(splitCommand[0].equals("getFileSize")){
				if(splitCommand.length == 2){
					System.out.println(directoryHandler.getFileSize(splitCommand[1]));
				}
				else{
					throw new InvalidCommandException(command);
				}
			}
			else if(splitCommand[0].equals("getFilesForDateRange")){
				String filePathString = command.split(" ")[1];
				System.out.println(directoryHandler.getFileSize(filePathString));
			}
			else if( splitCommand[0].equals("getFilesForExcludedExtensions")){
				String filePathString = command.split(" ")[1];
				System.out.println(directoryHandler.getFileSize(filePathString));
			}
			else if(splitCommand[0].equals("getFilesForExtensions")){
				String filePathString = command.split(" ")[1];
				System.out.println(directoryHandler.getFileSize(filePathString));
			}
			else if(splitCommand[0].equals("getFilesForExtensionsAndExcludedExtensions")){
				String filePathString = command.split(" ")[1];
				System.out.println(directoryHandler.getFileSize(filePathString));
			}
			else if(splitCommand[0].equals("getFilesForSearchName")){
				String filePathString = command.split(" ")[1];
				System.out.println(directoryHandler.getFileSize(filePathString));
			}
			else if(splitCommand[0].equals("getFilesForSearchNameAndExcludedExtensions")){
				String filePathString = command.split(" ")[1];
				System.out.println(directoryHandler.getFileSize(filePathString));
			}
			else if(splitCommand[0].equals("getFilesForSearchNameAndExtensions")){
				String filePathString = command.split(" ")[1];
				System.out.println(directoryHandler.getFileSize(filePathString));
			}
			else if(splitCommand[0].equals("getFilesForSearchNameAndExtensionsAndExcludedExtensions")){
				String filePathString = command.split(" ")[1];
				System.out.println(directoryHandler.getFileSize(filePathString));
			}
			else if(splitCommand[0].equals("getFilesWithNames")){
				String filePathString = command.split(" ")[1];
				System.out.println(directoryHandler.getFileSize(filePathString));
			}
			else if(splitCommand[0].equals("moveFiles")){
				if(splitCommand.length == 4){
					if(splitCommand[2].equals("default")){
						if(splitCommand[3].equals("true")){
							directoryHandler.moveFiles(splitCommand[1], null, true);
						}
						else if(splitCommand[3].equals("false")){
							directoryHandler.moveFiles(splitCommand[1], null, false);
						}
						else{
							throw new InvalidCommandException(command);
						}
					}
					else{
						if(splitCommand[3].equals("true")){
							directoryHandler.moveFiles(splitCommand[1], splitCommand[2], true);
						}
						else if(splitCommand[3].equals("false")){
							directoryHandler.moveFiles(splitCommand[1], splitCommand[2], false);
						}
						else{
							throw new InvalidCommandException(command);
						}
					}
				}
				System.out.println("Done");
			}
			else if(splitCommand[0].equals("renameFile")){
				if(splitCommand.length  == 3){
					directoryHandler.renameFile(splitCommand[1], splitCommand[2]);
				}
				else{
					throw new InvalidCommandException(command);
				}
			}
			else if(splitCommand[0].equals("updateConfig")){
				String repositoryName;
				String configString;
				ConfigUpdateTypes configUpdateType;
				if(splitCommand.length == 4){
					repositoryName = splitCommand[1];
					configString = splitCommand[2];
					if(splitCommand[3].equals("replace")){
						configUpdateType = ConfigUpdateTypes.REPLACE;
					}
					else if(splitCommand[3].equals("add")){
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
			else if(splitCommand[0].equals("writeToFile")){
				if(splitCommand.length == 3){

				}
				else{
					throw new InvalidCommandException(command);
				}
			}
			else if(command.equals("exit")){
				System.exit(0);
			}
			else{
				throw new InvalidCommandException(command);
			}
		}
	}
}