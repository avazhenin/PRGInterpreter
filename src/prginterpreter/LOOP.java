package prginterpreter;

import java.util.ArrayList;

public class LOOP {

    Worker utils;
    static String statement = ""; // loop statements from [LOOP] till [END LOOP]
    static boolean ifLoop = false; // if true, consider we are in loop session now
    int start = 0; // start position on loop from which counter goes
    int stop = 0; // end position on loop until which counter goes
    ArrayList<String> loopCommands; // list of commands to execute in a loop

    public LOOP() {
        /**
         * initiate variables
         */
        utils = new Worker();
        loopCommands = new ArrayList<String>();
    }

    // if LOOP statement found return true
    public static boolean isItLoop(String line) {
        if (line.toLowerCase().contains("loop")) {
            ifLoop = true;
            return true;
        }
        return false;
    }

    // collect data between [LOOP] and [END LOOP] statements
    public static void collectLoopStatemetns(String line) {
        statement += line + "\n";
    }

    // stop loop session if [END LOOP] was found
    public static boolean stopLoopSession(String line) {
        if (line.toLowerCase().contains("end loop")) {
            ifLoop = false;
            return true;
        }
        return false;
    }

    /**
     * parsing start and stop position for loop expression
     */
    public void setStartStop() {

        String temp;

        if (statement.indexOf("..") != -1) {
            // parse START
            temp = statement.substring("LOOP".length(), statement.indexOf("..")).trim();

            if (!utils.ifInteger(temp)) { // chech if value integer, if yes, then it's not a variable , [IF1]
                if (utils.ifVarExists(temp, true) && utils.VarValueIsNotNull(temp)) { // [IF2]
                    start = Integer.valueOf(utils.getVarValue(temp));
                } else {
                    Worker.Error();
                    return;
                }// [IF2]
            } else {
                start = Integer.valueOf(temp);
            } // [IF1]

            // parse STOP
            temp = statement.substring(statement.indexOf(temp) + temp.length() + 2, statement.indexOf("[")).trim();

            if (!utils.ifInteger(temp)) { // chech if value integer, if yes, then it's not a variable , [IF1]
                if (utils.ifVarExists(temp, true) && utils.VarValueIsNotNull(temp)) { // [IF2]
                    stop = Integer.valueOf(utils.getVarValue(temp));
                } else {
                    Worker.Error();
                    return;
                }// [IF2]
            } else {
                stop = Integer.valueOf(temp);
            } // [IF1]

            if (start > stop) {
                Worker.Error();
                System.out.println("Error... end point of LOOP cannot be less that start point");
                return;
            }

        } else {
            Worker.Error();
            System.out.println("Error... start and stop position in loop isn't defined");
            return;
        }
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

    /**
     * counts new lines in a given string
     */
    int newLinesCount(String line) {
        int newLinesFound = 0;
        for (int i = 0; i < line.length(); i++) { // loop and if new line found increment [newLinesFound]
            if (newLine(line.substring(i, i + 1).toCharArray()[0])) {
                newLinesFound++;
            }
        }
        return newLinesFound;
    }

    /**
     * Check [LOOP] syntax, if any [IF] statement appears then terminate program
     * also if more that one command in a line
     */
    boolean checkSyntax(String line) {
        int newLinesFound = newLinesCount(line);

        ArrayList<String> temp = utils.separateIfBlock(line);

        // process [IF] error trapping in a [LOOP]
        for (int i = 0; i < temp.size(); i++) {
            String string = temp.get(i);
            // if there is comment character, delete that line from command list
            if (string.indexOf("#") == 0) {
                line = line.replace(string, "");
            }
            // if [IF] statement was found, rise error
            if (string.toLowerCase().indexOf("if") == 0) {
                Worker.Error();
                System.out.println("Error... no IF statements allowed insided LOOP expressions on line " + StaticVariables.lineCount);
                return false;
            }
        }

        if (newLinesFound < (utils.CommandCount(line) - 1)) {
            Worker.Error();
            System.out.println("Error LOOP statement syntax [no lines separator was found]");
            return false;
        }

        if (newLinesFound != 0) { // if no new lines found, consider error in syntax
            return true;
        } else {
            Worker.Error();
            System.out.println("Error LOOP statement syntax [no lines separator was found]");
            return false;
        }
    }

    // stops [LOOP] session and assign default variable values
    void stopLoop() {
        loopCommands = null;
        statement = "";
        ifLoop = false;
    }

    /**
     * Main method of [LOOP] Class
     */
    void run(String line) {
        int startPos = 0;
        if (statement == null) {
            statement = line;
        }
        setStartStop(); // define start and stop positions for LOOP

        if (!StaticVariables.errorFound) { // error checking            
            String temp = line.substring(line.indexOf("[") + 1, line.indexOf("]")).trim();
//            System.out.println(temp + " | " + utils.CommandCount(temp));
            if (utils.CommandCount(temp) > 1) { // [IF commands count]  if there's more that one command, then new lines must be there
                if (checkSyntax(temp)) {  // check syntax
                    for (int i = 0; i < temp.length(); i++) { // loop 1                    
                        char c = temp.substring(i, i + 1).toCharArray()[0];
                        if (newLine(c) || (i + 1) == temp.length()) { // if new line was found or we reached end of a line, then add command
                            loopCommands.add(temp.substring(startPos, ((i + 1) == temp.length() ? temp.length() : i)));
                            startPos = i + 1;
                        }
                    }// loop 1                    
                } // check syntax
            } else {
                loopCommands.add(temp);
            } // [IF commands count]

            // perform interpreter
            for (int j = start; j < stop; j++) { // FOR 1
                if (StaticVariables.errorFound) {
                    return;
                }
                for (int i = 0; i < loopCommands.size(); i++) {// FOR 2
                    if (StaticVariables.errorFound) {
                        return;
                    }
                    new SwitchCommands().run(loopCommands.get(i));
                }// FOR 2
            }// FOR 1
            stopLoop();
        }// error checking
    }
}
