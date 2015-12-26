/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assembler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

/**
 *
 * @author Tasneem
 */
public class Parser {

    private String fileName ;

    public String read() {
        String line = null;

        try {
            FileReader fileReader = new FileReader(getFileName());
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuilder=new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append("\n");
            } 
            bufferedReader.close();
            return stringBuilder.toString();
        } catch (Exception ex) {
            System.out.println("Unable to open file '" + getFileName() + "'");
            return null;
        }
    }

    public void write(String text) {
        try {
            FileWriter fileWriter= new FileWriter(getFileName());
            BufferedWriter bufferedWriter= new BufferedWriter(fileWriter);
            String [] S=text.split("\n");
            for (String Str : S)
            {
                bufferedWriter.write(Str);
                bufferedWriter.newLine();
            }    
            bufferedWriter.close();
        } catch (Exception ex) {
            System.out.println("Error writing to file '" + getFileName() + "'");
        }
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param aFileName the fileName to set
     */
    public void setFileName(String aFileName) {
        fileName = aFileName;
    }
}


