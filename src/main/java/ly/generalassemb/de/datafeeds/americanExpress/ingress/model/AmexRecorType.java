package ly.generalassemb.de.datafeeds.americanExpress.ingress.model;

import ly.generalassemb.de.datafeeds.americanExpress.ingress.parser.CBNOTFileParser;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.parser.EPTRNFileParser;

/**
 * This ly.generalassemb.de.datafeeds.americanExpress.ingress.model is responsible for ..
 */
public enum AmexRecorType {
    EPTRN("EPTRN", "EPTRN", ".dat", ""),
    EPTRN_HEADER("EPTRN-HDR", "CSV", ".csv", ""),
    EPTRN_TRAILER("EPTRN-TRL", "CSV", ".csv", ""),
    EPTRN_SUMMARY("EPTRN-SUMMARY", "CSV", ".csv", "summary-"),
    EPTRN_SOC_DETAIL("EPTRN-SOC-DETAIL", "CSV", ".csv", "socdetails-"),
    EPTRN_ROC_DETAIL("EPTRN-ROC-DETAIL", "CSV", ".csv", "rocdetails-"),
    EPTRN_ADJUSTMENT_DETAIL("EPTRN-ADJ-DETAIL", "CSV", ".csv", "adjustments-"),
    CBNOT_DETAIL("CBNOT-DETAIL", "CSV", ".csv", "chargebackdetails-"),
    CBNOT("CBNOT", "CBNOT", ".dat", ""),
    CBNOT_HEADER("CBNOT-HEADER", "CBNOT", ".dat", ""),
    CBNOT_FOOTER("CBNOT-FOOTER", "CBNOT", ".dat", "");

    private final String prefix;
    private final String format;
    private final String suffix;
    private final String fileNamePrefix;

    AmexRecorType(String prefix, String format, String suffix, String fileNamePrefix) {
        this.prefix = prefix;
        this.format = format;
        this.suffix = suffix;
        this.fileNamePrefix = fileNamePrefix;
    }

    public String getFormat() {
        return format;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public String getFileNamePrefix() {
        return fileNamePrefix;
    }

    public static AmexRecorType getAmexRecordTypeForFileType(String type) {
        switch (type) {
            case "EPTRN" :
                return AmexRecorType.EPTRN;
            case "CBNOT":
                return AmexRecorType.CBNOT;
            default:
                throw new IllegalArgumentException("Are you mad !!");
        }
    }
}
