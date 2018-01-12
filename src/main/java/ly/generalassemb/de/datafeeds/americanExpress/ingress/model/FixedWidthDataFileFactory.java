package ly.generalassemb.de.datafeeds.americanExpress.ingress.model;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.model.file.*;


public class FixedWidthDataFileFactory {
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
