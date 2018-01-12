package ly.generalassemb.de.datafeeds.americanExpress.ingress.model.file;

import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;
import com.ancientprogramming.fixedformat4j.format.impl.FixedFormatManagerImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.model.EMINQ.Detail;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.model.EMINQ.Header;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.model.EMINQ.Trailer;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.model.FixedWidthDataFile;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.model.FixedWidthDataFileComponent;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.model.NotRegisteredException;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.model.S3CapableFWDF;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EMINQFixedWidthDataFile extends S3CapableFWDF {
    private final static ObjectMapper mapper = new ObjectMapper();
    private final static FixedFormatManager manager = new FixedFormatManagerImpl();
    private static final String id = "EMINQ";
    private String fileName;
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

    public EMINQFixedWidthDataFile() {
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
        this.fileName = fixedWidthDataFile.getAbsolutePath();
        BufferedReader reader = new BufferedReader(new FileReader(fixedWidthDataFile));
        String line;
        int lineNo=0;
        while ((line = reader.readLine()) != null) {
            this.inputFile.append(line);
            ++lineNo;
            switch (line.substring(0, 1)) {
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
            return ((Object) this).toString();
        }
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Map<FixedWidthDataFileComponent, String> toLoadableComponents() throws Exception {
        Map<FixedWidthDataFileComponent,String> output = new HashMap<>();
        CsvMapper csvMapper = new CsvMapper();
        // processed file is a json serialized "this"
        output.put(FixedWidthDataFileComponent.EMINQ_JSON_OBJECT,mapper.writeValueAsString(this));
        output.put(FixedWidthDataFileComponent.EMINQ_CSV_HEADER_COMPONENT,mapper.writeValueAsString(this.header));
        output.put(FixedWidthDataFileComponent.EMINQ_CSV_TRAILER_COMPONENT,mapper.writeValueAsString(this.trailer));
        output.put(FixedWidthDataFileComponent.EMINQ_CSV_INQUIRY_COMPONENT,mapper.writeValueAsString(this.getDetails()));
        output.put(FixedWidthDataFileComponent.EMINQ_FIXED_WIDTH_OBJECT,inputFile.toString());
        return output;
    }
}
