package im.ene.ikiro.sdk;

import android.support.annotation.NonNull;
import com.jins_jp.meme.MemeRealtimeData;

/**
 * Created by eneim on 2/5/17.
 */

public class MemeRealTimeDataFilter {

  private static final int IDLE_TIME = 750; // 750 milliseconds

  private final Source cmdSource;
  private Command lastCmd = Command.IDLE; // idle at first
  private long lastCmdTimeStamp;

  private GyroData calibGyroData; // stable state, may need to re-calibrate if user move
  private AccelData calibAccelData; // stable acceleration, may need to re-calibrate if user move

  public MemeRealTimeDataFilter(Source cmdSource) {
    this.cmdSource = cmdSource;
  }

  @NonNull public final Command getLastCmd() {
    return lastCmd;
  }

  public final Command update(MemeRealtimeData data) {
    if (isWaiting()) {
      lastCmd = Command.IDLE;
    } else {
      switch (cmdSource) {
        case EYE:
          if (data.getBlinkStrength() > 30) {
            return setCommand(Command.of(cmdSource, Action.EYE_BLINK));
          } else if (data.getEyeMoveLeft() > 0) {
            return setCommand(Command.of(cmdSource, Action.EYE_TURN_LEFT));
          } else if (data.getEyeMoveRight() > 0) {
            return setCommand(Command.of(cmdSource, Action.EYE_TURN_RIGHT));
          } else if (data.getEyeMoveUp() > 0) {
            return setCommand(Command.of(cmdSource, Action.EYE_TURN_UP));
          } else if (data.getEyeMoveDown() > 0) {
            return setCommand(Command.of(cmdSource, Action.EYE_TURN_DOWN));
          }
          break;
        case HEAD:
          break;
        default:
          break;
      }
    }
    return lastCmd;
  }

  // private implementations
  private boolean isWaiting() {
    return System.nanoTime() / (double) 1_000_000 - lastCmdTimeStamp < IDLE_TIME;
  }

  private Command setCommand(Command command) {
    lastCmd = command;
    lastCmdTimeStamp = System.nanoTime() / 1_000_000;
    return lastCmd;
  }
}
