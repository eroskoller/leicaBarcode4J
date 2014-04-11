/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.dasa.leica.leicabarcode4j;


import java.awt.Color;
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
import java.awt.print.PrinterJob;
import java.io.BufferedReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;

/**
 *
 * @author taragona
 */
public class LeicaZeros implements Printable {
    
    private BufferedImage imagem = null;
    private File fileImage = null;
    private int tc = 0;
    private int ta = 0;
    private int fc = 0;
    private int fa = 0;

    public File getFileImage() {
        return fileImage;
    }

    public void setFileImage(File fileImage) {
        this.fileImage = fileImage;
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
    
    public PrinterJob obtemJob(String tipoimpressora, String impressora) throws PrinterException {
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
    
    public void principalBarCode4J(PrinterJob job, String path,String codigo, String saida,String format) {
        
        byte[] buffer = null;
        try {
            buffer = buildBarCode128(path, codigo, format);
        } catch (IOException ex) {
            Logger.getLogger(LeicaZeros.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            System.out.println("buffer : "+buffer);
//            this.imagem = ImageIO.read(new ByteArrayInputStream(buffer));
//            fileImage = new File(path+codigo +"."+format);
            if(fileImage.isFile()){
                System.out.println("file.isFile()");
                System.out.println("file.getCanonicalPath(): "+fileImage.getCanonicalPath());
            }else{
                System.out.println("It's not a fucking File.....");
            }
            this.imagem = ImageIO.read(fileImage);
            geraSaida(saida, buffer);
        } catch (IOException ex) {
            Logger.getLogger(LeicaZeros.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(4);
        }
        try {
            geraSaida(saida, buffer);
        } catch (IOException ex) {
            Logger.getLogger(LeicaZeros.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(4);
        }
        
                if(this.fileImage != null){
                    this.fileImage.delete();
                }
        imprime(job);

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

    private void imprime(PrinterJob job) {
        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        MediaPrintableArea mpa = new MediaPrintableArea((float) 0.0, (float) 0.0, (float) 28.2, (float) 7.0, MediaPrintableArea.MM);
        System.out.println("aset.add(mpa) : "+aset.add(mpa));
        try {
            job.print(aset);
        } catch (Exception e) {
            System.err.println("erro no job.print()");
            e.printStackTrace();
        }
    }
    
    public byte[] buildBarCode128(String path,String code,  String format) throws IOException {

        if (code != null && code.length() >= 4 && code.length() <= 40
                && path != null && format != null && format.length() == 3) {
            
            //Open output file
//            this.fileImage = new File(path+code+"."+format);
            
            try {
                
                Code128Bean bean = new Code128Bean();
                final int dpi = 80;
                //Configure the barcode generator
                bean.setModuleWidth(UnitConv.in2mm(1.0f / dpi)); //makes the narrow bar 
                //width exactly one pixel
                bean.setHeight(9d);
//            bean.setWideFactor(3);
                bean.doQuietZone(true);
            
                
                OutputStream out = new FileOutputStream(this.fileImage);
                try {
                    //Set up the canvas provider for monochrome JPEG output 
                    BitmapCanvasProvider canvas = new BitmapCanvasProvider(
                            out, "image/"+format, dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0);

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
//            File imgPath = new File(path+code+"."+format);
            BufferedImage bufferedImage = ImageIO.read(this.fileImage);

            // get DataBufferBytes from Raster
            WritableRaster raster = bufferedImage .getRaster();
            DataBufferByte data   = (DataBufferByte) raster.getDataBuffer();
            byte[] arrayReturn = ( data.getData() );
            System.out.println("byte[] arrayReturn : "+arrayReturn);
            
            return arrayReturn;
        } else {
            System.out.println("Problemas  code : " + code + " path: " + path + "  format: " + format);
            return null;
        }

    }
      
    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if (pageIndex > 0) {return NO_SUCH_PAGE;}
        
        if(imagem == null){System.out.println("imagem is null ");}
        
        ((Graphics2D) graphics).translate(this.tc, this.ta);
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, fc, fa);
        System.out.println("graphics.drawImage(this.imagem, 0, 0, fc, fa, 0, 0, this.imagem.getWidth(), this.imagem.getHeight(), null) : "+graphics.drawImage(this.imagem, 0, 0, fc, fa, 0, 0, this.imagem.getWidth(), this.imagem.getHeight(), null));
        return PAGE_EXISTS;
    }
    
    public static void main(String[] args) {
        int ic = 0;
        int ia = 8;
        int tc = 5;
        int ta = 0;
        int fc = 70;
        int fa = 20;
        String codigo = "123456789012";
        String saida = null;
        String impressora = null;
        String tipoimpressora = null;
        int porta = 0;
        String path = "/tmp/"; //"/home/eros/Projects/Maven/LeicaProject/"

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
            }
        }
        LeicaZeros programa = new LeicaZeros();
        
        PrinterJob job = null;
        System.out.println("impressora = "+ impressora+"       tipo de impressora = "+tipoimpressora+"  porta = "+porta);
        try {
            System.out.println("job = programa.obtemJob(tipoimpressora, impressora)");
            job = programa.obtemJob(tipoimpressora, impressora);
        } catch (PrinterException ex) {
            Logger.getLogger(LeicaZeros.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
            System.exit(9);
        }
        if (porta != 0) {
            try (ServerSocket serverSocket = new ServerSocket(porta);
                    Socket clientSocket = serverSocket.accept();
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));) {
                    System.out.println("Getting socket....");
                while ((codigo = in.readLine()) != null) {
                    codigo = codigo.trim();
                    System.out.println("looping "+codigo);
                    if (codigo.length() == 0) {
                        continue;
                    }
                    programa.setFileImage(new File(path+codigo+".png"));
                    programa.principalBarCode4J(job,path ,codigo, saida,"png");
//                    programa.principal(fc, fa, tc, ta, job, codigo, saida, ic, ia);
                    
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
                Logger.getLogger(LeicaZeros.class.getName()).log(Level.SEVERE, null, ex);
                System.exit(10);
            }catch(Exception ex){
                System.out.println(ex.getMessage());
                Logger.getLogger(LeicaZeros.class.getName()).log(Level.SEVERE, null, ex);
                System.exit(10);
            }
        } else {
            programa.principalBarCode4J(job, path ,codigo, saida,"png");
//            programa.principal(fc, fa, tc, ta, job, codigo, saida, ic, ia);
        }
    }
    
}
