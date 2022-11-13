package rs.raf.specification;

/**
 * Helper class for referencing an implementation.
 */
public class DirectoryHandlerManager {
    /**
     * Directory handler.
     */
    private static IDirectoryHandlerSpecification directoryHandler;
    /**
     * Registers directory handler.
     *
     * @param iDirectoryHandlerSpecification specification.
     */
    public static void registerDirectoryHandler(IDirectoryHandlerSpecification iDirectoryHandlerSpecification) {
        directoryHandler = iDirectoryHandlerSpecification;
    }
    /**
     * Gets directory handler.
     *
     * @return IDirectoryHandlerSpecification specification.
     */
    public static IDirectoryHandlerSpecification getDirectoryHandler() {
        return directoryHandler;
    }
}