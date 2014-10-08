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
public class QRCodeBuilder {
    
     
     public byte[] buildBarQRCode(String filePath, Lamina lamina, String format,String deleteImg) throws IOException {
         
//         String code = "http://Crunchify.com/";   
//        String filePath = "/home/eros/CrunchifyQR.png";
        int width = 14;
        int height = 28;
//        String fileType = "png";
        byte[] arrayReturn = null;
        File imgFile = new File(filePath+ lamina.getAmostra() + "." + format);
        try {
            Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix byteMatrix = qrCodeWriter.encode(lamina.getAmostra(),BarcodeFormat.QR_CODE, width, height, hintMap);
            
            int CrunchifyWidth = byteMatrix.getWidth();
            BufferedImage image = new BufferedImage(CrunchifyWidth+100, CrunchifyWidth,
                    BufferedImage.TYPE_INT_RGB);
            image.createGraphics();
 
            Graphics2D graphics = (Graphics2D) image.getGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, CrunchifyWidth+100, CrunchifyWidth);
            graphics.setColor(Color.BLACK);
            int x = 28;
            graphics.drawString(lamina.getAmostra(), x, 8);
            graphics.drawString(lamina.getFap(), x, 18);
            graphics.drawString(lamina.getCodeOrdem(), x, 26);
            
            
            for (int i = 0; i < CrunchifyWidth; i++) {
                for (int j = 0; j < CrunchifyWidth; j++) {
                    if (byteMatrix.get(i, j)) {
                        graphics.fillRect(i, j, 1, 1);
                    }
                }
            }
            ImageIO.write(image, format, imgFile);
            
            image = ImageIO.read( new File(imgFile.getAbsolutePath()));
            // get DataBufferBytes from Raster
            WritableRaster raster = image.getRaster();
            DataBufferByte data = (DataBufferByte) raster.getDataBuffer();
            arrayReturn = (data.getData());
//            return  arrayReturn;
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
        System.out.println("\n\nYou have successfully created QR Code.");
         
        if(deleteImg != null && deleteImg.equalsIgnoreCase("s")){
            imgFile.delete();
       }
        
         return arrayReturn;
     }
     
}
