package org.georemindme.view;

import org.georemindme.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class mainActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Delete title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        requestWindowFeature(Window.FEATURE_PROGRESS);
        
        setContentView(R.layout.main);
        
        Button b = (Button) findViewById(R.id.boton);
        b.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), captureQRCodeActivity.class);
				i.putExtra("pista", 1);
				startActivity(i);
				
			}
		});
    }
}