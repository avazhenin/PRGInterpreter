//package prginterpreter;

/**
 *
 * @author lagi
 */
public class Println {

    // prints data with new line
    public static void run(String line) {

        Worker utils = new Worker();

        if (line.indexOf("\"") == -1) { // if there's no quotes, then consider it should be variable there
            try {
                // Find variable Name and then value, print error if such was not found
                if (line.trim().length() > 0) {
                    if (utils.ifVarExists(line, true)) { // check if variable exist in ArrayList(StaticVariables.newVariables)
                        if (!utils.VarValueIsNotNull(line)) { // if value of the variable is null just break performing
                            Worker.Error();
                            return;
                        }
                        System.out.println(utils.getVarValue(line));

                    } else {
                        Worker.Error();
                        System.out.println("Error.. Could not find variable [" + utils.parseVarName(line) + "] on line " + StaticVariables.lineCount + " while executing PRINTLN command"); // can not find such variable
                    }
                } else {
                    System.out.println();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else { // if there's a quotes, then there's no variable and we just print that
            if (utils.countQuotes(line) == 2) {
                System.out.println(line.replace("\"", ""));
            } else {
                Worker.Error();
                System.out.println("Error... println command syntax on line " + StaticVariables.lineCount);
                return;
            }
        }
    }
}
