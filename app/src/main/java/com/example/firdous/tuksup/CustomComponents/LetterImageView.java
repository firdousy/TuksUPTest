package com.example.firdous.tuksup.CustomComponents;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.example.firdous.tuksup.R;

import java.util.Random;

public class LetterImageView extends android.support.v7.widget.AppCompatImageView {

    private char mLetter;
    private Paint mTextPaint;

    public Paint getmBackgroundPaint() {
        return mBackgroundPaint;
    }

    public void setmBackgroundPaint(Paint mBackgroundPaint) {
        this.mBackgroundPaint = mBackgroundPaint;
    }

    private Paint mBackgroundPaint;
    private int mTextColor = Color.WHITE;
    private boolean isOval;

    public LetterImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mTextColor);
        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        mBackgroundPaint.setColor(randomColor());
    }

    public char getLetter() {
        return mLetter;
    }

    public void setLetter(char letter) {
        mLetter = letter;
        invalidate();
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int textColor) {
        mTextColor = textColor;
        invalidate();
    }

    public void setOval(boolean oval) {
        isOval = oval;
    }

    public boolean isOval() {
        return isOval;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (getDrawable() == null) {
            // Set a text font size based on the height of the view
            mTextPaint.setTextSize(canvas.getHeight() - getTextPadding() * 2);
            if (isOval()) {
                canvas.drawCircle(canvas.getWidth() / 2f, canvas.getHeight() / 2f, Math.min(canvas.getWidth(), canvas.getHeight()) / 2f,
                        mBackgroundPaint);
            } else {
                canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), mBackgroundPaint);
            }
            // Measure a text
            Rect textBounds = new Rect();
            mTextPaint.getTextBounds(String.valueOf(mLetter), 0, 1, textBounds);
            float textWidth = mTextPaint.measureText(String.valueOf(mLetter));
            float textHeight = textBounds.height();
            // Draw the text
            canvas.drawText(String.valueOf(mLetter), canvas.getWidth() / 2f - textWidth / 2f,
                    canvas.getHeight() / 2f + textHeight / 2f, mTextPaint);
        }
    }

    private float getTextPadding() {
        // Set a default padding to 8dp
        return 8 * getResources().getDisplayMetrics().density;
    }

    private int randomColor() {
        Random random = new Random();
        String[] colorsArr = getResources().getStringArray(R.array.colors);
        return Color.parseColor(colorsArr[random.nextInt(colorsArr.length)]);
    }
	
	/*
	    <string-array name="colors" translatable="false">
        <item>#99cc00</item>
        <item>#33b5e5</item>
        <item>#0099cc</item>
        <item>#547bca</item>
        <item>#aa66cc</item>
        <item>#9933cc</item>
        <item>#669900</item>
        <item>#aeb857</item>
        <item>#cc0000</item>
        <item>#df5948</itemhhhhh>
        <item>#ff4444</item>
        <item>#ae6b23</item>
        <item>#ff8800</item>
        <item>#e5ae4f</item>
        <item>#ffbb33</item>
        <item>#cccccc</item>
        <item>#888888</item>
    </string-array>
	
	*/
}