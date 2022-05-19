package ru.click;

import com.recognition.software.jdeskew.ImageDeskew;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.ImageHelper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Stream;

@Slf4j
public class tess4j {

    private static final List<Path> filesList;
    private static final Path resourcePath = Paths.get("C:\\click\\sandbox\\tess4j\\src\\main\\resources\\");
    private static final Path imagesPath = Paths.get(resourcePath.toString(), "images");
    private static final Path initialImage = Paths.get(imagesPath.toString(), "0.JPG");
    private static final Path resultDir = Paths.get(resourcePath.toString(), "results");
    private static final double MINIMUM_DESKEW_THRESHOLD = 1.0f;


    static {
        filesList = Arrays.asList(
                Paths.get(imagesPath.toString(), "0.JPG"),
                Paths.get(imagesPath.toString(), "15.JPG"),
                Paths.get(imagesPath.toString(), "30.JPG"),
                Paths.get(imagesPath.toString(), "45.JPG"),
                Paths.get(imagesPath.toString(), "90.JPG"),
                Paths.get(imagesPath.toString(), "15_pdf.pdf")
        );
    }

    public static void main(String[] args) {

        getAngles();
        // ocrSkewImages();

        //getRotatedImages();
        // getDeSkewImages();
        // getRotatedImages();
        // detectCriticalAngle();
        //  psm1Test();
        //  psm3Test();
        // psm3WithImageRotation();
    }

    private static void getAngles() {
        Path rotatedImagesPath = Paths.get(resourcePath.toString(), "images_rotated_by_tess");

        Map<Double, Double> angleMap = new TreeMap<>();

        try (Stream<Path> paths = Files.walk(Paths.get(rotatedImagesPath.toString()))) {
            paths.forEach(path -> {
                TessService tessService = new TessService();
                try {
                    double calcAngle = tessService.getAngle(path);
                    double angle = Double.parseDouble(
                            removeFileExtension(path.getFileName().toString(), true).replaceFirst("im_", "")
                    );

                    angleMap.put(angle, calcAngle);

                } catch (IOException e) {
                    log.error("Error getting angle for file '{}'", path, e);
                }
            });
        } catch (IOException e) {
            log.error("Error getting files", e);
        }

        angleMap.forEach((angle, calcAngle) -> {
            log.info("Angle: {}, Calculated angle: {}, Threshold: {}", angle, calcAngle, angle+calcAngle);
        });
    }

