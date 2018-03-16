package com.christerus;

import com.christerus.Models.Executer;
import com.christerus.Models.Lexer;
import com.christerus.Models.Token;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws IOException {
            String program = new String(Files.readAllBytes(Paths.get(args[0])));
            Lexer lexer = new Lexer();

            ArrayList<Token> tokens = lexer.parseProgram(program, false);

            for(Token token: tokens){
                System.out.println(token.toString());
            }

            System.out.println("Successfully parsed " + tokens.size() + " tokens in: " + lexer.getParseTime() + "ms");


        Executer executer = new Executer(tokens);
        executer.run();
        executer.debugFunctions();
    }
}
