package rs.raf.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DirectoryHandlerConfig {
    private String maxRepositorySize;
    private int maxFileCount;
    private String excludedExtensionsString;
    public DirectoryHandlerConfig(final String maxRepositorySize, final int maxFileCount, final String excludedExtensionsString) {
        this.maxRepositorySize = maxRepositorySize;
        this.maxFileCount = maxFileCount;
        this.excludedExtensionsString = excludedExtensionsString;
    }
    public DirectoryHandlerConfig(){
        this.maxRepositorySize = "1073741824";
        this.maxFileCount = 20;
        this.excludedExtensionsString = "";
    }

    public String getMaxRepositorySize() {
        return maxRepositorySize;
    }

    public void setMaxRepositorySize(String maxRepositorySize) {
        this.maxRepositorySize = maxRepositorySize;
    }

    public int getMaxFileCount() {
        return maxFileCount;
    }

    public void setMaxFileCount(int maxFileCount) {
        this.maxFileCount = maxFileCount;
    }

    public String getExcludedExtensionsString() {
        return excludedExtensionsString;
    }

    public void setExcludedExtensionsString(String excludedExtensionsString) {
        this.excludedExtensionsString = excludedExtensionsString;
    }
}