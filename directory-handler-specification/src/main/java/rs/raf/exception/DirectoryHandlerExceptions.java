package rs.raf.exception;

public class DirectoryHandlerExceptions {
    public static class MaxFileCountExceededException extends Exception{
        public MaxFileCountExceededException(final String filePathString){
            super(String.format("Max file count for file/directory %s exceeded!", filePathString));
        }
    }
    public static class MaxRepositorySizeExceeded extends Exception{
        public MaxRepositorySizeExceeded(final String repositoryName){
            super(String.format("Max size for repository %s exceeded!", repositoryName));
        }
    }
}