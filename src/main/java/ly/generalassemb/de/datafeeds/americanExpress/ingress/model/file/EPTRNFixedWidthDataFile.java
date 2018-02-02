package ly.generalassemb.de.datafeeds.americanExpress.ingress.model.file;

import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;
import com.ancientprogramming.fixedformat4j.format.impl.FixedFormatManagerImpl;
import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.model.EPTRN.*;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.model.FixedWidthDataFile;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.model.FixedWidthDataFileComponent;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.model.S3CapableFWDF;
import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EPTRNFixedWidthDataFile extends S3CapableFWDF {
    @JsonIgnore
    private static final String id = "EPTRN";

    @JsonIgnore
    private final static FixedFormatManager manager = new FixedFormatManagerImpl();

    @JsonIgnore
    private final static ObjectMapper mapper = new ObjectMapper();


    private String fileName;

    @JsonIgnore
    private StringBuffer inputFile;

    @Override
    public String getId() {
        return (super.getId() == null)? id : super.getId();
    }

    EPTRNFixedWidthDataFile(File fileName) {
        try {
            this.parse(fileName);
        } catch (Exception e){
           e.printStackTrace();
        }
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    private Header header;
      List <PaymentIssued> payments;
    private Trailer trailer;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public List<PaymentIssued> getPayments() {
        return payments;
    }

    public void setPayments(List<PaymentIssued> payments) {
        this.payments = payments;
    }

    public Trailer getTrailer() {
        return trailer;
    }

    public void setTrailer(Trailer trailer) {
        this.trailer = trailer;
    }

    public EPTRNFixedWidthDataFile() {
        payments = new ArrayList<>();
    }

    @Override
    public String toString() {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ((Object) this).toString();
        }
    }
    @Override
    public FixedWidthDataFile parse(File fixedWidthDataFile) throws Exception {
        fileName = fixedWidthDataFile.getName();
        inputFile = new StringBuffer();

        BufferedReader reader = new BufferedReader(new FileReader(fixedWidthDataFile));
        String line;
        MerchantSubmission currentMerchantSubmission = null;
        PaymentIssued currentPaymentIssued = null;
        int lineNo = 0;
        while ((line = reader.readLine())!=null){
            ++lineNo;
            inputFile.append(line).append('\n');
            String htIndicator = line.substring(0,5); // first 5 bytes
            if ("DFHDR".equals(htIndicator)) {
                // when you see a header, it means you need to create a new reconciled payment object
                currentPaymentIssued = new PaymentIssued();
                this.header = new Header().parse(manager, line);
            } else if ("DFTRL".equals(htIndicator)){
                this.trailer = new Trailer().parse(manager,line);
                payments.add(currentPaymentIssued);
                currentPaymentIssued=null;
            } else {
                if (currentPaymentIssued == null){
                    throw new ParseException("Detail record not expected", lineNo);
                }
                String dtIndicator = line.substring(42,45);
                if ("100".equals(dtIndicator)) {
                    // payment
                    PaymentRecord paymentRecord = new PaymentRecord().parse(manager,line);
                    currentPaymentIssued.setPaymentRecord(paymentRecord);
                    this.payments.add(currentPaymentIssued);

                } else if ("210".equals(dtIndicator)) {
                    //SOC
                    SOCRecord socRecord = new SOCRecord().parse(manager,line);
                    currentMerchantSubmission = new MerchantSubmission();
                    currentMerchantSubmission.setSocRecord(socRecord);
                    currentPaymentIssued.getSubmissions().add(currentMerchantSubmission);
                }else if ("311".equals(dtIndicator)) {
                    if (currentMerchantSubmission==null){
                        throw new ParseException("ROC Record not expected", lineNo);
                    }
                    //ROC
                    ROCRecord roc = new ROCRecord().parse(manager,line);
                    currentMerchantSubmission.getRocRecords().add(roc);
                }else if ("220".equals(dtIndicator)) {
                    //Chargeback
                    ChargebackRecord chargebackRecord = new ChargebackRecord().parse(manager,line);
                    currentPaymentIssued.getChargebacks().add(chargebackRecord);
                }else if ("230".equals(dtIndicator)) {
                    //Adjustment
                    AdjustmentRecord adjustmentRecord = new AdjustmentRecord().parse(manager,line);
                    currentPaymentIssued.getAdjustments().add(adjustmentRecord);
                }else if ("240".equals(dtIndicator)) {
                    //Asset Billing
                    OtherRecord other = new OtherRecord().parse(manager,line);
                    currentPaymentIssued.getOther().add(other);
                }else if ("241".equals(dtIndicator)) {
                    //Take one commission
                    OtherRecord other = new OtherRecord().parse(manager,line);
                    currentPaymentIssued.getOther().add(other);
                }else if ("250".equals(dtIndicator)) {
                    //Other Fees
                    OtherRecord other = new OtherRecord().parse(manager,line);
                    currentPaymentIssued.getOther().add(other);
                } else {
                    throw new ParseException("Unexpected data record", lineNo);
                }

            }
        }
        reader.close();
        if (this.trailer == null){
            throw new ParseException("No trailer record after parsed file is completely consumed", lineNo);
        } else if (lineNo != this.trailer.getRecordCount()){
            throw new ParseException(String.format("Data file trailer indicates %d data records, but only %d were encountered",this.trailer.getRecordCount(),lineNo ),lineNo);
        }
        return this;
    }

    // given a directory name, dump
    // 1. Payment Records
    // 2. SOC Records
    // 3. ROC Records
    // 4. Adjustment Records
    // 5. Chargeback Records
    // 6. Other Records
    // 7. Header and Trailer Records
    // In CSV or TSV file format. Include column headers

    //

    private List<PaymentRecord> getAllPaymentRecords() {
        List<PaymentRecord> out = new ArrayList<>();
         this.payments.forEach(p->out.add(p.getPaymentRecord()));
         return out;
    }

    private List<ChargebackRecord> getAllChargebackRecords(){
        List<ChargebackRecord> out = new ArrayList<>();
        this.payments.forEach(p -> out.addAll(p.getChargebacks()));
        return out;
    }

    private List<AdjustmentRecord> getAllAdjustmentRecords(){
        List<AdjustmentRecord> out = new ArrayList<>();
        this.payments.forEach(p -> out.addAll(p.getAdjustments()) );
        return out;
    }
    private List<SOCRecord> getAllSOCRecords(){
        List<SOCRecord> out = new ArrayList<>();
        this.payments.forEach(p ->p.getSubmissions().forEach(s->out.add(s.getSocRecord())));
        return out;
    }
    private List<ROCRecord> getAllROCRecords(){
        List<ROCRecord> out = new ArrayList<>();
        this.payments.forEach(p ->p.getSubmissions().forEach(s->out.addAll(s.getRocRecords())));
        return out;
    }
    private List<OtherRecord> getAllOtherRecords(){
        List<OtherRecord> out = new ArrayList<>();
        this.payments.forEach(p->out.addAll(p.getOther()));
        return out;
    }
    @Override
    public Map<FixedWidthDataFileComponent, String> toLoadableComponents() throws Exception {
        Map<FixedWidthDataFileComponent,String> output = new HashMap<>();
        CsvMapper csvMapper = new CsvMapper();
        output.put(FixedWidthDataFileComponent.EPTRN_JSON_OBJECT,mapper.writeValueAsString(this));
        output.put(FixedWidthDataFileComponent.EPTRN_CSV_HEADER_COMPONENT,csvMapper.writer(csvMapper.schemaFor(Header.class).withHeader()).writeValueAsString(this.getHeader()));
        output.put(FixedWidthDataFileComponent.EPTRN_CSV_PAYMENT_COMPONENT, csvMapper.writer(csvMapper.schemaFor(PaymentRecord.class).withHeader()).writeValueAsString(this.getAllPaymentRecords()));
        output.put(FixedWidthDataFileComponent.EPTRN_CSV_CHARGEBACK_COMPONENT, csvMapper.writer(csvMapper.schemaFor(ChargebackRecord.class).withHeader()).writeValueAsString(this.getAllChargebackRecords()));
        output.put(FixedWidthDataFileComponent.EPTRN_CSV_ADJUSTMENT_COMPONENT,csvMapper.writer(csvMapper.schemaFor(AdjustmentRecord.class).withHeader()).writeValueAsString(this.getAllAdjustmentRecords()));
        output.put(FixedWidthDataFileComponent.EPTRN_CSV_OTHER_COMPONENT,csvMapper.writer(csvMapper.schemaFor(OtherRecord.class).withHeader()).writeValueAsString(this.getAllOtherRecords()));
        output.put(FixedWidthDataFileComponent.EPTRN_CSV_SOC_COMPONENT,csvMapper.writer(csvMapper.schemaFor(SOCRecord.class).withHeader()).writeValueAsString(this.getAllSOCRecords()));
        output.put(FixedWidthDataFileComponent.EPTRN_CSV_ROC_COMPONENT,csvMapper.writer(csvMapper.schemaFor(ROCRecord.class).withHeader()).writeValueAsString(this.getAllROCRecords()));
        output.put(FixedWidthDataFileComponent.EPTRN_CSV_TRAILER_COMPONENT,csvMapper.writer(csvMapper.schemaFor(Trailer.class).withHeader()).writeValueAsString(this.getTrailer()));
        output.put(FixedWidthDataFileComponent.EPTRN_FIXED_WIDTH_OBJECT,inputFile.toString());
        return output;
    }

    @Override
    public Map<FixedWidthDataFileComponent, String> toLoadableComponents(ObjectMapper objectMapper, CsvMapper csvMapper) throws Exception {
        Map<FixedWidthDataFileComponent,String> output = new HashMap<>();
        output.put(FixedWidthDataFileComponent.EPTRN_JSON_OBJECT, objectMapper.writeValueAsString(this));
        output.put(FixedWidthDataFileComponent.EPTRN_CSV_HEADER_COMPONENT,csvMapper.writer(csvMapper.schemaFor(Header.class).withHeader()).writeValueAsString(this.getHeader()));
        output.put(FixedWidthDataFileComponent.EPTRN_CSV_PAYMENT_COMPONENT, csvMapper.writer(csvMapper.schemaFor(PaymentRecord.class).withHeader()).writeValueAsString(this.getAllPaymentRecords()));
        output.put(FixedWidthDataFileComponent.EPTRN_CSV_CHARGEBACK_COMPONENT, csvMapper.writer(csvMapper.schemaFor(ChargebackRecord.class).withHeader()).writeValueAsString(this.getAllChargebackRecords()));
        output.put(FixedWidthDataFileComponent.EPTRN_CSV_ADJUSTMENT_COMPONENT,csvMapper.writer(csvMapper.schemaFor(AdjustmentRecord.class).withHeader()).writeValueAsString(this.getAllAdjustmentRecords()));
        output.put(FixedWidthDataFileComponent.EPTRN_CSV_OTHER_COMPONENT,csvMapper.writer(csvMapper.schemaFor(OtherRecord.class).withHeader()).writeValueAsString(this.getAllOtherRecords()));
        output.put(FixedWidthDataFileComponent.EPTRN_CSV_SOC_COMPONENT,csvMapper.writer(csvMapper.schemaFor(SOCRecord.class).withHeader()).writeValueAsString(this.getAllSOCRecords()));
        output.put(FixedWidthDataFileComponent.EPTRN_CSV_ROC_COMPONENT,csvMapper.writer(csvMapper.schemaFor(ROCRecord.class).withHeader()).writeValueAsString(this.getAllROCRecords()));
        output.put(FixedWidthDataFileComponent.EPTRN_CSV_TRAILER_COMPONENT,csvMapper.writer(csvMapper.schemaFor(Trailer.class).withHeader()).writeValueAsString(this.getTrailer()));
        output.put(FixedWidthDataFileComponent.EPTRN_FIXED_WIDTH_OBJECT,inputFile.toString());
        return output;
    }
}
