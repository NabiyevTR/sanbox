package ru.click;

import com.recognition.software.jdeskew.ImageDeskew;
import lombok.Setter;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;

public class TessService {

    private final ITesseract tesseract = new Tesseract();
    private String language = "rus+eng";
    private String dataPath = "C:/click/TikaDocExtractor/tessdata";

    @Setter
    private int pagePageSegMode = 3;
    @Setter
    private int setOcrEngineMode = 3;


    public double getAngle(Path path) throws IOException {
        BufferedImage bi = ImageIO.read(path.toFile());
        return getAngle(bi);
    }

    public double getAngle(BufferedImage bi) {
        CustomImageDeskew customImageDeskew = new CustomImageDeskew(bi);
        customImageDeskew.setCAlphaStart(-45);
        customImageDeskew.setCAlphaStep(0.2);
        customImageDeskew.setCSteps(45 * 2 * 5);
        return customImageDeskew.getSkewAngle();
    }

    public String doOcr(Path path) throws TesseractException {
        tesseract.setLanguage(language);
        tesseract.setDatapath(dataPath);
        tesseract.setPageSegMode(pagePageSegMode);
        tesseract.setOcrEngineMode(setOcrEngineMode);
        return tesseract.doOCR(path.toFile());
    }

    public String doOcr(BufferedImage bi) throws TesseractException {
        tesseract.setLanguage(language);
        tesseract.setDatapath(dataPath);
        tesseract.setPageSegMode(pagePageSegMode);
        tesseract.setOcrEngineMode(setOcrEngineMode);
        return tesseract.doOCR(bi);
    }
}
