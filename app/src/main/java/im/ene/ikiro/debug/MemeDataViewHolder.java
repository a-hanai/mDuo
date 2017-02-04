package im.ene.ikiro.debug;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import im.ene.ikiro.R;
import im.ene.ikiro.sdk.DataWindow;

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

  public void bind(MemeDataAdapter adapter, DataWindow data) {
    actions.removeAllViews();

    TextView action1 = (TextView) inflater.inflate(R.layout.debug_text_view, actions, false);
    action1.setText("Eye:\t" + data.getEyeCommand() + "\nHead:\t" + data.getHeadCommand());
    actions.addView(action1);

    ///

    entries.removeAllViews();

    TextView entry1 = (TextView) inflater.inflate(R.layout.debug_text_view, entries, false);
    entry1.setText("getPowerLeft: " + data.getEndData().getPowerLeft());
    entries.addView(entry1);

    TextView entry2 = (TextView) inflater.inflate(R.layout.debug_text_view, entries, false);
    entry2.setText("getEyeMoveLeft: "
        + data.getEndData().getEyeMoveLeft()
        + "\ngetEyeMoveRight: "
        + data.getEndData().getEyeMoveRight());
    entries.addView(entry2);
  }
}
