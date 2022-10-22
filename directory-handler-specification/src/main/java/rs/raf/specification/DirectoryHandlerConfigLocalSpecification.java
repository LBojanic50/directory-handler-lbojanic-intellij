package rs.raf.specification;

import java.io.IOException;

public interface DirectoryHandlerConfigLocalSpecification {
    void updateMaxRepositorySize(final String directoryName, final String maxRepositorySize) throws IOException;
    void updateMaxFileCount(final String directoryName, final String maxFileCount) throws IOException;
    void updateExcludedExtensions(final String directoryName, final String excludedExtensions) throws IOException;
}
