/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.img.generator;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author eros
 */
public class ImgCreator {
    public static void creator(){
        BufferedImage image = new BufferedImage( 40, 20, BufferedImage.TYPE_INT_ARGB );  
        Graphics g = image.createGraphics();  
    }
    
    public static void main(String[] args) {
        ImgCreator.creator();
    }
}
