package ly.generalassemb.de.datafeeds.americanExpress.ingress;

import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;
import com.ancientprogramming.fixedformat4j.format.impl.FixedFormatManagerImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.model.CBNOT.Detail;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.model.EPAPE.ReconciledPayment;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.model.EPTRN.Header;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.model.EPTRN.SOCRecord;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.model.FixedWidthDataFile;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.model.FixedWidthDataFileFactory;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.model.S3CapableFWDF;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.model.file.EMCBKFixedWidthDataFile;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.model.file.EMINQFixedWidthDataFile;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.model.file.EPAPEFixedWidthDataFile;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.util.ReverseLineReader;
import org.junit.Ignore;
import org.junit.Test;
import sun.jvm.hotspot.utilities.Assert;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Stream;

public class TestFWDF {
    private final static FixedFormatManager manager = new FixedFormatManagerImpl();

    @Test
    public void testEPAPEParser() throws Exception {
        File file = new File("/Users/davidashirov/Source/GA/de-df-american-express-ingress/docs/GENERALASSEMBLYA60229.EPAPE#E89IA7M5942WRJ");
        FixedWidthDataFile testFile = new EPAPEFixedWidthDataFile().parse(file);
        System.out.println(testFile.toString());
    }


    @Test
    public void testEMCBKParser() throws Exception {
        File file = new File("/Users/davidashirov/Source/GA/de-df-american-express-ingress/docs/EMCBK sample test file_final.txt");
        FixedWidthDataFile testFile = new EMCBKFixedWidthDataFile().parse(file);
        System.out.println(testFile.toString());
    }

    @Ignore
    @Test
    public void testEMINQParser() throws Exception {
        File file = new File("");
        FixedWidthDataFile testFile = new EMINQFixedWidthDataFile().parse(file);
        System.out.println(testFile.toString());
    }

    @Test
    public void testFactory() throws Exception {
        File file = new File("/Users/davidashirov/Source/GA/de-df-american-express-ingress/docs/GENERALASSEMBLYA60229.EPAPE#E89IA7M5942WRJ");
        Assert.that(file.exists(), "File does not exist");
        FixedWidthDataFile testFile = FixedWidthDataFileFactory.getDataFile("EPAPE");
        testFile.parse(file);
        System.out.println(testFile.toString());
    }

    @Test
    public void testEPTRNParser() throws Exception {
        File file = new File("/Users/davidashirov/Source/GA/de-df-american-express-ingress/docs/GENERALASSEMBLYA59662.EPTRN-M15HFK42332SX6");
        FixedWidthDataFile testFile = FixedWidthDataFileFactory.getDataFile("EPTRN").parse(file);
        System.out.println(testFile.toString());
    }

    @Test
    public void testFactoryOnPath() throws Exception{
        Path filePath = Paths.get("/Users/davidashirov/Source/GA/de-df-american-express-ingress/docs/GENERALASSEMBLYA60229.EPAPE#E89IA7M5942WRJ");
        String type = S3CapableFWDF.getFileType(filePath);
        FixedWidthDataFile testFile = FixedWidthDataFileFactory.getDataFile(type);
        testFile.parse(filePath.toFile());
        System.out.println(testFile.toString());
    }

    @Test
    public void testEPTRNToDisk() throws Exception {
        File file = new File("/Users/davidashirov/Source/GA/de-df-american-express-ingress/docs/GENERALASSEMBLYA59662.EPTRN-M15HFK42332SX6");
        FixedWidthDataFile testFile = FixedWidthDataFileFactory.getDataFile("EPTRN").parse(file);
        testFile.toDirectory(new File("/Users/davidashirov/parser_test"));
    }
    @Test
    public void testEPEAPEoS3() throws Exception {
        File file = new File("/Users/davidashirov/Source/GA/de-df-american-express-ingress/docs/GENERALASSEMBLYA60229.EPAPE#E89IA7M5942WRJ");
        FixedWidthDataFile testFile = FixedWidthDataFileFactory.getDataFile("EPAPE").parse(file);
        testFile.toS3(new Config(new File("/Users/davidashirov/Source/GA/de-df-american-express-ingress/src/test/resources/df-american-express-ingress-test.properties").getAbsoluteFile().toURI().toURL()));
    }

    @Test
    public void testEPEAPEoRedshift() throws Exception {
        File file = new File("/Users/davidashirov/Source/GA/de-df-american-express-ingress/docs/GENERALASSEMBLYA60229.EPAPE#E89IA7M5942WRJ");
        FixedWidthDataFile testFile = FixedWidthDataFileFactory.getDataFile("EPAPE").parse(file);
        testFile.toRedshift(new Config(new File("/Users/davidashirov/Source/GA/de-df-american-express-ingress/src/test/resources/df-american-express-ingress-test.properties").getAbsoluteFile().toURI().toURL()));
    }

