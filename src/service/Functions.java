package service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Functions {
    public static List<String> findFile(String path){
        File file = new File(path);
        List<String> lines = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()){
                String linea = scanner.nextLine();
                lines.add(linea);
            }
            scanner.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        System.out.println(lines);
        return lines;
    }

    public static List<String> rerfactorChords(List<String> lines){
        List<String> newLines = new ArrayList<>();

        for (int i = 0; i < lines.size(); i = i +2){

            String currentLine = lines.get(i);
            String originalNextLine = lines.get(i + 1);
            String nextLine = lines.get(i + 1);
            int indexBeginChord = 0;
            int indexEndChord = 0;
            int numberChords = 0;
            Character lastChar = null;
            Character nextChar = null;
            Character currentChar = null;
            String chord = null;

            for (int j = 0; j < currentLine.length(); j++){

                if (j > 0) {
                    lastChar = currentLine.charAt(j - 1);
                }else {
                    lastChar = 0;
                }

                if (j < currentLine.length() - 1 && currentLine.length() > 1) {
                    nextChar = currentLine.charAt(j + 1);
                }else {
                    nextChar = 0;
                }
                currentChar = currentLine.charAt(j);

                if(currentChar != 32  && lastChar == 32 || lastChar == 0){
                    indexBeginChord = j;
                }

                if (currentChar != 32 && nextChar == 0 || nextChar == 32){
                    indexEndChord = j;
                }

                if(numberChords == 0){
                    if (indexBeginChord == 0 && currentLine.length() == 1){
                         chord = currentLine.substring(indexBeginChord);
                         nextLine = nextLine.substring(0, indexBeginChord) +
                                 "[" +
                                 chord +
                                 "]" +
                                 nextLine.substring(indexBeginChord);
                         newLines.add(nextLine);
                         numberChords = numberChords + 1;
                    } else if ( currentChar != 32 && indexBeginChord == 0 && nextChar == 32 || nextChar == 0 ) {
                        chord = currentLine.substring(indexBeginChord, indexEndChord + 1);
                        nextLine = nextLine.substring(0, indexBeginChord) +
                                "[" +
                                chord +
                                "]" +
                                nextLine.substring(indexBeginChord);
                        newLines.add(nextLine);
                        numberChords = numberChords + 1;
                    } else if (indexBeginChord > 0 && indexEndChord > 0 && indexBeginChord != indexBeginChord) {
                        chord = currentLine.substring(indexBeginChord, indexEndChord);
                        nextLine = nextLine.substring(0, indexBeginChord) +
                                "[" +
                                chord +
                                "]" +
                                nextLine.substring(indexBeginChord);
                        newLines.add(nextLine);
                        indexBeginChord = 0;
                        indexEndChord = 0;
                        numberChords = numberChords + 1;
                    } else if (indexBeginChord > 0 && indexEndChord > 0 && indexBeginChord < indexEndChord) {
                        chord = currentLine.substring(indexBeginChord, indexEndChord + 1);
                        nextLine = nextLine.substring(0, indexBeginChord) +
                                "[" +
                                chord +
                                "]" +
                                nextLine.substring(indexBeginChord);
                        newLines.add(nextLine);
                        indexBeginChord = 0;
                        indexEndChord = 0;
                        numberChords = numberChords + 1;
                    }
                } else{

                    int a = nextLine.length() - originalNextLine.length();

                    if (indexBeginChord > 0 && indexEndChord > 0 && indexBeginChord < indexEndChord) {
                        chord = currentLine.substring(indexBeginChord, indexEndChord+1);
                        nextLine = nextLine.substring(0, indexBeginChord + a) +
                                "[" +
                                chord +
                                "]" +
                                nextLine.substring(indexBeginChord + a);
                        newLines.set(newLines.size()-1, nextLine);
                        indexBeginChord = 0;
                        indexEndChord = 0;
                        numberChords = numberChords + 1;
                    } else if (indexBeginChord > 0 && indexEndChord > 0 && nextChar != 32 && indexBeginChord == indexEndChord) {
                        chord = String.valueOf(currentLine.charAt(indexBeginChord));
                        nextLine = nextLine.substring(0, indexBeginChord + a) +
                                "[" +
                                chord +
                                "]" +
                                nextLine.substring(indexBeginChord + a);
                        newLines.set(newLines.size()-1, nextLine);
                        indexBeginChord = 0;
                        indexEndChord = 0;
                        numberChords = numberChords + 1;
                    }
                }
            }
        }
        return newLines;
    }

    public static List<String> findSlash(List<String> newLines){
        List<String> newLinesSlash = new ArrayList<>();
        newLines.stream().forEach(linea -> {

            String newLine = linea;
            int numberChords = 0;

            for (int i = 0; i < linea.length(); i++){
                char currentChar = linea.charAt(i);
                int a = newLine.length() - linea.length();
                if (numberChords == 0) {
                    if (currentChar == 47) {
                        newLine = newLine.substring(0, i) +
                                "]" +
                                newLine.charAt(i) +
                                "[" +
                                newLine.substring(i + 1);
                        numberChords = numberChords +1;
                    }
                } else {
                    if (currentChar == 47) {
                        newLine = newLine.substring(0, i + a) +
                                "]" +
                                newLine.charAt(i + a) +
                                "[" +
                                newLine.substring(i + 1 + a);
                        numberChords = numberChords + 1;
                    }
                }
            }
            newLinesSlash.add(newLine);
        });
        return newLinesSlash;
    }

    public static void saveFile(List<String> newLinesSlash, String path){
        FileWriter flwriter = null;
        try {
            //crea el flujo para escribir en el archivo
            flwriter = new FileWriter(path);
            //crea un buffer o flujo intermedio antes de escribir directamente en el archivo
            BufferedWriter bfwriter = new BufferedWriter(flwriter);

            newLinesSlash.stream().forEach(linea -> {
                try {
                    bfwriter.write(linea + "\n");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            //cierra el buffer intermedio
            bfwriter.close();
            System.out.println("Archivo creado satisfactoriamente..");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (flwriter != null) {
                try {//cierra el flujo principal
                    flwriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
