package main.utility;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReadWrite {

    public static void writeToFile(String pathToWriteTo, String msgToWrite, boolean append, boolean newLine) {
        BufferedWriter bw = null;
        try {
            File logFile = new File(pathToWriteTo);
            bw = new BufferedWriter(new FileWriter(logFile.getAbsoluteFile(), append));
            if(newLine)
                bw.write(System.lineSeparator());
            bw.write(msgToWrite);

        } catch (IOException e) {
            Aspect.LOG.info("IOE1 in general writeToFile method");
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException ex) {
                Aspect.LOG.info("IOE2 in general writeToFile method");
            }
        }

    }

    //read from file
    public static String readFromFile(String path) {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path));
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                sb.append(System.lineSeparator());
                sb.append(sCurrentLine);
            }
        } catch (IOException e) {
            Aspect.LOG.info("ReadWrite - IOE 1 wiejfi");
        } finally {
            try {

                if (br != null)
                    br.close();

            } catch (IOException ex) {
                Aspect.LOG.info("ReadWrite - IOE 2 waiejf");

            }
        }
        return sb.toString();
    }

    public static List<String> readFromFileToStringList(String path) {
        List<String> toReturn = new ArrayList<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path));
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                toReturn.add(sCurrentLine);
            }
        } catch (IOException e) {
            Aspect.LOG.info("IOE 1 wiejfi");
        } finally {
            try {

                if (br != null)
                    br.close();

            } catch (IOException ex) {
                Aspect.LOG.info("IOE 2 waiejf");

            }
        }
        return toReturn;
    }

}
