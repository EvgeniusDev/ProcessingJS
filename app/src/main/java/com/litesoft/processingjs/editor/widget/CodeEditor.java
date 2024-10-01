package com.litesoft.processingjs.editor.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ScrollView;

import com.litesoft.processingjs.R;
import com.litesoft.processingjs.editor.plugin.base.Plugin;
import com.litesoft.processingjs.editor.plugin.base.PluginContainer;
import com.litesoft.processingjs.editor.plugin.autocomplete.AutoCloseBracketsPlugin;
import com.litesoft.processingjs.editor.plugin.autocomplete.AutoIndentPlugin;
import com.litesoft.processingjs.editor.plugin.codeblock.BracketsHighlightPlugin;
import com.litesoft.processingjs.editor.plugin.codeblock.CodeBlockHighlightPlugin;
import com.litesoft.processingjs.editor.plugin.linenumbers.LineNumberPlugin;
import com.litesoft.processingjs.editor.plugin.syntaxhighlight.SyntaxHighlightPlugin;
import com.litesoft.processingjs.editor.theme.ITheme;

import com.litesoft.processingjs.project.files.TextFile;
import java.util.List;
import android.view.MotionEvent;
import android.widget.Toast;

public class CodeEditor extends MultiAutoCompleteTextView {
    private PluginContainer pluginContainer;
    
    private ViewGroup codeContainer;
    private ScrollView verticalScroller;
    private HorizontalScrollView horizontalScroller;
    
    private ITheme theme;
    
    public CodeEditor(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        pluginContainer = new PluginContainer(this);
        attachPlugin(LineNumberPlugin.class);
        
        addTextChangedListener(textWatcher);
    }

    
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence sequence, int p2, int p3, int p4) {
            for (Plugin plugin : getPlugins()) {
                plugin.onTextChanged(sequence.toString());
            }
        }

        @Override
        public void beforeTextChanged(CharSequence sequence, int p2, int p3, int p4) {
            for (Plugin plugin : getPlugins()) {
                plugin.onBeforeTextChanged(sequence.toString());
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            for (Plugin plugin : getPlugins()) {
                plugin.onAfterTextChanged(editable);
            }
        }
    };
    
    
    public void openFile(TextFile file) {
        if (file.getName().endsWith(".js")) {
            attachPlugin(SyntaxHighlightPlugin.class);
            attachPlugin(BracketsHighlightPlugin.class);
            attachPlugin(CodeBlockHighlightPlugin.class);
            attachPlugin(AutoCloseBracketsPlugin.class);
            attachPlugin(AutoIndentPlugin.class);
        }
        
        insertText(file.readFile(), 0);
        
        for (Plugin plugin : getPlugins()) {
            plugin.onTextInserted();
        }
    }
    
    
    public void bindContainer(ViewGroup container) {
        codeContainer = container;
        horizontalScroller = codeContainer.findViewById(R.id.scroller_horz);
        verticalScroller = codeContainer.findViewById(R.id.scroller_vert);
        
        verticalScroller.setOnScrollChangeListener(new OnScrollChangeListener() {
            @Override
            public void onScrollChange(View p1, int p2, int p3, int p4, int p5) {
                for (Plugin plugin : getPlugins()) {
                    plugin.onScrollChanged();
                }
                invalidate();
            }
        });
            
        
        
        post(new Runnable() {
            @Override
            public void run() {
                setMinWidth(codeContainer.getWidth());
                setMinHeight(codeContainer.getHeight());
              //  setMaxHeight(codeContainer.getHeight());
            }
        });
    }
    
    
    public void setTheme(ITheme theme) {
        this.theme = theme;

        setTextColor(theme.colorTextPlain);
        setBackgroundColor(theme.colorBackground);
        getTextCursorDrawable().setTint(theme.colorTextPlain);

        for (Plugin plugin : getPlugins()) {
            plugin.onThemeChanged(theme);
        }
    }
    
    
    public ITheme getTheme() {
        return theme;
    }
    
    
    public void insertText(String text, int position) {
        removeTextChangedListener(textWatcher);
        getText().insert(position, text);
        addTextChangedListener(textWatcher);
    }
   
    
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        for (Plugin plugin : getPlugins()) {
            plugin.onSizeChanged(w, h);
        }
        
        super.onSizeChanged(w, h, oldw, oldh);
        
    }

    @Override
    protected void onScrollChanged(int horiz, int vert, int oldHoriz, int oldVert) {
        
        /*for (Plugin plugin : getPlugins()) {
            plugin.onScrollChanged();
        }
        */
       super.onScrollChanged(horiz, vert, oldHoriz, oldVert);
    }
    
    
    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
        
        if (pluginContainer == null) {
            return;
        }
        
        for (Plugin plugin : getPlugins()) {
            plugin.onSelectionChanged(selStart, selEnd);
        }
    }

    
    @Override
    protected void onDraw(Canvas canvas) {
        for (Plugin plugin : getPlugins()) {
            plugin.onDrawBehind(canvas);
        }
        
        super.onDraw(canvas);
        
        for (Plugin plugin : getPlugins()) {
            plugin.onDrawFront(canvas);
        }
    }
    
    
    public int getCurrentLine() {
        if (getLayout() == null)
            return -1;
        
        return getLayout().getLineForOffset(getSelectionStart());
    }
    
    
    public int getTopVisibleLine() {
        int line = getScrollVert() / getLineHeight();

        if (line < 0) {
            return 0;
        }

        if (line >= getLineCount()) {
            return getLineCount()-1;
        } else {
            return line;
        }
    }


    public int getBottomVisibleLine() {
        int line = getTopVisibleLine() + getContainerHeight() / getLineHeight() + 1;

        if (line < 0) {
            return 0;
        }

        if (line >= getLineCount()) {
            return getLineCount()-1;
        } else {
            return line;
        }
    }
    
    
    public int getScrollHorz() {
        //return super.getScrollX();
        return horizontalScroller.getScrollX();
    }
    
    
    public int getScrollVert() {
       // return super.getScrollY();
        return verticalScroller.getScrollY();
    }


    public int getContainerHeight() {
        //return getHeight();
        return codeContainer.getHeight();
    }

    
    public float[] getCharPosition(int position) {
        if (getLayout() == null) {
            return null;
        }

        float xOffset = getCompoundPaddingLeft(); //TODO hopefully no one uses Arabic (right-aligned localities)... because getCompoundPaddingStart() was introduced in a later API level

        //Calculate coordinates
        float x = Math.max(xOffset + getLayout().getPrimaryHorizontal(position), 1);
        float y = getLineHeight() * getLayout().getLineForOffset(position) + getCompoundPaddingTop();
        return new float[] {x, y};
    }
    
    
    public void attachPlugin(Class<? extends Plugin> clazz) {
        pluginContainer.attachPlugin(clazz);
    }
    
    
    public void detachPlugin(Class<? extends Plugin> clazz) {
        pluginContainer.detachPlugin(clazz);
    }
    
    
    public List<Plugin> getPlugins() {
        return pluginContainer.getPlugins();
    }
}
