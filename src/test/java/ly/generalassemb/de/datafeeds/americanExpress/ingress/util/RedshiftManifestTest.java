package ly.generalassemb.de.datafeeds.americanExpress.ingress.util;

import com.amazonaws.services.s3.AmazonS3URI;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.TimeZone;

/**
 * Created by davidashirov on 3/12/17.
 */
public class RedshiftManifestTest {

    @Test
    public void testConstructor(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        RedshiftManifest manifest = new RedshiftManifest();
        LocalDate date = LocalDate.now();
        for (int i = 0; i < 10; i++){
            AmazonS3URI s3url = new AmazonS3URI("s3://" + "mybucket" + "/" + date +".csv"  );

            manifest.addEntry(new RedshiftManifestEntry(true, s3url));
        }
        System.out.println(manifest.toString());
    }
}
