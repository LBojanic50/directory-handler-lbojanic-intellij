package rs.raf.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DirectoryWithMaxFileCount {
    private String directoryName;
    private int maxFileCount;
    @Override
    public String toString() {
        return "DirectoryWithMaxFileCount{" +
                "directoryName='" + directoryName + '\'' +
                ", maxFileCount=" + maxFileCount +
                '}';
    }
}