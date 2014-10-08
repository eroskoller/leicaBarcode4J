/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.img.generator;

import br.com.dasa.leica.Lamina;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import javax.imageio.ImageIO;

/**
 *
 * @author eros
 */
public class QRCodeBuilderTest {
     public static void main(String[] args) {
         
        String myCodeText = "http://Crunchify.com/";   
        String filePath = "/home/eros/CrunchifyQR.png";
        int width = 14;
        int height = 28;
        String fileType = "png";
        File myFile = new File(filePath);
        try {
            Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix byteMatrix = qrCodeWriter.encode(myCodeText,BarcodeFormat.QR_CODE, width, height, hintMap);
            
            int CrunchifyWidth = byteMatrix.getWidth();
            BufferedImage image = new BufferedImage(CrunchifyWidth+100, CrunchifyWidth,
                    BufferedImage.TYPE_INT_RGB);
            image.createGraphics();
 
            Graphics2D graphics = (Graphics2D) image.getGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, CrunchifyWidth+100, CrunchifyWidth);
            graphics.setColor(Color.BLACK);
            int x = 40;
            graphics.drawString("amostra", x, 10);
            graphics.drawString("fap fap fap fap", x, 20);
            graphics.drawString("cod. order", x, 30);
            
            
            for (int i = 0; i < CrunchifyWidth; i++) {
                for (int j = 0; j < CrunchifyWidth; j++) {
                    if (byteMatrix.get(i, j)) {
                        graphics.fillRect(i, j, 1, 1);
                    }
                }
            }
            ImageIO.write(image, fileType, myFile);
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
        System.out.println("\n\nYou have successfully created QR Code.");
        
        // insite branch 0.111 mergo into master 
        // develop branch
        // merging  into master
        //creating a new stable branch 0.112
        //trying smartgit
        // fail to use smartgit cause of the fucking proxy
        
    }       
  
}
