package rs.raf.model;

public class DirectoryHandlerConfig {
    private String maxRepositorySize;
    private int maxFileCount;
    private String[] excludedExtensions;
    public DirectoryHandlerConfig(String maxRepositorySize, int maxFileCount, String[] excludedExtensions) {
        this.maxRepositorySize = maxRepositorySize;
        this.maxFileCount = maxFileCount;
        this.excludedExtensions = excludedExtensions;
    }
    public DirectoryHandlerConfig(){
        this.maxRepositorySize = "1073741824";
        this.maxFileCount = 20;
        this.excludedExtensions = new String[] { "" };
    }
    public String getMaxRepositorySize() {
        return maxRepositorySize;
    }
    public int getMaxFileCount() {
        return maxFileCount;
    }
    public String[] getExcludedExtensions() {
        return excludedExtensions;
    }
    public void setMaxRepositorySize(String maxRepositorySize) {
        this.maxRepositorySize = maxRepositorySize;
    }
    public void setMaxFileCount(int maxFileCount) {
        this.maxFileCount = maxFileCount;
    }
    public void setExcludedExtensions(String[] excludedExtensions) {
        this.excludedExtensions = excludedExtensions;
    }
}