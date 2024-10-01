package com.litesoft.processingjs.editor.plugin.linenumbers;

import com.litesoft.processingjs.editor.plugin.base.Plugin;

import android.graphics.Paint;
import android.graphics.Color;
import android.graphics.Canvas;
import android.text.Layout;
import com.litesoft.processingjs.editor.theme.ITheme;

public class LineNumberPlugin extends Plugin {
    private Paint numberPaint = new Paint();
    private Paint dividerPaint = new Paint();
    private Paint panelPaint = new Paint();
    private Paint selectedLinePaint = new Paint();
    
    private int gutterWidth = 0;
    private int gutterDigitCount = 0;
    private int gutterMargin = 20;
    
    private int defaultPadding;
    
    @Override
    public void attach() {
        super.attach();
        
        numberPaint.setColor(Color.GRAY);
        numberPaint.setAntiAlias(true);
        numberPaint.setTextSize(editor.getTextSize());
        numberPaint.setTypeface(editor.getTypeface());
        numberPaint.setTextAlign(Paint.Align.RIGHT);
        
        dividerPaint.setColor(Color.LTGRAY);
        dividerPaint.setStrokeWidth(2);
        
        panelPaint.setColor(Color.WHITE);
        
        selectedLinePaint.setColor(Color.rgb(230, 230, 230));
        
        defaultPadding = editor.getPaddingLeft();
    }

    
    @Override
    public void detach() {
        super.detach();
        editor.setPadding(defaultPadding, defaultPadding, defaultPadding, defaultPadding);
    }

    @Override
    public void onThemeChanged(ITheme theme) {
        super.onThemeChanged(theme);
        
        numberPaint.setColor(theme.colorLineNumberDigits);
        dividerPaint.setColor(theme.colorLineNumberDivider);
        panelPaint.setColor(theme.colorLineNumberPanel);
        selectedLinePaint.setColor(theme.colorSelectedLine);
    }

    
    @Override
    public void onDrawBehind(Canvas canvas) {
        super.onDrawBehind(canvas);
        
        //draw selected line
        if (editor.isFocused()) {
            int line = editor.getCurrentLine();
            int height = editor.getLineHeight();
            int paddingTop = editor.getPaddingTop();
            int lineY = height * line + paddingTop;

            canvas.drawRect(0, lineY, editor.getWidth(), lineY + height, selectedLinePaint);
        }
        
        updateGutter();

        int topVisibleLine = editor.getTopVisibleLine();
        int bottomVisibleLine = editor.getBottomVisibleLine();
        int textRight = (gutterWidth - gutterMargin/2);

        Layout layout = editor.getLayout();
        if (layout == null) return;

        
        //draw panel
        canvas.drawRect(
            0,
            editor.getScrollVert(),
            gutterWidth,
            editor.getScrollVert() + editor.getContainerHeight(),
            panelPaint
        );

        
        //draw divider
        canvas.drawLine(
            gutterWidth,
            editor.getScrollVert(),
            gutterWidth,
            editor.getScrollVert() + editor.getContainerHeight(),
            dividerPaint
        );
        
        
        //draw digits
        while (topVisibleLine <= bottomVisibleLine) {
            canvas.drawText(
                String.valueOf(topVisibleLine+1), 
                textRight, 
                layout.getLineBaseline(topVisibleLine) + editor.getPaddingTop(), 
                numberPaint
            );

            topVisibleLine++;
        }
    }

    
    private void updateGutter() {
        int count = 2;
        int widestNumber = 0;
        float widestWidth = 0f;
        
        gutterDigitCount = String.valueOf(editor.getLineCount()).length();
        
        for (int i=0; i<9; i++) {
            float width = numberPaint.measureText(String.valueOf(i));
            if (width > widestWidth) {
                widestNumber = i;
                widestWidth = width;
            }
        }
        
        if (gutterDigitCount >= count) {
            count = gutterDigitCount;
        }
        
        StringBuilder builder = new StringBuilder();
        for (int i=0; i<count; i++) {
            builder.append(String.valueOf(widestNumber));
        }
        
        gutterWidth = (int) numberPaint.measureText(builder.toString());
        gutterWidth += gutterMargin;
        
        if (editor.getPaddingLeft() != gutterWidth + gutterMargin) {
            editor.setPadding(gutterWidth + gutterMargin, editor.getPaddingTop(), editor.getPaddingRight(), editor.getPaddingBottom());
        }
    }
}
