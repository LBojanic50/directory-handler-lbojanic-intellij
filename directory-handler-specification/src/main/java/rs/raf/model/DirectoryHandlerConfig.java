package rs.raf.model;

public class DirectoryHandlerConfig {
    private String maxRepositorySize;
    private String excludedExtensionsString;
    public DirectoryHandlerConfig(final String maxRepositorySize, final String excludedExtensionsString) {
        this.maxRepositorySize = maxRepositorySize;
        this.excludedExtensionsString = excludedExtensionsString;
    }
    public DirectoryHandlerConfig(){
        this.maxRepositorySize = "1073741824";
        this.excludedExtensionsString = "";
    }
    public String getMaxRepositorySize() {
        return maxRepositorySize;
    }
    public void setMaxRepositorySize(String maxRepositorySize) {
        this.maxRepositorySize = maxRepositorySize;
    }

    public String getExcludedExtensionsString() {
        return excludedExtensionsString;
    }
    public void setExcludedExtensionsString(String excludedExtensionsString) {
        this.excludedExtensionsString = excludedExtensionsString;
    }
}