package gavin.gesturelock;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by gavin on 2016-07-01.
 */
public class Preferences {
	public static String password(Context ctx) {
		SharedPreferences sp = ctx.getSharedPreferences("gesture",
				Context.MODE_PRIVATE);
		return sp.getString("gesture", "");
	}

	public static void setPassword(Context ctx, String password) {
		SharedPreferences sp = ctx.getSharedPreferences("gesture",
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("gesture", password);
		editor.commit();
	}
}
