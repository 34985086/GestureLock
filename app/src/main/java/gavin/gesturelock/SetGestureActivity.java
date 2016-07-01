package gavin.gesturelock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import gavin.gesturelock.lockview.GestureView;
import gavin.gesturelock.lockview.GestureView.OnPasswordListener;

public class SetGestureActivity extends Activity
		implements OnPasswordListener {

	private static final String TAG = "SetGestureActivity";

	private TextView mTv;
	private GestureView mGestureView;
	private Button mBtnReset;

	private String mPassword;
	private boolean mIsFirst = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_gesture);

		mTv = (TextView) findViewById(R.id.textView);
		mGestureView = (GestureView) findViewById(R.id.lockView);
		mGestureView.setChangeListener(this);

		mBtnReset = (Button) findViewById(R.id.btnReset);
		mBtnReset.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mPassword = "";
				mIsFirst = true;
				mGestureView.reset();
				mBtnReset.setVisibility(View.GONE);
				mTv.setText(R.string.LockInputPassword);
			}
		});
	}

	@Override
	public boolean onPasswordChanged(String password) {
		Log.d(TAG, "onPasswordChanged() called with: "
				+ "password = [" + password + "]");
		if (mIsFirst) {
			mIsFirst = false;
			mPassword = password;
			mGestureView.reset();
			mBtnReset.setVisibility(View.VISIBLE);
			mTv.setText(R.string.LockInputPasswordAgain);
			return true;
		} else {
			if (password.equals(mPassword)) {
				Preferences.setPassword(SetGestureActivity.this, password);

				startActivity(new Intent(SetGestureActivity.this,
						MainActivity.class));

				SetGestureActivity.this.finish();
				return true;
			}else {
				mIsFirst = true;
				mPassword = "";
				mTv.setText(R.string.LockPasswordError);
				mBtnReset.setVisibility(View.GONE);

				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						mGestureView.reset();
						mTv.setText(R.string.LockInputPassword);
					}
				}, 1000);
				return false;
			}
		}
	}
}
