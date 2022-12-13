import service.Functions;

import java.util.List;

public class Main {
    public static void main(String [] args){
        List<String> lines = Functions.findFile("C:\\Users\\DANIEL\\Desktop\\Desarrollos\\organizar-canciones\\src\\canciones\\cancion1.txt");
        List<String> newLines = Functions.rerfactorChords(lines);
        List<String> newLinesSlash = Functions.findSlash(newLines);
        Functions.saveFile(newLinesSlash, "C:\\Users\\DANIEL\\Desktop\\Desarrollos\\organizar-canciones\\src\\canciones2\\cancion1.txt");
    }
}