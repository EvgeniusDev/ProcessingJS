package com.litesoft.processingjs.lang;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Lexer {
    private final String OPERATORS = "+-*/=<>!?&|";
    
    private String text;
    private int length;
    private int pos;
    private List<SyntaxHighlightResult> results;
    
    private final Pattern METHOD1 = Pattern.compile("(?<=(function)) (\\w+)");
    
    public List<SyntaxHighlightResult> execute(String input) {
        results = new ArrayList<>();
        text = input;
        length = text.length();
        pos = 0;
        
        Matcher matcher = METHOD1.matcher(text);
        
        while (matcher.find()) {
            TokenType type = TokenType.FUNCTION;
            SyntaxHighlightResult result = new SyntaxHighlightResult(matcher.start(), matcher.end(), type);
            results.add(result);
        }
        
        
        while (pos < length) {
            char ch = peek(0);
            
            if (OPERATORS.indexOf(ch) != -1) {
                tokenizeOperator();
            }
            else if (ch == '"') {
                tokenizeString();
            }
            else if (Character.isLetter(ch) || ch == '_') {
                tokenizeWord();
            }
            else if (Character.isDigit(ch) || (Character.isDigit(peek(1)) && ch == '.')) {
                tokenizeNumber();
            } 
            else {
                next();
            }
        }
        
        return results;
    }
    
    
    private void tokenizeOperator() {
        int start = pos;
        int end = pos;
        char ch = peek(0);
        
        if (ch == '/') {
            if (peek(1) == '/') {
                tokenizeComment();
                return;
            } else if (peek(1) == '*') {
                tokenizeBlockComment();
                return;
            }
        }
        
        while (OPERATORS.indexOf(ch) != -1) {
            ch = next();
        }
        
        end = pos;
        
        switch (text.substring(start, end)) {
            case "+":
            case "-":
            case "*":
            case "/":
            case "=":
            case "+=":
            case "-=":
            case "*=":
            case "/=":
            case "++":
            case "--":
            case "<":
            case ">":
            case "<=":
            case ">=":
            case "==":
            case "!":
            case "!=":
            case "&":
            case "&&":
            case "|":
            case "||":
                results.add(new SyntaxHighlightResult(start, end, TokenType.OPERATOR));
        }
    }
    
    
    private void tokenizeNumber() {
        int start = pos;
        int end = pos;
        char ch = peek(0);
        
        while (Character.isDigit(ch) || ch == '.') {
            ch = next();
        }
        
        end = pos;
        results.add(new SyntaxHighlightResult(start, end, TokenType.NUMBER));
    }
    
    
    private void tokenizeWord() {
        int start = pos;
        int end = pos;
        char ch = peek(0);
        ch = next();
        
        while (Character.isLetter(ch) || Character.isDigit(ch) || ch == '_') {
            ch = next();
        }
        
        end = pos;
        
        if (isKeyword(text.substring(start, end))) {
            results.add(new SyntaxHighlightResult(start, end, TokenType.KEYWORD));
        } else {
            if (peek(0) == '(') {
                results.add(new SyntaxHighlightResult(start, end, TokenType.FUNCTION));
            }
        }
    }
    
    
    private void tokenizeString() {
        int start = pos;
        int end = pos;
        char ch = peek(0);
        
        ch = next();
        
        while (true) {
            if (ch == '"') {
                next();
                break;
            }
            
            if ("\n\r\0".indexOf(ch) != -1) {
                break;
            }
            ch = next();
        }

        end = pos;
        
        if (Pattern.matches("\"#([a-fA-F\\d]{6}|[a-fA-F\\d]{8})\"", text.substring(start, pos))) {
            results.add(new SyntaxHighlightResult(start, end, TokenType.STRING_HEX));
        } else {
            results.add(new SyntaxHighlightResult(start, end, TokenType.STRING));
        }
    }
    
    
    private void tokenizeComment() {
        int start = pos;
        int end = pos;
        char ch = peek(0);
        ch = next();
        ch = next();
        
        while ("\n\r\0".indexOf(ch) == -1) {
            ch = next();
        }
        
        end = pos;
        results.add(new SyntaxHighlightResult(start, end, TokenType.SINGLE_COMMENT));
    }
    
    
    private void tokenizeBlockComment() {
        int start = pos;
        int end = pos;
        char ch = peek(0);
        ch = next();
        ch = next();
        
        while (true) {
            if (ch == '*' && peek(1) == '/') {
                next();
                next();
                break;
            }
            
            if (ch == '\0') 
                break;
                
            ch = next();
        }
        
        end = pos;
        results.add(new SyntaxHighlightResult(start, end, TokenType.BLOCK_COMMENT));
    }
    
    
    private char next() {
        pos++;
        return peek(0);
    }
    
    private char peek(int relativePos) {
        int position = pos + relativePos;
        if (position >= length) 
            return '\0';
        return text.charAt(position);
    }
    
    private boolean isKeyword(String word) {
        for (String str : keywords) {
            if (str.equals(word)) {
                return true;
            }
        }
        
        return false;
    }
    
    static final String[] keywords = {
        "function",
        "super",
        "this",
        "async",
        "await",
        "export",
        "from",
        "extends",
        "final",
        "implements",
        "native",
        "private",
        "protected",
        "public",
        "static",
        "delete",
        "new",
        "in",
        "instanceof",
        "typeof",
        "of",
        "with",
        "break",
        "case",
        "catch",
        "continue",
        "default",
        "do",
        "else",
        "for",
        "if",
        "import",
        "package",
        "return",
        "switch",
        "try",
        "while",
        "const",
        "var",
        "let",
        "class",
        "interface",
        "constructor",
        "true",
        "false",
        "null",
        "nan",
        "undefined"
    };
}
