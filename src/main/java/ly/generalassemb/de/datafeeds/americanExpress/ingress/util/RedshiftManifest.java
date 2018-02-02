package ly.generalassemb.de.datafeeds.americanExpress.ingress.util;

import com.amazonaws.services.s3.AmazonS3URI;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidashirov on 3/12/17.
 */
public class RedshiftManifest {
    public static String getContentType(){
        return "application/json";
    }

    private List<RedshiftManifestEntry> entries = new ArrayList<>();

    public void addEntry(RedshiftManifestEntry entry) {
        entries.add(entry);
    }

    public RedshiftManifest() {
    }

    public void addAll(List<AmazonS3URI> entries, boolean mandatory) {
        for (AmazonS3URI url : entries) {
            this.addEntry(new RedshiftManifestEntry(mandatory, url));
        }
    }

    public RedshiftManifest(List<AmazonS3URI> s3urls, boolean mandatory) {
        addAll(s3urls, mandatory);
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public List<RedshiftManifestEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<RedshiftManifestEntry> entries) {
        this.entries = entries;
    }
}

