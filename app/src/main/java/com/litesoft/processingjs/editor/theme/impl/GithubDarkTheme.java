package com.litesoft.processingjs.editor.theme.impl;

import com.litesoft.processingjs.editor.theme.ITheme;
import android.graphics.Color;

public class GithubDarkTheme extends ITheme {
    
    public GithubDarkTheme() {
        colorBackground = Color.parseColor("#1b1b1b");
        colorSelectedLine= Color.parseColor("#2e353a");
        colorLineNumberDigits = Color.parseColor("#444f56");
        colorLineNumberDivider = Color.parseColor("#1b1e23");
        colorLineNumberPanel = Color.parseColor("#1b1b1b");
        colorPairBrackets = Color.parseColor("#444f56");
        colorPairBracketsText = Color.parseColor("#e76e7d");
        colorBlocksLine = Color.parseColor("#444f56");
        colorTextPlain = Color.parseColor("#e0e3e8");
        colorKeywords = Color.parseColor("#e76e7d");
        colorFunctions = Color.parseColor("#b492f0");
        colorTypes = Color.parseColor("#e76e7d");
        colorNumbers = Color.parseColor("#87b4eb");
        colorStrings = Color.parseColor("#a4c9f3");
        colorOperators = Color.parseColor("#e76e7d");
        colorBrackets = Color.parseColor("#e0e3e8");
        colorComments = Color.parseColor("#6b727a");
    }
}
