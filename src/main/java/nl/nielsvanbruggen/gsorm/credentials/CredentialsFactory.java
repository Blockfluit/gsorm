package nl.nielsvanbruggen.gsorm.credentials;

import com.google.auth.oauth2.GoogleCredentials;
import nl.nielsvanbruggen.gsorm.GoogleSheet;

import java.io.IOException;
import java.io.InputStream;

public class CredentialsFactory {

    private CredentialsFactory() {}

    public static GoogleCredentials getGoogleCredentials() {
        // TODO: Add better exception handling.
        try (InputStream credentials = GoogleSheet.class.getResourceAsStream("/credentials.json")) {
            if(credentials == null) return null;

            return GoogleCredentials.fromStream(credentials);
        } catch (IOException e) {
            return null;
        }
    }
}
