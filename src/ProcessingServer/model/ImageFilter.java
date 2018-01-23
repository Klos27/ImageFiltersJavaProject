package ProcessingServer.model;

import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;


public class ImageFilter {

    //==============================================================================
    // Get file name and extansion:
    //==============================================================================
    public static String getFileExtension(File file) {
        String name = file.getName();
        try {
            return name.substring(name.lastIndexOf(".") + 1);
        } catch (Exception e) {
            return "";
        }
    }
    public static String getFileExtension(String name) {
        try {
            if(name.lastIndexOf(".") < 0)
                return "";
            else
                return name.substring(name.lastIndexOf(".") + 1);
        } catch (Exception e) {
            return "";
        }
    }
    private static String getFilePathWithoutExt(File file){
        String name = file.getPath();
        try {
            if(name.lastIndexOf(".") < 0)
                return name;
            else
                return name.substring(0,name.lastIndexOf("."));
        } catch (Exception e) {
            return name;
        }
    }
    private static String getFilePathWithoutExt(String name){
        try {
            if(name.lastIndexOf(".") < 0)
                return name;
            else
                return name.substring(0,name.lastIndexOf("."));
        } catch (Exception e) {
            return name;
        }
    }
    public static String getOutputFilePath(File file){
        String outputName = getFilePathWithoutExt(file) + "Out." + getFileExtension(file);    // result C:\\fileNameOut.extansion
        return outputName;
    }
    public static String getOutputFilePath(String name){
        String outputName = getFilePathWithoutExt(name) + "Out." + getFileExtension(name);    // result C:\\fileNameOut.extansion
        return outputName;
    }
    //==============================================================================
    // Convert Image:
    //==============================================================================
        /*
  1 == Sepia (default)
  2 == Negative
  3 == MirrorImage
  4 == GrayScale
  5 == BlackAndWhite
  6 == RedImage
  7 == GreenImage
  8 == BlueImage
   */
    public static boolean convertImage(String pathName, int type){
        switch (type){
            case 1:
                return sepia(pathName);
            case 2:
                return negative(pathName);
            case 3:
                return mirror(pathName);
            case 4:
                return grayScale(pathName);
            case 5:
                return blackAndWhite(pathName);
            case 6:
                return redImage(pathName);
            case 7:
                return greenImage(pathName);
            case 8:
                return blueImage(pathName);
            default:
                return sepia(pathName);
        }
    }

    private static boolean checkIfFileIsCorrect(String ext){
        if (!ext.equalsIgnoreCase("jpg") &&
            !ext.equalsIgnoreCase("jpeg") &&
            !ext.equalsIgnoreCase("png") &&
            !ext.equalsIgnoreCase("bmp") &&
            !ext.equalsIgnoreCase("gif")) {
                System.out.println("Wybrano niepoprawny format pliku");
                System.out.println("Dostępne formaty: JPG, JPEG, PNG, BMP, GIF");
                System.out.println("Przy wyborze ruchomego GIF, wynikiem będzie nieruchoma pierwsza klatka obrazu");
                return false;
        }
        else
            return true;
    }

    //==============================================================================
    // Image Filters:
    //==============================================================================

    private static boolean sepia(String pathName) {
        BufferedImage img = null;
        File f = null;
        String ext = "jpg";
        // read img
        try {
            f = new File(pathName);
            if (f.length() > 0) {
                System.out.println("File size: " + f.length());
                img = ImageIO.read(f);
                ext = ImageFilter.getFileExtension(f);
                System.out.println(ext);
            }
            else
                return false;
        } catch (IOException e) {
            System.out.println(e);
            return false;
        }

        if ( !checkIfFileIsCorrect(ext)){
            return false;
        }
        // get img size
        int width = img.getWidth();
        int height = img.getHeight();
        int type = img.getType();
        System.out.println("Image type: " + type);
        //convert to sepia
        int p,a,r,g,b;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                p = img.getRGB(x, y);
                a = (p >> 24) & 0xff;
                r = (p >> 16) & 0xff;
                g = (p >> 8) & 0xff;
                b = p & 0xff;

                // calc new r,g,b sepia
                int tr = (int) (0.393 * r + 0.769 * g + 0.189 * b);
                int tg = (int) (0.349 * r + 0.686 * g + 0.168 * b);
                int tb = (int) (0.282 * r + 0.534 * g + 0.131 * b);

                //check if correct
                if (tr > 255)
                    r = 255;
                else
                    r = tr;
                if (tg > 255)
                    g = 255;
                else
                    g = tg;
                if (tb > 255)
                    b = 255;
                else
                    b = tb;

                //set RGB
                p = (a << 24) | (r << 16) | (g << 8) | b;
                img.setRGB(x, y, p);
            }
        }

        String outputName = getOutputFilePath(f);    // result C:\\fileNameOut.extansion
        //write img
        try{
            File fNew = new File(outputName);
            ImageIO.write(img,ext,fNew);
            return true;
        }
        catch(IOException e){
            System.out.println(e);
            return false;
        }
    }
