package nl.nielsvanbruggen.gsorm.credentials;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CredentialsFactory {
    private static final Logger logger = LoggerFactory.getLogger(CredentialsFactory.class);
    private static final String CREDENTIALS_JSON_ENV = "GOOGLE_SHEET_CREDENTIALS_JSON";
    private static final String CREDENTIALS_JSON_FILE = "/credentials.json";

    private CredentialsFactory() {}

    public static Credentials getDefaultCredentials() throws IllegalStateException {
        // Try to resolve credentials from env variable.
        String json = System.getenv(CREDENTIALS_JSON_ENV);
        if(json != null) {
            try (InputStream is = new ByteArrayInputStream(json.getBytes())) {
                return GoogleCredentials.fromStream(is);
            } catch (IOException e) {
                logger.trace(e.getMessage(), e);
            }
        }

        // Try to resolve credentials from credentials file.
        try (InputStream credentials = CredentialsFactory.class.getResourceAsStream(CREDENTIALS_JSON_FILE)) {
            if(credentials == null) throw new IOException("Could not find: " + CREDENTIALS_JSON_FILE);

            return GoogleCredentials.fromStream(credentials);
        } catch (IOException e) {
            logger.trace(e.getMessage(), e);
        }

        throw new IllegalStateException("Could not resolve default credentials");
    }
}
