package ly.generalassemb.de.datafeeds.americanExpress.ingress.util;

import com.ancientprogramming.fixedformat4j.exception.FixedFormatException;
import com.ancientprogramming.fixedformat4j.format.FixedFormatter;
import com.ancientprogramming.fixedformat4j.format.FormatInstructions;
import javafx.util.converter.LocalDateTimeStringConverter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by davidashirov on 12/1/17.
 */
public class LocalDateTimeFormatter implements FixedFormatter{
        @Override
        public Object parse(String s, FormatInstructions formatInstructions) throws FixedFormatException {
            DateTimeFormatter format = DateTimeFormatter.ofPattern(formatInstructions.getFixedFormatPatternData().getPattern());
            if ( (s!=null) && (s.length()>0)) {
                return LocalDateTime.parse(s,format);
            }
            return null;
        }
        @Override
        public String format(Object o, FormatInstructions formatInstructions) throws FixedFormatException {
            DateTimeFormatter format = DateTimeFormatter.ofPattern(formatInstructions.getFixedFormatPatternData().getPattern());
            if ((o!=null)){
                try {
                    return ((LocalDateTime) o).format(format);
                } catch (Exception e){
                    throw  new FixedFormatException("Error formatting object", e);
                }
            }
            return null;
        }
}
/*
0123456789123201701010          1000000000000123456789}               53112615            00000000000098}00000000000008}000972500000000000001{00000000000000{1111111000000000000000000000000000000000000000000000000000000{00000000000002{PPSHORTNAME                   PAYEENAME                             GENERAL ASSBLY      USD00000000000001}IBAN:                             BIC:
0123456789123201701010          1000000000000123456789}               53112615            00000000000098}00000000000008}000972500000000000001{00000000000000011111110000000000000000000000000000000000000000000000000000000{0000000000002{PPSHORTNAME                   PAYEENAME                             GENERAL ASSBLY      USD00000000000001}IBAN:                             BIC:
*/