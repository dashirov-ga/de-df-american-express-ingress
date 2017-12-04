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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dashirov on 5/9/17.
 */
public class TestCobolUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestCobolUtils.class);
    private static final FixedFormatManager manager = new FixedFormatManagerImpl();

    @Test
    public void summaryEPAPE(){

        List<FileHeaderRecord> headers = new ArrayList<>();
        List<FileTrailerRecord> trailers = new ArrayList<>();
        List<PaymentRecord> payments = new ArrayList<>();
        List<PricingRecord> pricing = new ArrayList<>();
        List<SOCRecord> socs = new ArrayList<>();
        List<ROCRecord> rocs = new ArrayList<>();
        List<AdjustmentRecord> adjustments = new ArrayList<>();
        try {
            File sampleFile = new File("/Users/davidashirov/Source/GA/de-df-american-express-ingress/docs/EPAPE sample test file_fixed_final.txt");
            FileReader fileReader = new FileReader(sampleFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while((line=bufferedReader.readLine())!=null){
                String header_footer_indicator = line.substring(0,5);
                Object fileRecord;
                switch (header_footer_indicator){
                    case "DFHDR":
                        System.out.printf("%5s - %s (%d)\n", header_footer_indicator, "File Header", line.length());
                        fileRecord = manager.load(FileHeaderRecord.class, line);
                        System.out.println(fileRecord.toString());
                        if (fileRecord != null)
                            headers.add((FileHeaderRecord) fileRecord);
                        break;
                    case "DFTLR":
                        System.out.printf("%5s - %s (%d)\n", header_footer_indicator, "File Footer", line.length());
                        fileRecord = manager.load(FileTrailerRecord.class, line);
                        System.out.println(fileRecord.toString());
                        if (fileRecord != null)
                            trailers.add((FileTrailerRecord) fileRecord);
                        break;
                    default:
                        String recordId = line.substring(32,35);

                        switch (recordId){
                            case "100":
                                System.out.printf("%3s - %s (%d)\n",recordId,"Payment Record",line.length());
                                fileRecord = manager.load(PaymentRecord.class, line);
                                System.out.println(fileRecord.toString());
                                if (fileRecord!=null)
                                    payments.add((PaymentRecord)fileRecord);
                                break;
                            case "110":
                                System.out.printf("%3s - %s (%d)\n",recordId,"Pricing Summary Record",line.length());
                                fileRecord = manager.load(PricingRecord.class, line);
                                System.out.println(fileRecord.toString());
                                if (fileRecord!=null)
                                    pricing.add((PricingRecord)fileRecord);
                                break;
                            case "210":
                                System.out.printf("%3s - %s (%d)\n",recordId,"SOC Record",line.length());
                                try {
                                    fileRecord = manager.load(SOCRecord.class,line);
                                    System.out.println(fileRecord.toString());
                                    if (fileRecord!=null)
                                        socs.add((SOCRecord) fileRecord);
                                } catch (Exception e){
                                    System.out.println(e.getMessage());
                                }

                                break;
                            case "260":
                                System.out.printf("%3s - %s (%d)\n",recordId,"ROC Record",line.length());

                                try {
                                    fileRecord = manager.load(ROCRecord.class,line);
                                    System.out.println(fileRecord.toString());
                                    if (fileRecord!=null)
                                        rocs.add((ROCRecord) fileRecord);
                                } catch (Exception e){
                                    System.out.println(e.getMessage());
                                }

                                break;
                            case "230":
                                System.out.printf("%3s - %s (%d)\n",recordId,"Adjustment Record",line.length());
                                try {
                                    fileRecord = manager.load(AdjustmentRecord.class,line);
                                    System.out.println(fileRecord.toString());
                                    if (fileRecord!=null)
                                        adjustments.add((AdjustmentRecord)fileRecord);
                                } catch (Exception e){
                                    System.out.println(e.getMessage());
                                }
                                break;
                        }

                }




            }
            if (!headers.isEmpty()) {
                System.out.println("HEADERS PARSED:");
                System.out.println(FileHeaderRecord.toCsv(headers));
            }

            if (!trailers.isEmpty()) {
                System.out.println("TRAILERS PARSED:");
                System.out.println(FileTrailerRecord.toCsv(trailers));
            }

            if (!adjustments.isEmpty()) {
                System.out.println("ADJUSTMENT PARSED:");
                System.out.println(AdjustmentRecord.toCsv(adjustments));
            }
            if (!socs.isEmpty()) {
                System.out.println("SOCs PARSED:");
                System.out.println(SOCRecord.toCsv(socs));
            }
            if (!rocs.isEmpty()) {
                System.out.println("ROCs PARSED:");
                System.out.println(ROCRecord.toCsv(rocs));
            }
            if (!pricing.isEmpty()) {
                System.out.println("Pricing PARSED:");
                System.out.println(PricingRecord.toCsv(pricing));
            }
            if (!payments.isEmpty()) {
                System.out.println("Payments PARSED:");
                System.out.println(PaymentRecord.toCsv(payments));
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
