package im.ene.ikiro.game;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import im.ene.ikiro.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eneim on 2/5/17.
 */

public class MemeLogDialog extends DialogFragment {

  public static MemeLogDialog newInstance() {
    Bundle args = new Bundle();
    MemeLogDialog fragment = new MemeLogDialog();
    fragment.setArguments(args);
    return fragment;
  }

  Callback callback;

  @Override public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof Callback) {
      this.callback = (Callback) context;
    }
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.recycler_view, container, false);
  }

  @BindView(R.id.recycler_view) RecyclerView recyclerView;

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.bind(this, view);
  }

  Adapter adapter;

  @Override public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
    adapter = new Adapter(this.callback);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setAdapter(adapter);
  }

  public void addMemeId(String memeId) {
    this.adapter.addMeme(memeId);
  }

  private static class Adapter extends RecyclerView.Adapter {

    List<String> memeIds = new ArrayList<>();

    final Callback callback;

    public Adapter(Callback callback) {
      this.callback = callback;
    }

    @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext())
          .inflate(android.R.layout.simple_list_item_1, parent, false);
      final RecyclerView.ViewHolder viewHolder = new RecyclerView.ViewHolder(view) {
      };

      viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          if (callback != null) {
            callback.onClickToMemeId(v, memeIds.get(viewHolder.getAdapterPosition()));
          }
        }
      });

      return viewHolder;
    }

    @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
      ((TextView) holder.itemView).setText("Meme: " + memeIds.get(position));
    }

    @Override public int getItemCount() {
      return memeIds.size();
    }

    public void addMeme(String memeId) {
      synchronized (this) {
        memeIds.add(memeId);
        notifyItemChanged(getItemCount() - 1);
      }
    }
  }

  public interface Callback {

    void onClickToMemeId(View view, String memeId);
  }
}
