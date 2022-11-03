package rs.raf.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;

public class LocalFile {
    private File file;
    private BasicFileAttributes fileMetadata;

    public LocalFile(File file) throws IOException {
        this.file = file;
        this.fileMetadata = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public BasicFileAttributes getFileMetadata() {
        return fileMetadata;
    }

    public void setFileMetadata(BasicFileAttributes fileMetadata) {
        this.fileMetadata = fileMetadata;
    }
}
