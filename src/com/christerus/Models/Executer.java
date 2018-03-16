package com.christerus.Models;

import com.christerus.Utils.ExecuterUtil;

import java.util.ArrayList;

public class Executer {

    public ArrayList<Token> program;
    public ArrayList<Function> functions;

    public Executer(ArrayList<Token> program){
        this.program = program;
        functions = new ArrayList<>();

        parseFunctions();
    }

    public void run(){

    }

    private void parseFunctions(){
        for (int i = 0; i < program.size(); i++) {
            if (program.get(i).getTokenType().toString().equals("FUNC")) {
                Function function = new Function(program.get(i).getValue().toString(), Token.Type.FUNC, ExecuterUtil.readScope(program, i), ExecuterUtil.readParameters(program, i+2));
                functions.add(function);
            }
            if (program.get(i).getTokenType().toString().equals("RFUNC")) {
                Function function = new Function(program.get(i).getValue().toString(), Token.Type.RFUNC, ExecuterUtil.readScope(program, i), ExecuterUtil.readParameters(program,i+2));
                functions.add(function);
            }
        }
    }


    /**
     * Debugging
     */
    public void debugFunctions(){
        for(Function function: functions){
            System.out.println("------------------------------------");
            System.out.println("Function: " + function.getName());
            System.out.println("Type: " + function.getFuncType().toString());
            System.out.println("Parameter tokens: ");
            for(Token token: function.getParameters()){
                System.out.println(token.toString());
            }

            System.out.println("Function body:");
            for(Token token: function.getCode()){
                System.out.println(token.toString());
            }
        }
    }
}
