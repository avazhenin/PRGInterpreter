package prginterpreter;

public class CALCULATE {

    // performs calculation of two numbers 
    public static void run(String line) {

        Worker utils = new Worker();

        if (utils.getNextMathSignPos(line) == -1) {
            Worker.Error();
            System.out.println("Mathematical sign has not been found on line " + StaticVariables.lineCount + " during performing CALCULATE command");
            return;
        }

        if (!utils.checkVariable(utils.parseVarName(line))) { // if cannot find variable, exit
            return;
        }

        int resIdx = Integer.parseInt(utils.findVar(utils.parseVarName(line))[1]); // get array index for variable
        line = line.substring(line.indexOf('=') + 1, line.length()).trim(); // remove result variable from statement
        char sign; // here we will store Mathematical sign, to perform specific action

        String posibleVar; // either it variable or just a Math number we will store it here

        // find next Math Sign position and parse it (delete '=' , '"' signs)
        posibleVar = utils.parseVarName(line.substring(0, utils.getNextMathSignPos(line)));
        // check either it numberic value or a parameter that could be stored in StaticVariables.newVariables ArrayList
        if (!utils.checkVariable(posibleVar)) { // if cannot find variable, exit
            Worker.Error();
            return;
        }

        if (!utils.VarValueIsNotNull(posibleVar)) { // if value of the variable is null just break performing
            Worker.Error();
            return;
        }

        posibleVar = (utils.findVar(posibleVar) == null) ? posibleVar : (utils.getVarValue(posibleVar));

        int value1 = Integer.parseInt(posibleVar); // parse found value

        // find next Math Sign position and parse it (delete '=' , '"' signs)
        posibleVar = utils.parseVarName(line.substring(utils.getNextMathSignPos(line) + 1, line.length()));

        if (!utils.checkVariable(posibleVar)) {
            Worker.Error();
            return; // if cannot find variable, exit            
        }

        if (!utils.VarValueIsNotNull(posibleVar)) { // if value of the variable is null just break performing
            Worker.Error();
            return;
        }
        // check either it numberic value or a parameter that could be stored in StaticVariables.newVariables ArrayList
        posibleVar = (utils.findVar(posibleVar) == null) ? posibleVar : (utils.getVarValue(posibleVar));

        int value2 = Integer.parseInt(posibleVar); // parse found value

        sign = line.substring(utils.getNextMathSignPos(line), utils.getNextMathSignPos(line) + 1).toCharArray()[0]; // get math sign

        // goes to specific Mathematical operations considering Mathematical sign
        switch (sign) {
            case '+':
                StaticVariables.newVariables.get(resIdx).varValue = String.valueOf(value1 + value2);
                break;
            case '-':
                StaticVariables.newVariables.get(resIdx).varValue = String.valueOf(value1 - value2);
                break;
            case '*':
                StaticVariables.newVariables.get(resIdx).varValue = String.valueOf(value1 * value2);
                break;
            case '/':
                StaticVariables.newVariables.get(resIdx).varValue = String.valueOf(value1 / value2);
                break;
        } // end of switch case
    }
}
