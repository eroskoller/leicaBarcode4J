/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dasa.leica;


import br.com.img.generator.Code128Util;
import br.com.img.generator.QRCodeBuilder;
import com.google.gson.Gson;
import com.google.zxing.WriterException;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaPrintableArea;

/**
 *
 * @author eros
 */
public class Leica implements Printable {

    public static void main(String[] args) throws IOException {

        System.out.println("Inside class Leica ");
        int ic = 0;
        int ia = 8;
        int tc = 5;
        int ta = 0;
        int fc = 70;
        int fa = 20;
        String codigo = null;
        String saida = null;
        String impressora = null;
        String tipoimpressora = null;
        int porta = 0;
        String deleteImg = "S";
        String currentPath = Leica.class.getProtectionDomain().getCodeSource().getLocation().getFile();

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-ic":
                    ic = Integer.parseInt(args[++i]);
                    break;
                case "-ia":
                    ia = Integer.parseInt(args[++i]);
                    break;
                case "-tc":
                    tc = Integer.parseInt(args[++i]);
                    break;
                case "-ta":
                    ta = Integer.parseInt(args[++i]);
                    break;
                case "-fc":
                    fc = Integer.parseInt(args[++i]);
                    break;
                case "-fa":
                    fa = Integer.parseInt(args[++i]);
                    break;
                case "-codigo":
                    codigo = args[++i];
                    break;
                case "-saida":
                    saida = args[++i];
                    break;
                case "-impressora":
                    impressora = args[++i];
                    break;
                case "-tipoimpressora":
                    tipoimpressora = args[++i];
                    break;
                case "-impressoras":
                    impressoras(null, tipoimpressora);
                    System.exit(0);
                    break;
                case "-porta":
                    porta = Integer.parseInt(args[++i]);
                    break;
                case "-deleteImg":
                    deleteImg = args[++i];
                    break;
            }
        }
        Leica programa = new Leica();
        PrinterJob job = null;
        try {
            job = programa.obtemJob(tipoimpressora, impressora);
        } catch (PrinterException ex) {
            Logger.getLogger(Leica.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(9);
        }
        
        if (porta != 0) {
            //            try (ServerSocket serverSocket = new ServerSocket(porta);
            while (true) {
                ServerSocket serverSocket = null;
                Socket clientSocket =  null;
                try {
                    serverSocket = new ServerSocket(porta);
                    clientSocket = serverSocket.accept();
                    clientSocket.setKeepAlive(true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    
                    while ((codigo = in.readLine()) != null) {
                        if (codigo.length() >= 12) {
                            codigo = codigo.trim().replaceAll("\\D", "");
                            System.out.println("codigo: " + codigo);
                            if (codigo.length() == 0) {
                                System.out.println("codigo.length() == 0");     
                                continue;
                            }
                            System.out.println("codigo.trim() : " + codigo.trim() + "     currentPath : " + currentPath);
                            if(codigo.length() == 12){
                                programa.principal(fc, fa, tc, ta, job, codigo, saida, ic, ia, currentPath,"Interleaved2of5Code128",deleteImg);
                            }else if(codigo.length() > 12){
                                programa.principal(fc, fa, tc, ta, job, codigo, saida, ic, ia, currentPath,"QRCode",deleteImg);
                            }
                            
                        } else {
                            System.out.println("codigo.length() != 12)"); System.out.println("codigo = "+codigo);
                        }
                    }
                    
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                    Logger.getLogger(Leica.class.getName()).log(Level.SEVERE, null, ex);
//                System.exit(10);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                    Logger.getLogger(Leica.class.getName()).log(Level.SEVERE, null, ex);
//                System.exit(10);
                }
                if(clientSocket != null && !clientSocket.isClosed()){
                    clientSocket.close();
                    serverSocket.close();
                }
//                new File(currentPath + codigo + ".png").delete();
                System.out.println("Out......................");
            }
            
            
            
        } else {
//            programa.principal(fc, fa, tc, ta, job, codigo, saida, ic, ia, currentPath,"Interleaved2of5Code128");
        }
    }

    public static void stop(){
        System.out.println("Exiting serice .    .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .           ");
        System.exit(0);
    }
    
    private BufferedImage imagem = null;
    private int tc = 0;
    private int ta = 0;
    private int fc = 0;
    private int fa = 0;

    private void principal(int fc, int fa, int tc, int ta, PrinterJob job, String codigo, String saida, int ic, int ia, String path,String codeType,String deleteImg) {
        this.tc = tc;
        this.ta = ta;
        this.fc = fc;
        this.fa = fa;
        byte[] buffer = null;
        try {
//            System.out.println("path, codigo, ic, ia : "+path+ codigo);
            switch(codeType){
                case "Interleaved2of5Code128":
                    buffer = makeInterleaved2of5Code128(path, codigo, ic, ia,deleteImg);
                        break;
                case "QRCode":
                    Lamina lamina = new Gson().fromJson(codigo, Lamina.class);
                    buffer = makeQRCode(path, lamina, ic, ia,deleteImg);
                    break;
                 default:
                     buffer = makeInterleaved2of5Code128(path, codigo, ic, ia,deleteImg);
            }
            
//            System.out.println("buffer: " + buffer);
        } catch (WriterException ex) {
            Logger.getLogger(Leica.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
//            System.exit(1);
        } catch (IOException ex) {
            Logger.getLogger(Leica.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
//            System.exit(2);
        }

        try {
//            imagem = ImageIO.read(new ByteArrayInputStream(buffer));

//            File f = new File
            Image imageReal = ImageIO.read(new File(path + codigo + ".png"));
            BufferedImage buffered = (BufferedImage) imageReal;
            imagem = buffered;

        } catch (IOException ex) {
            System.out.println(ex);
            Logger.getLogger(Leica.class.getName()).log(Level.SEVERE, null, ex);
//            System.exit(3);
        }

        try {
            geraSaida(saida, buffer);
        } catch (IOException ex) {
            Logger.getLogger(Leica.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex);
//            System.exit(4);
        }

        imprime(job);

    }

    private byte[] makeInterleaved2of5Code128(String path, String codigo, int comprimento, int altura,String deleteImg) throws WriterException, IOException {
//        System.out.println("path, codigo, \"png\":  " + path+ codigo+ "png");
        Code128Util code = new Code128Util();
        byte[] arrayByte = code.buildBarCode128(path, codigo, "png",deleteImg);
//        System.out.println("arrayByte: " + arrayByte);
        return arrayByte;
//        return fos.toByteArray();
    }
    
        private byte[] makeQRCode(String path, Lamina lamina, int comprimento, int altura,String deleteImg) throws WriterException, IOException {
//        System.out.println("path, codigo, \"png\":  " + path+ codigo+ "png");
        QRCodeBuilder code = new QRCodeBuilder();
        byte[] arrayByte = code.buildBarQRCode(path, lamina, "png",deleteImg);
//        System.out.println("arrayByte: " + arrayByte);
        return arrayByte;
//        return fos.toByteArray();
    }

    private void geraSaida(String saida, byte[] buffer) throws FileNotFoundException, IOException {
        if (saida == null) {
            return;
        }
        FileOutputStream fos = new FileOutputStream(saida);
        fos.write(buffer);
        fos.flush();
        fos.close();
    }

    private PrinterJob obtemJob(String tipoimpressora, String impressora) throws PrinterException {
        if (impressora == null) {
            return null;
        }
        PrintService ps = null;
        ps = impressoras(impressora, tipoimpressora);
        if (ps == null) {
            throw new RuntimeException("Impressora e tipo não encontrados!!" + impressora + " " + tipoimpressora);
        }

        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(this);

        job.setPrintService(ps);
        job.setCopies(1);
        return job;
    }

    private void imprime(PrinterJob job) {
        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        MediaPrintableArea mpa = new MediaPrintableArea((float) 0.0, (float) 0.0, (float) 28.2, (float) 7.0, MediaPrintableArea.MM);
        aset.add(mpa);
        try {
            job.print(aset);
        } catch (PrinterException e) {
            System.err.println("erro no job.print()");
            System.out.println(e.getMessage());
        }
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if (pageIndex > 0) {
            return NO_SUCH_PAGE;
        }

        ((Graphics2D) graphics).translate(this.tc, this.ta);
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, fc, fa);
        if (this.imagem == null) {
            System.out.println("this.imagem == null");
        }
        graphics.drawImage(this.imagem, 0, 0, fc, fa, 0, 0, this.imagem.getWidth(), this.imagem.getHeight(), null);
        return PAGE_EXISTS;
    }

    private static PrintService impressoras(String nome, String tipoimpressora) {
        PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
        pras.add(new Copies(1));
        PrintService pss[] = null;
        if (tipoimpressora == null) {
            pss = PrintServiceLookup.lookupPrintServices(null, pras);
        } else {
            switch (tipoimpressora) {
                case "gif":
                    pss = PrintServiceLookup.lookupPrintServices(DocFlavor.INPUT_STREAM.GIF, pras);
                    break;
                case "png":
                    pss = PrintServiceLookup.lookupPrintServices(DocFlavor.INPUT_STREAM.PNG, pras);
                    break;
                case "jpeg":
                    pss = PrintServiceLookup.lookupPrintServices(DocFlavor.INPUT_STREAM.JPEG, pras);
                    break;
            }
        }
        if (pss == null || pss.length == 0) {
            throw new RuntimeException("Sem serviço de impressao!!");
        }
        PrintService ps = null;
        for (PrintService tps : pss) {
            if (nome == null) {
                System.out.println("Impressora " + tps.getName() + " disponivel");
            } else {
                if (tps.getName().equals(nome)) {
                    ps = tps;
                }
            }
        }
        return ps;
    }
}
