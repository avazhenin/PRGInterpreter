package prginterpreter;

import java.io.File;
import java.util.ArrayList;

public class Worker {

    public Worker() {
    }

    // perform actions to fills arrays
    public static void initiate() {
        fillCommandList(); // fill commands array
        fillMathSigns(); // fill math signs array
    }

    // returns command from the line
    public String getCommand(String line, boolean returnCommand) {
        for (int i = 0; i < StaticVariables.commandList.size(); i++) {
            if (line.toUpperCase().contains(StaticVariables.commandList.get(i)) && line.toUpperCase().indexOf(StaticVariables.commandList.get(i)) == 0) {
                return StaticVariables.commandList.get(i);
            }
        }
        if (returnCommand) {
            // consider that variable is substring from index 0 to gap index, if there's no gap consifer the entire line as a variable
            Worker.Error();
            return line.trim().substring(0, ((line.trim().indexOf(" ")) == -1) ? line.length() : line.trim().indexOf(" "));
        } else {
            Worker.Error();
            return "exit";
        }
    }

    // Fill commands list
    public static void fillCommandList() {
        StaticVariables.commandList.add("INTEGER");
        StaticVariables.commandList.add("STRING");
        StaticVariables.commandList.add("LET");
        StaticVariables.commandList.add("CALCULATE");
        StaticVariables.commandList.add("PRINTLN");
        StaticVariables.commandList.add("PRINT");
        StaticVariables.commandList.add("LOOP");
        StaticVariables.commandList.add("IF");
    }

    //finds variable name that stores in ArrayList and returns array with variable name and its index
    public String[] findVar(String value) {

        String[] var = new String[2]; // initiate array for storing varName and it's array index

        for (int i = 0; i < StaticVariables.newVariables.size(); i++) {
            if (value.equalsIgnoreCase(StaticVariables.newVariables.get(i).varName)) {
                var[0] = StaticVariables.newVariables.get(i).varName.toUpperCase();
                var[1] = String.valueOf(i);
                return var;
            }
        }
        return null;
    }

    // just removes waste signs
    public String parseVarName(String value) {
        if (value.indexOf('=') == -1) {
            return value.trim();
        } else {
            return value.substring(0, value.indexOf('=')).trim();
        }
    }

    // list of Mathematical signs
    public static void fillMathSigns() {
        StaticVariables.mathSigns.add('+');
        StaticVariables.mathSigns.add('-');
        StaticVariables.mathSigns.add('*');
        StaticVariables.mathSigns.add('/');
    }

    // this one searches for Math sign and tell its position
    public int getNextMathSignPos(String line) {

        char[] buff = line.toCharArray();

        for (int i = 0; i < buff.length; i++) { // statement loop

            for (int j = 0; j < StaticVariables.mathSigns.size(); j++) { // inner loop
                if (buff[i] == StaticVariables.mathSigns.get(j)) {
                    return i;
                }
            } // end of inner loop

        } // end of statement loop  
        return -1;
    }

    // erase command , doesn't matter whether it in upper case or lower
    public String eraseCommand(String line, String command) {
        int commandPos = line.toUpperCase().indexOf(command);

        return line.substring(commandPos + command.length(), line.length()).trim();
    }

    // erase varibale name from the line, usefull if variable consist reserved words
    public String eraseVariable(String value) {
        for (int i = 0; i < StaticVariables.newVariables.size(); i++) {
            // if we find a variable and the start position in line is not 0, then it's not a variable
            // consider that variable should be in the begining of the line
            if (value.toUpperCase().contains(StaticVariables.newVariables.get(i).varName) && value.toUpperCase().indexOf(StaticVariables.newVariables.get(i).varName) == 0) {
                int commandPos = value.toUpperCase().indexOf(StaticVariables.newVariables.get(i).varName);
                return value.substring(commandPos + StaticVariables.newVariables.get(i).varName.length() + 1, value.length()).trim();
            }
        }
        return null;

    }

    /**
     * If variable not exists in case if it's not an integer, raising error
     */
    public boolean checkVariable(String line) {
        if (!ifVarExists(line, false) && !ifInteger(line)) {
            Error();
            System.out.println("Error.. Could not find variable [" + line + "] on line " + StaticVariables.lineCount); // can not find such variable            
            return false;
        }
        return true;
    }

    // checks if the number is integer
    public boolean ifInteger(String line) {
        try {
            int i = Integer.valueOf(line); // try to assign converted string to int
            return true;
        } catch (Exception e) {
            return false; // return false if error found
        }
    }

    // return false if variable has no value or null
    public boolean VarValueIsNotNull(String posibleVar) {
        if (!ifInteger(posibleVar)) {
            if (StaticVariables.newVariables.get(Integer.parseInt(findVar(posibleVar)[1])).varValue != "") {
                return true;
            }
            Error();
            System.out.println("Error... variable [" + posibleVar + "] has no value");
            return false;
        }
        return true;

    }

