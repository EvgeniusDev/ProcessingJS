package com.litesoft.processingjs.editor.lang;

public class SyntaxHighlightResult {
    public int start;
    public int end;
    public TokenType type;

    public SyntaxHighlightResult(int start, int end, TokenType type) {
        this.start = start;
        this.end = end;
        this.type = type;
    }
}
