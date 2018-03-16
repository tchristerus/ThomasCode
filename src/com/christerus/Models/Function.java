package com.christerus.Models;

import java.util.ArrayList;

/**
 * Created by thomas on 16/03/2018.
 */
public class Function {

    private String funcName;
    public Token.Type funcType;
    private ArrayList<Token> code;
    private ArrayList<Token> parameters;

    public Function(String funcName, Token.Type funcType, ArrayList<Token> code, ArrayList<Token> parameters){
        this.funcName = funcName;
        this.funcType = funcType;
        this.code = code;
        this.parameters = parameters;
    }

    public String getName(){
        return funcName;
    }

    public ArrayList<Token> getParameters(){
        return parameters;
    }

    public ArrayList<Token> getCode(){
        return code;
    }

    public Token.Type getFuncType(){
        return funcType;
    }
}