    // get variable value
    public String getVarValue(String varName) {
        return StaticVariables.newVariables.get(Integer.parseInt(findVar(varName)[1])).varValue;
    }

    // get variable type
    public String getVarType(String varName) {
        return StaticVariables.newVariables.get(Integer.parseInt(findVar(varName)[1])).varType;
    }

    // error trapping
    public static boolean checkErrors(String[] params) {
        if (params.length == 1) {
            if (new File(params[0]).exists()) {
                if (params[0].toLowerCase().indexOf(".tpl") != -1) {
                    return true;
                } else {
                    System.out.println("File name should have .tpl extension");
                    return false;
                }
            } else {
                System.out.println("File with name '" + params[0] + "' does not exists.");
                return false;
            }
        } else {
            System.out.println("The number of passed parameters is wrong.");
            return false;
        }
    }

    // if variable was found return true
    public boolean ifVarExists(String varName, boolean debug) {
        if (findVar(varName) != null) {
            return true;
        }
        Error();
        if (debug) {
            System.out.println("Error.. variable [" + varName + "] was not found.");
        }
        return false;

    }

    // returns number of commands in a line
    public static int CommandCount(String line) {
        line = line.toUpperCase();
        line = new Worker().replaceStringsQuotes(line);
        int cnt = 0;

        for (int i = 0; i < StaticVariables.commandList.size(); i++) { // [LOOP 1]
            while (line.indexOf(StaticVariables.commandList.get(i)) != -1) { // [WHILE 1]
                line = line.replaceFirst(StaticVariables.commandList.get(i), ""); // replace first if found
                cnt++;
            } // [WHILE 1] 
        }// [LOOP 1]

        return cnt;
    }

    /**
     * Delete all data inside quotes. Input : println "true" println "Hi there"
     * Output : println println
     */
    public String replaceStringsQuotes(String line) {
        int pos1, pos2;
        while (line.indexOf("\"") != -1) {
            pos1 = line.indexOf("\"");
            line = line.replaceFirst("\"", "");

            pos2 = line.indexOf("\"");
            line = line.replaceFirst("\"", "");

            line = line.replace(line.substring(pos1, pos2), "");
        }
        return line;
    }

    public static void Error() {
        StaticVariables.errorFound = true;
    }

    // if String has no quotes for Print commands, then raising error
    public boolean stringSyntax(String line) {
        if (line.contains("\"")) {
            return true;
        }
        return false;
    }

    // return true if new line was found
    boolean newLine(char symbol) {
        if (symbol == (char) 10) {
            return true;
        }
        if (symbol == (char) 13) {
            return true;
        }
        if (symbol == ((char) 10 + (char) 13)) {
            return true;
        }
        if (symbol == ((char) 13 + (char) 10)) {
            return true;
        }
        return false;
    }

    // searches for new lines in a string, returns number of found new lines
    int newLinesCount(String line) {
        int newLinesFound = 0;
        
        for (int i = 0; i < line.length(); i++) { // loop and if new line found increment [newLinesFound]
            if (newLine(line.substring(i, i + 1).toCharArray()[0])) {
                newLinesFound++;
            }
        }
        return newLinesFound;
    }

    // check loop statement syntax
    boolean countCommandsOnOneLine(String line) {
        int newLinesFound = newLinesCount(line);

        if (newLinesFound < (CommandCount(line) - 1)) {
            Worker.Error();
            System.out.println("Error statement syntax [no lines separator was found] on line " + StaticVariables.lineCount);
            return false;
        }

        if (newLinesFound != 0) { // if no new lines found, consider error in syntax
            return true;
        } else {
            Worker.Error();
            System.out.println("Error statement syntax [no lines separator was found]" + StaticVariables.lineCount);
            return false;
        }
    }

    /**
     * count '"' quotes and returns amount
     */
    public int countQuotes(String line) {
        char[] c = line.toCharArray();
        int l = 0;
        for (int i = 0; i < c.length; i++) {
            if (c[i] == '"') {
                l++;
            }
        }
        return l;
    }

    /**
     * when block of IF statement code is passing inside, we need to separate it
     * in different lines to process here we just return array of commands, that
     * will pass into Interpreter Class
     */
    public ArrayList<String> separateIfBlock(String code) {
        String temp = "";
        char[] c = code.toCharArray();
        ArrayList<String> commands = new ArrayList<>();
        if (code.trim().length() > 0) { // IF 1

            for (int i = 0; i < c.length; i++) { // FOR 1
                if (c[i] != ((char) 10)) { // IF 2
                    temp += c[i];
                    if (i + 1 == c.length) {
                        commands.add(temp);
                        temp = "";
                    }
                } else {
                    commands.add(temp);
                    temp = "";
                } // IF 2
            }// FOR 1

            return commands;
        } else {
            Error();
            return null;
        } // IF 1
    }

}
