package im.ene.ikiro.sdk;

import com.jins_jp.meme.MemeRealtimeData;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eneim on 2/5/17.
 */

public class MemeActionFilter {

  public synchronized void onNewData(MemeRealtimeData data) {
    if (this.headActions != null) {
      for (OnHeadActionListener listener : this.headActions) {
        listener.onHeadAction(headActionFilter.update(data));
      }
    }

    if (this.eyeActions != null) {
      for (OnEyeActionListener listener : this.eyeActions) {
        listener.onEyeAction(eyeActionFilter.update(data));
      }
    }
  }

  public interface OnHeadActionListener {

    void onHeadAction(Command action);
  }

  public interface OnEyeActionListener {

    void onEyeAction(Command action);
  }

  private List<OnHeadActionListener> headActions;
  private List<OnEyeActionListener> eyeActions;

  public void addOnHeadActionListener(OnHeadActionListener listener) {
    if (this.headActions == null) {
      this.headActions = new ArrayList<>();
    }
    this.headActions.add(listener);
  }

  public void removeOnHeadActionListener(OnHeadActionListener listener) {
    if (this.headActions != null) {
      this.headActions.remove(listener);
    }
  }

  public void addOnEyeActionListener(OnEyeActionListener listener) {
    if (this.eyeActions == null) {
      this.eyeActions = new ArrayList<>();
    }
    this.eyeActions.add(listener);
  }

  public void removeOnEyeActionListener(OnEyeActionListener listener) {
    if (this.eyeActions != null) {
      this.eyeActions.remove(listener);
    }
  }

  private final MemeRealTimeDataFilter eyeActionFilter;
  private final MemeRealTimeDataFilter headActionFilter;

  public MemeActionFilter(GyroData calibGyroData) {
    this.eyeActionFilter = new MemeRealTimeDataFilter(Source.EYE, calibGyroData);
    this.headActionFilter = new MemeRealTimeDataFilter(Source.HEAD, calibGyroData);
  }
}
