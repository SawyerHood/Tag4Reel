package com.example.tag4reeltest;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.tag4reeltest.R;

public class CatchesFragment extends Fragment {
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	private static final String ARG_SECTION_NUMBER = "section_number";

	//Holds the data needed for each individual list item.
	SimpleAdapter sAdapter;
	
    // Progress Dialog
    private ProgressDialog pDialog;
    
    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();
    
    //The listview that holds all the catch data.
    ListView lv;
    
    //JSON names for each JSON object.
    private static final String TAG_CATCHES = "catches";
    private static final String TAG_ANGLER = "angler";
    private static final String TAG_FISH = "fish";
    private static final String TAG_SUCCESS = "success";

    JSONParser jsonParser = new JSONParser();
    
	public CatchesFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.catch_near_you, container, false);
		
		lv = (ListView) rootView.findViewById(R.id.listView1);
		
		new CatchLoader().execute();
		setupListView();
		
		return rootView;
	}
	
	//Add click functionality to the list items for the catch.
	private void setupListView()
	{
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				
				Intent in = new Intent(CatchesFragment.this.getActivity(), EditActivity.class);
				Log.d("cid" , ((TextView) view.findViewById(R.id.description)).getText().toString());
				in.putExtra("catch_id",((TextView) view.findViewById(R.id.catch_id)).getText().toString());
				startActivity(in);
			}
			
		});
	}
	
    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class CatchLoader extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CatchesFragment.this.getActivity());
            pDialog.setMessage("Loading catches. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
 
        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
        	try {
				List<HashMap<String,Object>> fillMaps = new ArrayList<HashMap<String,Object>>();
				
				
			    // Create the item mapping
			    String[] from = new String[] { "title", "description", "catch_id" };
			    int[] to = new int[] { R.id.title, R.id.description, R.id.catch_id };
				
			    HashMap<String, Object> map;// = new HashMap<String, Object>();
//			    map.put("title", "First title"); // This will be shown in R.id.title
//			    map.put("description", "description 1"); // And this in R.id.description
//			    fillMaps.add(map);
//
//			    map = new HashMap<String, Object>();
//			    map.put("title", "Second title");
//			    map.put("description", "description 2");
//			    fillMaps.add(map);
			    
			    try {
			    	
		            // Building Parameters
		            List<NameValuePair> params = new ArrayList<NameValuePair>();
		 
		            // getting JSON Object
		            // Check to see if there is already a fish with the given tag.
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
		            //Log.d("Create Response", "check if tag exists response: " + json.toString());
			    	
					//JSONObject jObj = new JSONObject( StubSupplier.queryForCatchesJSON() );
					
					//JSONArray jArray = jObj.getJSONArray(TAG_CATCHES);
					for(int i = 0;i<jArr.length();i++)
					{
						JSONObject catchItem = jArr.getJSONObject(i);
						
						map = new HashMap<String,Object>();
						map.put("title", Integer.toString(catchItem.getInt("angler_id")));
						map.put("description",Integer.toString(catchItem.getInt("tag_id")));
						map.put("catch_id",Integer.toString(catchItem.getInt("catch_id")));
						fillMaps.add(map);
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    
			    

			    sAdapter = new SimpleAdapter(CatchesFragment.this.getActivity(), fillMaps, R.layout.catch_list_item, from, to);
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
            CatchesFragment.this.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
            		ListView listView = (ListView) CatchesFragment.this.getActivity().findViewById(R.id.listView1);
            		listView.setAdapter(sAdapter);
                }
            });
 
        }
 
    }
	
	

}
