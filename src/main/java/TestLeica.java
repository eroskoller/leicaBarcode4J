
import br.com.dasa.leica.Lamina;
import br.com.img.generator.QRCodeBuilder;
import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;

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
        String tokenTest ="eros|glauco|c|";
        
        StringTokenizer st = new StringTokenizer(tokenTest,"|");
        
        System.out.println(st.countTokens());
//        
//        while(st.hasMoreTokens()){
//            System.out.println(st.nextToken());
//        }
        
        
        File myFile = new File("/home/eros/max_chars.png");
        QRCodeBuilder qrb = new QRCodeBuilder();
        qrb.buildBarQRCode(new Lamina("120000000000|120000000000|5000-80000000", "|"), "png",myFile);
        
//        Code128Util code128 = new Code128Util();
//        code128.buildBarCode128("9886767", "png", myFile);
        
    }
    
}
