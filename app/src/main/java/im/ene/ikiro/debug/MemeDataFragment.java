package im.ene.ikiro.debug;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.jakewharton.rxrelay2.PublishRelay;
import com.jakewharton.rxrelay2.Relay;
import com.jins_jp.meme.MemeRealtimeData;
import com.jins_jp.meme.MemeRealtimeListener;
import im.ene.ikiro.R;
import im.ene.ikiro.sdk.Action;
import im.ene.ikiro.sdk.Command;
import im.ene.ikiro.sdk.DataWindow;
import im.ene.ikiro.sdk.GyroData;
import im.ene.ikiro.sdk.MemeActionFilter;
import io.reactivex.disposables.Disposable;
import java.util.ArrayList;

/**
 * Created by eneim on 2/4/17.
 */

public class MemeDataFragment extends Fragment
    implements MemeRealtimeListener, MemeActionFilter.OnEyeActionListener,
    MemeActionFilter.OnHeadActionListener {

  private static final String TAG = "MemeDataFragment";

  public static MemeDataFragment newInstance() {
    MemeDataFragment fragment = new MemeDataFragment();
    return fragment;
  }

  @BindView(R.id.recycler_view) RecyclerView recyclerView;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.meme_data_fragment, container, false);
  }

  LinearLayoutManager layoutManager;
  MemeDataAdapter adapter;

  Relay<MemeRealtimeData> relay;
  Disposable disposable;
  Disposable rxDisposable;

  MemeRealtimeData latestEntry;
  MemeRealtimeData hasBlinkEntry;
  DataWindow dataWindow;
  DataWindow hasBlinkWindow;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    relay = PublishRelay.create();
  }

  @Override public void onDestroy() {
    super.onDestroy();
    if (disposable != null && !disposable.isDisposed()) {
      disposable.dispose();
    }

    if (rxDisposable != null && !rxDisposable.isDisposed()) {
      rxDisposable.dispose();
    }

    if (relay != null) {
      relay = null;
    }
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.bind(this, view);
    layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
    recyclerView.setLayoutManager(layoutManager);
    adapter = new MemeDataAdapter();
    recyclerView.setAdapter(adapter);

    //disposable = relay.subscribe(new Consumer<MemeRealtimeData>() {
    //  @Override public void accept(MemeRealtimeData data) throws Exception {
    //    latestEntry = data;
    //  }
    //});

    //rxDisposable = Observable.interval(500, TimeUnit.MILLISECONDS)
    //    .subscribeOn(AndroidSchedulers.mainThread())
    //    .subscribe(new Consumer<Long>() {
    //      @Override public void accept(Long aLong) throws Exception {
    //        if (calibGyroData == null) {
    //          return;
    //        }
    //
    //        if (dataWindow == null) {
    //          dataWindow = new DataWindow(calibGyroData);
    //        }
    //
    //        if (hasNewEntry && latestEntry != null) {
    //          dataWindow.update(latestEntry);
    //          adapter.addEntry(dataWindow);
    //          recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
    //
    //          hasNewEntry = false;
    //          // renew
    //          dataWindow = new DataWindow(calibGyroData);
    //        }
    //      }
    //    });
  }

  boolean hasNewEntry;
  GyroData calibGyroData;
  MemeActionFilter actionFilter;
  private final GyroCalibrator calibrator = new GyroCalibrator();

  @Override public void memeRealtimeCallback(MemeRealtimeData data) {
    // First 10 seconds will be used to calibrate the Gyro position.
    if (calibrator.getStartTime() == null) {
      calibrator.setStartTime(SystemClock.elapsedRealtime());
    }

    if (calibrator.getStartTime() + 10 * 1000 > SystemClock.elapsedRealtime()) {
      synchronized (calibrator) {
        calibrator.add(new GyroData(data.getPitch(), data.getRoll(), data.getYaw()));
      }

      return;
    }

    if (calibGyroData == null) {
      calibGyroData = calibrator.getCalibrated();
    }

    if (actionFilter == null) {
      actionFilter = new MemeActionFilter(calibGyroData);
      actionFilter.addOnEyeActionListener(this);
      actionFilter.addOnHeadActionListener(this);
    }

    actionFilter.onNewData(data);

    latestEntry = data;
    hasNewEntry = true;
  }

  @Override public void onEyeAction(Command action) {
    if (action.getAction() != Action.IDLE) {
      Log.d(TAG, "onEyeAction() called with: action = [" + action + "]");
      adapter.addEntry(action);
      recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
    }
  }

  @Override public void onHeadAction(Command action) {
    if (action.getAction() != Action.IDLE) {
      Log.d(TAG, "onHeadAction() called with: action = [" + action + "]");
      adapter.addEntry(action);
      recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
    }
  }

  private static class GyroCalibrator extends ArrayList<GyroData> {

    private Long startTime;

    float avrPitch = 0;
    float avrRoll = 0;
    float avrYaw = 0;

    GyroCalibrator() {
    }

    public Long getStartTime() {
      return startTime;
    }

    public void setStartTime(Long startTime) {
      this.startTime = startTime;
    }

    @Override public boolean add(GyroData gyroData) {
      avrPitch += gyroData.getPitch();
      avrRoll += gyroData.getRoll();
      avrYaw += gyroData.getYaw();
      return super.add(gyroData);
    }

    GyroData getCalibrated() {
      int size = this.size();
      return new GyroData(avrPitch / (float) size, avrRoll / (float) size, avrYaw / (float) size);
    }
  }
}
