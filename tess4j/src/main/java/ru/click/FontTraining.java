package ru.click;

import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.TesseractException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Slf4j
public class FontTraining {

    private static final Path workDir = Paths.get("C:\\click\\sandbox\\tess4j\\src\\main\\resources\\font_training");

    public static void main(String[] args) {
        doOcrAndSave("source_im.pdf", "ocr_after_training.txt");
    }

    public static String doOcr(String path) throws TesseractException {
        Path filePath = Paths.get(workDir.toString(), path);
        TessService service = new TessService();
        String result = service.doOcr(filePath);
        log.info("OCR result for file '{}':\n{}", filePath, result);
        return result;
    }


    public static void doOcrAndSave(String in, String out) {
        Path outPath = Paths.get(workDir.toString(), out);
        try {
            String result = doOcr(in);
            Files.write(outPath, result.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
        } catch (TesseractException e) {
            log.error("Can not ocr file '{}'", in, e);
        } catch (IOException e) {
            log.error("Cann not write file '{}'", out, e);
        }
    }
}
