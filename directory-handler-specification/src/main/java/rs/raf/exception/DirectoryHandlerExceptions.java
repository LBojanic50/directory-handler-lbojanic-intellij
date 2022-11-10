package rs.raf.exception;

import java.nio.file.InvalidPathException;
import java.util.List;

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
    public static class NonExistentRepositoryException extends Exception{
        public NonExistentRepositoryException(final String repositoryName){
            super(String.format("No repository with name %s!", repositoryName));
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
    public static class InvalidParameterException extends Exception{
        public InvalidParameterException(final String parameters){
            super(String.format("Invalid parameter(s) %s!", parameters));
        }
    }
    public static class InvalidConfigParametersException extends Exception{
        public InvalidConfigParametersException(final String configString){
            super(String.format("Invalid parameter(s) %s in config!", configString));
        }
    }
    public static class ValueInConfigCannotBeLessThanOneException extends Exception{
        public ValueInConfigCannotBeLessThanOneException(final String value){
            super(String.format("Value %s cannot be less than 1!", value));
        }
    }
    public static class InvalidCommandException extends Exception{
        public InvalidCommandException(final String command){
            super(String.format("Invalid command %s!", command));
        }
    }
}