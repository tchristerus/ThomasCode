package com.christerus.Models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * Created by thomas on 13/03/2018.
 */

public class Lexer {
    private ArrayList<Token> tokens;
    private long parsingStarted; // nanotime
    private long lastParseTime = 0;
    private String operands[] = {"+", "-", "/", "%", "=", "==", "!="};
    private String functions[] = {"print", "ask"};
    private String classes[] = {"class"};
    private String currentBuffer = "";
    private char requiredNext = '\u0000';
    private boolean readingString = false;
    private boolean readingVariableOrName = false;
    private boolean readingParameters = false;

    // Scopes
    private boolean inScope = false; // between { and }

    // classes
    private boolean awaitingClassName = false;

    public Lexer(){
        tokens = new ArrayList<>();
    }

    public ArrayList<Token> parseProgram(String program, boolean debug){
        // timing
        parsingStarted = System.nanoTime();
        int line = 1, pos = 0;
        char[] chars = program.toCharArray();


        for(int i = 0; i < chars.length; i++){
            char currChar = chars[i];
            currentBuffer += chars[i];
            pos++;

            if(debug)
                System.out.println(currentBuffer);



            if(!readingString) {
                // skipping spaces if not between ""
                if(currChar == ' ' && i < chars.length){
                    clearBuffer();
                    continue;
                }

                // checking requiredChar
                if (requiredNext != '\u0000' && requiredNext != currChar) {
                    throwError("Expected '" + requiredNext + "' but got '" + currChar + "'", line, pos);
                }


                // checking new line
                if (currChar == '\n') {
                    line++;
                    pos = 0;
                    clearBuffer();
                }

                // checking class
                if (Arrays.stream(classes).parallel().anyMatch(currentBuffer::contains)) {
                    awaitClassName();
                    clearBuffer();
                }


                // Checking for function call and requiring parameters
                if (Arrays.stream(functions).parallel().anyMatch(currentBuffer::contains)) {
                    newToken(Token.Type.FUNCTION, currentBuffer);
                    clearBuffer();
                    setRequiredNext('(');
                    readingParameters = true;
                }

                //if (requiredNext == currChar) {
                    // Checking for parantheses
                    if (currChar == '(') {
                        newToken(Token.Type.PARENTHESE_OPEN, "(");
                        clearRequiredNext();
                        clearBuffer();
                    } else if (currChar == ')') {
                        newToken(Token.Type.PARENTHESE_CLOSE, ")");
                        clearRequiredNext();
                        clearBuffer();
                        readingParameters = false;
                    }

                // Checking for scopes
                if (currChar == '{') {
                    newToken(Token.Type.SCOPE_OPEN, "{");
                    clearRequiredNext();
                    clearBuffer();
                    startScope();
                } else if (currChar == '}') {
                    newToken(Token.Type.SCOPE_CLOSE, "}");
                    clearRequiredNext();
                    clearBuffer();
                    endScope();
                }
                //}

                // Checking for operands
                if (Arrays.asList(operands).contains("" + currChar)) {
                    if (chars.length >= i + 1) {
                        String tmpOperand = currChar + "" + chars[i + 1];
                        if (Arrays.asList(operands).contains(tmpOperand)) {
                            newToken(Token.Type.OPERAND, tmpOperand);
                            clearBuffer();
                            i += 2; // moving 2 forward because we the next character already
                        } else {
                            newToken(Token.Type.OPERAND, currChar);
                            clearBuffer();
                        }
                    } else {
                        newToken(Token.Type.OPERAND, currChar);
                        clearBuffer();
                    }
                }

                // Checking for digits
                if(currentBuffer.matches("-?\\d+")){
                    if(!Character.isDigit((chars[i+1])))
                        newToken(Token.Type.NUM, currentBuffer);
                }

                // reading variables / names (<variable>)
                if(currChar == '<'){
                    startReadingVariableOrName();
                    clearBuffer();
                }

                if(readingVariableOrName){
                    if(currChar == '>'){
                        if(awaitingClassName) {
                            newToken(Token.Type.CLASS, currentBuffer.substring(0, currentBuffer.length() - 1));
                            setRequiredNext('{');
                        }
                        else
                            newToken(Token.Type.VARIABLE_NAME, currentBuffer.substring(0, currentBuffer.length() - 1));
                        clearBuffer();
                        endAwaitClassName();
                        endReadingVariableOrName();
                    }
                }

                // Checking end of statement (;)
                if(currChar == ';'){
                    if(!readingParameters){
                        newToken(Token.Type.SEMICOLON, currChar);
                        clearBuffer();
                    }else{
                        throwError("Unexpected semicolon, left something unclosed?", line, pos);
                    }
                }
            }

            // Checking / reading string
            if(readingString && currChar == '"'){
                currentBuffer = currentBuffer.substring(0, currentBuffer.length() - 1);
                newToken(Token.Type.STRING, currentBuffer);
                clearBuffer();
                readingString = false;
            }else if(!readingString && currChar == '"') {
                readingString = true;
                clearBuffer();
            }




        }
        lastParseTime = TimeUnit.MILLISECONDS.convert((System.nanoTime() - parsingStarted), TimeUnit.NANOSECONDS);
        //System.out.println("Successfully parsed in: " + TimeUnit.MILLISECONDS.convert((System.nanoTime() - parsingStarted), TimeUnit.NANOSECONDS) + "ms");
        return tokens;
    }

    public long getParseTime(){
        return lastParseTime;
    }

    private void clearBuffer(){
        currentBuffer = "";
    }

    private void setRequiredNext(char required){
        requiredNext = required;
    }

    private void clearRequiredNext(){
        requiredNext = '\u0000';
    }

    private void newToken(Token.Type tokenType, Object value){
        tokens.add(new Token(tokenType, value));

    }

    private void startScope(){
        inScope = true;
    }

    private void endScope(){
        inScope = true;
    }

    private void awaitClassName(){
        awaitingClassName = true;
    }

    private void endAwaitClassName(){
        awaitingClassName = false;
    }

    private void startReadingVariableOrName(){
        readingVariableOrName = true;
    }

    private void endReadingVariableOrName(){
        readingVariableOrName = false;
    }

    private void throwError(String text, int line, int col){
        long time = TimeUnit.MILLISECONDS.convert((System.nanoTime() - parsingStarted), TimeUnit.NANOSECONDS);
        System.out.println("Fatal error: " + text + " | line: " + line + " col: " + col + "\nError occured after: " + time + "ms");
        System.exit(0);
    }
}