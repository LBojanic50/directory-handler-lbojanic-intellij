package rs.raf.model;

import lombok.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
/**
 * Local file with java.io.File and file metadata.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocalFile {
    /**
     * java.io.File file.
     */
    private File file;
    /**
     * File metadata.
     */
    private BasicFileAttributes fileMetadata;
    /**
     * Local file constructor with java.io.File parameter.
     * @param file java.io.File
     */
    public LocalFile(File file) throws IOException {
        this.file = file;
        this.fileMetadata = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
    }
    /**
     * Local file to string.
     */
    @Override
    public String toString() {
        return "LocalFile{" +
                "file=" + file +
                ", fileMetadata=" + fileMetadata +
                '}';
    }
}