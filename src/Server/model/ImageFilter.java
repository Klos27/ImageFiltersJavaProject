package Server.model;

import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;


public class ImageFilter {

    //==============================================================================
    // Get file name and extansion:
    //==============================================================================
    private static String getFileExtension(File file) {
        String name = file.getName();
        try {
            return name.substring(name.lastIndexOf(".") + 1);
        } catch (Exception e) {
            return "";
        }
    }
    private static String getFileExtension(String name) {
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
        String name = file.getName();
        try {
            if(name.lastIndexOf(".") < 0)
                return name;
            else
                return name.substring(0,name.lastIndexOf(".") - 1);
        } catch (Exception e) {
            return name;
        }
    }
    private static String getFilePathWithoutExt(String name){
        try {
            if(name.lastIndexOf(".") < 0)
                return name;
            else
                return name.substring(0,name.lastIndexOf(".") - 1);
        } catch (Exception e) {
            return name;
        }
    }
    public static String getOutputFilePath(File file){
        String outputName = getFilePathWithoutExt(file) + "Out." + getFileExtension(file);    // result C:\\fileNameOut.extansion
        return outputName;
    }
    public static String getOutputFilePath(String name){
        String outputName = name.substring(0,name.lastIndexOf(".") - 1) + "Out." + getFileExtension(name);    // result C:\\fileNameOut.extansion
        return outputName;
    }
    //==============================================================================
    // Convert Image:
    //==============================================================================
    public static boolean convertImage(String pathName, int type){
        switch (type){
            case 1:
                return sepia(pathName);
            case 2:
                return false; //TODO add other filters
            default:
                return sepia(pathName);
        }
    }
    //==============================================================================
    // Image Filters:
    //==============================================================================

    // pathName = D:\7.bmp or D:\\7.bmp
    private static boolean sepia(String pathName) {
        //TODO check if this function is correct
        BufferedImage img = null;
        File f = null;
        String ext = "jpg";
        // read img
        try {
            f = new File(pathName);
            if (f.length() > 1) {
                System.out.println(f.length());
            }
            img = ImageIO.read(f);
            ext = ImageFilter.getFileExtension(f);
            System.out.println(ext);
        } catch (IOException e) {
            System.out.println(e);
        }
        if (ext.equalsIgnoreCase("tif")) {
            System.out.println("Wybrano niepoprawny format pliku");
            System.out.println("Dostępne formaty: JPG, JPEG, PNG, BMP, GIF");
            System.out.println("Przy wyborze ruchomego GIF, wynikiem będzie nieruchoma pierwsza klatka obrazu");
            return false;
            //exit(1);
        }
        // get img size
        int width = img.getWidth();
        int height = img.getHeight();
        int type = img.getType();
        System.out.println(type);
        //convert to sepia
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int p = img.getRGB(x, y);
                int a = (p >> 24) & 0xff;
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p & 0xff;

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

//        String outputName = "D:\\Output." + ext;
        String outputName = getOutputFilePath(f);    // result C:\\fileNameOut.extansion
        //write img
        try{
            f = new File(outputName);
            ImageIO.write(img,ext,f);
            return true;
        }
        catch(IOException e){
            System.out.println(e);
            return false;
        }
    }
//--------- Mirror
        private static void mirror() throws IOException{
        //TODO this function
//            // get img size
//            int width = img.getWidth();
//            int height = img.getHeight();
//
//            int type = img.getType();
//            //mirror
//            BufferedImage mirror = new BufferedImage(1, 1, type);
//            try {
//                mirror = new BufferedImage(width * 2, height, type);
//            } catch (java.lang.OutOfMemoryError e) {
//                System.out.println("Przekroczono maksymalny rozmiar obrazu, wybierz inny obraz");
//                exit(1);
//            }
//
//            for (int y = 0; y < height; y++)
//                for (int lx = 0, rx = width * 2 - 1; lx < width; lx++, rx--) {
//                    int p = img.getRGB(lx, y);
//
//                    mirror.setRGB(lx, y, p);
//                    mirror.setRGB(rx, y, p);
//                }
        }
        private static void negative() throws IOException {
            //TODO this function
//---------- negative

//            r = 255 - r;
//            g = 255 - g;
//            b = 255 - b;
//
//            set RGB
//            p = (a << 24) | (r << 16) | (g << 8) | b;
//
//            img.setRGB(x, y, p);
        }
}