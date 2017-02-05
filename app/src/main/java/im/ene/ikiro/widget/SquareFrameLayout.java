package im.ene.ikiro.widget;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by eneim on 2/5/17.
 */

public class SquareFrameLayout extends FrameLayout {

  public SquareFrameLayout(@NonNull Context context) {
    super(context);
  }

  public SquareFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public SquareFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs,
      @AttrRes int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public SquareFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs,
      @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    int widthMode = MeasureSpec.getMode(widthMeasureSpec);
    int heightMode = MeasureSpec.getMode(heightMeasureSpec);

    int widthSize = MeasureSpec.getSize(widthMeasureSpec);
    int heightSize = MeasureSpec.getSize(heightMeasureSpec);

    int size = Math.min(widthSize, heightSize);

    if (heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.EXACTLY) {
      size = heightSize;
    } else if (widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.EXACTLY) {
      size = widthSize;
    }

    setMeasuredDimension(size, size);
  }
}
