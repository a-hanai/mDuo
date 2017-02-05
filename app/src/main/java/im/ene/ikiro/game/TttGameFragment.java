package im.ene.ikiro.game;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import im.ene.ikiro.R;
import java.util.Arrays;

/**
 * Created by eneim on 2/5/17.
 */

public class TttGameFragment extends Fragment {

  public static TttGameFragment newInstance() {
    Bundle args = new Bundle();
    TttGameFragment fragment = new TttGameFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.layout_game_board, container, false);
  }

  Unbinder unbinder;
  @BindView(R.id.recycler_view) RecyclerView recyclerView;

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    unbinder = ButterKnife.bind(this, view);
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
    BoardAdapter adapter = new BoardAdapter();

    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setAdapter(adapter);
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    if (unbinder != null) {
      unbinder.unbind();
    }
  }

  static class BoardAdapter extends RecyclerView.Adapter<CellViewHolder> {

    private final Boolean[] states = new Boolean[9];
    private int cursorPosition = RecyclerView.NO_POSITION;  // move over by meme action

    public BoardAdapter() {
      Arrays.fill(states, null);
    }

    @Override public CellViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
      View view =
          LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_cell, parent, false);
      final CellViewHolder viewHolder = new CellViewHolder(view);
      viewHolder.button.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          check(viewHolder.getAdapterPosition(), Boolean.TRUE);
          setCursorPosition(viewHolder.getAdapterPosition());
        }
      });
      return viewHolder;
    }

    @Override public void onBindViewHolder(CellViewHolder holder, int position) {
      holder.bind(this, getState(position));
    }

    Boolean getState(int pos) {
      return states[pos];
    }

    @Override public int getItemCount() {
      return 9;
    }

    void setCursorPosition(int pos) {
      if (pos != cursorPosition && pos >= 0 && pos < getItemCount()) {
        int oldCurPos = cursorPosition;
        this.cursorPosition = pos;
        notifyItemChanged(pos);
        notifyItemChanged(oldCurPos);
      }
    }

    void check(int pos, Boolean willCheck) {
      if (pos >= 0 && pos < getItemCount()) {
        states[pos] = willCheck;
        notifyItemChanged(pos);
      }
    }
  }

  static class CellViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.cell_button) ImageButton button;
    @BindView(R.id.cell_overlay) View overlay;

    public CellViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    void bind(BoardAdapter adapter, Object item) {
      if (item != null && item instanceof Boolean) {
        int res = Boolean.TRUE.equals(item) ? R.drawable.ic_button_shape_oval
            : R.drawable.ic_button_close;

        button.setImageResource(res);
      } else {
        button.setImageResource(R.drawable.blank_button);
      }

      if (getAdapterPosition() == adapter.cursorPosition) {
        overlay.setVisibility(View.VISIBLE);
      } else {
        overlay.setVisibility(View.GONE);
      }
    }
  }
}
