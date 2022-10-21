package rs.raf.specification;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.drive.Drive;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface DirectoryHandlerGoogleDriveSpecification {
    /**
     * Gets authenticated Google Drive user credentials from credentials file of the specified path, HTTP_TRANSPORT, JSON_FACTORY, SCOPES, TOKENS_DIRECTORY_PATH
     * @param CREDENTIALS_FILE_PATH path to the credentials.json file
     * @param HTTP_TRANSPORT http client
     * @param JSON_FACTORY json serializer
     * @param SCOPES scopes that the instance of the api connection will use
     * @param TOKENS_DIRECTORY_PATH directory where to store the session tokens
     */
    Credential getCredentials(final InputStream inputStream, final String CREDENTIALS_FILE_PATH, final NetHttpTransport HTTP_TRANSPORT, final JsonFactory JSON_FACTORY, final List<String> SCOPES, final String TOKENS_DIRECTORY_PATH) throws IOException;
    /**
     * Creates a Google Drive directory with the default name;
     * @param googleDriveClient Google Drive authenticated client with scopes;
     * @throws GoogleJsonResponseException 
     * @throws IOException 
     */
    String createGoogleDriveDirectory(final Drive googleDriveClient) throws GoogleJsonResponseException, IOException;
    /**
     * Creates a Google Drive directory with the specified name;
     * @param googleDriveClient Google Drive authenticated client with scopes;
     * @param directoryName name of directory to create
     * @throws IOException 
     */
    String createGoogleDriveDirectory(final Drive googleDriveClient, final String directoryName) throws IOException;
    /**
     * Creates a Google Drive file with the default name;
     * @param googleDriveClient Google Drive authenticated client with scopes;
     * @throws IOException 
     */
    //String createGoogleDriveFile(final Drive googleDriveClient) throws IOException;
    /**
     * Creates a Google Drive text file with the default name in the specified directory;
     * @param googleDriveClient Google Drive authenticated client with scopes;
     * @param directoryId name of directory to create the file in
     */
    String createGoogleDriveFile(final Drive googleDriveClient, final String directoryId) throws IOException;
    /**
     * Creates a Google Drive text file with the default name in the root directory;
     * @param googleDriveClient Google Drive authenticated client with scopes;
     * @param directoryName name of directory to create the file in
     * @param fileName file name
     */
    String createGoogleDriveFile(final Drive googleDriveClient, final String directoryName, final String fileName) throws IOException;
    /**
     * Creates a Google Drive text file with the default name in the root directory;
     * @param googleDriveClient Google Drive authenticated client with scopes;
     * @param directoryName name of directory to create the file in
     * @param fileName file name
     * @param fileExtension file extension
     */   
    String createGoogleDriveFile(final Drive googleDriveClient, final String directoryName, final String fileName, final String fileExtension) throws IOException;
    List<String> getFileList(final Drive googleDriveClient, final String directoryName) throws IOException;
    String getDirectoryIdByName(final Drive googleDriveClient, final String directoryName) throws IOException;
}