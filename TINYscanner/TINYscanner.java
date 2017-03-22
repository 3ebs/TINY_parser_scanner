package TINYscanner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class TINYscanner {

    private String scannerOutput;
    private String[] lines;
    private BufferedReader br;
    private String lineInput, number, id;
    private final String reservedWords[] = {"if", "then", "else", "end", "repeat", "until", "read", "write"};
    private boolean reservedWordFlag;
    private boolean endLineFlag;

    public TINYscanner(File f) {
        ArrayList<String> Templines = new ArrayList<String>();
        String line = "";
        try {
            br = new BufferedReader(new FileReader(f));
            while (line != null) {
                line = br.readLine();
                Templines.add(line);
            }
        } catch (Exception e) {
            System.out.println("[Error] File not found!\n[Error] Reading file!");
        }
        lines = Templines.toArray(new String[Templines.size()]);
        reservedWordFlag = false;
        endLineFlag = false;
        number = "";
        id = "";
        scannerOutput = "";
    }

    public String doScan() {
        for(int k = 0; k < lines.length; k++) {
            lineInput = lines[k];
            if(lineInput == null) break;
            endLineFlag = false;
            for (int i = 0; i < lineInput.length(); i++) {
                switch (lineInput.charAt(i)) {
                    case '{':
                        for (; i < lineInput.length(); i++) {
                            if(lineInput.charAt(i) == '}') {
                                break;
                            }
                        }   break;
                    case '+':
                        scannerOutput += Character.toString(lineInput.charAt(i)) + "," +"ADD" + "\n";
                        break;
                    case '-':
                        scannerOutput += Character.toString(lineInput.charAt(i)) + "," +"MINUS" + "\n";
                        break;
                    case '*':
                        scannerOutput += Character.toString(lineInput.charAt(i)) + "," +"MULTIPLY" + "\n";
                        break;
                    case '/':
                        scannerOutput += Character.toString(lineInput.charAt(i)) + "," +"DIVIDE" + "\n";
                        break;
                    case '=':
                        scannerOutput += Character.toString(lineInput.charAt(i)) + "," +"EQUAL" + "\n";
                        break;
                    case '<':
                        scannerOutput += Character.toString(lineInput.charAt(i)) + "," +"LESS" + "\n";
                        break;
                    case '>':
                        scannerOutput += Character.toString(lineInput.charAt(i)) + "," +"GREATER" + "\n";
                        break;
                    case '(':
                        scannerOutput += Character.toString(lineInput.charAt(i)) + "," +"PARENTHESE_OPEN" + "\n";
                        break;
                    case ')':
                        scannerOutput += Character.toString(lineInput.charAt(i)) + "," +"PARENTHESE_CLOSE" + "\n";
                        break;
                    case ';':
                        scannerOutput += Character.toString(lineInput.charAt(i)) + "," +"SEMI" + "\n";
                        break;
                    case ':':
                        if (lineInput.charAt(i + 1) == '=') {
                            scannerOutput += Character.toString(lineInput.charAt(i)) + Character.toString(lineInput.charAt(i + 1)) + "," +"ASSIGN" + "\n";
                            i++;
                        }   break;
                    default:
                        if(Character.isDigit(lineInput.charAt(i))) {
                            while (Character.isDigit(lineInput.charAt(i))) {
                                number += Character.toString(lineInput.charAt(i));
                                if(i == lineInput.length()-1) 
                                {
                                    endLineFlag = true;
                                    break;
                                }
                                else i++;
                            }
                            if(!endLineFlag) i--;
                            scannerOutput += number + "," +"NUM" + "\n";
                            number = "";
                        }
                        else if (Character.isAlphabetic(lineInput.charAt(i))) {
                            while (Character.isDigit(lineInput.charAt(i)) || Character.isAlphabetic(lineInput.charAt(i)) || lineInput.charAt(i) == '_') {
                                id += Character.toString(lineInput.charAt(i));
                                if(i == lineInput.length()-1) 
                                {
                                    endLineFlag = true;
                                    break;
                                }
                                else i++;
                            }
                            for (int ii = 0; ii < 8; ii++) {
                                if (reservedWords[ii].equals(id)) {
                                    reservedWordFlag = true;
                                    break;
                                }
                            }
                            if (reservedWordFlag) {
                                scannerOutput += id + "," +"RESERVED_WORD" + "\n";
                            }
                            else {
                                scannerOutput += id + "," +"ID" + "\n";
                            }
                            reservedWordFlag = false;
                            id = "";
                            if(!endLineFlag) i--;
                        }   break;
                }
            }
        }
        return scannerOutput;
    }
}
