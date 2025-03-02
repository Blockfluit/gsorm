package nl.nielsvanbruggen.gsorm;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import nl.nielsvanbruggen.gsorm.deserializers.Deserializer;
import nl.nielsvanbruggen.gsorm.deserializers.TableDeserializer;
import nl.nielsvanbruggen.gsorm.resolvers.chains.ResolverChain;
import nl.nielsvanbruggen.gsorm.resolvers.chains.ResolverChainFactory;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.List;

public class GoogleSheet<T> {
    private final Class<T> clazz;
    private final Deserializer deserializer;
    private final Sheets sheets;
    private final String spreadsheetId;
    private final String range;

    private GoogleSheet(Builder<T> builder) {
        this.clazz = builder.clazz;
        this.deserializer = builder.deserializer;
        this.sheets = builder.sheets;
        this.spreadsheetId = builder.spreadsheetId;
        this.range = builder.range;
    }

    public List<T> getValues() throws IOException {
        List<List<Object>> rows = sheets.spreadsheets()
                .values()
                .get(spreadsheetId, range)
//                .get("1wAyGptuZSE98XLlxGiCVuk0rqkZ5RhoO0wDS0z59SG4", "A:Z")
                .execute()
                .getValues();

        return deserializer.map(rows, clazz);
    }

    // TODO: Properly implement this.
    private static GoogleCredentials getGoogleCredentials() {
        try (InputStream credentials = GoogleSheet.class.getResourceAsStream("/credentials.json")) {
            if(credentials == null) return null;

            return GoogleCredentials.fromStream(credentials);
        } catch (IOException e) {
            return null;
        }
    }

    public static class Builder<T> {
        private final Class<T> clazz;

        private Deserializer deserializer;
        private Sheets sheets;
        private String spreadsheetId;
        private String range = "A:Z";
        private String applicationName = "Default Google Sheet App";

        public Builder(Class<T> clazz) {
            this.clazz = clazz;
        }

        public Builder<T> deserializer(Deserializer deserializer) {
            this.deserializer = deserializer;
            return this;
        }

        public Builder<T> sheets(Sheets sheets) {
            this.sheets = sheets;
            return this;
        }

        public Builder<T> range(String range) {
            this.range = range;
            return this;
        }

        public Builder<T> applicationName(String applicationName) {
            this.applicationName = applicationName;
            return this;
        }

        public GoogleSheet<T> build(String spreadsheetId) throws GeneralSecurityException, IOException {
            NetHttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
            JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

            this.spreadsheetId = spreadsheetId;

            // Default deserializer
            if(this.deserializer == null) {
                ResolverChain resolverChain = ResolverChainFactory.getSimpleResolverChain();
                this.deserializer = new TableDeserializer(resolverChain);
            }

            // Default Sheets
            if(this.sheets == null) {
                this.sheets = new Sheets.Builder(transport, jsonFactory, new HttpCredentialsAdapter(getGoogleCredentials()))
                        .setApplicationName(applicationName)
                        .build();
            }

            return new GoogleSheet<>(this);
        }
    }
}
