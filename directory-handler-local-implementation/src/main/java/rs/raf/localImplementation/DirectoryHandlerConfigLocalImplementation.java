package rs.raf.localImplementation;

import rs.raf.specification.DirectoryHandlerConfigLocalSpecification;

import java.io.*;
import java.util.Properties;

import static rs.raf.specification.DirectoryHandlerLocalSpecification.workingDirectory;

public class DirectoryHandlerConfigLocalImplementation implements DirectoryHandlerConfigLocalSpecification {
    @Override
    public void updateMaxRepositorySize(final String directoryName, final String maxRepositorySize) throws IOException {
        Properties properties = new Properties();
        FileInputStream fileInputStream = new FileInputStream(String.format(workingDirectory + "\\src\\%s\\config.properties", directoryName));
        InputStream inputStream = fileInputStream;
        properties.load(inputStream);
        properties.setProperty("maxRepositorySize", maxRepositorySize);
        OutputStream outputStream = new FileOutputStream(String.format(workingDirectory + "\\src\\%s\\config.properties", directoryName));
        properties.store(outputStream, "updateMaxRepositorySize");
    }

    @Override
    public void updateMaxFileCount(final String directoryName, final String maxFileCount) throws IOException {
        Properties properties = new Properties();
        FileInputStream fileInputStream = new FileInputStream(String.format(workingDirectory + "\\src\\%s\\config.properties", directoryName));
        InputStream inputStream = fileInputStream;
        properties.load(inputStream);
        properties.setProperty("maxFileCount", maxFileCount);
        OutputStream outputStream = new FileOutputStream(String.format(workingDirectory + "\\src\\%s\\config.properties", directoryName));
        properties.store(outputStream, "updateMaxRepositorySize");
    }

    @Override
    public void updateExcludedExtensions(final String directoryName, final String excludedExtensions) throws IOException {
        Properties properties = new Properties();
        FileInputStream fileInputStream = new FileInputStream(String.format(workingDirectory + "\\src\\%s\\config.properties", directoryName));
        InputStream inputStream = fileInputStream;
        properties.load(inputStream);
        properties.setProperty("excludedExtensions", excludedExtensions);
        OutputStream outputStream = new FileOutputStream(String.format(workingDirectory + "\\src\\%s\\config.properties", directoryName));
        properties.store(outputStream, "updateMaxRepositorySize");
    }
}