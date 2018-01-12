package ly.generalassemb.de.datafeeds.americanExpress.ingress.model.file;

import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;
import com.ancientprogramming.fixedformat4j.format.impl.FixedFormatManagerImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.model.EPAPE.*;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.model.FixedWidthDataFile;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.model.FixedWidthDataFileComponent;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.model.NotRegisteredException;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.model.S3CapableFWDF;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EPAPEFixedWidthDataFile extends S3CapableFWDF {
    private static final String id = "EPAPE";
    private final static FixedFormatManager manager = new FixedFormatManagerImpl();
    private final static ObjectMapper mapper = new ObjectMapper();
    private String fileName;
    private StringBuffer inputFile;

    List<ReconciledPayment> paymentList;


    public void put(ReconciledPayment reconciledPayment) {
        this.paymentList.add(reconciledPayment);
    }

    public EPAPEFixedWidthDataFile(File fileName) {
        this.fileName = fileName.getName();
        try {
            this.parse(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public EPAPEFixedWidthDataFile() {
        this.paymentList = new ArrayList<>();
    }

    public List<ReconciledPayment> getPaymentList() {
        return paymentList;
    }

    public void setPaymentList(List<ReconciledPayment> paymentList) {
        this.paymentList = paymentList;
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
        BufferedReader reader = new BufferedReader(new FileReader(fixedWidthDataFile));
        String line;


        ReconciledPayment currentReconciledPayment = null;
        MerchantSubmission currentMerchantSubmission = null;
        String currentSubmissionSeBranchNumber = null;

        int lineNo = 0;
        while ((line = reader.readLine()) != null) {
            ++lineNo;
            String htIndicator = line.substring(0, 5);
            if ("DFHDR".equals(htIndicator)) {
                // when you see a header, it means you need to create a new reconciled payment object
                currentReconciledPayment = new ReconciledPayment();
                currentReconciledPayment.setHeader(new Header().parse(manager, line));

            } else {

                if (currentReconciledPayment == null)
                    throw new ParseException("Trailer record not expected", lineNo);


                if ("DFTLR".equals(htIndicator)) {
                    currentReconciledPayment.setTrailer(new Trailer().parse(manager, line));
                    this.paymentList.add(currentReconciledPayment);
                    currentReconciledPayment = null; // TODO: test if breaks
                } else {
                    String dtIndicator = line.substring(32, 35);
                    if ("100".equals(dtIndicator)) {
                        // payment summary
                        currentReconciledPayment.put(new PaymentRecord().parse(manager, line));
                    } else if ("110".equals(dtIndicator)) {
                        // pricing summary
                        currentReconciledPayment.put(new PricingRecord().parse(manager, line));
                    } else {
                        if ("210".equals(dtIndicator)) {
                            // summary of charge
                            SOCRecord soc = new SOCRecord().parse(manager, line);
                            // SOC always opens a new submission!

                            // 1. If previous submission is open for data accumulation, close it and push to master
                            if (currentMerchantSubmission != null)
                                currentReconciledPayment.put(currentMerchantSubmission);
                            // 2. Open a new submission with a SOC record
                            currentMerchantSubmission = new MerchantSubmission();
                            currentMerchantSubmission.put(soc);
                            // 3. Remember Branch number to interrupt accumulation if SOC-less adjustments
                            // for another branch start pouring in
                            currentSubmissionSeBranchNumber = soc.getSubmissionSeBranchNumber();

                        } else if ("260".equals(dtIndicator)) {
                            // record of charge
                            if (currentMerchantSubmission == null)
                                throw new ParseException("ROC record not expected", lineNo);
                            currentMerchantSubmission.put(new ROCRecord().parse(manager, line));
                        } else if ("230".equals(dtIndicator)) {
                            // adjustment
                            // These are tricky: usually they follow SOC/ROCs, but sometimes they're just on their own
                            // E.g. When you haven't created any new charges or refunds (ROCs) but Chargebacks were there
                            // and Amex reached out to you to satisfy the adjustments.
                            AdjustmentRecord adjustmentRecord = new AdjustmentRecord().parse(manager, line);

                            if (currentSubmissionSeBranchNumber == null) {
                                currentMerchantSubmission = new MerchantSubmission();
                                currentSubmissionSeBranchNumber = adjustmentRecord.getSubmissionSeBranchNumber();
                            } else if (!adjustmentRecord.getSubmissionSeBranchNumber().equals(currentSubmissionSeBranchNumber)) {
                                // If new branch is reporting, ship out previous submission and start a new one
                                currentReconciledPayment.put(currentMerchantSubmission);
                                currentMerchantSubmission = new MerchantSubmission();
                                currentMerchantSubmission.put(adjustmentRecord);
                            }
                            currentMerchantSubmission.put(adjustmentRecord);

                        } else {
                            throw new NotRegisteredException("Don't know how to parse this line.");
                        }
                    }
                }
            }
        }
        return this;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Map<FixedWidthDataFileComponent, String> toLoadableComponents() throws Exception {
        Map<FixedWidthDataFileComponent, String> output = new HashMap<>();
        CsvMapper csvMapper = new CsvMapper();
        // processed file is a json serialized "this"
        output.put(FixedWidthDataFileComponent.EPAPE_JSON_OBJECT, mapper.writeValueAsString(this));

        output.put(FixedWidthDataFileComponent.EPAPE_CSV_HEADER_COMPONENT,

                csvMapper.writer(csvMapper.schemaFor(Header.class).withHeader())
                        .writeValueAsString(this.getPaymentList().stream().map(ReconciledPayment::getHeader).collect(Collectors.toList()))

        );

        output.put(FixedWidthDataFileComponent.EPAPE_CSV_PAYMENT_COMPONENT,

                csvMapper.writer(csvMapper.schemaFor(PaymentRecord.class).withHeader())
                        .writeValueAsString(this.getPaymentList()
                                .stream()
                                .map(ReconciledPayment::getPaymentSummary)
                                .collect(Collectors.toList()))

        );

        output.put(FixedWidthDataFileComponent.EPAPE_CSV_ADJUSTMENT_COMPONENT,
                csvMapper.writer(csvMapper.schemaFor(AdjustmentRecord.class).withHeader())
                        .writeValueAsString(
                                this.getPaymentList()
                                        .stream()
                                        .map(ReconciledPayment::getMerchantSubmissions)
                                        .map(
                                                merchantSubmissions -> merchantSubmissions
                                                        .stream()
                                                        .map(MerchantSubmission::getAdjustments)
                                        )
                                        .collect(Collectors.toList())
                        )

        );

        output.put(FixedWidthDataFileComponent.EPAPE_CSV_SOC_COMPONENT,

                csvMapper.writer(csvMapper.schemaFor(SOCRecord.class).withHeader()).
                        writeValueAsString(
                                this.getPaymentList()
                                        .stream()
                                        .map(ReconciledPayment::getMerchantSubmissions)
                                        .map(
                                                merchantSubmissions -> merchantSubmissions
                                                        .stream()
                                                        .map(MerchantSubmission::getSocRecord)
                                        )
                                        .collect(Collectors.toList())
                        )

        );

        output.put(FixedWidthDataFileComponent.EPAPE_CSV_ROC_COMPONENT,
                csvMapper.writer(csvMapper.schemaFor(SOCRecord.class).withHeader())
                        .writeValueAsString(
                                this.getPaymentList()
                                        .stream()
                                        .map(ReconciledPayment::getMerchantSubmissions)
                                        .map(
                                                merchantSubmissions -> merchantSubmissions.stream()
                                                        .map(MerchantSubmission::getRocRecords)
                                        )
                                        .collect(Collectors.toList())
                        )

        );

        output.put(FixedWidthDataFileComponent.EPAPE_CSV_TRAILER_COMPONENT,
                csvMapper.writer(csvMapper.schemaFor(Trailer.class).withHeader())
                        .writeValueAsString(this.getPaymentList().stream().map(ReconciledPayment::getTrailer).collect(Collectors.toList()))

        );
        // input file is a collection of all file lines "as-is"
        output.put(FixedWidthDataFileComponent.EPAPE_FIXED_WIDTH_OBJECT, inputFile.toString());
        return output;
    }

}
