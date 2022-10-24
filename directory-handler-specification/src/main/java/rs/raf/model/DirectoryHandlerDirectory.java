package rs.raf.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.nio.file.Path;
import java.nio.file.Paths;

import static rs.raf.specification.DirectoryHandlerLocalSpecification.workingDirectory;

@Getter
@Setter
public class DirectoryHandlerDirectory {
    private String repositoryName;
    private String directoryName;
    private Path repositoryPath;
    private Path directoryPath;
    public DirectoryHandlerDirectory(String parRepositoryName, String parDirectoryName){
        this.repositoryName = parRepositoryName;
        this.directoryName = parDirectoryName;
        repositoryPath = Paths.get(String.format("directory-handler-project", repositoryName));
        directoryPath = repositoryPath.resolve(directoryName);

        String s = repositoryPath.toAbsolutePath().toString();
    }
}