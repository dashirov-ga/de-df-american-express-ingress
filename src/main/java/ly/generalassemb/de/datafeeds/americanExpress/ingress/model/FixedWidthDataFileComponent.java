package ly.generalassemb.de.datafeeds.americanExpress.ingress.model;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum FixedWidthDataFileComponent {
    EPTRN_FIXED_WIDTH_OBJECT("EPTRN", "text/plain", ".dat", false, false,"american_express_eptrn_file_ingested",
            ImmutableList.builder().add("nexus").build()),
    EPTRN_JSON_OBJECT("EPTRN","application/json",".json",false, false,"american_express_eptrn_file_processes",
            ImmutableList.builder().add("nexus").build()),
    EPTRN_CSV_HEADER_COMPONENT("EPTRN-HEADER","text/csv",".csv",false, false,"american_express_eptrn_header",
            ImmutableList.builder().add("nexus").build()),
    EPTRN_CSV_TRAILER_COMPONENT("EPTRN-TRAILER","text/csv",".csv",false, false,"american_express_eptrn_trailer",
            ImmutableList.builder().add("nexus").build()),
    EPTRN_CSV_PAYMENT_COMPONENT("EPTRN-SUMMARY","text/csv",".csv",true,true,"american_express_eptrn_payment",
            ImmutableList.builder().add("payment_number").build()),
    EPTRN_CSV_CHARGEBACK_COMPONENT("EPTRN-CBK-DETAIL","text/csv",".csv",false,false,"american_express_eptrn_chargeback",
            ImmutableList.builder().add("payment_number").build()),
    EPTRN_CSV_ADJUSTMENT_COMPONENT("EPTRN-ADJ-DETAIL","text/csv",".csv",true,true,"american_express_eptrn_adjustment",
            ImmutableList.builder().add("payment_number").build()),
    EPTRN_CSV_OTHER_COMPONENT("EPTRN-OTH-DETAIL","text/csv",".csv",true,true,"american_express_eptrn_other",
            ImmutableList.builder().add("payment_number").build()),
    EPTRN_CSV_SOC_COMPONENT("EPTRN-SOC-DETAIL","text/csv",".csv",true,true,"american_express_eptrn_summary_of_charges",
            ImmutableList.builder().add("payment_number").build()),
    EPTRN_CSV_ROC_COMPONENT("EPTRN-ROC-DETAIL","text/csv",".csv",true,true,"american_express_eptrn_record_of_charges",
            ImmutableList.builder().add("payment_number").build()),
    CBNOT_FIXED_WIDTH_OBJECT("CBNOT","text/plain",".dat",false, false,"american_express_cbnot_file_ingested",
            ImmutableList.builder().add("nexus").build()),
    CBNOT_JSON_OBJECT("CBNOT","application/json",".json",false,false,"american_express_cbnot_file_processed",
            ImmutableList.builder().add("nexus").build()),
    CBNOT_CSV_HEADER_COMPONENT("CBNOT-HEADER","text/csv",".csv",false, false,"american_express_cbnot_header",
            ImmutableList.builder().add("nexus").build()),
    CBNOT_CSV_TRAILER_COMPONENT("CBNOT-TRAILER","text/csv",".csv",false, false,"american_express_cbnot_trailer",
            ImmutableList.builder().add("nexus").build()),
    CBNOT_CSV_CHARGEBACK_NOTIFICATION_COMPONENT("CBNOT-CBK-DETAIL","text/csv",".csv",true,true,"american_express_cbnot_chargeback",
            ImmutableList.builder().add("chargeback_adjustment_number").build()),
    EPAPE_FIXED_WIDTH_OBJECT("EPAPE","text/plain",".dat",false, false,"american_express_epape_file_ingested",
            ImmutableList.builder().add("nexus").build()),
    EPAPE_JSON_OBJECT("EPAPE","application/json",".json",false,false,"american_express_epape_file_processed",
            ImmutableList.builder().add("nexus").build()),
    EPAPE_CSV_HEADER_COMPONENT("EPAPE-HEADER","text/csv",".csv",false, false,"american_express_epape_header",
            ImmutableList.builder().add("nexus").build()),
    EPAPE_CSV_TRAILER_COMPONENT("EPAPE-TRAILER","text/csv",".csv",false, false,"american_express_epape_trailer",
            ImmutableList.builder().add("nexus").build()),
    EPAPE_CSV_PAYMENT_COMPONENT("EPAPE-SUMMARY","text/csv",".csv",true,true,"american_express_epape_payment",
            ImmutableList.builder().add("generated_payment_number").build()),
    EPAPE_CSV_ADJUSTMENT_COMPONENT("EPAPE-ADJ-DETAIL","text/csv",".csv",true,true,"american_express_epape_adjustment",
            ImmutableList.builder().add("generated_payment_number").build()),
    EPAPE_CSV_SOC_COMPONENT("EPAPE-SOC-DETAIL","text/csv",".csv",true,true,"american_express_epape_summary_of_charges",
            ImmutableList.builder().add("generated_payment_number").build()),
    EPAPE_CSV_ROC_COMPONENT("EPAPE-ROC-DETAIL","text/csv",".csv",true,true,"american_express_epape_record_of_charges",
            ImmutableList.builder().add("generated_payment_number").build()),
    EMINQ_FIXED_WIDTH_OBJECT("EMINQ","text/plain",".dat",false, false,"american_express_eminq_file_ingested",
            ImmutableList.builder().add("nexus").build()),
    EMINQ_JSON_OBJECT("EMINQ","application/json",".json",true,true,"american_express_eminq_file_processed",
            ImmutableList.builder().add("nexus").build()),
    EMINQ_CSV_HEADER_COMPONENT("EMINQ-HEADER","text/csv",".csv",false, false,"american_express_eminq_header",
            ImmutableList.builder().add("nexus").build()),
    EMINQ_CSV_TRAILER_COMPONENT("EMINQ-TRAILER","text/csv",".csv",false, false,"american_express_eminq_trailer",
            ImmutableList.builder().add("nexus").build()),
    EMINQ_CSV_INQUIRY_COMPONENT("EMINQ-INQ-DETAIL","text/csv",".csv",true,true,"american_express_eminq_inquiry",
            ImmutableList.builder().add("roc_reference_number").build()),
    EMCBK_FIXED_WIDTH_OBJECT("EMCBK","text/plain",".dat",false, false,"american_express_emcbk_file_ingested",
            ImmutableList.builder().add("nexus").build()),
    EMCBK_JSON_OBJECT("EMCBK","application/json",".json",false, false,"american_express_emcbk_file_processed",
            ImmutableList.builder().add("nexus").build()),
    EMCBK_CSV_HEADER_COMPONENT("EMCBK-HEADER","text/csv",".csv",false, false,"american_express_emcbk_header",
            ImmutableList.builder().add("nexus").build()),
    EMCBK_CSV_TRAILER_COMPONENT("EMCBK-TRAILER","text/csv",".csv",false, false,"american_express_emcbk_trailer",
            ImmutableList.builder().add("nexus").build()),
    EMCBK_CSV_CHARGEBACK_COMPONENT("EMCBK-CBK-DETAIL","text/csv",".csv",true,true,"american_express_emcbk_chargeback",
            ImmutableList.builder().add("roc_reference_number").build());

    private final String s3Prefix;
    private final String format;
    private final String fileNameExtension;
    private final boolean defaulSerializeToRedshift;
    private final boolean defaultSerializeToPostgres;
    private final String defaultTableName;
    private final ImmutableList<Object> primaryKey;


    FixedWidthDataFileComponent(String s3Prefix, String format, String fileNameExtension,
                                boolean serializeToRedshift, boolean serializeToPostgres, String tableName,
                                ImmutableList<Object> primaryKey
    ) {
        this.s3Prefix = s3Prefix;
        this.format = format;
        this.fileNameExtension = fileNameExtension;
        this.defaulSerializeToRedshift = serializeToRedshift;
        this.defaultSerializeToPostgres = serializeToPostgres;
        this.defaultTableName = tableName;
        this.primaryKey = primaryKey;
    }

    public ImmutableList<Object> getPrimaryKey() {
        return primaryKey;
    }

    public String getS3Prefix() {
        return s3Prefix;
    }

    public String getFormat() {
        return format;
    }

    public String getFileNameExtension() {
        return fileNameExtension;
    }

    public boolean isDefaulSerializeToRedshift() {
        return defaulSerializeToRedshift;
    }

    public boolean isDefaultSerializeToPostgres() {
        return defaultSerializeToPostgres;
    }

    public String getDefaultTableName() {
        return defaultTableName;
    }
}

