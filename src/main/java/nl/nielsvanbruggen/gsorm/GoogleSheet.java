package nl.nielsvanbruggen.gsorm;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.auth.Credentials;
import com.google.auth.http.HttpCredentialsAdapter;
import nl.nielsvanbruggen.gsorm.credentials.CredentialsFactory;
import nl.nielsvanbruggen.gsorm.deserializers.Deserializer;
import nl.nielsvanbruggen.gsorm.deserializers.TableDeserializer;
import nl.nielsvanbruggen.gsorm.resolvers.chains.TypeResolverChain;
import nl.nielsvanbruggen.gsorm.resolvers.chains.TypeResolverChainFactory;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public class GoogleSheet<T> {
    private static final String DEFAULT_RANGE = "A:Z";
    private static final String DEFAULT_APPLICATION_NAME = "Default Google Sheet App";

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
                .execute()
                .getValues();

        return deserializer.map(rows, clazz);
    }

    public static class Builder<T> {
        private static final JsonFactory DEFAULT_JSON_FACTORY = GsonFactory.getDefaultInstance();

        private final Class<T> clazz;
        private final String spreadsheetId;

        private Credentials credentials;
        private Deserializer deserializer;
        private Sheets sheets;
        private String range = DEFAULT_RANGE;
        private String applicationName = DEFAULT_APPLICATION_NAME;

        public Builder(Class<T> clazz, String spreadsheetId) {
            this.clazz = clazz;
            this.spreadsheetId = spreadsheetId;
        }

        public Builder<T> setDeserializer(Deserializer deserializer) {
            this.deserializer = deserializer;
            return this;
        }

        public Builder<T> setSheets(Sheets sheets) {
            this.sheets = sheets;
            return this;
        }

        public Builder<T> setRange(String range) {
            this.range = range;
            return this;
        }

        public Builder<T> setApplicationName(String applicationName) {
            this.applicationName = applicationName;
            return this;
        }

        public Builder<T> setCredentials(Credentials credentials) {
            this.credentials = credentials;
            return this;
        }

        public GoogleSheet<T> build() throws GeneralSecurityException, IOException {
            NetHttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();

            // Default deserializer
            if(this.deserializer == null) {
                TypeResolverChain typeResolverChain = TypeResolverChainFactory.getBasicTypeResolverChain();
                this.deserializer = new TableDeserializer(typeResolverChain);
            }

            // Default credentials.
            if(this.credentials == null) {
                this.credentials = CredentialsFactory.getDefaultCredentials();
            }

            // Default Sheets
            if(this.sheets == null) {
                this.sheets = new Sheets.Builder(transport, DEFAULT_JSON_FACTORY, new HttpCredentialsAdapter(credentials))
                        .setApplicationName(applicationName)
                        .build();
            }

            return new GoogleSheet<>(this);
        }
    }
}
