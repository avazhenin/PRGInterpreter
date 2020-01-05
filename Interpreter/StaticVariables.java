//package prginterpreter;

import java.util.ArrayList;

public class StaticVariables {

    static ArrayList<DefinedVars> newVariables = new ArrayList<DefinedVars>(); // all new variable stored here
    static ArrayList<String> commandList = new ArrayList<String>(); // list of commands program can handle
    static ArrayList<Character> mathSigns = new ArrayList<Character>(); // contains all the mathematical sign, that program can handle
    static int lineCount = 1; // count lines
    static boolean errorFound = false; // if true, exit program with error

    public StaticVariables() {
    }

    static void initiateStatics() {
        commandList = new ArrayList<String>(); // initiate Array
        newVariables = new ArrayList<DefinedVars>(); // initiate array for new variables
        mathSigns = new ArrayList<Character>(); // initiate array for storing Math signs
    }

    // inner class for storing variable names and its values
    static class DefinedVars {

        String varName;
        String varType;
        String varValue;

        public DefinedVars(String varName, String varType, String varValue) {
            this.varName = varName;
            this.varType = varType;
            this.varValue = varValue;
        }
    }
}
