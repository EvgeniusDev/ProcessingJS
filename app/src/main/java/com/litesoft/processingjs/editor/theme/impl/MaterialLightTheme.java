package com.litesoft.processingjs.editor.theme.impl;

import android.graphics.Color;
import com.litesoft.processingjs.editor.theme.ITheme;

public class MaterialLightTheme extends ITheme {
    public MaterialLightTheme() {
        colorBackground = Color.parseColor("#fafafa");
        colorSelectedLine= Color.parseColor("#e7e7e8");
        colorLineNumberDigits = Color.parseColor("#aabfc9");
        colorLineNumberDivider = Color.parseColor("#aabfc9");
        colorLineNumberPanel = Color.parseColor("#fafafa");
        colorPairBrackets = Color.parseColor("#aabfc9");
        colorPairBracketsText = Color.parseColor("#546e7a");
        colorBlocksLine = Color.parseColor("#aabfc9");
        colorTextPlain = Color.parseColor("#546e7a");
        colorKeywords = Color.parseColor("#7c4dff");
        colorFunctions = Color.parseColor("#6182b8");
        colorTypes = Color.parseColor("#f76d47");
        colorNumbers = Color.parseColor("#91b859");
        colorStrings = Color.parseColor("#91b859");
        colorOperators = Color.parseColor("#39adb5");
        colorBrackets = Color.parseColor("#39adb5");
        colorComments = Color.parseColor("#aabfc9");
    }
}