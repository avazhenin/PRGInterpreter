//package prginterpreter;

/**
 *
 * @author lagi
 */
public class Print {

    // prints without new line
    public static void run(String line) {

        Worker utils = new Worker();

        if (line.indexOf("\"") == -1) {
            try {
                if (utils.ifVarExists(line, true)) {
                    // Find variable Name and then value, print error if such was found
                    if (!utils.VarValueIsNotNull(line)) { // if value of the variable is null just break performing
                        Worker.Error();
                        return;
                    }
                    System.out.print(utils.getVarValue(line));
                } else {
                    Worker.Error();
                    System.out.println("Error.. Could not find variable on line " + StaticVariables.lineCount); // can not find such variable
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            if (utils.countQuotes(line) == 2) {
                System.out.print(line.replace("\"", ""));
            } else {
                Worker.Error();
                System.out.println("Error... print command syntax on line " + StaticVariables.lineCount);
                return;
            }
        }
    }
}