//--------- Mirror
        private static boolean mirror(String pathName){
            BufferedImage img = null;
            File f = null;
            String ext = "jpg";
            // read img
            try {
                f = new File(pathName);
                if (f.length() > 0) {
                    System.out.println("File size: " + f.length());
                    img = ImageIO.read(f);
                    ext = ImageFilter.getFileExtension(f);
                    System.out.println(ext);
                }
                else
                    return false;
            } catch (IOException e) {
                System.out.println(e);
                return false;
            }

            if ( !checkIfFileIsCorrect(ext)){
                return false;
            }
            // get img size
            int width = img.getWidth();
            int height = img.getHeight();
            int type = img.getType();
            System.out.println("Image type: " + type);

            // Init buffer
            BufferedImage mirror;
            try {
                mirror = new BufferedImage(width * 2, height, type);
            } catch (java.lang.OutOfMemoryError e) {
                System.out.println("Przekroczono maksymalny rozmiar obrazu, wybierz inny obraz");
                return false;
            } catch (Exception e){
                return false;
            }
            //convert to mirror
            int p;
            for (int y = 0; y < height; y++)
                for (int lx = 0, rx = width * 2 - 1; lx < width; lx++, rx--) {
                    p = img.getRGB(lx, y);
                    mirror.setRGB(lx, y, p);
                    mirror.setRGB(rx, y, p);
                }

            String outputName = getOutputFilePath(f);    // result C:\\fileNameOut.extansion
            //write img
            try{
                File fNew = new File(outputName);
                ImageIO.write(mirror,ext,fNew);
                return true;
            }
            catch(IOException e){
                System.out.println(e);
                return false;
            }
        }
//--------- Negative
        private static boolean negative(String pathName) {
            BufferedImage img = null;
            File f = null;
            String ext = "jpg";
            // read img
            try {
                f = new File(pathName);
                if (f.length() > 0) {
                    System.out.println("File size: " + f.length());
                    img = ImageIO.read(f);
                    ext = ImageFilter.getFileExtension(f);
                    System.out.println(ext);
                }
                else
                    return false;
            } catch (IOException e) {
                System.out.println(e);
                return false;
            }

            if ( !checkIfFileIsCorrect(ext)){
                return false;
            }
            // get img size
            int width = img.getWidth();
            int height = img.getHeight();
            int type = img.getType();
            System.out.println("Image type: " + type);
            //convert to negative
            int p,a,r,g,b;
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    p = img.getRGB(x, y);
                    a = (p >> 24) & 0xff;
                    r = (p >> 16) & 0xff;
                    g = (p >> 8) & 0xff;
                    b = p & 0xff;

                    r = 255 - r;
                    g = 255 - g;
                    b = 255 - b;

                    //set RGB
                    p = (a << 24) | (r << 16) | (g << 8) | b;
                    img.setRGB(x, y, p);
                }
            }

            String outputName = getOutputFilePath(f);    // result C:\\fileNameOut.extansion
            //write img
            try{
                File fNew = new File(outputName);
                ImageIO.write(img,ext,fNew);
                return true;
            }
            catch(IOException e){
                System.out.println(e);
                return false;
            }

        }
//--------- GrayScale
    private static boolean grayScale(String pathName) {
        BufferedImage img = null;
        File f = null;
        String ext = "jpg";
        // read img
        try {
            f = new File(pathName);
            if (f.length() > 0) {
                System.out.println("File size: " + f.length());
                img = ImageIO.read(f);
                ext = ImageFilter.getFileExtension(f);
                System.out.println(ext);
            }
            else
                return false;
        } catch (IOException e) {
            System.out.println(e);
            return false;
        }

        if ( !checkIfFileIsCorrect(ext)){
            return false;
        }
        // get img size
        int width = img.getWidth();
        int height = img.getHeight();
        int type = img.getType();
        System.out.println("Image type: " + type);
        //convert to GrayScale
        int p,a,r,g,b;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                p = img.getRGB(x, y);
                a = (p >> 24) & 0xff;
                r = (p >> 16) & 0xff;
                g = (p >> 8) & 0xff;
                b = p & 0xff;

                int gray =  (int)((r * 0.30) + (g * 0.59) + (b * 0.11));
                if(gray > 255)
                    gray = 255;

                //set RGB
                p = (a << 24) | (gray << 16) | (gray << 8) | gray;
                img.setRGB(x, y, p);
            }
        }

        String outputName = getOutputFilePath(f);    // result C:\\fileNameOut.extansion
        //write img
        try{
            File fNew = new File(outputName);
            ImageIO.write(img,ext,fNew);
            return true;
        }
        catch(IOException e){
            System.out.println(e);
            return false;
        }

    }
