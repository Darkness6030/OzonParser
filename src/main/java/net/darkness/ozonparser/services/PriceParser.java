package net.darkness.ozonparser.services;

import net.sourceforge.tess4j.*;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

@Service
public class PriceParser {
    private final ITesseract tesseract;

    public PriceParser() {
        this.tesseract = new Tesseract();
        tesseract.setLanguage("rus");
        tesseract.setDatapath("assets");
    }

    public Float parsePrice(byte[] file) {
        try (ByteArrayInputStream bytes = new ByteArrayInputStream(file)) {
            BufferedImage image = ImageIO.read(bytes);
            if (image == null) {
                throw new IOException("Unable to read image, possibly unsupported format or corrupted file.");
            }
            return parsePrice(image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public float parsePrice(BufferedImage image) {
        try {
            return OzonTextParser.extractPrice(tesseract.doOCR(image));
        } catch (TesseractException e) {
            throw new RuntimeException(e);
        }
    }
}
