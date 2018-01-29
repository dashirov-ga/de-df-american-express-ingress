package ly.generalassemb.de.datafeeds.americanExpress.ingress.serializer;

import java.util.EnumMap;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.model.AmexRecordType;

/**
 * This ly.generalassemb.de.datafeeds.americanExpress.ingress.serializer is responsible for ..
 */
public class AmexFeedFileSerializerFactory {

    private static final EnumMap<AmexRecordType, AmexFeedFileSerializer> FEED_FILE_SERIALIZER_ENUM_MAP =
            new EnumMap<>(AmexRecordType.class);

    static {
        FEED_FILE_SERIALIZER_ENUM_MAP.put(AmexRecordType.CBNOT_DETAIL, new ChargeBackDetailsSerializer());
        FEED_FILE_SERIALIZER_ENUM_MAP.put(AmexRecordType.EPTRN_ADJUSTMENT_DETAIL, new AdjustmentDetailSerializer());
        FEED_FILE_SERIALIZER_ENUM_MAP.put(AmexRecordType.EPTRN_ROC_DETAIL, new ROCDetailSerializer());
        FEED_FILE_SERIALIZER_ENUM_MAP.put(AmexRecordType.EPTRN_SOC_DETAIL, new SocDetailSerializer());
        FEED_FILE_SERIALIZER_ENUM_MAP.put(AmexRecordType.EPTRN_SUMMARY, new SummaryFileSerializer());
    }

    public static AmexFeedFileSerializer getAmexFeedFileSerializerFor(AmexRecordType recorType) {
        return FEED_FILE_SERIALIZER_ENUM_MAP.get(recorType);
    }
}