//--------- BlackAndWhite
    private static boolean blackAndWhite(String pathName) {
        BufferedImage img = null;
        File f = null;
        String ext = "jpg";
        // read img
        try {
            f = new File(pathName);
            if (f.length() > 0) {
                System.out.println("File size: " + f.length());
                img = ImageIO.read(f);
                ext = ImageFilter.getFileExtension(f);
                System.out.println(ext);
            }
            else
                return false;
        } catch (IOException e) {
            System.out.println(e);
            return false;
        }

        if ( !checkIfFileIsCorrect(ext)){
            return false;
        }
        // get img size
        int width = img.getWidth();
        int height = img.getHeight();
        int type = img.getType();
        System.out.println("Image type: " + type);
        //convert to BlackAndWhite
        int p,a,r,g,b;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                p = img.getRGB(x, y);
                a = (p >> 24) & 0xff;
                r = (p >> 16) & 0xff;
                g = (p >> 8) & 0xff;
                b = p & 0xff;

                int blackAndWhite =  (int)((r * 0.30) + (g * 0.59) + (b * 0.11));
                if(blackAndWhite > 192){
                    blackAndWhite = 255;
                }
                else
                    blackAndWhite = 0;

                //set RGB
                p = (a << 24) | (blackAndWhite << 16) | (blackAndWhite << 8) | blackAndWhite;
                img.setRGB(x, y, p);
            }
        }

        String outputName = getOutputFilePath(f);    // result C:\\fileNameOut.extansion
        //write img
        try{
            File fNew = new File(outputName);
            ImageIO.write(img,ext,fNew);
            return true;
        }
        catch(IOException e){
            System.out.println(e);
            return false;
        }
    }
//--------- RedImage
    private static boolean redImage(String pathName) {
        BufferedImage img = null;
        File f = null;
        String ext = "jpg";
        // read img
        try {
            f = new File(pathName);
            if (f.length() > 0) {
                System.out.println("File size: " + f.length());
                img = ImageIO.read(f);
                ext = ImageFilter.getFileExtension(f);
                System.out.println(ext);
            }
            else
                return false;
        } catch (IOException e) {
            System.out.println(e);
            return false;
        }

        if ( !checkIfFileIsCorrect(ext)){
            return false;
        }
        // get img size
        int width = img.getWidth();
        int height = img.getHeight();
        int type = img.getType();
        System.out.println("Image type: " + type);
        //convert to RedImage
        int p,a,r,g,b;
        g = 0;
        b = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                p = img.getRGB(x, y);
                a = (p >> 24) & 0xff;
                r = (p >> 16) & 0xff;

                //set RGB
                p = (a << 24) | (r << 16) | (g << 8) | b;
                img.setRGB(x, y, p);
            }
        }

        String outputName = getOutputFilePath(f);    // result C:\\fileNameOut.extansion
        //write img
        try{
            File fNew = new File(outputName);
            ImageIO.write(img,ext,fNew);
            return true;
        }
        catch(IOException e){
            System.out.println(e);
            return false;
        }
    }
//--------- GreenImage
    private static boolean greenImage(String pathName) {
        BufferedImage img = null;
        File f = null;
        String ext = "jpg";
        // read img
        try {
            f = new File(pathName);
            if (f.length() > 0) {
                System.out.println("File size: " + f.length());
                img = ImageIO.read(f);
                ext = ImageFilter.getFileExtension(f);
                System.out.println(ext);
            }
            else
                return false;
        } catch (IOException e) {
            System.out.println(e);
            return false;
        }

        if ( !checkIfFileIsCorrect(ext)){
            return false;
        }
        // get img size
        int width = img.getWidth();
        int height = img.getHeight();
        int type = img.getType();
        System.out.println("Image type: " + type);
        //convert to RedImage
        int p,a,r,g,b;
        r = 0;
        b = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                p = img.getRGB(x, y);
                a = (p >> 24) & 0xff;
                g = (p >> 8) & 0xff;

                //set RGB
                p = (a << 24) | (r << 16) | (g << 8) | b;
                img.setRGB(x, y, p);
            }
        }

        String outputName = getOutputFilePath(f);    // result C:\\fileNameOut.extansion
        //write img
        try{
            File fNew = new File(outputName);
            ImageIO.write(img,ext,fNew);
            return true;
        }
        catch(IOException e){
            System.out.println(e);
            return false;
        }
    }
//--------- BlueImage
    private static boolean blueImage(String pathName) {
        BufferedImage img = null;
        File f = null;
        String ext = "jpg";
        // read img
        try {
            f = new File(pathName);
            if (f.length() > 0) {
                System.out.println("File size: " + f.length());
                img = ImageIO.read(f);
                ext = ImageFilter.getFileExtension(f);
                System.out.println(ext);
            }
            else
                return false;
        } catch (IOException e) {
            System.out.println(e);
            return false;
        }

        if ( !checkIfFileIsCorrect(ext)){
            return false;
        }
        // get img size
        int width = img.getWidth();
        int height = img.getHeight();
        int type = img.getType();
        System.out.println("Image type: " + type);
        //convert to RedImage
        int p,a,r,g,b;
        r = 0;
        g = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                p = img.getRGB(x, y);
                a = (p >> 24) & 0xff;
                b = p & 0xff;

                //set RGB
                p = (a << 24) | (r << 16) | (g << 8) | b;
                img.setRGB(x, y, p);
            }
        }

        String outputName = getOutputFilePath(f);    // result C:\\fileNameOut.extansion
        //write img
        try{
            File fNew = new File(outputName);
            ImageIO.write(img,ext,fNew);
            return true;
        }
        catch(IOException e){
            System.out.println(e);
            return false;
        }
    }
}

