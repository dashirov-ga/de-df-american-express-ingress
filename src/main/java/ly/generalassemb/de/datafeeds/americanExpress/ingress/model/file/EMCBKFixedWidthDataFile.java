package ly.generalassemb.de.datafeeds.americanExpress.ingress.model.file;

import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;
import com.ancientprogramming.fixedformat4j.format.impl.FixedFormatManagerImpl;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.model.EMCBK.Detail;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.model.EMCBK.Header;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.model.EMCBK.Trailer;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.model.FixedWidthDataFile;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.model.FixedWidthDataFileComponent;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.model.NotRegisteredException;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.model.S3CapableFWDF;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EMCBKFixedWidthDataFile extends S3CapableFWDF {
    @JsonIgnore
    private final static ObjectMapper mapper = new ObjectMapper();
    @JsonIgnore
    private final static FixedFormatManager manager = new FixedFormatManagerImpl();
    @JsonIgnore
    private static final String id = "EMCBK";

    private String fileName;

    @JsonIgnore
    private StringBuffer inputFile;
    public String getFileName() {
        return fileName;
    }
    public String getInputFile() {
        return inputFile.toString();
    }

    private Header header;
    private List<Detail> details;
    private Trailer trailer;

    public void put(Trailer t) {
        this.trailer = t;
    }

    public EMCBKFixedWidthDataFile() {
        details = new ArrayList<>();
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public List<Detail> getDetails() {
        return details;
    }

    public void setDetails(List<Detail> details) {
        this.details = details;
    }

    public Trailer getTrailer() {
        return trailer;
    }

    public void setTrailer(Trailer trailer) {
        this.trailer = trailer;
    }

    @Override
    public FixedWidthDataFile parse(File fixedWidthDataFile) throws Exception {
        this.inputFile = new StringBuffer();
        this.fileName = fixedWidthDataFile.getName();
        BufferedReader reader = new BufferedReader(new FileReader(fixedWidthDataFile));
        String line;
        int lineNo = 0;
        while ((line = reader.readLine()) != null) {
            ++lineNo;
            this.inputFile.append(line).append("\n");
            String typeIndicator = line.substring(0, 1);
            switch (typeIndicator) {
                case "H":
                    this.header = new Header().parse(manager, line);
                    break;
                case "T":
                    this.trailer = new Trailer().parse(manager, line);
                    break;
                case "D":
                    this.details.add(new Detail().parse(manager, line));
                    break;
                default:
                    throw new NotRegisteredException("Don't know how to parse this line.");
            }
        }
        return this;
    }

    @Override
    public String toString() {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return super.toString();
        }
    }

    @Override
    public String getId() {
        return (super.getId() == null)? id : super.getId();
    }

    @Override
    public Map<FixedWidthDataFileComponent, String> toLoadableComponents() throws Exception {
        Map<FixedWidthDataFileComponent,String> output = new HashMap<>();
        CsvMapper csvMapper = new CsvMapper();
        // processed file is a json serialized "this"
        output.put(FixedWidthDataFileComponent.EMCBK_JSON_OBJECT,mapper.writeValueAsString(this));
        output.put(FixedWidthDataFileComponent.EMCBK_CSV_HEADER_COMPONENT,csvMapper.writer(csvMapper.schemaFor(Header.class).withHeader()).writeValueAsString(this.header));
        output.put(FixedWidthDataFileComponent.EMCBK_CSV_TRAILER_COMPONENT,csvMapper.writer(csvMapper.schemaFor(Trailer.class).withHeader()).writeValueAsString(this.trailer));
        output.put(FixedWidthDataFileComponent.EMCBK_CSV_CHARGEBACK_COMPONENT,csvMapper.writer(csvMapper.schemaFor(Detail.class).withHeader()).writeValueAsString(this.details));
        output.put(FixedWidthDataFileComponent.EMCBK_FIXED_WIDTH_OBJECT,inputFile.toString());
        return output;
    }

    @Override
    public Map<FixedWidthDataFileComponent, String> toLoadableComponents(ObjectMapper objectMapper, CsvMapper csvMapper) throws Exception {
        Map<FixedWidthDataFileComponent,String> output = new HashMap<>();
        // processed file is a json serialized "this"
        output.put(FixedWidthDataFileComponent.EMCBK_JSON_OBJECT,objectMapper.writeValueAsString(this));
        output.put(FixedWidthDataFileComponent.EMCBK_CSV_HEADER_COMPONENT,csvMapper.writer(csvMapper.schemaFor(Header.class).withHeader()).writeValueAsString(this.header));
        output.put(FixedWidthDataFileComponent.EMCBK_CSV_TRAILER_COMPONENT,csvMapper.writer(csvMapper.schemaFor(Trailer.class).withHeader()).writeValueAsString(this.trailer));
        output.put(FixedWidthDataFileComponent.EMCBK_CSV_CHARGEBACK_COMPONENT,csvMapper.writer(csvMapper.schemaFor(Detail.class).withHeader()).writeValueAsString(this.details));
        output.put(FixedWidthDataFileComponent.EMCBK_FIXED_WIDTH_OBJECT,inputFile.toString());
        return output;
    }
}
