package im.ene.ikiro.sdk;

/**
 * Created by eneim on 2/4/17.
 */

public enum Action {

  // Idle
  IDLE,

  // from gyro data
  YAW_LEFT, YAW_RIGHT, PITCH_FORWARD, PITCH_BACKWARD, ROLL_LEFT, ROLL_RIGHT,

  // from accelerator data
  HEAD_TURN_LEFT, HEAD_TURN_RIGHT, HEAD_TURN_FORWARD, HEAD_TURN_BACKWARD,

  // Eye movement
  EYE_TURN_LEFT, EYE_TURN_RIGHT, EYE_TURN_UP, EYE_TURN_DOWN, EYE_BLINK;
}
