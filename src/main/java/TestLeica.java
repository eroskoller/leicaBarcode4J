
import br.com.dasa.leica.Lamina;
import br.com.img.generator.Code128Util;
import br.com.img.generator.QRCodeBuilder;
import java.io.File;
import java.io.IOException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author eros
 */
public class TestLeica {
    public static void main(String[] args) throws IOException {
//        String tokenTest ="eros|glauco|koller|1|2|3|4|5|6";
//        
//        StringTokenizer st = new StringTokenizer(tokenTest,"|");
//        
//        System.out.println(st.countTokens());
//        
//        while(st.hasMoreTokens()){
//            System.out.println(st.nextToken());
//        }
        
        
        File myFile = new File("/home/eros/test.png");
        QRCodeBuilder qrb = new QRCodeBuilder();
        qrb.buildBarQRCode(new Lamina("0998766565|988787666|87756412334", "|"), "png",myFile);
        
//        Code128Util code128 = new Code128Util();
//        code128.buildBarCode128("9886767", "png", myFile);
        
    }
    
}
