package ly.generalassemb.de.datafeeds.americanExpress.ingress;

import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;
import com.ancientprogramming.fixedformat4j.format.impl.FixedFormatManagerImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.model.EPAPE.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by dashirov on 5/9/17.
 */



public class TestCobolUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestCobolUtils.class);
    private static final FixedFormatManager manager = new FixedFormatManagerImpl();




 /**
    @Test
    public void testEPAPEParser(){
        List<EPAPEFile> fileProcessed = new ArrayList<>();
        try {
           int lineNo=0;
           File file = new File("/Users/davidashirov/Source/GA/de-df-american-express-ingress/docs/EPAPE sample Fixed.txt");
           FileReader fileReader = new FileReader(file);
           BufferedReader reader = new BufferedReader(fileReader);
           String line;
           FixedFormatManager manager = new FixedFormatManagerImpl();

           EPAPEFile aFile = null;
           while ((line = reader.readLine()) != null) {
               lineNo++;
               // read the file one line at a time until it is exhausted
               // check if the line look like header or trailer. If it does, open or close a payment batch
               // otherwise process file contents
               // LOGGER.debug(String.format("Read line '%s'", line));

               String header_footer_indicator = line.substring(0, 5);
               if (aFile!=null && header_footer_indicator.equals("DFTLR")) {
                   LOGGER.debug(String.format("%03d Line is a Trailer Record. Stop reading from this file.",lineNo));
                   Trailer ftr;
                   if ((ftr = manager.load(Trailer.class, line)) != null) {
                       aFile.setTrailerRecord(ftr);
                       fileProcessed.add(aFile);
                   }
                   aFile = null;
                   continue; // stop reading the data file after file trailer
               } else if (aFile == null && header_footer_indicator.equals("DFHDR")) {
                   LOGGER.debug(String.format("%03d Line is a Header Record. This is a new file. ",lineNo));
                   Header fhr;
                   if ((fhr = manager.load(Header.class, line)) != null)
                       aFile = EPAPEFile.FileBuilder.aFile().withHeaderRecord(fhr).withPaymentList(new ArrayList<>()).build();
                   continue; // move onto the next line
               }

              // LOGGER.debug("Line is a Detail Record. Determining its type ");

               String recordId = line.substring(32, 35);
                   // while processing the file, determine wht record type the line represents by examining
                   // combined record code

               if (recordId.equals("100")) {
                   LOGGER.debug(String.format("%03d Line is a type %s  %s record",lineNo, recordId,PaymentRecord.class));
                   PaymentRecord p = manager.load(PaymentRecord.class, line);
                   if (p != null) {
                       if (aFile == null){
                            throw new ParseException("Payment Record must follow File Header Record");
                       }
                       ReconciledPayment aPayment = ReconciledPayment.ReconciledPaymentBuilder.aReconciledPayment().withPayment(p).build();
                       aFile.getPaymentList().add(aPayment);

                   }
               } else if (recordId.equals("110")) {// Skip pricing records for now, not interested
                   LOGGER.debug(String.format("%03d Line is a type %s %s record",lineNo, recordId,PricingRecord.class));
                   LOGGER.debug("Not interested in pricing records");
               } else if (recordId.equals("210")) {
                   LOGGER.debug(String.format("%03d Line is a type %s  %s record",lineNo, recordId,SOCRecord.class));
                   // Assert SOC Record was seen after Payment record, which has been seen after Header record
                   SOCRecord soc = manager.load(SOCRecord.class, line);
                   if (soc != null) {
                       if (aFile==null || aFile.getPaymentList()==null){
                           throw new ParseException("SOC Record must follow Payment Record, File Header Record");
                       }
                       ReconciledPayment currentPayment = aFile.getPaymentList().get(aFile.getPaymentList().size() - 1);
                       soc.setPaymentId( currentPayment.getPaymentSummary().getPaymentId() );
                       currentPayment.getMerchantSubmissions().add(
                               MerchantSubmission.Builder.aMerchantSubmission().withSocRecord(soc).build());
                   }
               } else if (recordId.equals("260")) {
                   LOGGER.debug(String.format("%03d Line is a type %s  %s record",lineNo, recordId,ROCRecord.class));
                   ROCRecord roc = manager.load(ROCRecord.class, line);
                   if (roc != null ) {
                           if (aFile == null || aFile.getPaymentList() == null || aFile.getPaymentList().size() == 0 ){
                               throw new ParseException("ROC Record must follow SOC Record, Payment Record, File Header Record");
                           }
                           ReconciledPayment currentPayment = aFile.getPaymentList().get(aFile.getPaymentList().size() - 1);
                           if (currentPayment == null){
                               throw new ParseException("ROC Record must follow SOC Record, Payment Record, File Header Record");
                           }

                           if ( currentPayment.getMerchantSubmissions() == null || currentPayment.getMerchantSubmissions().size() == 0) {
                              throw new ParseException("ROC Record must follow SOC Record, Payment Record, File Header Record");

                           }
                           MerchantSubmission currentMerchantSubmission = currentPayment.getMerchantSubmissions().get(currentPayment.getMerchantSubmissions().size() - 1);
                           roc.setPaymentId(currentMerchantSubmission.getSocRecord().getPaymentId());
                           roc.setSocId(currentMerchantSubmission.getSocRecord().getSocId());
                           currentMerchantSubmission.getRocRecords().add(roc);

                   }
               } else if (recordId.equals("230")) {
                   LOGGER.debug(String.format("%03d Line is a type %s %s record",lineNo,recordId,AdjustmentRecord.class));
                   AdjustmentRecord adj = manager.load(AdjustmentRecord.class, line);
                   if (adj != null){
                       if (aFile == null){
                           throw new ParseException("Asjustment record must follow Payment Record, File Header Record");
                       }
                       ReconciledPayment currentPayment = aFile.getPaymentList().get(aFile.getPaymentList().size() - 1);

                       adj.setPaymentId(currentPayment.getPaymentSummary().getPaymentId());
                       currentPayment.getAdjustments().add(adj);

                   }

               }

           }
           reader.close();
           fileReader.close();
           System.out.println(fileProcessed);
           List<PaymentRecord> p = new ArrayList<>();
           List<SOCRecord> s = new ArrayList<>();
           List<ROCRecord> r = new ArrayList<>();
           List<AdjustmentRecord> a = new ArrayList<>();
            for ( EPAPEFile epapeFile: fileProcessed) {
                for ( ReconciledPayment reconciledPayment : epapeFile.getPaymentList()) {
                    p.add(reconciledPayment.getPaymentSummary());
                    for ( MerchantSubmission submission  : reconciledPayment.getMerchantSubmissions()) {
                             s.add(submission.getSocRecord());
                             r.addAll(submission.getRocRecords());
                    }
                    a.addAll(reconciledPayment.getAdjustments());
                }
            }
            System.out.println(CsvUtil.toCSV(p,PaymentRecord.class));
            System.out.println(CsvUtil.toCSV(a,AdjustmentRecord.class));
            System.out.println(CsvUtil.toCSV(s,SOCRecord.class));
            System.out.println(CsvUtil.toCSV(r,ROCRecord.class));
       } catch (  IOException | ParseException e){
           e.printStackTrace();
       }
    }
 */
    @Test
    public void testAnnotations(){
        AdjustmentRecord b =
                AdjustmentRecord.Builder.anAdjustmentRecord()
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

}
