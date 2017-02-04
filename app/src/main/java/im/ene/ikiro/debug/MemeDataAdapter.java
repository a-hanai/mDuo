package im.ene.ikiro.debug;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import im.ene.ikiro.sdk.Command;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eneim on 2/4/17.
 */

public class MemeDataAdapter extends RecyclerView.Adapter<MemeDataViewHolder> {

  final List<Command> entries = new ArrayList<>();

  @Override public MemeDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(MemeDataViewHolder.LAYOUT_RES, parent, false);
    @SuppressWarnings("UnnecessaryLocalVariable") MemeDataViewHolder viewHolder =
        new MemeDataViewHolder(view);
    return viewHolder;
  }

  @Override public void onBindViewHolder(MemeDataViewHolder holder, int position) {
    holder.bind(this, entries.get(position));
  }

  @Override public int getItemCount() {
    return entries.size();
  }

  private Handler handler = new Handler();

  public void addEntry(Command entry) {
    entries.add(entry);
    handler.post(new Runnable() {
      @Override public void run() {
        notifyItemInserted(entries.size() - 1);
      }
    });
  }
}
