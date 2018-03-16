package com.christerus.Utils;

import com.christerus.Models.Token;

import java.util.ArrayList;

/**
 * Created by thomas on 16/03/2018.
 */
public class ExecuterUtil {

    public static ArrayList<Token> readParameters(ArrayList<Token> program, int startIndex) {
        ArrayList<Token> parameters = new ArrayList<>();

        for (int i = startIndex; i < program.size(); i++) {
            if(!program.get(i).getTokenType().toString().equals("PARENTHESE_CLOSE")) {
                parameters.add(program.get(i));
            }else{
                return parameters;
            }
        }
        return null;
    }

    public static ArrayList<Token> readScope(ArrayList<Token> program, int startIndex){
        ArrayList<Token> scope = new ArrayList<>();
        int scopesOpened = 0; // Counting nested scopes so we know if we are at the last }
        int scopesClosed = 0;
        boolean inScope = false;

        for (int i = startIndex; i < program.size(); i++) {
            if(getTokenType(program, i).equals("SCOPE_OPEN")){
                if(!inScope) {
                    inScope = true;
                    continue;
                }else{
                    scopesOpened++;
                }
            }else if(getTokenType(program, i).equals("SCOPE_CLOSE")) {
                if(scopesClosed == scopesOpened){
                    return scope;
                }else{
                    scopesClosed++;
                }
            }else{
                if(inScope)
                    scope.add(program.get(i));
            }
        }
        return scope;
    }

    public static String getTokenType(ArrayList<Token> program, int index){
        return program.get(index).getTokenType().toString();
    }

    public static String getTokenValue(ArrayList<Token> program, int index){
        return program.get(index).getValue().toString();
    }
}
