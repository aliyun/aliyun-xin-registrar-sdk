package xin.nic.sdk.registrar.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class Base64Util {

    public static String encodeUTF8(String str) {
        if (str == null || str.length() == 0) {
            return "";
        }
        try {
            return encode(str, "UTF-8");
        } catch (RuntimeException e) {
            return null;
        }
    }

    public static String decodeUTF8(String str) {
        if (str == null || str.length() == 0) {
            return "";
        }
        try {
            return decode(str, "UTF-8");
        } catch (RuntimeException e) {
            return null;
        }
    }

    public static String encode(String str, String charset) throws RuntimeException {
        byte[] bytes;
        try {
            bytes = str.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Unsupported charset: " + charset, e);
        }
        byte[] encoded = encode(bytes);
        try {
            return new String(encoded, "ASCII");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("ASCII is not supported!", e);
        }
    }

    public static String decode(String str, String charset) throws RuntimeException {
        byte[] bytes;
        try {
            bytes = str.getBytes("ASCII");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("ASCII is not supported!", e);
        }
        byte[] decoded = decode(bytes);
        try {
            return new String(decoded, charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Unsupported charset: " + charset, e);
        }
    }

    public static String encodeToString(byte[] bytes) throws RuntimeException {
        try {
            return new String(encode(bytes), "ASCII");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("ASCII is not supported!", e);
        }
    }

    public static byte[] decodeToByte(String base64Str) throws RuntimeException {
        return decode(base64Str.getBytes());
    }

    public static byte[] encode(byte[] bytes) throws RuntimeException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            encode(inputStream, outputStream);
        } catch (IOException e) {
            throw new RuntimeException("Unexpected I/O error", e);
        } finally {
            try {
                inputStream.close();
            } catch (Throwable ignored) {
            }
            try {
                outputStream.close();
            } catch (Throwable ignored) {
            }
        }
        return outputStream.toByteArray();
    }

    public static byte[] decode(byte[] bytes) throws RuntimeException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            decode(inputStream, outputStream);
        } catch (IOException e) {
            throw new RuntimeException("Unexpected I/O error", e);
        } finally {
            try {
                inputStream.close();
            } catch (Throwable ignored) {
            }
            try {
                outputStream.close();
            } catch (Throwable ignored) {
            }
        }
        return outputStream.toByteArray();
    }

    private static void decode(InputStream inputStream, OutputStream outputStream) throws IOException {
        copy(new Base64InputStream(inputStream), outputStream);
    }

    private static void encode(InputStream inputStream, OutputStream outputStream) throws IOException {
        Base64OutputStream aux = new Base64OutputStream(outputStream);
        copy(inputStream, aux);
        aux.commit();
    }

    private static void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        // 4K Buffer
        byte[] b = new byte[4096];
        int len;
        while ((len = inputStream.read(b)) != -1) {
            outputStream.write(b, 0, len);
        }
    }

    private static class Shared {
        static final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        static final char   pad   = '=';
    }

    private static class Base64InputStream extends InputStream {
        private InputStream inputStream;
        private int[]       buffer;
        private int         bufferCounter = 0;
        private boolean     eof           = false;

        public Base64InputStream(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        public int read() throws IOException {
            if (buffer == null || bufferCounter == buffer.length) {
                if (eof) {
                    return -1;
                }
                acquire();
                if (buffer.length == 0) {
                    buffer = null;
                    return -1;
                }
                bufferCounter = 0;
            }
            return buffer[bufferCounter++];
        }

        private void acquire() throws IOException {
            char[] four = new char[4];
            int i = 0;
            do {
                int b = inputStream.read();
                if (b == -1) {
                    if (i != 0) {
                        // modify by Shaopeng.wei
                        // auto add pad char
                        b = (int) Shared.pad;
                        // throw new IOException("Bad base64 stream");
                    } else {
                        buffer = new int[0];
                        eof = true;
                        return;
                    }
                }
                char c = (char) b;
                if (Shared.chars.indexOf(c) != -1 || c == Shared.pad) {
                    four[i++] = c;
                } else if (c != '\r' && c != '\n') {
                    throw new IOException("Bad base64 stream");
                }
            } while (i < 4);
            boolean padded = false;
            for (i = 0; i < 4; i++) {
                if (four[i] != Shared.pad) {
                    if (padded) {
                        throw new IOException("Bad base64 stream");
                    }
                } else {
                    if (!padded) {
                        padded = true;
                    }
                }
            }
            int l;
            if (four[3] == Shared.pad) {
                if (inputStream.read() != -1) {
                    throw new IOException("Bad base64 stream");
                }
                eof = true;
                if (four[2] == Shared.pad) {
                    l = 1;
                } else {
                    l = 2;
                }
            } else {
                l = 3;
            }
            int aux = 0;
            for (i = 0; i < 4; i++) {
                if (four[i] != Shared.pad) {
                    aux = aux | (Shared.chars.indexOf(four[i]) << (6 * (3 - i)));
                }
            }
            buffer = new int[l];
            for (i = 0; i < l; i++) {
                buffer[i] = (aux >>> (8 * (2 - i))) & 0xFF;
            }
        }

        public void close() throws IOException {
            inputStream.close();
        }
    }

    private static class Base64OutputStream extends OutputStream {
        private OutputStream outputStream = null;
        private int          buffer       = 0;
        private int          bytecounter  = 0;

        public Base64OutputStream(OutputStream outputStream) {
            this.outputStream = outputStream;
        }

        public void write(int b) throws IOException {
            int value = (b & 0xFF) << (16 - (bytecounter * 8));
            buffer = buffer | value;
            bytecounter++;
            if (bytecounter == 3) {
                commit();
            }
        }

        public void close() throws IOException {
            commit();
            outputStream.close();
        }

        protected void commit() throws IOException {
            if (bytecounter > 0) {
                char b1 = Shared.chars.charAt((buffer << 8) >>> 26);
                char b2 = Shared.chars.charAt((buffer << 14) >>> 26);
                char b3 = (bytecounter < 2) ? Shared.pad : Shared.chars.charAt((buffer << 20) >>> 26);
                char b4 = (bytecounter < 3) ? Shared.pad : Shared.chars.charAt((buffer << 26) >>> 26);
                outputStream.write(b1);
                outputStream.write(b2);
                outputStream.write(b3);
                outputStream.write(b4);
                bytecounter = 0;
                buffer = 0;
            }
        }
    }
}
