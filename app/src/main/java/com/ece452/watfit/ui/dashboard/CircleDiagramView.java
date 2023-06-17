package com.ece452.watfit.ui.dashboard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

public class CircleDiagramView extends View {
    private int score;

    private Paint circlePaint;
    private Paint fillPaint;
    private Paint textPaint;

    public CircleDiagramView(Context context) {
        super(context);
        init();
        setScore(80);
    }

    public CircleDiagramView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        setScore(80);
    }

    public CircleDiagramView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        setScore(80);
    }

    private void init() {
        circlePaint = new Paint();
        circlePaint.setColor(Color.WHITE);
        circlePaint.setStyle(Paint.Style.STROKE);

        fillPaint = new Paint();
        fillPaint.setColor(Color.YELLOW);
        fillPaint.setStyle(Paint.Style.STROKE);
        fillPaint.setStrokeWidth(80);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(40f);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
    }

    public void setScore(int score) {
        this.score = score;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int radius = Math.min(centerX, centerY) - 100;

        float sweepAngle = score * 3.6f;

        canvas.drawCircle(centerX, centerY, radius, circlePaint);

        float startAngle = -90;
        float endAngle = startAngle + sweepAngle;

        canvas.drawArc(centerX - radius, centerY - radius, centerX + radius, centerY + radius, startAngle, sweepAngle, false, fillPaint);

        canvas.drawText(String.valueOf(score), centerX, centerY + textPaint.getTextSize() / 3, textPaint);
    }
}
