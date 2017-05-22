package ly.generalassemb.de.datafeeds.americanExpress.ingress.model.CBNOT;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.sun.istack.internal.Nullable;
import org.javamoney.moneta.format.MonetaryAmountDecimalFormatBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.zalando.jackson.datatype.money.FieldNames;
import org.zalando.jackson.datatype.money.MonetaryAmountFormatFactory;


import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.format.MonetaryAmountFormat;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by dashirov on 5/17/17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "CBNOT_CHARGEBACK_AMOUNT"
})
public class TestMonetaryAmountJsonSerialization {
    @JsonProperty("CBNOT_CHARGEBACK_AMOUNT")
    @NotNull
    @JsonSerialize(using = MonetaryAmountSerializer.class)
    private final MonetaryAmount chargebackAmount = Monetary.getDefaultAmountFactory().setCurrency("USD").setNumber(-12.50).create();


    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testThis() throws JsonProcessingException {
        String json = mapper.writeValueAsString(this);
        System.out.println(json);
        Assert.assertEquals("{\"CBNOT_CHARGEBACK_AMOUNT\":\"-12.50\"}", json);
    }

    public static class MonetaryAmountSerializer extends JsonSerializer<MonetaryAmount> {
        public void serialize(MonetaryAmount monetaryAmount,
                              JsonGenerator jsonGenerator,
                              SerializerProvider serializerProvider) throws IOException {
            StringBuilder sb = new StringBuilder();
            MonetaryAmountDecimalFormatBuilder
                    .of("###0.00").withCurrencyUnit(monetaryAmount.getCurrency()).build()
                    .print(sb, monetaryAmount);
            jsonGenerator.writeString(sb.toString());
        }
    }

}
