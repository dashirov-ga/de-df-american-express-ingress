package ly.generalassemb.de.datafeeds.americanExpress.ingress.model;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.model.file.*;

import java.io.File;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FixedWidthDataFileFactory {
    private static final Pattern fileNamePattern = Pattern.compile("^(?<account>\\p{Alnum}+)\\.(?<type>EPAPE|EPTRN|CBNOT|EMINQ|EMCBK)[#-](?<fileId>\\p{Alnum}+)");
    public static FixedWidthDataFile getDataFile (Path filePath) throws Exception {
                Matcher m = fileNamePattern.matcher(filePath.getFileName().toString());
                String type = (m.matches()) ? m.group("type") : filePath.getFileName().toString();
                FixedWidthDataFile dataFile =  getDataFile(type);
                dataFile.parse(filePath.toFile());
                return dataFile;
    }
    public static FixedWidthDataFile getDataFile (String fileType) throws NotRegisteredException {
        if (fileType != null){
            if (fileType.equals("EPTRN")){
                return new EPTRNFixedWidthDataFile();
            } else if ( fileType.equals("CBNOT")){
                return new CBNOTFixedWidthDataFile();
            } else if ( fileType.equals("EMCBK")){
                return new EMCBKFixedWidthDataFile();
            } else if (fileType.equals("EMINQ")){
                return new EMINQFixedWidthDataFile();
            } else if (fileType.equals("EPAPE")){
                return new EPAPEFixedWidthDataFile();
            }
        }
        throw new NotRegisteredException("Unregistered file type");
    }
}
