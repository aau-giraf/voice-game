package dk.aau.cs.giraf.cars;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import dk.aau.cs.giraf.oasis.lib.Helper;
import dk.aau.cs.giraf.oasis.lib.models.Profile;


public class StartupActivity extends Activity {
	private long guardianId;
	public static Profile guardian;
	private long childId;
	public static Profile child;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_startup);
		Helper helper = new Helper(this);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {        	   
			try{
				guardianId = extras.getLong("currentGuardianID");
				guardian = helper.profilesHelper.getProfileById(guardianId);

				TextView textViewG = (TextView) findViewById(R.id.textView1);
				textViewG.setText("Guardian: " + guardian.getFirstname() + " " + guardian.getSurname());
			}
			catch(NullPointerException e) {

			}
			finally{
				childId = extras.getLong("currentChildID");
				child = helper.profilesHelper.getProfileById(childId);
				TextView textViewC = (TextView) findViewById(R.id.textView2);
				textViewC.setText("Child: " + child.getFirstname() + " " + child.getSurname());
			}
		}
		Settings.load(childId, this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.startup, menu);
		return true;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	public void showGameView(View view) {
		Intent intent = new Intent(StartupActivity.this, GameActivity.class);
		StartupActivity.this.startActivity(intent);
	}

	public void showSettingsView(View view) {
		Intent intent = new Intent(StartupActivity.this, SettingsActivity.class);
		intent.putExtra("currentGuardianID", guardianId);
		intent.putExtra("currentChildId", childId);
		StartupActivity.this.startActivity(intent);
	}

	public void showHelpView(View view) {
		Intent intent = new Intent(StartupActivity.this, HelpActivity.class);
		StartupActivity.this.startActivity(intent);
	}
}
