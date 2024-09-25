package com.litesoft.processingjs.editor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

import com.litesoft.processingjs.GlobalEventBus;
import com.litesoft.processingjs.events.FileEditedEvent;
import com.litesoft.processingjs.events.FileSavedEvent;
import com.litesoft.processingjs.project.files.TextFile;
import com.litesoft.processingjs.utils.FileUtil;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.text.style.CharacterStyle;

public class CodeEditorView extends EditText {
    private Paint currentLinePaint;
    private Paint numberLinesPaint;
    
    private TextWatcher textWatcher;
    
    private String indent = "    ";
    
    private TextFile textFile;
    
    public CodeEditorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        currentLinePaint = new Paint();
        currentLinePaint.setColor(Color.parseColor("#362f2d"));
        
        numberLinesPaint = new Paint();
        numberLinesPaint.setColor(Color.WHITE);
        numberLinesPaint.setAntiAlias(true);
        numberLinesPaint.setTypeface(getTypeface());
        numberLinesPaint.setTextSize(getTextSize());
        
        setIncludeFontPadding(false);
        
        textWatcher = new TextWatcher() {
            private String oldText;
            
            @Override
            public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
            }
            
            @Override
            public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {
                oldText = getText().toString();
            }

            @Override
            public void afterTextChanged(Editable e) {
                String text = getText().toString();

                
                // Compare the old text and the new text
                // TODO: Does this check fail in any corner cases (like mass-text insertion / deletion)?
                if (text.length() == oldText.length() + 1 && getSelectionStart() > 0) {
                    char pressedChar = text.charAt(getSelectionStart() - 1);

                    pressKeys(String.valueOf(pressedChar));
                }
            }
        };
        
        addTextChangedListener(textWatcher);
    }
    
    public void openFile(TextFile file) {
        textFile = file;
        processText(textFile.readFile());
    }
    
        
    public void pressKeys(String pressed) {
        // Detect the ENTER key
        if (pressed.length() == 1 && pressed.charAt(0) == '\n') {
            pressEnter();
        }

        // Automatically add a closing brace (if the user has enabled curly brace insertion)
        if (pressed.charAt(0) == '{') {
            getText().insert(getSelectionStart(), "}");
            setSelection(getSelectionStart() - 1);
        }
    }

    public void pressEnter() {
        int lastLineNum = getCurrentLine() - 1;

        //Get the indentation of the previous line
        String[] lines = getText().toString().split("\n");
        String lastLine = "";
        String lastIndent = "";

        //Calculate the indentation of the previous line
        if(lines.length > 0) {
            lastLine = lines[Math.min(lastLineNum, lines.length - 1)];

            for(int i = 0; i < lastLine.length(); i ++) {
                if(lastLine.charAt(i) == ' ')
                    lastIndent += ' ';
                else
                    break;
            }
        }

        //Determine the last character of the previous line (not counting whitespace)
        char lastChar = ' ';
        String trimmedLastLine = lastLine.trim();
        if(trimmedLastLine.length() > 0) {
            lastChar = trimmedLastLine.charAt(trimmedLastLine.length() - 1);
        }

        //Automatically indent
        if(lastChar == '{') {

            //Automatically increase the indent if this is a new code block
            getText().insert(getSelectionStart(), lastIndent + indent);

            //Automatically press enter again so that everything lines up nicely.. This is incredibly hacky...
            //Also make sure that the user has enabled curly brace insertion
            if(getText().length() > getSelectionStart() && getText().charAt(getSelectionStart()) == '}') {
                //Add a newline (the extra space is so that we don't recursively detect a newline; adding at least two characters at once sidesteps this possibility)
                getText().insert(getSelectionStart(), "\n" + lastIndent + " ");
                //Move the cursor back (hacky...)
                setSelection(getSelectionStart() - (lastIndent.length() + 2));
                //Remove the extra space (see above)
                getText().replace(getSelectionStart() + 1, getSelectionStart() + 2, "");
            }
        } else {
            //Regular indentation
            getText().insert(getSelectionStart(), lastIndent);
        }
	}
    
    private void processText(String text) {
        removeTextChangedListener(textWatcher);
        setText(text);
        addTextChangedListener(textWatcher);
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        if (isFocused()) {
            int line = getCurrentLine();
            int height = getLineHeight();
            canvas.drawRect(getPaddingLeft(), getPaddingTop() + line * height, getWidth() - getPaddingRight(), getPaddingTop() + line * height + height, currentLinePaint);
        }
        
        int lineCount = getLineCount();
        int height = getLineHeight();
        int pad = (int) numberLinesPaint.measureText(lineCount+"00");
        setPadding(pad, getPaddingTop(), getPaddingRight(), getPaddingBottom());
        
        for (int i=1; i<lineCount+1; i++) {
            canvas.drawText(String.valueOf(i), 10, (getPaddingTop()+height*i)-getTextSize()/4, numberLinesPaint);
        }
        
        super.onDraw(canvas);
    }
    
    private int getCurrentLine() {
        return getLayout().getLineForOffset(getSelectionStart());
    }
}
