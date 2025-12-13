package com.trashheroesbe.global.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import java.io.ByteArrayOutputStream;
import org.springframework.stereotype.Component;

@Component
public class QrCodeGenerator {

    public byte[] generatePngBytes(String content, int size) {
        try {
            BitMatrix matrix = new MultiFormatWriter()
                .encode(content, BarcodeFormat.QR_CODE, size, size);

            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                MatrixToImageWriter.writeToStream(matrix, "PNG", out);
                return out.toByteArray();
            }
        } catch (Exception e) {
            throw new IllegalStateException("QR 코드 생성에 실패했습니다.", e);
        }
    }
}

