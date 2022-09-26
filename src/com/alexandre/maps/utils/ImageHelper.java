package com.alexandre.maps.utils;

import com.alexandre.maps.Main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

public class ImageHelper {

    public static boolean isURL(String path) {
        try {
            new URL(path).toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static BufferedImage getImage(String path) throws Exception {
        if (isURL(path)) {
            URL url = new URL(path);
            return ImageIO.read(url.openStream());
        }

        File imageFile = new File(Main.imageDir, path);
        if (!imageFile.isFile()) {
            throw new Exception("Specified path is not a file (" + path + ")");
        }

        return ImageIO.read(imageFile);
    }

    public static BufferedImage getImageWithBackground(Color background, BufferedImage image, int col, int row) {
        BufferedImage img = new BufferedImage(col * 128, row * 128, image.getType());
        Graphics2D g = (Graphics2D)img.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(background);
        g.fillRect(0, 0, img.getWidth(), img.getHeight());
        g.drawImage(image, (col * 128 - image.getWidth()) / 2, (row * 128 - image.getHeight()) / 2, image.getWidth(), image.getHeight(), null);
        g.dispose();

        return img;
    }

    public static BufferedImage getPreviewImage(BufferedImage img) {
        BufferedImage previewImage = new BufferedImage(128, 128, img.getType());
        Graphics2D g = (Graphics2D) previewImage.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int widthP = 128;
        int heightP = Math.round(img.getHeight() * (128.0f / img.getWidth()));

        if (img.getHeight() > img.getWidth()) {
            heightP = 128;
            widthP = Math.round(img.getWidth() * (128.0f / img.getHeight()));
        }

        g.drawImage(img.getScaledInstance(widthP, heightP, 4), (previewImage.getWidth() - widthP) / 2, (previewImage.getHeight() - heightP) / 2, widthP, heightP, null);
        g.dispose();

        return previewImage;
    }
}
