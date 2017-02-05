package im.ene.ikiro;

import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by eneim on 2/5/17.
 */

public class BaseActivity extends AppCompatActivity {

  @TargetApi(23) protected void maybeRequestLocationPermission() {
    if (checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION)
        != PackageManager.PERMISSION_GRANTED) {
      requestPermissions(new String[] { android.Manifest.permission.ACCESS_COARSE_LOCATION }, 1);
    }
  }

  @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    if (requestCode == 1) {
      if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        Log.d("PERMISSION", "Succeeded");
        Toast.makeText(BaseActivity.this, "Permission Request Succeed", Toast.LENGTH_SHORT).show();
      } else {
        Log.d("PERMISSION", "Failed");
        Toast.makeText(BaseActivity.this, "Permission Request Failed", Toast.LENGTH_SHORT).show();
      }
    } else {
      super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
  }
}
