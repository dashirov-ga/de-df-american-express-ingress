package ly.generalassemb.de.datafeeds.americanExpress.ingress.model;

/**
 * This ly.generalassemb.de.datafeeds.americanExpress.ingress.model is responsible for ..
 */
public enum AmexRecordType {
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

    AmexRecordType(String prefix, String format, String suffix, String fileNamePrefix) {
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

    public static AmexRecordType getAmexRecordTypeForFileType(String type) {
        switch (type) {
            case "EPTRN" :
                return AmexRecordType.EPTRN;
            case "CBNOT":
                return AmexRecordType.CBNOT;
            default:
                throw new IllegalArgumentException("Are you mad !!");
        }
    }
}
