//package prginterpreter;

public class SwitchCommands {

    // perform particular action depending on specific command
    public static void run(String line) {
        Worker utils = new Worker();
        if (line.length() != 0) { // skip if line length = 0
            if (!line.substring(0, 1).equals("#")) { // skip comments
                
                switch (utils.getCommand(line.trim(), false)) { // switch start
                    case "PRINTLN": // if the command is println, then perform commandPrintln procedure
                        Println.run(utils.eraseCommand(line, "PRINTLN"));
                        break;
                    case "PRINT": // if the command is PRINT, then perform commandPrintln procedure
                        Print.run(utils.eraseCommand(line, "PRINT"));
                        break;
                    case "INTEGER": // if the command is INTEGER which means initiating variable, then perform newVar
                        CreateNewVariable.run(utils.eraseCommand(line, "INTEGER"), "INTEGER");
                        break;
                    case "STRING": // if the command is STRING which means initiating variable, then perform newVar
                        CreateNewVariable.run(utils.eraseCommand(line, "STRING"), "STRING");
                        break;
                    case "LET": // if the command is LET, which assigning a value to a variable, then perform setVarValue procedure 
                        AssignVariableValue.run(utils.eraseCommand(line, "LET"));
                        break;
                    case "CALCULATE": // if the command is CALCULATE, then perform commandCalculate procedure
                        Calculate.run(utils.eraseCommand(line, "CALCULATE"));
                        break;
                    case "LOOP": // if the command is CALCULATE, then perform commandCalculate procedure
                        new Loop().run(line.trim());
                        break;
                    case "IF": // if the command is CALCULATE, then perform commandCalculate procedure
                        new IF().run(line.trim());
                        break;
                    case "exit": // if non of this where found - just exit and do nothing
                        System.out.println("Unknown command [" + utils.getCommand(line.trim(), true) + "] on line " + StaticVariables.lineCount);
                        break;
                }// switch start
            } // skip comments
        } // skip if line length = 0
    } // end of method run
}
