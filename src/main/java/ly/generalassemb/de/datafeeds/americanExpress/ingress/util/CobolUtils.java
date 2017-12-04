package ly.generalassemb.de.datafeeds.americanExpress.ingress.util;

/**
 * Created by davidashirov on 11/30/17.
 */
import java.math.BigDecimal;
import java.math.BigInteger;

public class CobolUtils {

    /**
     * Pad with zero a given number
     * @param num  string representation of a number
     * @param w    the total number of digit
     * @param d    number of digit after the decimal point
     * @param signed  is it a signed number
     * @return  the zero-padded number as a string, COBOL number (signed or not)
     */
    public static String padNum(String num, int w, int d, boolean signed) {
        if (num == null) {
            return padNum(0, w, d, signed);
        }
        else {
            return padNum(Double.parseDouble(num), w, d, signed);
        }
    }

    /**
     * Pad with zero a given number
     * @param num  the number
     * @param w    the total number of digit
     * @param d    number of digit after the decimal point
     * @param signed  is it a signed number
     * @return  the zero-padded number s  string,  COBOL number (signed or not)
     */
    public static String padNum(double num, int w, int d, boolean signed) {
        // decimal point
        String format1 = "%." + d + "f";
        String temp = String.format(format1, num);
        temp = temp.replace(",", "").replace(".", ""); // remove decimal point and separator
        int i = Integer.parseInt(temp);
        if (i < 0) {
            w++;  //
        }
        // leading zeroes
        String format2 = String.format("%%0%dd", w + d);
        return (!signed) ? String.format(format2, i) : toCobolSignedString(String.format(format2, i));
    }

    /**
     * Build a string representation using the COBOL PIC S9()... style representation
     * Ascii based, not EBCDIC
     * @param snumber
     * @return string
     */
    public static String toCobolSignedString(String snumber) {
        int i = Integer.parseInt(snumber);
        char lastChar = snumber.charAt(snumber.length() - 1 );
        char value = (i >= 0) ? CobolUtils.getChar(true, lastChar) : CobolUtils.getChar(false, lastChar);
        StringBuffer sb = new StringBuffer(snumber);
        sb.setCharAt(sb.length() - 1, value);
        return (i >= 0) ? sb.toString() : sb.toString().substring(1); // if LT 0, remove the minus
    }

    /**
     * Returns a double from Cobol signed representation PIC S9()V9...
     * Ascii based, not EBCDIC
     * @param snumber
     * @param digit number of digit after the decimal point
     * @return double
     */
    public static double fromCobolSignedString(String snumber, int digit) {
        String value;
        char lastdigit = snumber.charAt(snumber.length() - 1);
        if (CobolUtils.isPositiveOverpunch(lastdigit)) {
            char reallastdigit = CobolUtils.getNumber(lastdigit);
            value = snumber.substring(0, snumber.length() - 1) + String.valueOf(reallastdigit);
        }
        else {
            char reallastdigit = CobolUtils.getNumber(lastdigit);
            value = "-" + snumber.substring(0, snumber.length() - 1) + String.valueOf(reallastdigit);

        }
        if (digit > 0) {
            value = value.substring(0, value.length() - digit + 1) + "." + value.substring(value.length() - digit);
        }

        BigDecimal bd = new BigDecimal(Double.parseDouble(value));
        bd = bd.setScale(digit,BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }

    public static void main(String[] args) {
        // num to cobol
        System.out.println(CobolUtils.padNum("-60398.74", 9, 2, true));
        System.out.println(CobolUtils.padNum(-60398.74, 9, 2, true));
        System.out.println(CobolUtils.padNum("60398.74", 9, 2, true));
        System.out.println(CobolUtils.padNum("42000", 5, 0, true));
        System.out.println(CobolUtils.padNum("-42000", 5, 0, true));
        System.out.println(CobolUtils.padNum("1.23", 6, 2, true));
        System.out.println(CobolUtils.padNum(1.23d, 6, 2, true));
        System.out.println(CobolUtils.padNum(-10d, 6, 0, true));
        System.out.println(CobolUtils.padNum(1d, 6, 0, true));
        System.out.println(CobolUtils.padNum(new BigDecimal(0).toEngineeringString(), 6, 0, true));
        System.out.println(CobolUtils.fromCobolSignedString("{", 0));
        // "-60398.74" --> "0000603987M"
        // -60398.74 --> "0000603987M"
        // "60398.74" --> "0000603987D"
        // "42000" --> "4200{"
        // "-42000" --> "4200}"
        // "1.23" --> "0000012C"
        // 1.23 --> "0000012C"
        System.out.println(CobolUtils.toCobolSignedString("123"));
        // "123" --> 12C

        // cobol to num
        System.out.println(CobolUtils.fromCobolSignedString("4200{", 0));
        System.out.println(CobolUtils.fromCobolSignedString("4200}", 0));
        System.out.println(CobolUtils.fromCobolSignedString("603987D", 2));
        System.out.println(CobolUtils.fromCobolSignedString("603987M", 2));
        // "4200{" --> 42000
        // "4200}" --> -42000
        // "603987D" --> 60398.74
        // "603987M" --> -60398.74
    }

    // ref :  https://github.com/ethiclab/cb2java/blob/master/src/net/sf/cb2java/types/Decimal.java
    private static char getNumber(char overpunch) {
        switch(overpunch) {
            case 'R':
            case 'I':
                return '9';
            case 'Q':
            case 'H':
                return '8';
            case 'P':
            case 'G':
                return '7';
            case 'O':
            case 'F':
                return '6';
            case 'N':
            case 'E':
                return '5';
            case 'M':
            case 'D':
                return '4';
            case 'L':
            case 'C':
                return '3';
            case 'K':
            case 'B':
                return '2';
            case 'J':
            case 'A':
                return '1';
            case '}':
            case '{':
                return '0';
        }
        throw new IllegalArgumentException("invalid char for a signed number: " + overpunch);
    }

    private static char getChar(boolean positive, char overpunch) {
        if (positive) {
            switch(overpunch) {
                case '0': return '{';
                case '1': return 'A';
                case '2': return 'B';
                case '3': return 'C';
                case '4': return 'D';
                case '5': return 'E';
                case '6': return 'F';
                case '7': return 'G';
                case '8': return 'H';
                case '9': return 'I';
            }
        }
        else {
            switch(overpunch) {
                case '9': return 'R';
                case '8': return 'Q';
                case '7': return 'P';
                case '6': return 'O';
                case '5': return 'N';
                case '4': return 'M';
                case '3': return 'L';
                case '2': return 'K';
                case '1': return 'J';
                case '0': return '}';
            }
        }
        throw new IllegalArgumentException("invalid number: " + overpunch);
    }

    private static boolean isPositiveOverpunch(char overpunch) {
        switch(overpunch) {
            case '{':
            case 'A':
            case 'B':
            case 'C':
            case 'D':
            case 'E':
            case 'F':
            case 'G':
            case 'H':
            case 'I':
                return true;
            case '}':
            case 'J':
            case 'K':
            case 'L':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
                return false;
        }
        throw new IllegalArgumentException("invalid char for a signed number: " + overpunch);
    }
}