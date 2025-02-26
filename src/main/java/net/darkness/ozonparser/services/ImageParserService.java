package net.darkness.ozonparser.services;

import lombok.RequiredArgsConstructor;
import net.sourceforge.tess4j.*;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
public class ImageParserService {

    private final ITesseract tesseract;
    private final ExecutorService executorService;

    public ImageParserService() {
        this.tesseract = new Tesseract();
        tesseract.setLanguage("rus");
        tesseract.setDatapath("assets");
        this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    public CompletableFuture<String> parseFileAsync(byte[] file) {
        return CompletableFuture.supplyAsync(() -> parseFile(file), executorService);
    }

    public String parseFile(byte[] file) {
        try (ByteArrayInputStream bytes = new ByteArrayInputStream(file)) {
            BufferedImage image = ImageIO.read(bytes);
            if (image == null) {
                throw new IOException("Unable to read image, possibly unsupported format or corrupted file.");
            }
            return parseImage(image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Deprecated
    public String parsePath(String path) throws IOException {
        return parseFile(new File(path));
    }

    public String parseFile(File file) throws IOException {
        BufferedImage image = ImageIO.read(file);
        return parseImage(image);
    }

    private String parseImage(BufferedImage image) {
        try {
            return tesseract.doOCR(image);
        } catch (TesseractException e) {
            throw new RuntimeException(e);
        }
    }
}
