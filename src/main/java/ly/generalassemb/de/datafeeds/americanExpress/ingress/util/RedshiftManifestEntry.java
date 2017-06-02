package ly.generalassemb.de.datafeeds.americanExpress.ingress.util;

import com.amazonaws.services.s3.AmazonS3URI;

import java.net.URI;

/**
 * Created by davidashirov on 3/12/17.
 */
public class RedshiftManifestEntry {
    boolean mandatory = true;
    URI url;

    public RedshiftManifestEntry(boolean mandatory, AmazonS3URI url) {
        this.mandatory = mandatory;
        this.url = url.getURI();
    }

    public RedshiftManifestEntry() {
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public URI getUrl() {
        return url;
    }

    public void setUrl(AmazonS3URI url) {
        this.url = url.getURI();
    }
}

