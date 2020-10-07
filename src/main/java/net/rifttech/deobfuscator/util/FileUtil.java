package net.rifttech.deobfuscator.util;

import lombok.experimental.UtilityClass;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@UtilityClass
public class FileUtil {

    /**
     * Reads bytes from a {@link InputStream}
     * @param in The {@link InputStream} to read bytes from
     * @return The bytes of {@param in}
     */
    public byte[] readBytes(InputStream in) throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            byte[] b = new byte[1024];
            int len;

            while ((len = in.read(b)) != -1)
                out.write(b, 0, len);

            return out.toByteArray();
        }
    }
}
