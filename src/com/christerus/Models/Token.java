package com.christerus.Models;

/**
 * Created by thomas on 13/03/2018.
 */
public class Token {
    public enum Type {NUM, STRING, VARIABLE_NAME, BOOLEAN, FUNCTION, CLASS,  OPERAND, SEMICOLON, PARENTHESE_OPEN, PARENTHESE_CLOSE, SCOPE_OPEN, SCOPE_CLOSE}
    private Type tokenType;
    private Object value;

    public Token(Type type, Object value){
        this.tokenType = type;
        this.value = value;
    }

    public Type getTokenType(){
        return tokenType;
    }

    public Object getValue(){
        return value;
    }

    @Override
    public String toString(){
        return "{" + tokenType.toString() + ":" + value.toString() + "}";
    }
}
