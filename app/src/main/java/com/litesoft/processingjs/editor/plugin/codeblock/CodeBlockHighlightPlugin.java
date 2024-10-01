package com.litesoft.processingjs.editor.plugin.codeblock;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.litesoft.processingjs.editor.plugin.base.Plugin;
import com.litesoft.processingjs.editor.theme.ITheme;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class CodeBlockHighlightPlugin extends Plugin {
    private Paint paint;

    @Override
    public void attach() {
        super.attach();
        
        var theme = editor.getTheme();
        
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        paint.setColor(theme.colorBlocksLine);
    }

    
    @Override
    public void onThemeChanged(ITheme theme) {
        paint.setColor(theme.colorBlocksLine);
    }


    @Override
    public void onDrawFront(Canvas canvas) {
        Stack<Integer> stack = new Stack<>();
        Map<Integer, Integer> pairs = new HashMap<>();

        String text = editor.getText().toString();

        for (int i=0; i<text.length(); i++) {
            char c = text.charAt(i);

            if (c == '{') {
                stack.push(i);
            } else if (c == '}') {
                if (!stack.isEmpty()) {
                    int openIndex = stack.pop();
                    pairs.put(openIndex, i);
                }
            }
        }

        int topOffset = editor.getLineHeight();

        for (Map.Entry<Integer, Integer> entry : pairs.entrySet()) {
            int openIndex = entry.getKey();
            int closeIndex = entry.getValue();

            float[] openPos = editor.getCharPosition(openIndex);
            float[] closePos = editor.getCharPosition(closeIndex);

            if (openPos == null || closePos == null) {
                continue;
            }

            if (openPos[1] < closePos[1]) {
                canvas.drawLine(closePos[0], closePos[1], closePos[0], openPos[1] + topOffset, paint);
            }
        }
    }
}
