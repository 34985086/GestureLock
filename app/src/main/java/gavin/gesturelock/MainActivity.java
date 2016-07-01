package gavin.gesturelock;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class MainActivity extends Activity {

	private int mCountBackKey = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public void onBackPressed() {
		if (mCountBackKey == 0) {
			mCountBackKey = 1;
			Toast.makeText(this, R.string.appExit, Toast.LENGTH_SHORT).show();

			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					mCountBackKey = 0;
				}
			}, 2000);
			return;
		} else {
			MainActivity.this.finish();
		}

		super.onBackPressed();
	}
}
