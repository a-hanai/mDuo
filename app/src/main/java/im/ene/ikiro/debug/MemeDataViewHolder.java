package im.ene.ikiro.debug;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.jins_jp.meme.MemeRealtimeData;
import im.ene.ikiro.R;

/**
 * Created by eneim on 2/4/17.
 */

public class MemeDataViewHolder extends RecyclerView.ViewHolder {

  static final int LAYOUT_RES = R.layout.vh_data_entry;

  @BindView(R.id.data_entries) LinearLayout entries;
  @BindView(R.id.data_actions) LinearLayout actions;
  final LayoutInflater inflater;

  public MemeDataViewHolder(View itemView) {
    super(itemView);
    ButterKnife.bind(this, itemView);
    inflater = LayoutInflater.from(itemView.getContext());
  }

  public void bind(MemeDataAdapter adapter, MemeRealtimeData data) {
    entries.removeAllViews();

    TextView textView3 = (TextView) inflater.inflate(R.layout.debug_text_view, entries, false);
    textView3.setText("getPowerLeft: " + data.getPowerLeft());
    entries.addView(textView3);

    ///

    TextView textView = (TextView) inflater.inflate(R.layout.debug_text_view, entries, false);
    textView.setText("getAccX: "
        + data.getAccX()
        + "\ngetAccY: "
        + data.getAccY()
        + "\ngetAccZ: "
        + data.getAccZ());
    entries.addView(textView);

    ///

    TextView textView2 = (TextView) inflater.inflate(R.layout.debug_text_view, entries, false);
    textView2.setText("getBlinkSpeed: "
        + data.getBlinkSpeed()
        + "\ngetBlinkStrength: "
        + data.getBlinkStrength()
        + "\ngetFitError: "
        + data.getFitError());
    entries.addView(textView2);

    ///

    TextView textView4 = (TextView) inflater.inflate(R.layout.debug_text_view, entries, false);
    textView4.setText("getEyeMoveLeft: "
        + data.getEyeMoveLeft()
        + "\ngetEyeMoveRight: "
        + data.getEyeMoveRight()
        + "\ngetEyeMoveDown: "
        + data.getEyeMoveDown()
        + "\ngetEyeMoveUp: "
        + data.getEyeMoveUp());
    entries.addView(textView4);

    ///

    TextView textView5 = (TextView) inflater.inflate(R.layout.debug_text_view, entries, false);
    textView5.setText("getPitch: "
        + data.getPitch()
        + "\ngetRoll: "
        + data.getRoll()
        + "\ngetYaw: "
        + data.getYaw());
    entries.addView(textView5);
  }
}
