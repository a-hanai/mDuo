package im.ene.ikiro.game;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by eneim on 2/5/17.
 */

public class ChatFragment extends Fragment {

  public static ChatFragment newInstance() {
    Bundle args = new Bundle();
    ChatFragment fragment = new ChatFragment();
    fragment.setArguments(args);
    return fragment;
  }
}
