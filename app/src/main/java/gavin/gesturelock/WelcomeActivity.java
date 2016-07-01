package gavin.gesturelock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class WelcomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);

		/*delay to change the activity*/
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent intent;
				if (Preferences.password(WelcomeActivity.this).isEmpty()) {
					intent = new Intent(WelcomeActivity.this, SetGestureActivity.class);
				} else {
					intent = new Intent(WelcomeActivity.this, UnlockActivity.class);
				}
				startActivity(intent);
				WelcomeActivity.this.finish();
			}
		}, 2000);
	}
}
