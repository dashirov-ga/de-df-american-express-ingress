package ly.generalassemb.de.datafeeds.americanExpress.ingress;

import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;
import com.ancientprogramming.fixedformat4j.format.impl.FixedFormatManagerImpl;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.model.EPAPE.*;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.lang3.*;

import java.io.*;
import java.util.Date;

/**
 * Created by dashirov on 5/9/17.
 */
public class TestCobolUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestCobolUtils.class);
    private static final FixedFormatManager manager = new FixedFormatManagerImpl();

    @Test
    public void summaryEPAPE(){
        // "amex_payee_number","payment_year","payment_number","record_type","detail_record_type","payment_date","payment_amount","debit_balance_amount","aba_bank_number","payee_direct_deposit_acount_number"
        // "6318979531","2017","171A8468","1","0","2017-06-22","5091090","0","53112615","0008746"

        String serializedString =
                        StringUtils.rightPad("XxxxxxxxxX",10," ") +
                        StringUtils.rightPad("___",3," ")+
                        StringUtils.rightPad("20170101",8," ")+
                        StringUtils.leftPad(" ",1," ")+
                        StringUtils.rightPad(" ",10," ")+
                        StringUtils.leftPad("1",1,"0")+
                        StringUtils.rightPad("00",2," ")+
                        StringUtils.leftPad(" ",5," ")+
                        StringUtils.leftPad("1}",15,"0")+ // SettlementAmount S#
                        StringUtils.rightPad("XxxxxxxxxxxxxxX",15," ")+
                        StringUtils.rightPad("____________________",20," ")+
                        StringUtils.leftPad("0A",15,"0")+ // SettlementGrossAmount S#
                        StringUtils.leftPad("0A",15,"0")+ // TaxAmount S#
                        StringUtils.leftPad("2222222",7,"0")+ // TaxRate #
                        StringUtils.leftPad("0A",15,"0")+ // ServiceFeeAmount S#
                        StringUtils.leftPad(" ",15," ")+ // Filler S#
                        StringUtils.leftPad("1111111",7,"0")+ // ServiceFeeRate #
                        StringUtils.leftPad(" ",55," ")+ // Filler S#
                        StringUtils.leftPad("{",15,"0")+ // Filler S#
                        StringUtils.rightPad("PPSHORTNAME",30," ") +
                        StringUtils.rightPad("PAYEENAME",38," ") +
                        StringUtils.rightPad("GENERAL ASSBLY",20," ") +
                        StringUtils.rightPad("USD",3," ") +
                        StringUtils.leftPad("{",15,"0")+ // PreviousDebitBalance S#
                        StringUtils.rightPad("IBAN:",34," ") +
                        StringUtils.rightPad("BIC:",12," ") +
                        StringUtils.rightPad(" ",55," ")
                        ;

        System.out.println("'" +serializedString+"'");
        System.out.println(serializedString.length());

        PaymentRecord record = manager.load(PaymentRecord.class, serializedString);
        System.out.println(record.toString());

        System.out.println("'" + manager.export(record) +"'");

        Assert.assertEquals("Serialized -> Deserialized -> Serialized: NOK",serializedString,manager.export(record));
System.out.println("Actual:");
        String sample=
"979000003700220130620000000000001000000000000004981168H                                   00000005076091H00000000008629M000000000000000086293O00000000000000{000000000000000000000{0000{00000000000000{0000{00000000000000{00000000000000{                              MXLSAT DMWCHGB WUZWIUU                PRIMARY             AUD00000000000000{                                                                                                    ";
        PaymentRecord sampleRecord = manager.load(PaymentRecord.class, sample);
        System.out.println(sampleRecord.toString());

        try {
            File sampleFile = new File("/Users/davidashirov/Source/GA/de-df-american-express-ingress/docs/EPAPE sample test file_fixed_final.txt");
            FileReader fileReader = new FileReader(sampleFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while((line=bufferedReader.readLine())!=null){
                String header_footer_indicator = line.substring(0,5);
                switch (header_footer_indicator){
                    case "DFHDR":
                        System.out.printf("%5s - %s (%d)\n", header_footer_indicator, "File Header", line.length());
                        break;
                    case "DFTLR":
                        System.out.printf("%5s - %s (%d)\n", header_footer_indicator, "File Footer", line.length());
                        break;
                    default:
                        String recordId = line.substring(32,35);
                        Object fileRecord;
                        switch (recordId){
                            case "100":
                                System.out.printf("%3s - %s (%d)\n",recordId,"Payment Record",line.length());
                                fileRecord = manager.load(PaymentRecord.class, line);
                                System.out.println(fileRecord.toString());
                                break;
                            case "110":
                                System.out.printf("%3s - %s (%d)\n",recordId,"Pricing Summary Record",line.length());
                                fileRecord = manager.load(PricingRecord.class, line);
                                System.out.println(fileRecord.toString());
                                break;
                            case "210":
                                System.out.printf("%3s - %s (%d)\n",recordId,"SOC Record",line.length());
                                System.out.println(sample.substring(13,21));
                                System.out.println(sample.substring(40,48));
                                try {
                                    fileRecord = manager.load(SOCRecord.class,sample);
                                    System.out.println(fileRecord.toString());
                                } catch (Exception e){
                                    System.out.println(e.getMessage());
                                }
                                break;
                            case "260":
                                System.out.printf("%3s - %s (%d)\n",recordId,"ROC Record",line.length());

                                try {
                                    fileRecord = manager.load(ROCRecord.class,sample);
                                    System.out.println(fileRecord.toString());
                                } catch (Exception e){
                                    System.out.println(e.getMessage());
                                }

                                break;
                            case "230":
                                System.out.printf("%3s - %s (%d)\n",recordId,"Adjustment Record",line.length());
                                try {
                                    fileRecord = manager.load(AdjustmentRecord.class,sample);
                                    System.out.println(fileRecord.toString());
                                } catch (Exception e){
                                    System.out.println(e.getMessage());
                                }
                                break;
                        }

                }




            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    @Test
    public void testAnnotations(){
        AdjustmentRecord b =
                AdjustmentRecordBuilder.anAdjustmentRecord()
                        .withAdjustmentMessageDescription("Desc").
                        withSettlementDate(new Date()).build();
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper.schemaFor(AdjustmentRecord.class).withColumnSeparator(',').withHeader();
        ObjectWriter myObjectWriter = mapper.writer(schema);
        try {
            System.out.println(myObjectWriter.writeValueAsString(b));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        System.out.println(b.toString());


    }

    @Test
    public void testAdjustmentRecordToCSV(){
        AdjustmentRecord b = AdjustmentRecordBuilder.anAdjustmentRecord().withAdjustmentRecordMessageCode("TEST").withSettlementDate(new Date()).build();
        System.out.println(b.toCsv());
        System.out.println(b.toString());
    }

    @Test
    public void testPaymentRecordToCSV(){
        PaymentRecord b = PaymentRecordBuilder.aPaymentRecord().withSEBankAccountNumber("12312312").withSettlementDate(new Date()).build();
        System.out.println(b.toCsv());
        System.out.println(b.toString());
    }

}
