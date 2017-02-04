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
    this.headFilter = new MemeRealTimeDataFilter(Source.HEAD, calibGyroData, null);
    this.eyeFilter = new MemeRealTimeDataFilter(Source.EYE, calibGyroData, null);
  }

  private MemeRealtimeData endData;

  public void setEndData(MemeRealtimeData endData) {
    this.endData = endData;
    this.eyeFilter.update(endData);
    this.headFilter.update(endData);
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
