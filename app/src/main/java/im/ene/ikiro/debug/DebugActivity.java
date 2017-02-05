package im.ene.ikiro.debug;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.jins_jp.meme.MemeConnectListener;
import com.jins_jp.meme.MemeLib;
import com.jins_jp.meme.MemeResponse;
import com.jins_jp.meme.MemeResponseListener;
import com.jins_jp.meme.MemeScanListener;
import com.jins_jp.meme.MemeStatus;
import im.ene.ikiro.BaseActivity;
import im.ene.ikiro.R;

public class DebugActivity extends BaseActivity
    implements MemeConnectListener, MemeResponseListener {

  private static final String TAG = "MEME";

  MemeLib memeLib;
  MemeDataFragment fragment;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    memeLib = MemeLib.getInstance();
    fragment = (MemeDataFragment) getSupportFragmentManager().findFragmentById(R.id.content);
    if (fragment == null) {
      fragment = MemeDataFragment.newInstance();
      getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
    }

    maybeRequestLocationPermission();
    //// Write a message to the database
    //FirebaseDatabase database = FirebaseDatabase.getInstance();
    //DatabaseReference myRef = database.getReference("message_meme");
    //
    //myRef.setValue("Hello, World!");
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_meme_action, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    int itemId = item.getItemId();

    if (itemId == R.id.action_scan) {
      startScan(null);
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    if (memeLib.isScanning()) {
      memeLib.stopScan();
    }

    if (memeLib.isDataReceiving()) {
      memeLib.stopDataReport();
    }

    if (memeLib.isConnected()) {
      memeLib.disconnect();
      memeLib = null;
    }
  }

  public void startScan(View view) {
    Log.d("SCAN", "start scanning...");
    memeLib.setMemeConnectListener(this);

    MemeStatus status = memeLib.startScan(new MemeScanListener() {
      @Override public void memeFoundCallback(String s) {
        Log.d("SCAN", "found: " + s);

        if (s.contains("D4") || s.contains("8E:02")) {
          // memeLib.connect(s);
          Log.i(TAG, "memeFoundCallback: " + memeLib.connect(s));

          if (memeLib.isScanning()) {
            memeLib.stopScan();
            Log.d("SCAN", "scan stopped.");
          }
        }
      }
    });

    Log.i(TAG, "startScan: " + status);
  }

  @Override public void memeConnectCallback(boolean b) {
    Log.d(TAG, "memeConnectCallback() called with: b = [" + b + "]");
    if (b) {
      Log.i(TAG, "memeLib.isCalibrated(): " + memeLib.isCalibrated());
      memeLib.setResponseListener(this);
      if (fragment != null) {
        memeLib.startDataReport(fragment);
      }
    }
  }

  @Override public void memeDisconnectCallback() {
    Log.d(TAG, "memeDisconnectCallback() called");
  }

  @Override public void memeResponseCallback(MemeResponse memeResponse) {
    Log.d(TAG, "memeResponseCallback() called with: memeResponse = [" + memeResponse + "]");
  }
}
