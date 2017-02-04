package im.ene.ikiro.sdk;

import com.jins_jp.meme.MemeRealtimeData;

/**
 * Created by eneim on 2/4/17.
 */

public class DataWindow {

  private final MemeRealtimeData startData;

  public DataWindow(MemeRealtimeData startData) {
    this.startData = startData;
  }

  private MemeRealtimeData endData;

  public void setEndData(MemeRealtimeData endData) {
    this.endData = endData;
  }

  public MemeRealtimeData getEndData() {
    return endData;
  }

  public Action getAccAction() {
    return Action.IDLE;
  }

  public Action getGyroAction() {
    return Action.IDLE;
  }

  public Action getEyeAction() {
    return Action.IDLE;
  }
}
