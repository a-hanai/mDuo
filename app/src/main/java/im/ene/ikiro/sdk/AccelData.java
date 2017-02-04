package im.ene.ikiro.sdk;

/**
 * Created by eneim on 2/5/17.
 */

public class AccelData {

  private final float accX;
  private final float accY;
  private final float accZ;

  public AccelData(float accX, float accY, float accZ) {
    this.accX = accX;
    this.accY = accY;
    this.accZ = accZ;
  }

  public float getAccX() {
    return accX;
  }

  public float getAccY() {
    return accY;
  }

  public float getAccZ() {
    return accZ;
  }
}
