package prginterpreter;

public class InitiateVariable {

    // creates new variable and stores it in ArrayList under specific name
    public static void run(String varName, String type) {
        StaticVariables.newVariables.add(new StaticVariables.DefinedVars(varName.toUpperCase(), type, ""));
    }
}
