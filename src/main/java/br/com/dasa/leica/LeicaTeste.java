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
        QRCodeBuilder qRCodeBuilder = new QRCodeBuilder();
        Lamina lamina = new Lamina("amostra", "numero fap ", "cod. ordem");
        byte[] arrayByte = qRCodeBuilder.buildBarQRCode("/home/eros/", lamina, "png","n");
        
//        System.out.println(new File("/home/eros/CrunchifyQR2.png").getAbsolutePath());
    }
    
}
