package com.example.tag4reeltest;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.loginwithswipe.R;

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
    
    //JSON names for each JSON object.
    private static final String TAG_CATCHES = "catches";
    private static final String TAG_ANGLER = "angler";
    private static final String TAG_FISH = "fish";
    private static final String TAG_SUCCESS = "success";

	public CatchesFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.catch_near_you, container, false);
		
		new LoadAllProducts().execute();
		
		return rootView;
	}
	
    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class LoadAllProducts extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CatchesFragment.this.getActivity());
            pDialog.setMessage("Loading products. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
 
        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
        	try {
				Thread.sleep(2000);
				List<HashMap<String,Object>> fillMaps = new ArrayList<HashMap<String,Object>>();
				
				
			    // Create the item mapping
			    String[] from = new String[] { "title", "description" };
			    int[] to = new int[] { R.id.title, R.id.description };
				
			    HashMap<String, Object> map = new HashMap<String, Object>();
			    map.put("title", "First title"); // This will be shown in R.id.title
			    map.put("description", "description 1"); // And this in R.id.description
			    fillMaps.add(map);

			    map = new HashMap<String, Object>();
			    map.put("title", "Second title");
			    map.put("description", "description 2");
			    fillMaps.add(map);
			    
			    try {
					JSONObject jObj = new JSONObject( StubSupplier.queryForCatchesJSON() );
					
					JSONArray jArray = jObj.getJSONArray(TAG_CATCHES);
					for(int i = 0;i<jArray.length();i++)
					{
						JSONObject catchItem = jArray.getJSONObject(i);
						
						map = new HashMap<String,Object>();
						map.put("title", catchItem.getString(TAG_ANGLER));
						map.put("description",catchItem.getString(TAG_FISH));
						fillMaps.add(map);
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    
			    

			    sAdapter = new SimpleAdapter(CatchesFragment.this.getActivity(), fillMaps, R.layout.catch_list_item, from, to);
			} catch (InterruptedException e) {
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
