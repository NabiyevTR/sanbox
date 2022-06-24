package ru.click.ghostscript;

import org.ghost4j.Ghostscript;
import org.ghost4j.GhostscriptException;

public class GhostScriptService {

    public void fixFile(String inFileName, String outFileName) throws GhostscriptException {
        String[] gsArgs = new String[7];
        gsArgs[0] = "-sDEVICE=pdfwrite";
        gsArgs[1] = "-dSAFER";
        gsArgs[2] = "-dBATCH";
        gsArgs[3] = "-dNOPAUSE";
        gsArgs[4] = "-dPDFSETTINGS=/default";
        gsArgs[5] = "-sOutputFile=" + outFileName.replace("\\", "/");
        gsArgs[6] = inFileName.replace("\\", "/");
        run(gsArgs);
    }

    public void optimizeFile(String inFileName, String outFileName) throws GhostscriptException {
        String[] gsArgs = new String[10];
        gsArgs[0] = "-sOutputFile='" + outFileName + "'";
        gsArgs[1] = "-sDEVICE=pdfwrite";
        gsArgs[2] = "-dCompatibilityLevel=1.4";
        gsArgs[3] = "-dPDFSETTINGS=/screen"; //lower quality, smaller size. (72 dpi)
        //  gsArgs[3] = "-dPDFSETTINGS=/ebook"; //for better quality, but slightly larger pdfs. (150 dpi)
        //  gsArgs[3] = "-dPDFSETTINGS=/prepress"; //output similar to Acrobat Distiller "Prepress Optimized" setting (300 dpi)
        //  gsArgs[3] = "-dPDFSETTINGS=/printer"; // selects output similar to the Acrobat Distiller "Print Optimized" setting (300 dpi)
        //  gsArgs[3] = "-dPDFSETTINGS=/default"; //selects output intended to be useful across a wide variety of uses, possibly at the expense of a larger output file
        gsArgs[4] = "-dEmbedAllFonts=false";
        gsArgs[5] = "-dSubsetFonts=true";
        gsArgs[6] = "-dConvertCMYKImagesToRGB=true";
        gsArgs[7] = "-dCompressFonts=true";
        gsArgs[8] = "-dDetectDuplicateImages=true";
        gsArgs[9] = inFileName;
        run(gsArgs);
    }

    private void run(String[] gsArgs) throws GhostscriptException {

        try {
            Ghostscript.deleteInstance();
        } catch (GhostscriptException e) {
            //nothing
        }

        Ghostscript gs = Ghostscript.getInstance();
        try {
            synchronized (gs) {
                gs.initialize(gsArgs);

                gs.exit();
            }
        } catch (GhostscriptException e) {
            throw new GhostscriptException(e.getMessage());
        } finally {
            try {
                Ghostscript.deleteInstance();
            } catch (GhostscriptException e) {
                //nothing
            }
        }
    }
}
