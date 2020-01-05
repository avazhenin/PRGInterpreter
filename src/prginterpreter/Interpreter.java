package prginterpreter;

import java.io.*;

public class Interpreter {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        System.out.println("TPL Inerpreter by Anatoliy Vazhenin \n");
        if (Worker.checkErrors(args)) { // open IF statement #1        
            // initiate Readser from file
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(args[0]))));

            Worker.initiate(); // initiate all the arrays and fill them

            String line = new String(""); // command lines
            while (!(line = br.readLine().trim()).equalsIgnoreCase("end")) { // start while

                // if error flag is set, then exit
                if (StaticVariables.errorFound) {
                    return;
                }
                if (line.length() > 0 && !line.substring(0, 1).equalsIgnoreCase("#")) { // if line consist some data then proceed

                    if (LOOP.isItLoop(line) || LOOP.ifLoop) { // check if it's loop statement, then loop futher collecting data into String variable until [end loop] found

                        do {
                            // exit if END LOOP key word was found on the same line
                            if (line.toLowerCase().contains("end loop")) {
                                break;
                            }
                            LOOP.collectLoopStatemetns(line); // collect data
                            StaticVariables.lineCount++;
                        } while (!(line = br.readLine().trim()).equalsIgnoreCase("end loop"));
                        LOOP.collectLoopStatemetns(line); // collect data
                        SwitchCommands.run(LOOP.statement); // run interpreter with entire loop statement

                    } else if (IF.isItIF(line) || IF.ifSession) { // start of IF class statement

                        do {
                            // if [END IF] was found on the same line, then exit
                            if (line.toLowerCase().contains("end if")) {
                                break;
                            }
                            IF.collectIFStatemetns(line); // collect data
                            StaticVariables.lineCount++;
                        } while (!(line = br.readLine().trim()).equalsIgnoreCase("end if"));
                        IF.collectIFStatemetns(line); // collect data
                        SwitchCommands.run(IF.ifStatement);// if interpreter with entire loop statement

                    } else { // end of IF class statement
                        // if it's not a [LOOP] or [IF], but a simple command, just pass it in interpreter
                        SwitchCommands.run(line.trim());
                    }
                }
                StaticVariables.lineCount++; // count number of lines
            } // end of while loop 

            System.out.println();
            System.out.println("TPL finished. Lines processed [" + StaticVariables.lineCount + "]");
        } // close IF statement #1
    }
}
