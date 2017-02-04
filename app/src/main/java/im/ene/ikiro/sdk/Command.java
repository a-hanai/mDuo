package im.ene.ikiro.sdk;

/**
 * Created by eneim on 2/5/17.
 */

public class Command {

  private final Source source;

  private final Action action;

  public Command(Source source, Action action) {
    this.source = source;
    this.action = action;
  }

  public static Command of(Source source, Action action) {
    return new Command(source, action);
  }

  @Override public String toString() {
    return "Command{" + "source=" + source + ", action=" + action + '}';
  }

  public Source getSource() {
    return source;
  }

  public Action getAction() {
    return action;
  }
}
