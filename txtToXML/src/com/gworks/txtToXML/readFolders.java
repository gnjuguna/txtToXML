package com.gworks.txtToXML;
import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;


public class readFolders {
  public static File folder = new File("/Users/george/Downloads/jsizambia/");
  static String temp = "";

  public static void readFolder() {
    // TODO Auto-generated method stub
    System.out.println("Reading files under the folder "+ folder.getAbsolutePath());
    listFilesForFolder(folder);
  }

  public static void listFilesForFolder(final File folder) {
    File fileToConvert = new File("");
    for (final File fileEntry : folder.listFiles()) {
      if (fileEntry.isDirectory()) {
        // System.out.println("Reading files under the folder "+folder.getAbsolutePath());
        listFilesForFolder(fileEntry);
      } else {
        if (fileEntry.isFile()) {
          temp = fileEntry.getName();
          if ((temp.substring(temp.lastIndexOf('.') + 1, temp.length()).toLowerCase()).equals("txt"))
              //System.out.println(folder.getAbsolutePath() + "/" + temp);
              //System.out.println(temp);
              fileToConvert = new File(folder.getAbsolutePath() + "/" + temp);
              System.out.println("xxx" + fileToConvert);
               convert(fileToConvert);
        }

      }
    }
  }

	public static void convert(File file) {
        //Prepares to read from the input file.
        File input = file;	
        Scanner scanner;
        try{
            scanner = new Scanner(input);
        }catch(Exception e){
        	System.out.println(input.toString());
            System.out.println("Input file does not exist or is not readable. Program will exit.");
            return;
        }
        
        //Prepares to write to the output file.
        File output = new File(input.toString().replace("txt", "xml"));
        
        System.out.println("zzz" + output.toString());
        PrintWriter writer;
        try{
            writer = new PrintWriter(output);
        }catch(Exception e){
            System.out.println("Output file location is not writable. Program will exit.");
            return;
        }
        
        boolean firstLine = true;
        long totalLines = 0;
        long processedLines = 0;
        String[] headers = null;
        
        //Reads in every line of the text file and attempts to convert it into XML elements.
        writer.println("<records>");
        while(scanner.hasNext()){
            String line = scanner.nextLine().trim();
            
            String[] elements = line.split("\t");
            
            //If it's the first line, it becomes the header line, and the operation moves on to the next line.  Otherwise, continue as normal.
            if(firstLine){
                headers = elements.clone();
                firstLine = false;
                continue;
            }
            
            ++totalLines; //The first line is not counted in totalLines.
            
            //Skips this line if there aren't enough elements (in regards to the number of headers).
            if(elements.length < headers.length){
                continue;
            }
            
            writer.println("<data>");
            //Prints each element from the text file line as an XML element.
            for(int i = 0; i < headers.length; ++i){
                writer.println("<" + headers[i] + ">" + elements[i] + "</" + headers[i] + ">");
            }
            
            //Prints an extra line (for readability) after every group of elements.
            writer.println("</data>");
            
            ++processedLines;
        }
        writer.println("</records>");
        
        //Cleans up and displays confirmation message.
        scanner.close();
        writer.close();
        System.out.printf(
            "XML file created at %s\n" +
            "%d/%d lines processed.\n",
            output.getPath(),
            processedLines,
            totalLines);
		
	}

}