    private static void ocrSkewImages() {

        Path rotatedImagesPath = Paths.get(resourcePath.toString(), "images_rotated_by_tess");
        TessService tessService = new TessService();
        tessService.setPagePageSegMode(1);


        try (Stream<Path> paths = Files.walk(Paths.get(rotatedImagesPath.toString()))) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach(p -> {
                        File imageFile = new File(p.toString());
                        BufferedImage bi = null;
                        try {
                            bi = ImageIO.read(imageFile);
                        } catch (IOException e) {
                            log.error("Cannot read image '{}'", p, e);
                        }
                        ImageDeskew id = new ImageDeskew(bi);
                        double imageSkewAngle = id.getSkewAngle();
                        log.info("Rotation angle for file '{}' = {}", p, imageSkewAngle);
                        if ((imageSkewAngle > MINIMUM_DESKEW_THRESHOLD || imageSkewAngle < -(MINIMUM_DESKEW_THRESHOLD))) {
                            bi = ImageHelper.rotateImage(bi, -imageSkewAngle);
                            log.info("Image '{}' was rotated", p);
                        }

                        try {
                            String ocrResult = tessService.doOcr(bi);
                            Path output = Paths.get(
                                    resultDir.toString(),
                                    "psm_3_critical_angle_with_descew",
                                    removeFileExtension(p.getFileName().toString(), true) + ".txt"
                            );

                            Files.write(
                                    output,
                                    ocrResult.getBytes(StandardCharsets.UTF_8),
                                    StandardOpenOption.CREATE
                            );

                        } catch (TesseractException | IOException e) {
                            log.error("Cannot OCR file '{}'", p, e);
                        }
                    });
        } catch (IOException e) {
            log.error("Error getting files", e);
        }
    }


    private static void getDeSkewImages() {

        Path rotatedImagesPath = Paths.get(resourcePath.toString(), "images_rotated_by_tess");

        try (Stream<Path> paths = Files.walk(Paths.get(rotatedImagesPath.toString()))) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach(p -> {
                        File imageFile = new File(p.toString());
                        BufferedImage bi = null;
                        try {
                            bi = ImageIO.read(imageFile);
                        } catch (IOException e) {
                            log.error("Cannot read image '{}'", p, e);
                        }
                        ImageDeskew id = new ImageDeskew(bi);
                        double imageSkewAngle = id.getSkewAngle();
                        log.info("Rotation angle for file '{}' = {}", p, imageSkewAngle);
                        if ((imageSkewAngle > MINIMUM_DESKEW_THRESHOLD || imageSkewAngle < -(MINIMUM_DESKEW_THRESHOLD))) {
                            bi = ImageHelper.rotateImage(bi, -imageSkewAngle);
                            log.info("Image '{}' was rotated", p);
                        }

                        Path outputDirPath = Paths.get(resultDir.toString(), "images_deskew");
                        File outputFile = new File(outputDirPath.toString(), p.getFileName().toString());

                        try {
                            ImageIO.write(bi, "jpg", outputFile);
                        } catch (IOException e) {
                            log.error("Cannot save file '{}'", outputFile.getPath(), e);
                        }
                    });
        } catch (IOException e) {
            log.error("Error getting files", e);
        }
    }

    private static void getRotatedImages() {
        final double startAngle = 0.0f;
        final double finishAngle = 90.0f;
        final double angleInterval = 1.0f;
        double angle = startAngle;

        File imageFile = new File(initialImage.toString());

        BufferedImage bi = null;
        try {
            bi = ImageIO.read(imageFile);
        } catch (IOException e) {
            log.error("Cannot read file '{}'", initialImage, e);
        }

        while (angle <= finishAngle) {
            BufferedImage rbi = ImageHelper.rotateImage(bi, -angle);

            File outputfile = new File(
                    Paths.get(resourcePath.toString(), "images_rotated_by_tess").toString(),
                    "im_" + angle + ".jpg");
            try {
                ImageIO.write(rbi, "jpg", outputfile);
            } catch (IOException e) {
                log.error("Cannot save file '{}'", outputfile.getPath(), e);
            }
            angle += angleInterval;
        }
    }


    public static void detectCriticalAngle() {
        final double startAngle = 0.0f;
        final double finishAngle = 30.0f;
        final double angleInterval = 1.0f;
        double angle = startAngle;

        TessService tessService = new TessService();
        tessService.setPagePageSegMode(3);

        File imageFile = new File(initialImage.toString());

        BufferedImage bi = null;
        try {
            bi = ImageIO.read(imageFile);
        } catch (IOException e) {
            log.error("Cannot read file '{}'", initialImage, e);
        }

        while (angle <= finishAngle) {

            BufferedImage rbi = ImageHelper.rotateImage(bi, -angle);
            log.info("Image '{}' was rotated, angle '{}'", initialImage, angle);

            try {
                Files.write(
                        Paths.get(
                                resultDir.toString(),
                                "psm_3_critical_angle",
                                "angle_" + angle + ".txt"),
                        tessService.doOcr(rbi).getBytes(StandardCharsets.UTF_8),
                        StandardOpenOption.CREATE
                );
            } catch (IOException | TesseractException e) {
                log.error("Cannot OCR file '{}'", initialImage, e);
            }
            angle += angleInterval;
        }

    }

    private static void psm1Test() {
        TessService tess = new TessService();
        tess.setPagePageSegMode(1);
        processImages(
                tess,
                Paths.get(resultDir.toString(), "psm_1")
        );
    }

    private static void psm3Test() {
        TessService tess = new TessService();
        tess.setPagePageSegMode(3);
        processImages(
                tess,
                Paths.get(resultDir.toString(), "psm_3")
        );
    }

    private static void psm3WithImageRotation() {
        TessService tess = new TessService();
        tess.setPagePageSegMode(3);
        // tess.setSetOcrEngineMode(1);
        processImageWithRotation(
                tess,
                Paths.get(resultDir.toString(), "psm_3_with_rotation")
        );
    }


    private static void processImageWithRotation(TessService service, Path resultDirectory) {

        filesList.forEach(path -> {
            File imageFile = new File(path.toString());
            BufferedImage bi = null;
            try {
                bi = ImageIO.read(imageFile);
            } catch (IOException e) {
                log.error("Cannot read image '{}'", path, e);
            }

            ImageDeskew id = new ImageDeskew(bi);
            double imageSkewAngle = id.getSkewAngle();
            log.info("Rotation angle for file '{}' = {}", path, imageSkewAngle);
            if ((imageSkewAngle > MINIMUM_DESKEW_THRESHOLD || imageSkewAngle < -(MINIMUM_DESKEW_THRESHOLD))) {
                bi = ImageHelper.rotateImage(bi, -imageSkewAngle);
                log.info("Image '{}' was rotated", path);
            }

            try {
                Files.write(
                        Paths.get(
                                resultDirectory.toString(),
                                removeFileExtension(path.getFileName().toString(), true) + ".txt"),
                        service.doOcr(bi).getBytes(StandardCharsets.UTF_8),
                        StandardOpenOption.CREATE
                );
            } catch (IOException | TesseractException e) {
                log.error("Cannot OCR file '{}'", path, e);
            }
        });


    }

    private static void processImages(TessService service, Path resultDirectory) {

        filesList.forEach(path -> {
            try {
                Files.write(
                        Paths.get(
                                resultDirectory.toString(),
                                removeFileExtension(path.getFileName().toString(), true) + ".txt"),
                        service.doOcr(path).getBytes(StandardCharsets.UTF_8),
                        StandardOpenOption.CREATE
                );
            } catch (IOException | TesseractException e) {
                log.error("Cannot OCR file '{}'", path, e);
            }
        });

    }

    public static String removeFileExtension(String filename, boolean removeAllExtensions) {
        if (filename == null || filename.isEmpty()) {
            return filename;
        }

        String extPattern = "(?<!^)[.]" + (removeAllExtensions ? ".*" : "[^.]*$");
        return filename.replaceAll(extPattern, "");
    }


}
