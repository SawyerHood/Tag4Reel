package com.example.tag4reeltest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class InsertActivity extends Activity {

	final static String catch_URL = "http://104.131.115.103:3000/api/catch";
	final static String fish_URL = "http://104.131.115.103:3000/api/fish";
	final static String species_URL = "http://104.131.115.103:3000/api/species";
	 
    // Progress Dialog
    private ProgressDialog pDialog;
    
    JSONParser jsonParser = new JSONParser();
    
    EditText fishNameET;
    EditText tagIDET;
    EditText sizeET;
    EditText baitUsedET;
    EditText riggingET;
    EditText weatherET;
	
    Button submitButton;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.catch_insert);
		
		fishNameET = (EditText) this.findViewById(R.id.fishNameET);
		tagIDET = (EditText) this.findViewById(R.id.tagIDET);
		sizeET = (EditText) this.findViewById(R.id.sizeET);
		baitUsedET = (EditText) this.findViewById(R.id.baitUsedET);
		riggingET = (EditText) this.findViewById(R.id.riggingET);
		weatherET = (EditText) this.findViewById(R.id.weatherET);
		
		submitButton = (Button) this.findViewById(R.id.insertButton);

		this.submitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new CatchInserter().execute("");
				
			}
			
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.insert, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void setupButtons()
	{
		this.submitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new CatchInserter().execute("");
				
			}
			
		});
		
	}
	
	
    /**
     * Background Async Task to insert a catch.
     * */
    class CatchInserter extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(InsertActivity.this);
            pDialog.setMessage("Inserting catch");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
 
        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {
            String fishName = fishNameET.getText().toString();
            String tagID = tagIDET.getText().toString();
            String size = sizeET.getText().toString();
            String baitUsed = baitUsedET.getText().toString();
            String riggingUsed = riggingET.getText().toString();
            String weather = weatherET.getText().toString();
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            String dateCatch = (dateFormat.format(date));
 
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("tag_id", tagID));
 
            // getting JSON Object
            // Check to see if there is already a fish with the given tag.
            JSONObject json = jsonParser.makeHttpJSONArrayRequest(fish_URL,
                    "GET", params);
            
            JSONArray jArr = null;
            try {
				jArr = json.getJSONArray("arr");
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
 
            // check log cat for response
            Log.d("Create Response", "check if tag exists response: " + json.toString());
 
            if(jArr.length() == 0)
            {
            	params.clear();
            	params.add(new BasicNameValuePair("name", fishName));
            	
            	//Get the species ID for the new fish.
                json = jsonParser.makeHttpJSONArrayRequest(species_URL, "GET", params);
                
                // check log cat for response
                Log.d("Create Response", "get species id response: " + json.toString());
                
                try {
    				jArr = json.getJSONArray("arr");
    				json = jArr.getJSONObject(0);
    			} catch (JSONException e1) {
    				// TODO Auto-generated catch block
    				e1.printStackTrace();
    			}
            	
                String speciesId = null;
            	try {
					speciesId = Integer.toString(json.getInt("species_id"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	
            	params.clear();
            	params.add(new BasicNameValuePair("tag_id", tagID));
            	params.add(new BasicNameValuePair("species_id", speciesId));
            	
            	//Create a new fish entry for the previously untagged fish.
                json = jsonParser.makeHttpRequest(fish_URL, "POST", params);
             
                // check log cat for response
                Log.d("Create Response", "insert new fish response: " + json.toString());
            	
            }
            
            // Building Parameters
            params.clear();
        	params.add(new BasicNameValuePair("angler_id", "3"));
        	params.add(new BasicNameValuePair("tag_id", tagID));
        	params.add(new BasicNameValuePair("weather", weather));
        	params.add(new BasicNameValuePair("date_catch", dateCatch));
        	params.add(new BasicNameValuePair("longitude", "68"));
        	params.add(new BasicNameValuePair("latitude", "92"));
        	params.add(new BasicNameValuePair("bait_used", baitUsed));
        	params.add(new BasicNameValuePair("rigging", riggingUsed));
        	params.add(new BasicNameValuePair("name_location", "Orlando"));
        	params.add(new BasicNameValuePair("size", size));
 
            //Create a catch entry for the angler's fish.
            json = jsonParser.makeHttpRequest(catch_URL, "POST", params);
 
            // check log cat for response
            Log.d("Create Response", "insert catch response: " + json.toString());
 
            return null;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }
 
    }
	
}
