package im.ene.ikiro.game;

import android.content.Context;
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
import java.util.List;

/**
 * Created by eneim on 2/5/17.
 */

public class TttGameFragment extends Fragment {

  private static final String GAME_USER_SIDE = "game:user:side";  // true or false

  public static TttGameFragment newInstance(boolean side) {
    TttGameFragment fragment = new TttGameFragment();
    Bundle args = new Bundle();
    args.putBoolean(GAME_USER_SIDE, side);
    fragment.setArguments(args);
    return fragment;
  }

  Boolean userSide; // true or false
  Boolean[] gameState = new Boolean[9];  // will be used to sync with Firebase

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments().containsKey(GAME_USER_SIDE)) {
      this.userSide = getArguments().getBoolean(GAME_USER_SIDE);
    }

    if (this.userSide == null) {
      getActivity().finish();
    }
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.layout_game_board, container, false);
  }

  Unbinder unbinder;
  @BindView(R.id.recycler_view) RecyclerView recyclerView;
  BoardAdapter adapter;

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    unbinder = ButterKnife.bind(this, view);
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
    Arrays.fill(gameState, null);
    adapter = new BoardAdapter(this.userSide, this.gameState);
    adapter.setClickHandler(new BoardAdapter.ClickHandler() {
      @Override void onChecked(View view, int pos) {
        gameState[pos] = userSide;
        if (callback != null) {
          callback.onGameStateChanged(gameState, pos);
        }
      }
    });

    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setAdapter(adapter);

    adapter.setCursorPosition(4);
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    if (unbinder != null) {
      unbinder.unbind();
    }
  }

  Callback callback;

  @Override public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof Callback) {
      this.callback = (Callback) context;
    }
  }

  public void check(int position) {
    adapter.check(position, adapter.side);
  }

  public int getCursorPosition() {
    return adapter.getCursorPosition();
  }

  public void moveCursor(int position) {
    adapter.setCursorPosition(position);
  }

  public void moveCursorClockWise() {
    adapter.setCursorPosition(adapter.getItemCount());
  }

  public void updateStates(List<Boolean> gameState) {
    adapter.updateBoard(gameState);
  }

  static class BoardAdapter extends RecyclerView.Adapter<CellViewHolder> {

    private final Boolean[] states;
    private int cursorPosition = RecyclerView.NO_POSITION;  // move over by meme action

    final Boolean side;

    ClickHandler clickHandler;

    public void setClickHandler(ClickHandler clickHandler) {
      this.clickHandler = clickHandler;
    }

    public BoardAdapter(Boolean side, Boolean[] states) {
      this.side = side;
      this.states = states;
    }

    @Override public CellViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
      View view =
          LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_cell, parent, false);
      final CellViewHolder viewHolder = new CellViewHolder(view);
      viewHolder.button.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          int pos = viewHolder.getAdapterPosition();
          if (clickHandler != null && pos != RecyclerView.NO_POSITION) {
            check(pos, BoardAdapter.this.side);
            setCursorPosition(pos);
            clickHandler.onChecked(v, pos);
          }
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

    int getCursorPosition() {
      return cursorPosition;
    }

    void check(int pos, Boolean willCheck) {
      if (pos >= 0 && pos < getItemCount()) {
        states[pos] = willCheck;
        notifyItemChanged(pos);
      }
    }

    void updateBoard(List<Boolean> states) {
      for (int i = 0; i < states.size(); i++) {
        this.states[i] = states.get(i);
      }

      notifyDataSetChanged();
    }

    static abstract class ClickHandler {

      abstract void onChecked(View view, int pos);
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

  public interface Callback {

    void onGameStateChanged(Boolean[] gameState, int changedPosition);
  }
}
