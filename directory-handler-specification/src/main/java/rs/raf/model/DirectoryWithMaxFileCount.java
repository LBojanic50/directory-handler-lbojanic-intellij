package rs.raf.model;

public class DirectoryWithMaxFileCount {
    public String directoryPathString;
    public int maxFileCount;
    public DirectoryWithMaxFileCount(String directoryPathString, int maxFileCount) {
        this.directoryPathString = directoryPathString;
        this.maxFileCount = maxFileCount;
    }
    public String getDirectoryPathString() {
        return directoryPathString;
    }
    public void setDirectoryPathString(String directoryPathString) {
        this.directoryPathString = directoryPathString;
    }
    public int getMaxFileCount() {
        return maxFileCount;
    }
    public void setMaxFileCount(int maxFileCount) {
        this.maxFileCount = maxFileCount;
    }
}