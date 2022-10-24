/*package rs.raf.specification;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.drive.Drive;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface DirectoryHandlerGoogleDriveSpecification {
    Credential getCredentials(final InputStream inputStream, final String CREDENTIALS_FILE_PATH, final NetHttpTransport HTTP_TRANSPORT, final JsonFactory JSON_FACTORY, final List<String> SCOPES, final String TOKENS_DIRECTORY_PATH) throws IOException;

    String createGoogleDriveDirectory(final Drive googleDriveClient) throws GoogleJsonResponseException, IOException;

    String createGoogleDriveDirectory(final Drive googleDriveClient, final String directoryName) throws IOException;

    String createGoogleDriveFile(final Drive googleDriveClient, final String directoryId) throws IOException;

    String createGoogleDriveFile(final Drive googleDriveClient, final String directoryName, final String fileName) throws IOException;

    String createGoogleDriveFile(final Drive googleDriveClient, final String directoryName, final String fileName, final String fileExtension) throws IOException;
    List<String> getFileList(final Drive googleDriveClient, final String directoryName) throws IOException;
    String getDirectoryIdByName(final Drive googleDriveClient, final String directoryName) throws IOException;
}*/