package rs.raf.model;

import java.nio.file.Path;
import java.nio.file.Paths;

public class DirectoryHandlerDirectory {
    private String repositoryName;
    private String directoryName;
    private Path repositoryPath;
    private Path directoryPath;
    public DirectoryHandlerDirectory(String parRepositoryName, String parDirectoryName){
        this.repositoryName = parRepositoryName;
        this.directoryName = parDirectoryName;
        repositoryPath = Paths.get("directory-handler-project", repositoryName);
        directoryPath = repositoryPath.resolve(directoryName);

        String s = repositoryPath.toAbsolutePath().toString();
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public String getDirectoryName() {
        return directoryName;
    }

    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }

    public Path getRepositoryPath() {
        return repositoryPath;
    }

    public void setRepositoryPath(Path repositoryPath) {
        this.repositoryPath = repositoryPath;
    }

    public Path getDirectoryPath() {
        return directoryPath;
    }

    public void setDirectoryPath(Path directoryPath) {
        this.directoryPath = directoryPath;
    }
}