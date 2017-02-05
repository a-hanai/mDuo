package im.ene.ikiro.game;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jins_jp.meme.MemeConnectListener;
import com.jins_jp.meme.MemeLib;
import com.jins_jp.meme.MemeRealtimeData;
import com.jins_jp.meme.MemeRealtimeListener;
import com.jins_jp.meme.MemeResponse;
import com.jins_jp.meme.MemeResponseListener;
import com.jins_jp.meme.MemeScanListener;
import com.jins_jp.meme.MemeStatus;
import im.ene.ikiro.BaseActivity;
import im.ene.ikiro.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by eneim on 2/5/17.
 */

public class GameBoardActivity extends BaseActivity
    implements MemeRealtimeListener, MemeResponseListener, MemeLogDialog.Callback,
    TttGameFragment.Callback {

  private static final String TAG = "Duo:Game";

  TttGameFragment gameFragment;
  ChatFragment chatFragment;
  MemeLogDialog memeLogDialog;

  DatabaseReference fbDbRef;

  String myUserId;
  Boolean mySide; // True or fall
  String gameId;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_board);
    maybeRequestLocationPermission();

    // first state: all "blank"
    for (int i = 0; i < 9; i++) {
      boardState.add(cellBlank);
    }

    //// Write a message to the database
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    fbDbRef = database.getReference("games");
    fbDbRef.addChildEventListener(this.childEventListener);
    fbDbRef.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override public void onDataChange(DataSnapshot dataSnapshot) {
        // Not here by time
        Iterable<DataSnapshot> snapshots = dataSnapshot.getChildren();
        for (DataSnapshot snapshot : snapshots) {
          if (Boolean.TRUE.equals(snapshot.getValue(Boolean.parseBoolean("active")))) {
            latestGameSnapShot = snapshot;
            break;
          }
        }

        if (latestGameSnapShot != null) {
          latestGameSnapShot.getRef().addValueEventListener(gameEventListener);
          //noinspection unchecked
          users = (ArrayList<String>) latestGameSnapShot.child("users").getValue();
          //noinspection unchecked
          ArrayList state = (ArrayList) latestGameSnapShot.child("tic_tac_toe").getValue();
          if (state != null) {
            //noinspection unchecked
            boardState = state;
          }

          // Init Game UI
          // initGameUI();
        }
      }

      @Override public void onCancelled(DatabaseError databaseError) {

      }
    });
  }

  MemeLib memeLib = MemeLib.getInstance();
  AlertDialog scanningDialog;

  @Override protected void onStart() {
    super.onStart();
    // Start scanning
    scanningDialog = new AlertDialog.Builder(this).setTitle("Start scanning for Memes")
        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
          @Override public void onClick(DialogInterface dialog, int which) {
            startScan(null);
            dialog.dismiss();
          }
        })
        .setOnDismissListener(new DialogInterface.OnDismissListener() {
          @Override public void onDismiss(DialogInterface dialog) {
            if (memeLogDialog == null) {
              memeLogDialog = MemeLogDialog.newInstance();
              memeLogDialog.show(getSupportFragmentManager(), MemeLogDialog.class.getSimpleName());
            }
          }
        })
        .create();

    scanningDialog.show();
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
    MemeStatus status = memeLib.startScan(new MemeScanListener() {
      @Override public void memeFoundCallback(final String s) {
        memeLogDialog.addMemeId(s);
        Log.d("SCAN", "found: " + s);
      }
    });

    Log.i(TAG, "startScan: " + status);
  }

  @Override public void memeRealtimeCallback(MemeRealtimeData memeRealtimeData) {

  }

  @Override public void memeResponseCallback(MemeResponse memeResponse) {
    Log.d(TAG, "memeResponseCallback() called with: memeResponse = [" + memeResponse + "]");
  }

  DataSnapshot latestGameSnapShot;
  ArrayList<String> users;
  List<String> boardState = new ArrayList<>();

  // Event for latestGameSnapShot
  ValueEventListener gameEventListener = new ValueEventListener() {
    @Override public void onDataChange(DataSnapshot dataSnapshot) {
      Log.d(TAG, "onDataChange() called with: dataSnapshot = [" + dataSnapshot + "]");
      if (gameFragment != null) {
        //noinspection unchecked
        ArrayList state = (ArrayList) dataSnapshot.child("tic_tac_toe").getValue();
        if (state != null) {
          //noinspection unchecked
          boardState = state;

          List<Boolean> booleanList = new ArrayList<>();
          for (int i = 0; i < state.size(); i++) {
            booleanList.add(state.get(i).equals(cellBlank) ? null
                : myUserId.equals(boardState.get(i)) ? mySide : !mySide);
          }

          gameFragment.updateStates(booleanList);
        }
      }
    }

    @Override public void onCancelled(DatabaseError databaseError) {

    }
  };

  private ChildEventListener childEventListener = new ChildEventListener() {
    @Override public void onChildAdded(DataSnapshot dataSnapshot, String s) {
      Log.d(TAG,
          "onChildAdded() called with: dataSnapshot = [" + dataSnapshot + "], s = [" + s + "]");
      if (dataSnapshot.getRef().getParent().getKey().equals("games") && Boolean.TRUE.equals(
          dataSnapshot.child("active").getValue()) && latestGameSnapShot == null) {
        latestGameSnapShot = dataSnapshot;
        latestGameSnapShot.getRef().addValueEventListener(gameEventListener);
        //noinspection unchecked
        users = (ArrayList<String>) dataSnapshot.child("users").getValue();
      }
    }

    @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override public void onCancelled(DatabaseError databaseError) {

    }
  };

  @Override public void onClickToMemeId(View view, final String memeId) {
    if (memeLogDialog != null) {
      memeLogDialog.dismiss();
    }

    if (memeLib.isScanning()) {
      memeLib.stopScan();
      Log.d("SCAN", "scan stopped.");
    }

    MemeConnectedHandler connectedHandler = new MemeConnectedHandler(memeId) {
      @Override void onConnectToMeme(String id, boolean b) {
        myUserId = id;
        // New game with this ID;
        if (latestGameSnapShot == null) {
          DatabaseReference newGame = fbDbRef.push();
          Map<String, Object> data = new HashMap<>();
          List<String> users = new ArrayList<>();
          users.add(myUserId);
          mySide = Boolean.TRUE;
          data.put("users", users);
          data.put("active", Boolean.TRUE);
          newGame.setValue(data, new DatabaseReference.CompletionListener() {
            @Override public void onComplete(DatabaseError databaseError,
                DatabaseReference databaseReference) {
            }
          });
        } else {
          if (users == null) {
            //noinspection unchecked
            users = (ArrayList<String>) latestGameSnapShot.child("users").getValue();
          }

          mySide = users.size() == 0 ? Boolean.TRUE : Boolean.FALSE;
          if (!users.contains(myUserId)) {
            users.add(myUserId);
          }

          latestGameSnapShot.getRef()
              .child("users")
              .setValue(users, new DatabaseReference.CompletionListener() {
                @Override public void onComplete(DatabaseError databaseError,
                    DatabaseReference databaseReference) {
                }
              });
        }

        initGameUI();
      }
    };

    memeLib.setMemeConnectListener(connectedHandler);
    memeLib.connect(memeId);
  }

  String cellBlank = "blank";

  @Override public void onGameStateChanged(Boolean[] gameState, int changedPosition) {
    String key = "tic_tac_toe";
    if (latestGameSnapShot != null) {
      //noinspection unchecked
      // ArrayList state = (ArrayList) latestGameSnapShot.child(key).getValue();
      boardState.set(changedPosition, myUserId);
      //if (state == null) {
      //  state = new ArrayList<>();
      //  for (int i = 0; i < gameState.length; i++) {
      //    //noinspection unchecked
      //    state.add(cellBlank);
      //  }
      //}

      //noinspection unchecked
      //state.set(changedPosition, myUserId);
      latestGameSnapShot.getRef()
          .child(key)
          .setValue(boardState, new DatabaseReference.CompletionListener() {
            @Override public void onComplete(DatabaseError databaseError,
                DatabaseReference databaseReference) {
              Log.d(TAG, "onComplete() called with: databaseError = ["
                  + databaseError
                  + "], databaseReference = ["
                  + databaseReference
                  + "]");
            }
          });
    }
  }

  void initGameUI() {
    gameFragment = (TttGameFragment) getSupportFragmentManager().findFragmentById(R.id.game_board);
    if (gameFragment == null) {
      gameFragment = TttGameFragment.newInstance(mySide);
      getSupportFragmentManager().beginTransaction()
          .replace(R.id.game_board, gameFragment)
          .commit();
    }
  }

  private static abstract class MemeConnectedHandler implements MemeConnectListener {

    private final String memeId;

    abstract void onConnectToMeme(String id, boolean b);

    MemeConnectedHandler(String memeId) {
      this.memeId = memeId;
    }

    @Override public final void memeConnectCallback(boolean b) {
      Log.d(TAG, "memeConnectCallback() called with: b = [" + b + "]");
      onConnectToMeme(this.memeId, b);
    }

    @Override public void memeDisconnectCallback() {

    }
  }
}
