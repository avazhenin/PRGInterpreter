package prginterpreter;

import java.util.ArrayList;

public class IF {

    Worker utils; // get Worker utilities
    public static String ifStatement = new String(""); // contains IF statement and data inside block
    public static boolean ifSession; // if [IF] was found, ifSession becomes true, to collect all the data in block till [END IF]
    private ArrayList<String> conditionalEXP; // contains conditional expressions signs
    private int leftEXP; // define left condition in round brackets
    private int righEXP; // define right condition in round brackets
    private String ifBlock = ""; // contain all the commands in IF ... ELSE or IF .. END IF block
    private String elseBlock = ""; // // contain all the commands in ELSE .. END IF block
    private ArrayList<String> commandsToExecute = new ArrayList<String>(); // list of commands that have been retrieved from blocks
    private String exprSign = ""; // defined expression ( == , < , > , !=)

    public IF() {
        utils = new Worker(); 
    }

    // fill array with possible conditional expressions 
    void fillConditionalEXP() {
        conditionalEXP = new ArrayList<>();
        conditionalEXP.add("<");
        conditionalEXP.add(">");
        conditionalEXP.add("==");
        conditionalEXP.add("!=");
    }

    /** 
     * trying to parse conditional expressions, such as ( == , < , > , !=)
     */
    void checkEXP(String line) {
        // try to find conditional expression
        for (int i = 0; i < conditionalEXP.size(); i++) {
            if (line.indexOf(conditionalEXP.get(i)) != -1) {
                exprSign = conditionalEXP.get(i);
                break;
            }
        }
        // if no conditional expression was found , exit with error
        if (exprSign.trim().length() == 0) {
            Worker.Error();
            System.out.println("Error... no logical expression was found");
            return;
        }

        //trying to parse left and right expression
        String temp = line.substring(0, line.indexOf(exprSign)).trim();
        // ckech if it's variable, if yes get variable type and value of that variable

        // if expression if not integer, then it could be a variable
        if (!utils.ifInteger(temp)) { // IF 1
            if (utils.ifVarExists(temp, false)) {// IF 2
                if (!utils.ifInteger(utils.getVarValue(temp))) { // IF 3
                    Worker.Error();
                    System.out.println("Error... only Integer values can be used in IF expression");
                    return;
                } else {
                    leftEXP = Integer.valueOf(utils.getVarValue(temp));
                } // IF 3
            }// IF 2

        } else { // if it's not a variable go here
            leftEXP = Integer.valueOf(temp);
        } // IF 1

        //trying to parse right and right expression
        temp = line.substring(line.indexOf(exprSign) + exprSign.length(), line.length()).trim();

        // ckech if it's variable, if yes get variable type and value of that variable        
        // if expression if not integer, then it could be a variable
        if (!utils.ifInteger(temp)) { // IF 1
            if (utils.ifVarExists(temp, false)) {// IF 2
                if (!utils.ifInteger(utils.getVarValue(temp))) { // IF 3
                    Worker.Error();
                    System.out.println("Error... only Integer values can be used in IF expression");
                    return;
                } else {
                    righEXP = Integer.valueOf(utils.getVarValue(temp));
                } // IF 3
            }// IF 2

        } else { // if it's not a variable go here
            righEXP = Integer.valueOf(temp);
        }// IF 1

//        System.out.println(leftEXP);
//        System.out.println(righEXP);
    }

    /**
     * execute IF or ELSE blocks, considering condition
     */
    void executeEXP() {
        switch (exprSign) {
            case "<":
                if (ifElseBlock()) { // with else statement
                    if (leftEXP < righEXP) {
                        executeBlock(ifBlock);
                    } else {
                        executeBlock(elseBlock);
                    }
                } else { // without else statement
                    if (leftEXP < righEXP) {
                        executeBlock(ifBlock);
                    }
                }
                break;
            case ">":
                if (ifElseBlock()) { // with else statement
                    if (leftEXP > righEXP) {
                        executeBlock(ifBlock);
                    } else {
                        executeBlock(elseBlock);
                    }
                } else { // without else statement
                    if (leftEXP > righEXP) {
                        executeBlock(ifBlock);
                    }
                }
                break;
            case "==":
                if (ifElseBlock()) { // with else statement
                    if (leftEXP == righEXP) {
                        executeBlock(ifBlock);
                    } else {
                        executeBlock(elseBlock);
                    }
                } else { // without else statement
                    if (leftEXP == righEXP) {
                        executeBlock(ifBlock);
                    }
                }
                break;
            case "!=":
                if (ifElseBlock()) { // with else statement
                    if (leftEXP != righEXP) {
                        executeBlock(ifBlock);
                    } else {
                        executeBlock(elseBlock);
                    }
                } else { // without else statement
                    if (leftEXP != righEXP) {
                        executeBlock(ifBlock);
                    }
                }
                break;
        }
    }

    /**
     * defining ArrayList with commands that were retrieved in IF / ELSE block and passing them into interpreter
     */
    void executeBlock(String code) {

        commandsToExecute = utils.separateIfBlock(code);

        // perform interpreter
        for (int i = 0; i < commandsToExecute.size(); i++) {
            if (StaticVariables.errorFound) {
                return;
            }
            new SwitchCommands().run(commandsToExecute.get(i));
        }
    }

    /**
     * if some LOOP statement appears in IF statement, raising error
     */
    boolean checkSyntax() {
        ArrayList<String> temp = utils.separateIfBlock(ifBlock);
        for (int i = 0; i < temp.size(); i++) {
            String string = temp.get(i);
            if (string.toLowerCase().indexOf("loop") == 0) {
                Worker.Error();
                System.out.println("Error... no LOOP statements allowed insided IF expressions on line " + StaticVariables.lineCount);
                return false;
            }
        }
        return true;
    }

    /**
     * if [IF] statement was found in the beginning of the line, then consider IF session
     */
    public static boolean isItIF(String line) {
        line = line.toLowerCase().trim();
        if (line.contains("if") && line.indexOf("if") == 0) {
            ifSession = true;
            return true;
        }
        return false;
    }

    // collect data between [IF] and [END IF] statements
    public static void collectIFStatemetns(String line) {
        ifStatement += line + "\n";
    }

    // stop IF session if [END IF] was found
    public static boolean stopIFSession(String line) {
        if (line.toLowerCase().contains("end if")) {
            ifSession = false;
            return true;
        }
        return false;
    }

    // return true if [ELSE] was found in a line
    boolean ifElseBlock() {
        if (ifStatement.toLowerCase().indexOf("else") != -1) {
            return true;
        }
        return false;
    }

    /**
     * assign initial values to variables, when [END IF] performed
     */
    void endOfIfStatement() {
        ifStatement = "";
        IF.ifSession = false;
    }

    // main void of [IF] Class 
    public void run(String line) {
        line = line.toLowerCase();
        fillConditionalEXP(); // fill array with conditional expressions
        String expression = line.substring(line.indexOf("(") + 1, line.indexOf(")")); // parse conditional expression
        ifBlock = line.substring(line.indexOf(")") + 1, (ifElseBlock()) ? line.indexOf("else") : line.indexOf("end if")).trim(); // parse if block to execute
        elseBlock = line.substring((ifElseBlock()) ? line.indexOf("else") + "else".length() : line.indexOf("end if"), line.indexOf("end if")).trim(); // parse else block to execute
        if (!checkSyntax()) {
            return;
        }
        checkEXP(expression);
        executeEXP();

        endOfIfStatement();
    }

}
