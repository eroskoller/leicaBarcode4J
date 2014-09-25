/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.img.generator;

import java.io.OutputStream;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;


/**
 *
 * @author eros
 */
public class CodeQRCodeUtil {
private static final String IMAGE_FORMAT = "png";

        /* (non-Javadoc)
         * @see co.syntx.examples.qr.IQRService#generateQRCode(java.lang.String, java.lang.String, java.io.OutputStream)
         */
	public void generateQRCode(int width, int height, String content, OutputStream outputStream) throws Exception { 
	    String imageFormat = IMAGE_FORMAT; 
	    BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE,width, height);
	    MatrixToImageWriter.writeToStream(bitMatrix, imageFormat, outputStream);     
	}



}
