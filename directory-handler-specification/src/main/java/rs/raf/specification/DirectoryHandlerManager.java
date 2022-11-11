package rs.raf.specification;

public class DirectoryHandlerManager {
    private static IDirectoryHandlerSpecification directoryHandler;
    public static void registerDirectoryHandler(IDirectoryHandlerSpecification dH){
        directoryHandler = dH;
    }
    public static IDirectoryHandlerSpecification getDirectoryHandler(){
        return directoryHandler;
    }
}