    @Test
    public void testFactoryPath() throws Exception {
        FixedWidthDataFile testFile = FixedWidthDataFileFactory.getDataFile(Paths.get("/Users/davidashirov/Source/GA/de-df-american-express-ingress/docs/GENERALASSEMBLYA60229.EPAPE#E89IA7M5942WRJ"));
        testFile.toDirectory(new File("/tmp"));
    }

    @Test
    public void testEPTRNToS3() throws Exception {
        File file = new File("/Users/davidashirov/Source/GA/de-df-american-express-ingress/docs/GENERALASSEMBLYA59662.EPTRN-M15HFK42332SX6");
        FixedWidthDataFile testFile = FixedWidthDataFileFactory.getDataFile("EPTRN").parse(file);
        testFile.toS3(new Config(new File("/Users/davidashirov/Source/GA/de-df-american-express-ingress/src/test/resources/df-american-express-ingress-test.properties").getAbsoluteFile().toURI().toURL()));
    }

    @Test
    public void testEPTRNToRedshift() throws Exception {
        File file = new File("/Users/davidashirov/Source/GA/de-df-american-express-ingress/docs/GENERALASSEMBLYA59662.EPTRN-E88HI255114G43");
        FixedWidthDataFile testFile = FixedWidthDataFileFactory.getDataFile("EPTRN").parse(file);
        testFile.toRedshift(new Config(new File("/Users/davidashirov/Source/GA/de-df-american-express-ingress/src/test/resources/df-american-express-ingress-test.properties").getAbsoluteFile().toURI().toURL()));
    }

    @Test
    public void parseHeader() {
        String h = "DFHDR062020170351000000GENERAL ASSEMBLY LLC                                                                                                                                                                                                                                                                                                                                                                                                                       ";
        Header hdr = manager.load(Header.class, h);
        System.out.println(hdr);
    }

    @Test
    public void parseSOC() {
        String s = "63189795316318979531          2017170D9727210201716920171691690090000191780{00005542D000018{000000{0000186219F0289000000000000000000000{0000{0000191780{0001B169013009                000001B-000000000055424 000000000000000 000000000000000 00000000 00000000-000000000000180-00015000                                                                                                                                                                          ";
        SOCRecord soc = manager.load(SOCRecord.class, s);
        System.out.println(soc.getDiscountRate());
    }

    @Test
    public void testCBNOTToRedshift() throws Exception {
        File file = new File("/Users/davidashirov/Source/GA/de-df-american-express-ingress/docs/GENERALASSEMBLYA59662.CBNOT-E87HKNN37154R4");
        FixedWidthDataFile testFile = FixedWidthDataFileFactory.getDataFile("CBNOT").parse(file);
        testFile.toRedshift(new Config(new File("/Users/davidashirov/Source/GA/de-df-american-express-ingress/src/test/resources/df-american-express-ingress-test.properties").getAbsoluteFile().toURI().toURL()));
    }

    @Test
    public void testDateFormat() throws ParseException {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-ddHHHmmss");
        System.out.println(f.format(new Date()));

        Date d = f.parse("2018-01-030001552");
        System.out.println(d.toString());
    }

    @Test
    public void testCBNOTHeader() throws Exception {
        File file = new File("/Users/davidashirov/Source/GA/de-df-american-express-ingress/docs/GENERALASSEMBLYA59662.CBNOT-E87HKNN37154R4");
        BufferedReader r = new BufferedReader(new FileReader(file));
        String header = r.readLine();
        ly.generalassemb.de.datafeeds.americanExpress.ingress.model.CBNOT.Header h =
                manager.load(ly.generalassemb.de.datafeeds.americanExpress.ingress.model.CBNOT.Header.class, header);
        System.out.println(new ObjectMapper().writer().writeValueAsString(h));
    }

    @Test
    public void testCBNOTTrailer() throws Exception {
        File file = new File("/Users/davidashirov/Source/GA/de-df-american-express-ingress/docs/GENERALASSEMBLYA59662.CBNOT-M11HEJN2158XYS");
        ReverseLineReader r = new ReverseLineReader(file, "UTF-8");
        String trailer = r.readLine();

        ly.generalassemb.de.datafeeds.americanExpress.ingress.model.CBNOT.Trailer t =
                manager.load(ly.generalassemb.de.datafeeds.americanExpress.ingress.model.CBNOT.Trailer.class, trailer);
        System.out.println(new ObjectMapper().writer().writeValueAsString(t));
    }

    @Test
    public void testCBNOTDetails() throws Exception {
        File file = new File("/Users/davidashirov/Source/GA/de-df-american-express-ingress/docs/GENERALASSEMBLYA59662.CBNOT-M11HEJN2158XYS");
        BufferedReader r = new BufferedReader(new FileReader(file));
        String header = r.readLine();
        String detail1 = r.readLine();
        String detail2 = r.readLine();
        String trailer = r.readLine();

        Detail d =
                manager.load(Detail.class, detail1);
        System.out.println(new ObjectMapper().writer().writeValueAsString(d));
    }
}
