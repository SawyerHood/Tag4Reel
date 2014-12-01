package com.example.tag4reeltest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class EditActivity extends Activity {
	
	TextView anglerNameEdit;
	TextView fishNameEdit;
	EditText sizeEdit;
	
	Button updateButton;
	private String catchId;
	
	// Progress Dialog
    private ProgressDialog pDialog;
    
    JSONParser jsonParser = new JSONParser();
    
    private String sizes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.catch_edit);
		
		this.anglerNameEdit = (TextView) this.findViewById(R.id.anglerNameEdit);
		this.fishNameEdit = (TextView) this.findViewById(R.id.fishNameEdit);
		this.sizeEdit = (EditText) this.findViewById(R.id.sizeEdit2);
		this.updateButton = (Button) this.findViewById(R.id.updateButton);
		
		Intent inIntent = getIntent();
		
		catchId = inIntent.getStringExtra("catch_id");
		new CatchLoader().execute("");
		
		updateButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sizes = sizeEdit.getText().toString();
				new CatchUpdater().execute("");
			}
			
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit, menu);
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
	
	
    class CatchLoader extends AsyncTask<String, String, String> {
    	 
    	private String anglerName;
    	private String fishName;
    	private String size;
    	
    	
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditActivity.this);
            pDialog.setMessage("Loading catch. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
 
        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
        	try {

			    try {
			    	
		            // Building Parameters
		            List<NameValuePair> params = new ArrayList<NameValuePair>();
		            params.add(new BasicNameValuePair("catch_id", catchId));
		 
		            // get the catch with the specified catch_id
		            JSONObject json = jsonParser.makeHttpJSONArrayRequest(InsertActivity.catch_URL,
		                    "GET", params);
		            
		            JSONArray jArr = null;
		            try {
						jArr = json.getJSONArray("arr");
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		 
		            // check log cat for response
		            //Log.d("get Response", "get catch response: " + json.toString());
			    	
					//JSONObject jObj = new JSONObject( StubSupplier.queryForCatchesJSON() );
					
					//JSONArray jArray = jObj.getJSONArray(TAG_CATCHES);
					for(int i = 0;i<jArr.length();i++)
					{
						JSONObject catchItem = jArr.getJSONObject(i);
						
						anglerName = Integer.toString(catchItem.getInt("angler_id"));
						fishName = Integer.toString(catchItem.getInt("tag_id"));
						size = Integer.toString(catchItem.getInt("size"));
						

					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
 
            return null;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            EditActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
					anglerNameEdit.setText(anglerName);
					fishNameEdit.setText(fishName);
					sizeEdit.setText(size);
                }
            });
 
        }
 
    }
    
    class CatchUpdater extends AsyncTask<String, String, String> {
   	 
    	private String anglerName;
    	private String fishName;
    	private String size;
    	
    	
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditActivity.this);
            pDialog.setMessage("Loading catch. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
 
        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("catch_id", catchId));
            params.add(new BasicNameValuePair("size", sizes));
 
            // get the catch with the specified catch_id
            JSONObject json = jsonParser.makeHttpJSONArrayRequest(InsertActivity.catch_URL,
                    "POST", params);
            



            return null;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
 
        }
 
    }
	
}
