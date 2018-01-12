package ly.generalassemb.de.datafeeds.americanExpress.ingress.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class ReverseLineReader {
    private static final int BUFFER_SIZE = 8192;
    private final FileChannel channel;
    private final String encoding;
    private long filePos;
    private ByteBuffer buf;
    private int bufPos;
    private ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private RandomAccessFile raf;
    private byte lastLineBreak = '\n';


    public ReverseLineReader(File file) throws IOException {
        this(file, null);
    }

    public ReverseLineReader(File file, String encoding) throws IOException {
        raf = new RandomAccessFile(file, "r");
        channel = raf.getChannel();
        filePos = raf.length();
        this.encoding = encoding;
    }

    public void close() throws IOException {
        raf.close();
    }

    public String readLine() throws IOException {
        byte c;
        while (true) {
            if (bufPos < 0) {
                if (filePos == 0) {
                    if (baos == null) {
                        return null;
                    }
                    String line = bufToString();
                    baos = null;
                    return line;
                }

                long start = Math.max(filePos - BUFFER_SIZE, 0);
                long end = filePos;
                long len = end - start;

                buf = channel.map(FileChannel.MapMode.READ_ONLY, start, len);
                bufPos = (int) len;
                filePos = start;

                // Ignore Empty New Lines
                c = buf.get(--bufPos);
                if (c == '\r' || c == '\n')
                    while (bufPos > 0 && (c == '\r' || c == '\n')) {
                        bufPos--;
                        c = buf.get(bufPos);
                    }
                if (!(c == '\r' || c == '\n'))
                    bufPos++;// IS THE NEW LENE
            }

            /*
             * This will ignore all blank new lines.
             */
            while (bufPos-- > 0) {
                c = buf.get(bufPos);
                if (c == '\r' || c == '\n') {
                    // skip \r\n
                    while (bufPos > 0 && (c == '\r' || c == '\n')) {
                        c = buf.get(--bufPos);
                    }
                    // restore cursor
                    if (!(c == '\r' || c == '\n'))
                        bufPos++;// IS THE NEW Line
                    return bufToString();
                }
                baos.write(c);
            }

			/*
			 *  If you don't want to ignore new line and would like
			 *  to print new line too then use below code
			 *  and comment out above while loop

			while (bufPos-- > 0) {
				byte c1 = buf.get(bufPos);
				if (c1 == '\r' || c1 == '\n') {
					if (c1 != lastLineBreak) {
						lastLineBreak = c1;
						continue;
					}
					lastLineBreak = c1;
					return bufToString();
				}
				baos.write(c1);
			}
			*/

        }
    }

    private String bufToString() throws UnsupportedEncodingException {
        if (baos.size() == 0) {
            return "";
        }

        byte[] bytes = baos.toByteArray();
        for (int i = 0; i < bytes.length / 2; i++) {
            byte t = bytes[i];
            bytes[i] = bytes[bytes.length - i - 1];
            bytes[bytes.length - i - 1] = t;
        }

        baos.reset();
        if (encoding != null)
            return new String(bytes, encoding);
        else
            return new String(bytes);
    }
}