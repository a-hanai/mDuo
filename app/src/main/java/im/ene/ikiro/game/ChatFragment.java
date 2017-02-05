package im.ene.ikiro.game;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.util.SortedList;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.util.SortedListAdapterCallback;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import im.ene.ikiro.R;
import im.ene.ikiro.sdk.Command;
import java.util.Collection;

/**
 * Created by eneim on 2/5/17.
 */

public class ChatFragment extends Fragment {

  static final String ARGS_MY_USER_ID = "my_user_id";
  static final String ARGS_EMOJI_FLAG = "partner_user_id";

  public static ChatFragment newInstance(String myUserId, Boolean isTrueUser) {
    ChatFragment fragment = new ChatFragment();
    Bundle args = new Bundle();
    args.putString(ARGS_MY_USER_ID, myUserId);
    args.putBoolean(ARGS_EMOJI_FLAG, isTrueUser);
    fragment.setArguments(args);
    return fragment;
  }

  private String myUserId;
  private Boolean emojiFlag;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      myUserId = getArguments().getString(ARGS_MY_USER_ID);
      emojiFlag = getArguments().getBoolean(ARGS_EMOJI_FLAG);
    }
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.layout_game_chat, container, false);
  }

  @BindView(R.id.recycler_view) RecyclerView recyclerView;
  @BindView(R.id.latest_action) TextView latestAction;

  private Adapter adapter;

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.bind(this, view);

    RecyclerView.LayoutManager layoutManager =
        new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
    recyclerView.setLayoutManager(layoutManager);

    adapter = new Adapter(myUserId, emojiFlag);
    recyclerView.setAdapter(adapter);
  }

  public void updateMessages(Collection<Message> messages) {
    adapter.messages.addAll(messages);
    recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
  }

  public void addMessage(String author, String message, long timeStamp) {
    adapter.messages.add(new Message(author, message, timeStamp));
    recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
  }

  public void addMessage(String author, String message) {
    adapter.messages.add(new Message(author, message, System.nanoTime() / 1_000_000));
    recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
  }

  public void setAction(Command action) {
    if (action != null) {
      latestAction.setText(action.getSource().name() + "@" + action.getAction().name());
    }
  }

  static class MessageViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.user_name) TextView userName;
    @BindView(R.id.message) TextView messageView;

    public MessageViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    public void bind(Adapter adapter, Message message) {
      userName.setText(message.user);
      // messageView.setText(message.message);
      messageView.setText(Boolean.TRUE.equals(adapter.emojiFlag) ?  //
          getEmojiByUnicode(GameBoardActivity.EMOJI_TRUE)
          : getEmojiByUnicode(GameBoardActivity.EMOJI_FALSE));
    }
  }

  public static class Message {

    private final String user;

    private final String message;

    private final long timeStamp;

    public Message(String user, String message, long timeStamp) {
      this.user = user;
      this.message = message;
      this.timeStamp = timeStamp;
    }
  }

  public static String getEmojiByUnicode(int unicode) {
    return new String(Character.toChars(unicode));
  }

  static class Adapter extends RecyclerView.Adapter<MessageViewHolder> {

    private final String myUserId;
    private final Boolean emojiFlag;

    final SortedList<Message> messages;

    public Adapter(String myUserId, Boolean emojiFlag) {
      this.myUserId = myUserId;
      this.emojiFlag = emojiFlag;
      this.messages = new SortedList<>(Message.class, new SortedListAdapterCallback<Message>(this) {
        @Override public int compare(Message o1, Message o2) {
          return Long.compare(o1.timeStamp, o2.timeStamp);
        }

        @Override public boolean areContentsTheSame(Message oldItem, Message newItem) {
          return oldItem.timeStamp == newItem.timeStamp;
        }

        @Override public boolean areItemsTheSame(Message item1, Message item2) {
          return item1.timeStamp == item2.timeStamp;
        }
      });
    }

    @Override public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view =
          LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_message, parent, false);
      return new MessageViewHolder(view);
    }

    @Override public void onBindViewHolder(MessageViewHolder holder, int position) {
      holder.bind(this, messages.get(position));
    }

    @Override public int getItemCount() {
      return messages.size();
    }
  }
}
