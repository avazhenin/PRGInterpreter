package prginterpreter;

public class LET {

    // assigns value to specific variable
    public static void run(String value) {

        Worker utils = new Worker();

        String[] variable = new String[2]; // variable that has been found
        String parsedValue = new String(); // pure value to set

        if ((variable = utils.findVar(utils.parseVarName(value))) != null) { // check if variable exists
            parsedValue = utils.eraseVariable(value); // get pure data from the line 
            parsedValue = parsedValue.replace("=", "").trim(); // removes waste sign from data
            parsedValue = parsedValue.replace("\"", "");
            // get var type to decide if trim is needed
            String varType = StaticVariables.newVariables.get(Integer.valueOf(variable[1])).varType;
            StaticVariables.newVariables.get(Integer.valueOf(variable[1])).varValue = (varType.equalsIgnoreCase("integer")) ? parsedValue.trim() : parsedValue; // assigns data to a variable
        } else {
            Worker.Error();
            System.out.println("Error.. Could not find variable [" + utils.parseVarName(value) + "] on line " + StaticVariables.lineCount + " while executing LET command"); // can not find such variable
        }
    }
}
