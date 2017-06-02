package ly.generalassemb.de.datafeeds.americanExpress.ingress.util;

import ly.generalassemb.de.datafeeds.americanExpress.ingress.AmexSignSuffix;

/**
 * Created by dashirov on 5/12/17.
 */
public class AmountParser {
    public static Long toLong(String prefix, String suffix){
        AmexSignSuffix ss = AmexSignSuffix.fromCharacter(suffix);
        if (ss != null )
            return ss.toLong( (prefix!=null && prefix.length()>0) ? prefix : "" );
        return null;
    }
    public static Long toLong(String amexSignedAmount){
        if ( (amexSignedAmount!=null) && (amexSignedAmount.length()>0))
           return  toLong(amexSignedAmount.substring(0,amexSignedAmount.length()-1),
                       amexSignedAmount.substring(amexSignedAmount.length()-1));
        return null;
    }
}
