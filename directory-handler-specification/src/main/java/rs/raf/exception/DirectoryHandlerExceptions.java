package rs.raf.exception;

import java.nio.file.InvalidPathException;

public class DirectoryHandlerExceptions {
    public static class MaxFileCountExceededException extends Exception{
        public MaxFileCountExceededException(final String filePathString){
            super(String.format("Max file count for file/directory %s exceeded!", filePathString));
        }
    }
    public static class MaxRepositorySizeExceededException extends Exception{
        public MaxRepositorySizeExceededException(final String repositoryName){
            super(String.format("Max size for repository %s exceeded!", repositoryName));
        }
    }
    public static class FileExtensionException extends Exception{
        public FileExtensionException(final String fileName){
            super(String.format("Cannot create file with the extension %s due to its exclusion in the configuration file!", fileName.substring(fileName.lastIndexOf("."))));
        }
    }
    public static class BadPathException extends Exception{
        public BadPathException(final String filePathString){
            super(String.format("%s is not a valid path!", filePathString));
        }
    }
    public static class NoFileAtPathException extends Exception{
        public NoFileAtPathException(final String filePathString){
            super(String.format("Not file at path %s!", filePathString));
        }
    }
}