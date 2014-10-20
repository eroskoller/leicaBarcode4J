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
    
     
        public byte[] buildBarQRCode(Lamina lamina, String format,File fileImage) throws IOException,NullPointerException {
         
//         String code = "http://Crunchify.com/";   
//        String filePath = "/home/eros/CrunchifyQR.png";
        int width = 14;
        int height = 28;
//        String fileType = "png";
        byte[] arrayReturn = null;
//        fileImage = new File(filePath+ lamina.getAmostra() + "." + format);
        try {
            Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix byteMatrix = qrCodeWriter.encode(lamina.getAmostra(),BarcodeFormat.QR_CODE, width, height, hintMap);
            
            int CrunchifyWidth = byteMatrix.getWidth();
            BufferedImage buffuredImage = new BufferedImage(CrunchifyWidth+100, CrunchifyWidth,
                    BufferedImage.TYPE_INT_RGB);
            buffuredImage.createGraphics();
 
            Graphics2D graphics = (Graphics2D) buffuredImage.getGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, CrunchifyWidth+100, CrunchifyWidth);
            graphics.setColor(Color.BLACK);
            int x = 28;
            graphics.drawString(lamina.getAmostra(), x, 9);
            graphics.drawString(lamina.getFap(), x, 19);
            graphics.drawString(lamina.getCodeOrdem(), x, 29);
            
            
            for (int i = 0; i < CrunchifyWidth; i++) {
                for (int j = 0; j < CrunchifyWidth; j++) {
                    if (byteMatrix.get(i, j)) {
                        graphics.fillRect(i, j, 1, 1);
                    }
                }
            }
            if (ImageIO.write(buffuredImage, format, fileImage)) {
                buffuredImage = ImageIO.read(fileImage);
                // get DataBufferBytes from Raster
                WritableRaster raster = buffuredImage.getRaster();
                DataBufferByte data = (DataBufferByte) raster.getDataBuffer();
                arrayReturn = data.getData();
            }
            
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }finally{
            
        }
        System.out.println("\n\nYou have successfully created QR Code.");
         
//        if(deleteImg){
//            fileImage.delete();
//       }
        
         return arrayReturn;
     }
     
}
