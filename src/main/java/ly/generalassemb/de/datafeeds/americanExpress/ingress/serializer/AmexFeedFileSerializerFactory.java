package ly.generalassemb.de.datafeeds.americanExpress.ingress.serializer;

import java.util.EnumMap;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.model.AmexRecorType;

/**
 * This ly.generalassemb.de.datafeeds.americanExpress.ingress.serializer is responsible for ..
 */
public class AmexFeedFileSerializerFactory {

    private static final EnumMap<AmexRecorType, AmexFeedFileSerializer> FEED_FILE_SERIALIZER_ENUM_MAP =
            new EnumMap<>(AmexRecorType.class);

    static {
        FEED_FILE_SERIALIZER_ENUM_MAP.put(AmexRecorType.CBNOT_DETAIL, new ChargeBackDetailsSerializer());
        FEED_FILE_SERIALIZER_ENUM_MAP.put(AmexRecorType.EPTRN_ADJUSTMENT_DETAIL, new AdjustmentDetailSerializer());
        FEED_FILE_SERIALIZER_ENUM_MAP.put(AmexRecorType.EPTRN_ROC_DETAIL, new ROCDetailSerializer());
        FEED_FILE_SERIALIZER_ENUM_MAP.put(AmexRecorType.EPTRN_SOC_DETAIL, new SocDetailSerializer());
        FEED_FILE_SERIALIZER_ENUM_MAP.put(AmexRecorType.EPTRN_SUMMARY, new SummaryFileSerializer());
    }

    public static AmexFeedFileSerializer getAmexFeedFileSerializerFor(AmexRecorType recorType) {
        return FEED_FILE_SERIALIZER_ENUM_MAP.get(recorType);
    }
}
