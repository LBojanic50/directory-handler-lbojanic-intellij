package rs.raf.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class DirectoryHandlerConfig {
    private long maxRepositorySize;
    private List<String> excludedExtensions;
    private List<DirectoryWithMaxFileCount> directoriesWithMaxFileCount;
    public DirectoryHandlerConfig(){
        this.maxRepositorySize = 1073741824;
        this.excludedExtensions = new ArrayList<>();
        this.directoriesWithMaxFileCount = new ArrayList<>();
    }
    @Override
    public String toString() {
        return "DirectoryHandlerConfig{" +
                "maxRepositorySize=" + maxRepositorySize +
                ", excludedExtensions=" + excludedExtensions +
                ", directoriesWithMaxFileCount=" + directoriesWithMaxFileCount +
                '}';
    }
}