package ly.generalassemb.de.datafeeds.americanExpress.ingress.model;

import com.amazonaws.services.s3.AmazonS3URI;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.Config;

import java.io.File;
import java.util.Map;

public interface FixedWidthDataFile {
    public FixedWidthDataFile parse(File fixedWidthDataFile) throws Exception;
    public String getId();
    public String getFileId();
    public String getFileName();
    public void toDirectory(File targetDirectory) throws Exception;
    public Map<FixedWidthDataFileComponent, String> toLoadableComponents( ) throws Exception;
    public Map<FixedWidthDataFileComponent,AmazonS3URI> toS3(Config configuration) throws Exception;
    public Map<FixedWidthDataFileComponent,String> toRedshift(Config configuration) throws Exception;
}
