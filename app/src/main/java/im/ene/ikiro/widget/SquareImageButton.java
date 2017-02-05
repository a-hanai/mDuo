package im.ene.ikiro.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;

/**
 * Created by eneim on 2/5/17.
 */

public class SquareImageButton extends AppCompatImageButton {

  public SquareImageButton(Context context) {
    super(context);
  }

  public SquareImageButton(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public SquareImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    int widthMode = MeasureSpec.getMode(widthMeasureSpec);
    int heightMode = MeasureSpec.getMode(heightMeasureSpec);

    int widthSize = MeasureSpec.getSize(widthMeasureSpec);
    int heightSize = MeasureSpec.getSize(heightMeasureSpec);

    int size = Math.min(widthSize, heightSize);

    if (widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.EXACTLY) {
      size = widthSize;
    } else if (heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.EXACTLY) {
      size = heightSize;
    }

    setMeasuredDimension(size, size);
  }
}
