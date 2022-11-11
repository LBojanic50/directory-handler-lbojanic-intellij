package rs.raf.model;

import lombok.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocalFile {
    private File file;
    private BasicFileAttributes fileMetadata;
    public LocalFile(File file) throws IOException {
        this.file = file;
        this.fileMetadata = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
    }
    @Override
    public String toString() {
        return "LocalFile{" +
                "file=" + file +
                ", fileMetadata=" + fileMetadata +
                '}';
    }
}