package rs.raf.main;

import rs.raf.specification.DirectoryHandlerManager;
import rs.raf.specification.IDirectoryHandlerSpecification;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {
	private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
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
		/*while(true){
			String command = reader.readLine();
			if(command.startsWith("createRepository")){
				String repositoryName = command.split(" ")[1];
				directoryHandler.createRepository(repositoryName);
			}
			if(command.startsWith("createDirectory")){
				String directoryPathString = command.split(" ")[1];
				directoryHandler.createDirectory(directoryPathString);
			}
			if(command.startsWith("createFile")){
				String filePathString = command.split(" ")[1];
				directoryHandler.createFile(filePathString);
			}
			if(command.startsWith("getDirectorySize")){
				String directoryPathString = command.split(" ")[1];
				System.out.println(directoryHandler.getDirectorySize(directoryPathString));
			}
			if(command.startsWith("getFileSize")){
				String filePathString = command.split(" ")[1];
				System.out.println(directoryHandler.getFileSize(filePathString));
			}
			if(command.equals("exit")){
				System.exit(0);
			}
		}*/
		System.out.println(directoryHandler.getFileCount("customRep"));
	}
}