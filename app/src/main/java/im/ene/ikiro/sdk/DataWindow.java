package im.ene.ikiro.sdk;

import com.jins_jp.meme.MemeRealtimeData;

/**
 * Created by eneim on 2/4/17.
 */

public class DataWindow {

  private final GyroData calibGyroData;

  private final MemeRealTimeDataFilter headFilter;
  private final MemeRealTimeDataFilter eyeFilter;

  public DataWindow(GyroData calibGyroData) {
    this.calibGyroData = calibGyroData;
    this.headFilter = new MemeRealTimeDataFilter(Source.HEAD, calibGyroData);
    this.eyeFilter = new MemeRealTimeDataFilter(Source.EYE, calibGyroData);
  }

  private MemeRealtimeData endData;

  public void update(MemeRealtimeData data) {
    this.endData = data;
    this.eyeFilter.update(data);
    this.headFilter.update(data);
  }

  public MemeRealtimeData getEndData() {
    return endData;
  }

  public Command getEyeCommand() {
    return eyeFilter.getLastCmd();
  }

  public Command getHeadCommand() {
    return headFilter.getLastCmd();
  }
}
