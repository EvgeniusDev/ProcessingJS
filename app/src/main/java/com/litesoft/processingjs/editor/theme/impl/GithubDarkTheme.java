package com.litesoft.processingjs.editor.theme.impl;

import com.litesoft.processingjs.editor.theme.ITheme;
import android.graphics.Color;

public class GithubDarkTheme extends ITheme {
    
    public GithubDarkTheme() {
        colorBackground = Color.parseColor("#24292d");
        colorSelectedLine= Color.parseColor("#2e353a");
        colorLineNumberDigits = Color.parseColor("#444f56");
        colorLineNumberDivider = Color.parseColor("#1b1e23");
        colorLineNumberPanel = Color.parseColor("#24292d");
        colorPairBrackets = Color.parseColor("#444f56");
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
