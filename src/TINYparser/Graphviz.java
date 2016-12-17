package TINYparser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;


public class Graphviz
{
    
   private static String TEMP_DIR ;   
   
    
   private static String DOT ;
   
   
    private StringBuffer graph = new StringBuffer();   
   
    
   public Graphviz() {   
       String osName = System.getProperty("os.name").toLowerCase();
       if(osName.startsWith("l")) 
       {
           DOT = "/usr/bin/dot";
           TEMP_DIR = "/tmp"; 
       }
       else if(osName.startsWith("w"))
       {
           DOT = "C:\\Program Files (x86)\\Graphviz2.38\\bin\\dot.exe";
           TEMP_DIR = "C:\\Windows\\Temp";
       }
   } 
   
    
   public String getDotSource() {   
      return graph.toString();   
   }   
   
    
   public void add(String line) {   
      graph.append(line);   
   }   
   
   
   public void addln(String line) {   
      graph.append(line+"\n");   
   }   
   
   
   public void addln() {   
      graph.append('\n');   
   }   
   
   
   public byte[] getGraph(String dot_source)   
   {   
      File dot;   
      byte[] img_stream = null;   
   
      try {   
         dot = writeDotSourceToFile(dot_source);   
         if (dot != null)   
         {   
            img_stream = get_img_stream(dot);   
            if (dot.delete() == false)   
               System.err.println("Warning: "+dot.getAbsolutePath()+" could not be deleted!");   
            return img_stream;   
         }   
         return null;   
      } catch (java.io.IOException ioe) { return null; }   
   }   
   
   public byte[] getSvgGraph(String dot_source)   
   {   
      File dot;   
      byte[] img_stream = null;   
   
      try {   
         dot = writeDotSourceToFile(dot_source);   
         if (dot != null)   
         {   
            img_stream = get_img_stream_svg(dot);   
            if (dot.delete() == false)   
               System.err.println("Warning: "+dot.getAbsolutePath()+" could not be deleted!");   
            return img_stream;   
         }   
         return null;   
      } catch (java.io.IOException ioe) { return null; }   
   }   
   
   
   public int writeGraphToFile(byte[] img, String file)   
   {   
      File to = new File(file);   
      return writeGraphToFile(img, to);   
   }   
   
   
   public int writeGraphToFile(byte[] img, File to)   
   {   
      try {   
         FileOutputStream fos = new FileOutputStream(to);   
         fos.write(img);   
         fos.close();   
      } catch (java.io.IOException ioe) { return -1; }   
      return 1;   
   }   
   
   
   private byte[] get_img_stream(File dot)   
   {   
      File img;   
      byte[] img_stream = null;   
   
      try {   
         img = File.createTempFile("graph_", ".gif", new File(Graphviz.TEMP_DIR));   
         String temp = img.getAbsolutePath();   
   
         Runtime rt = Runtime.getRuntime();   
         String cmd = DOT + " -Tgif "+dot.getAbsolutePath()+" -o "+img.getAbsolutePath();   
         Process p = rt.exec(cmd);   
         p.waitFor();   
   
         FileInputStream in = new FileInputStream(img.getAbsolutePath());   
         img_stream = new byte[in.available()];   
         in.read(img_stream);   
         // Close it if we need to   
         if( in != null ) in.close();   
   
         if (img.delete() == false)   
            System.err.println("Warning: "+img.getAbsolutePath()+" could not be deleted!");   
      }   
      catch (java.io.IOException ioe) {   
         System.err.println("Error:    in I/O processing of tempfile in dir "+Graphviz.TEMP_DIR+"\n");   
         System.err.println("       or in calling external command");   
         ioe.printStackTrace();   
      }   
      catch (java.lang.InterruptedException ie) {   
         System.err.println("Error: the execution of the external program was interrupted");   
         ie.printStackTrace();   
      }   
   
      return img_stream;   
   }   
   private byte[] get_img_stream_svg(File dot)   
   {   
      File img;   
      byte[] img_stream = null;   
   
      try {   
         img = File.createTempFile("graph_", ".svg", new File(Graphviz.TEMP_DIR));   
         String temp = img.getAbsolutePath();   
   
         Runtime rt = Runtime.getRuntime();   
         String cmd = DOT + " -Tsvg "+dot.getAbsolutePath()+" -o "+img.getAbsolutePath();   
         Process p = rt.exec(cmd);   
         p.waitFor();   
   
         FileInputStream in = new FileInputStream(img.getAbsolutePath());   
         img_stream = new byte[in.available()];   
         in.read(img_stream);   
         // Close it if we need to   
         if( in != null ) in.close();   
   
         if (img.delete() == false)   
            System.err.println("Warning: "+img.getAbsolutePath()+" could not be deleted!");   
      }   
      catch (java.io.IOException ioe) {   
         System.err.println("Error:    in I/O processing of tempfile in dir "+Graphviz.TEMP_DIR+"\n");   
         System.err.println("       or in calling external command");   
         ioe.printStackTrace();   
      }   
      catch (java.lang.InterruptedException ie) {   
         System.err.println("Error: the execution of the external program was interrupted");   
         ie.printStackTrace();   
      }   
   
      return img_stream;   
   }   
   
   private File writeDotSourceToFile(String str) throws java.io.IOException   
   {   
      File temp;   
      try {   
         temp = File.createTempFile("graph_", ".dot.tmp", new File(Graphviz.TEMP_DIR));   
         FileOutputStream fout = new FileOutputStream(temp);   
         byte[] ss = str.getBytes("UTF8");   
         fout.write(ss);   
         fout.close();   
      }   
      catch (Exception e) {   
         System.err.println("Error: I/O error while writing the dot source to temp file!");   
         return null;   
      }   
      return temp;   
   }   
   
   
   public String start_graph() {   
      return "graph G {";   
   }   
   
   public String end_graph() {   
      return "}";   
   }   
}