package im.ene.ikiro;

import android.app.Application;
import com.jins_jp.meme.MemeLib;

/**
 * Created by eneim on 2/2/17.
 */

public class IkiroApp extends Application {

  static final String APP_ID = "617485373522949";
  static final String APP_SECRET = "2rzvhp52kmoevmyws7evur4cywyuhvs7";

  @Override public void onCreate() {
    super.onCreate();
    MemeLib.setAppClientID(this, APP_ID, APP_SECRET);
    MemeLib.getInstance().setAutoConnect(false);
    MemeLib.getInstance().getMode();
  }
}
