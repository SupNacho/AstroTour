package ru.supernacho.at;

import android.os.Bundle;
import android.util.Pair;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new AstroTour(), config);
		CheckPermission.externalStorage(this);
	}

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Pair<Boolean, Boolean> result = CheckPermission.onRequestRWResult(requestCode, permissions, grantResults);
        if (result.first) Toast.makeText(this, "Read granted", Toast.LENGTH_LONG).show();
        if (result.second) Toast.makeText(this, "Write granted", Toast.LENGTH_SHORT).show();
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
