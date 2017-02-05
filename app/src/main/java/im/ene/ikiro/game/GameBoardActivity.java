package im.ene.ikiro.game;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import im.ene.ikiro.R;

/**
 * Created by eneim on 2/5/17.
 */

public class GameBoardActivity extends AppCompatActivity {

  TttGameFragment gameFragment;
  ChatFragment chatFragment;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_board);

    gameFragment = (TttGameFragment) getSupportFragmentManager().findFragmentById(R.id.game_board);
    if (gameFragment == null) {
      gameFragment = TttGameFragment.newInstance();
      getSupportFragmentManager().beginTransaction()
          .replace(R.id.game_board, gameFragment)
          .commit();
    }
  }
}
