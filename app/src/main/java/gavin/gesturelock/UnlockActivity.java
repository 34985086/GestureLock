package gavin.gesturelock;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.widget.TextView;

import gavin.gesturelock.lockview.GestureView;

public class UnlockActivity extends Activity
		implements GestureView.OnPasswordListener{

	private TextView mTv;
	private GestureView mGestureView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_unlock);

		mTv = (TextView)findViewById(R.id.textView);
		mGestureView = (GestureView) findViewById(R.id.lockView);
		mGestureView.setChangeListener(this);
	}

	@Override
	public boolean onPasswordChanged(String password) {
		if (Preferences.password(UnlockActivity.this).equals(password)) {

			mTv.setText(R.string.LockPasswordOk);

			Intent intent = new Intent(UnlockActivity.this, MainActivity.class);
			startActivity(intent);

			UnlockActivity.this.finish();

			return true;
		} else {
			mTv.setText(R.string.LockPasswordError);
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
