/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dasa.leica;

import br.com.img.generator.QRCodeBuilder;   
import java.io.File;
import java.io.IOException;




     
/**
 *
 * @author eros   
 */
public class LeicaTeste {

    public static void main(String[] args) throws IOException {
//        QRCodeBuilder qRCodeBuilder = new QRCodeBuilder();
//        byte[] arrayByte = qRCodeBuilder.buildBarQRCode("/home/eros/teste_qrcode", "101010101010", "101010101010", "123fap456", "a3f", "png");
        
        System.out.println(new File("/home/eros/CrunchifyQR.png").getAbsolutePath());
    }
    
}
