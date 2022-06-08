
package com.springbootutils.util;

import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

public class Base64Util {

    public Base64Util() {
    }

    public static String encodeBase64Str(byte[] bytes) {
        Encoder encoder = Base64.getEncoder();
        return new String(encoder.encode(bytes));
    }

    public static byte[] decodeBase64byte(String inputString) {
        Decoder decoder = Base64.getDecoder();
        return decoder.decode(inputString);
    }
}