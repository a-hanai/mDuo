package im.ene.ikiro.sdk;

/**
 * Created by eneim on 2/5/17.
 */

public class GyroData {

  private final float pitch;
  private final float roll;
  private final float yaw;

  public GyroData(float pitch, float roll, float yaw) {
    this.pitch = pitch;
    this.roll = roll;
    this.yaw = yaw;
  }

  public float getPitch() {
    return pitch;
  }

  public float getRoll() {
    return roll;
  }

  public float getYaw() {
    return yaw;
  }
}
