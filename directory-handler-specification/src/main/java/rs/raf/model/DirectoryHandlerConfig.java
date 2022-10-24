package rs.raf.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DirectoryHandlerConfig {
    private String maxRepositorySize;
    private int maxFileCount;
    private String[] excludedExtensions;
}
