
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
public class TestMaster {
    public static void main(String[] args) {
        String tokenTest ="eros|glauco|koller|1|2|3|4|5|6";
        
        StringTokenizer st = new StringTokenizer(tokenTest,"|");
        
        System.out.println(st.countTokens());
        
        while(st.hasMoreTokens()){
            System.out.println(st.nextToken());
        }
        
        
    }
    
}
