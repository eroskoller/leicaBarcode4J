/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.img.generator;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.imageio.ImageIO;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;

/**
 *
 * @author eros
 */
public class Code128Util {

        public byte[] buildBarCode128(String code, String format,File fileImage) throws IOException {

            try {
                Code128Bean bean = new Code128Bean();
                final int dpi = 80;
                //Configure the barcode generator
                bean.setModuleWidth(UnitConv.in2mm(1.0f / dpi)); //makes the narrow bar 
                //width exactly one pixel
                bean.setHeight(9d);
//            bean.setWideFactor(3);
                bean.doQuietZone(true);
                //Open output file
//                System.out.println("path + code + \".\" + format  :"+path + code + "." + format);
//                fileImage = new File(path + code + "." + format);
                OutputStream out = new FileOutputStream(fileImage);
                try {
                    //Set up the canvas provider for monochrome JPEG output 
                    BitmapCanvasProvider canvas = new BitmapCanvasProvider(out, "image/" + format, dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0);
                    //Generate the barcode
                    bean.generateBarcode(canvas, code);
                    //Signal end of generation
                    canvas.finish();
                } finally {
                    out.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
//            File imgFile = new File(path + code + "." + format);
            BufferedImage bufferedImage = ImageIO.read(fileImage);
            // get DataBufferBytes from Raster
            WritableRaster raster = bufferedImage.getRaster();
            DataBufferByte data = (DataBufferByte) raster.getDataBuffer();
            byte[] arrayReturn = data.getData();
            
            return arrayReturn;
    }



}
