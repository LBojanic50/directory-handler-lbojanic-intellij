package rs.raf.googleDriveImplementation;

import rs.raf.model.DirectoryHandlerConfig;
import rs.raf.specification.IDirectoryHandlerSpecification;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class DirectoryHandlerGoogleDriveImplementation implements IDirectoryHandlerSpecification {
    @Override
    public void createRepository() throws IOException {

    }

    @Override
    public void createRepository(String repositoryName) throws IOException {

    }

    @Override
    public void createRepository(DirectoryHandlerConfig directoryHandlerConfig) throws IOException {

    }

    @Override
    public void createRepository(String repositoryName, DirectoryHandlerConfig directoryHandlerConfig) throws IOException {

    }

    @Override
    public void createDirectory(String repositoryName) {

    }

    @Override
    public void createDirectory(String repositoryName, String directoryName) {

    }

    @Override
    public void createFile(String repositoryName, String directoryName, String fileExtension) {

    }

    @Override
    public void createFile(String repositoryName, String directoryName, String fileName, String fileExtension) {

    }

    @Override
    public void createDefaultConfig(String repositoryName) throws IOException {

    }

    @Override
    public void createConfig(String repositoryName, DirectoryHandlerConfig directoryHandlerConfig) throws IOException {

    }

    @Override
    public void updateConfig(String repositoryName, DirectoryHandlerConfig directoryHandlerConfig) throws IOException {

    }

    @Override
    public Properties getProperties(String repositoryName) throws IOException {
        return null;
    }

    @Override
    public long getDirectorySize(String repositoryName, String directoryName) throws FileNotFoundException, IOException {
        return 0;
    }

    @Override
    public long getFileSize(String repositoryName, String directoryName, String fileName) throws FileNotFoundException, IOException {
        return 0;
    }

    @Override
    public String arrayToString(String[] array) {
        return null;
    }

    @Override
    public void writeToFile(String repositoryName, String directoryName, String fileName, String textToWrite) throws IOException {

    }

    @Override
    public void deleteFile(String repositoryName, String directoryName, String fileName) throws IOException {

    }

    @Override
    public void downloadFile(String repositoryName, String directoryName, String fileName, boolean overwrite) throws IOException {

    }

    @Override
    public void downloadFile(String repositoryName, String directoryName, String fileName, String downloadPathString, boolean overwrite) throws IOException {

    }

    @Override
    public void moveOrRenameFile(String repositoryName, String directoryName, String fileName, String newName) throws IOException {

    }

    @Override
    public List<File> getFileList(String repositoryName, String directoryName) {
        return null;
    }

    @Override
    public int getFileCount(String repositoryName, String directoryName) {
        return 0;
    }

    @Override
    public List<File> getFilesForSearchName(String repositoryName, String directoryName, String search) {
        return null;
    }

    @Override
    public List<File> getFilesForSearchNameAndExtensions(String repositoryName, String directoryName, String search, String[] searchExtensions) {
        return null;
    }

    @Override
    public List<File> getFilesForSearchNameAndExcludedExtensions(String repositoryName, String directoryName, String search, String[] searchExcludedExtensions) {
        return null;
    }

    @Override
    public List<File> getFilesForSearchNameAndExtensionsAndExcludedExtensions(String repositoryName, String directoryName, String search, String[] searchExtensions, String[] searchExcludedExtensions) {
        return null;
    }

    @Override
    public List<File> getFilesForExtensions(String repositoryName, String directoryName, String[] searchExtensions) {
        return null;
    }

    @Override
    public List<File> getFilesForExcludedExtensions(String repositoryName, String directoryName, String[] searchExcludedExtensions) {
        return null;
    }

    @Override
    public List<File> getFilesForExtensionsAndExcludedExtensions(String repositoryName, String directoryName, String[] searchExtensions, String[] searchExcludedExtensions) {
        return null;
    }
}