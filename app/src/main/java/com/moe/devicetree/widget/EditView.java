package com.moe.devicetree.widget;
import android.view.View;
import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.graphics.Paint;
import android.view.WindowInsetsAnimation.Bounds;
import android.graphics.Rect;
import android.widget.RelativeLayout;
import android.text.TextPaint;
import android.text.StaticLayout;
import android.text.Editable;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.BaseInputConnection;
import android.text.InputType;
import android.view.inputmethod.InputMethodManager;

public class EditView extends View{
    private StringBuilder text=new StringBuilder();
    private TextPaint paint=new TextPaint();
    public EditView(Context context){
        super(context);
        paint.setAntiAlias(true);
        paint.setColor(0xff000000);
        paint.setTextSize(52);
        paint.setStrokeCap(Paint.Cap.SQUARE);
        paint.setStrokeWidth(12);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setStyle(Paint.Style.FILL);
        setWillNotDraw(false);
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    @Override
    public boolean onCheckIsTextEditor() {
        return true;
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        BaseInputConnection fic = new BaseInputConnection(this, true);
        outAttrs.actionLabel = null;
        outAttrs.inputType = InputType.TYPE_CLASS_TEXT;
        //outAttrs.imeOptions = EditorInfo.IME_ACTION_NEXT;
        return fic;
    }

   
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Rect bounds=new Rect();
        paint.getTextBounds(text.toString(),0,text.length(),bounds);
        int height=MeasureSpec.getSize(heightMeasureSpec);
        int width=MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(width,height);
        
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        StaticLayout.Builder layout=StaticLayout.Builder.obtain(text,0,text.length(),paint,Integer.MAX_VALUE);
        layout.build().draw(canvas);
        }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {

            // show the keyboard so we can enter text
            InputMethodManager imm = (InputMethodManager) getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(this, InputMethodManager.SHOW_FORCED);
        }
        return true;
        
    }
    public void setText(CharSequence text){
        this.text.setLength(0);
        this.text.append(text);
        postInvalidate();
    }
    public void append(CharSequence text){
        this.text.append(text);
        postInvalidate();
    }
    public CharSequence getText(){
        return text.toString();
    }
}
