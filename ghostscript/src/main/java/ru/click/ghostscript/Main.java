package ru.click.ghostscript;

import org.apache.log4j.BasicConfigurator;
import org.ghost4j.GhostscriptException;

public class Main {

    public static void main(String[] args) throws GhostscriptException {
        BasicConfigurator.configure();

        GhostScriptService service = new GhostScriptService();
        service.fixFile(
                "C:\\click\\rs-consumer-tesseract\\src\\test\\resources\\ghostscript\\multi_page.pdf",
                "C:\\click\\rs-consumer-tesseract\\src\\test\\resources\\ghostscript\\test\\100.pdf");
    }
}
