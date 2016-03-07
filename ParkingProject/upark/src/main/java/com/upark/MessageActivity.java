package com.upark;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.upark.db.DBHelper;
import com.upark.model.LoggedInUsers;

@SuppressWarnings("deprecation")
public class MessageActivity extends ActionBarActivity {


	ListView lstView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_messages);
		//getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		//getSupportActionBar().setSubtitle(getString(R.string.messages));
		lstView = (ListView) findViewById(R.id.lstMessages);

		DBHelper dbHelper = new DBHelper(this);
		dbHelper.openDataBase();
		String UserId = dbHelper.getUserId(LoggedInUsers.getUserName());
		lstView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dbHelper.getMessages(UserId)));
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.homeAsUp) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}