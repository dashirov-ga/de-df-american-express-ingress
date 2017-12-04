package ly.generalassemb.de.datafeeds.americanExpress.ingress;

/**
 * Created by dashirov on 5/10/17.
 */

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dashirov on 5/10/17.
 */
public enum AmexSignSuffix {
    A("A", 1L, 1L, "DEBIT"),
    B("B", 2L, 1L, "DEBIT"),
    C("C", 3L, 1L, "DEBIT"),
    D("D", 4L, 1L, "DEBIT"),
    E("E", 5L, 1L, "DEBIT"),
    F("F", 6L, 1L, "DEBIT"),
    G("G", 7L, 1L, "DEBIT"),
    H("H", 8L, 1L, "DEBIT"),
    I("I", 9L, 1L, "DEBIT"),
    LEFT_CURL("{", 0L, 1L, "DEBIT"),
    J("J", 1L, -1L, "CREDIT"),
    K("K", 2L, -1L, "CREDIT"),
    L("L", 3L, -1L, "CREDIT"),
    M("M", 4L, -1L, "CREDIT"),
    N("N", 5L, -1L, "CREDIT"),
    O("O", 6L, -1L, "CREDIT"),
    P("P", 7L, -1L, "CREDIT"),
    Q("Q", 8L, -1L, "CREDIT"),
    R("R", 9L, -1L, "CREDIT"),
    RIGHT_CURL("}", 0L, -1L, "CREDIT");

    private String characterValue;
    private long numericValue;
    private long multiplier;
    private String type;

    public String toString() {
        return this.characterValue;
    }

    public Long toLong() {
        return this.numericValue;
    }

    AmexSignSuffix(String characterValue, Long numericValue, Long multiplier, String type) {
        this.characterValue = characterValue;
        this.numericValue = numericValue;
        this.multiplier = multiplier;
        this.type = type;

    }


    public Long toLong (String numericPrefix) {
        Long prefix = 0L;
        if (numericPrefix != null && !numericPrefix.equals("")) {
             prefix = Long.valueOf(numericPrefix);
        }
        return ( prefix  * 10 + numericValue ) * multiplier;
    }

    public String toType() {
        return type;
    }

    public static AmexSignSuffix fromCharacter(String character){
        for (AmexSignSuffix s: AmexSignSuffix.values()){
            if (s.characterValue.equalsIgnoreCase(character))
                return s;
        }
        return null;
    }

    public static void main(String[] args){

        if (args.length != 1 ){
            System.err.println("Usage: java AmexSignSuffix <amex_signed_string>");
            System.exit(-1);
        }
        Pattern inputValidator = Pattern.compile("^[0-9]*[A-R}{]$");
        Matcher m = inputValidator.matcher(args[0]);
        if (m.matches()){
            System.out.println(
                AmexSignSuffix.fromCharacter( args[0].substring(args[0].length()-1)).toLong(args[0].substring(0, args[0].length() - 1))
            );
            System.exit(0);
        }
        System.err.println(args[0] + ": invalid input. Does it conform to '^[0-9]*[A-R}{]$'?");
    }

}