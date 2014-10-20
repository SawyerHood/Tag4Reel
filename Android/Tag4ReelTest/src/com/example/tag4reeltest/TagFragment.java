package com.example.tag4reeltest;

import com.example.loginwithswipe.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class TagFragment extends Fragment {
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	private static final String ARG_SECTION_NUMBER = "section_number";

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static TagFragment newInstance(int sectionNumber) {
		TagFragment fragment = new TagFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public TagFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_main, container, false);
		
		//Set's up the listeners for the buttons in TagFragment view.
		setupButtons(rootView);
		
		
		
		return rootView;
	}
	

	
	//This method adds the event listeners for the buttons present in the fragments layout.
	private void setupButtons(View v)
	{
		
		Button loginButton = (Button) v.findViewById(R.id.loginbutton);
		final Context context = this.getActivity();
		
		loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(TagFragment.this.getActivity(),LoginActivity.class);
				startActivity(intent);
			}
			
		});
	}
}
