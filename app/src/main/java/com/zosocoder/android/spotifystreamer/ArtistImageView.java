package com.zosocoder.android.spotifystreamer;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ArtistImageView extends ImageView {

    float widthPercent, heightPercent;

    public ArtistImageView(Context context) {
        super(context);
        setup();
    }

    public ArtistImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    public ArtistImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setup();
    }

    private void setup() { setScaleType(ScaleType.MATRIX); }

    public void setOffset(float widthPercent, float heightPercent) {
        this.widthPercent = widthPercent;
        this.heightPercent = heightPercent;
    }

    @Override
    protected boolean setFrame(int l, int t, int r, int b) {
        if (getDrawable() == null) return super.setFrame(l, t, r, b);

        Matrix matrix = getImageMatrix();
        float scale;
        int viewWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int viewHeight = getMeasuredHeight() - getPaddingBottom() - getPaddingTop();
        int drawableWidth = getDrawable().getIntrinsicWidth();
        int drawableHeight = getDrawable().getIntrinsicHeight();

        if (drawableWidth * viewHeight > drawableHeight * viewWidth) {
            scale = (float) viewHeight / drawableHeight;
        } else {
            scale = (float) viewWidth / drawableWidth;
        }

        RectF drawableRect = new RectF(
                0,
                0,
                drawableWidth,
                drawableHeight - (viewHeight/scale));
        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
        matrix.setRectToRect(drawableRect, viewRect, Matrix.ScaleToFit.FILL);

        setImageMatrix(matrix);

        return super.setFrame(l, t, r, b);
    }

    //    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int width = MeasureSpec.getSize(widthMeasureSpec);
////        int height = MeasureSpec.getSize(heightMeasureSpec);
//        int height = width * getDrawable().getIntrinsicHeight() / getDrawable().getIntrinsicWidth();
//        setMeasuredDimension(width, height);
//    }
}
