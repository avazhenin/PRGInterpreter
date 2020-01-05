/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package prginterpreter;

/**
 *
 * @author lagi
 */
public class CreateNewVariable {

    // creates new variable and stores it in ArrayList under specific name
    public static void run(String varName, String type) {
        StaticVariables.newVariables.add(new StaticVariables.DefinedVars(varName.toUpperCase(), type, ""));
    }
}
