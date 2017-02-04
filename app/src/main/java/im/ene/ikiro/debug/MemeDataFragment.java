package im.ene.ikiro.debug;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import im.ene.ikiro.sdk.DataWindow;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import java.util.concurrent.TimeUnit;

/**
 * Created by eneim on 2/4/17.
 */

public class MemeDataFragment extends Fragment implements MemeRealtimeListener {

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

    //rxDisposable = Observable.interval(1000, TimeUnit.MILLISECONDS)
    //    .subscribeOn(AndroidSchedulers.mainThread())
    //    .subscribe(new Consumer<Long>() {
    //      @Override public void accept(Long aLong) throws Exception {
    //        if (dataWindow == null) {
    //          dataWindow = new DataWindow(null);
    //        }
    //
    //        if (latestEntry != null) {
    //          dataWindow.setEndData(latestEntry);
    //          adapter.addEntry(dataWindow);
    //          recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
    //
    //          // renew
    //          dataWindow = new DataWindow(latestEntry);
    //        }
    //      }
    //    });

    //relay.subscribe(new Consumer<MemeRealtimeData>() {
    //  @Override public void accept(MemeRealtimeData data) throws Exception {
    //    hasBlinkWindow = new DataWindow(null);
    //    hasBlinkWindow.setEndData(data);
    //    adapter.addEntry(hasBlinkWindow);
    //    recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
    //  }
    //});

    Flowable.interval(500, TimeUnit.MILLISECONDS)
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<Long>() {
          @Override public void accept(Long aLong) throws Exception {
            if (hasBlinkEntry != null) {
              hasBlinkWindow = new DataWindow(null);
              hasBlinkWindow.setEndData(hasBlinkEntry);
              adapter.addEntry(hasBlinkWindow);
              recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
              hasBlinkEntry = null;
            }
          }
        });
  }

  @Override public void memeRealtimeCallback(MemeRealtimeData data) {
    latestEntry = data;
    // relay.accept(latestEntry);
    if (data.getBlinkSpeed() > 0 || data.getEyeMoveLeft() > 0 || data.getEyeMoveRight() > 0) {
      // relay.accept(data);
      hasBlinkEntry = data;
    }
  }
}
