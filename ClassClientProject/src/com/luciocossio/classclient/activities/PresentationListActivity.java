package com.luciocossio.classclient.activities;

import android.os.Bundle;

public abstract class PresentationListActivity extends BaseClientActivity {

	protected String lastFilename;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView();		
		lastFilename = "presentation.pptx";		
		setListListener();		
		this.updateList();
	}

	public PresentationListActivity() {
		super();
	}

	protected abstract void setContentView();

	protected abstract void setListListener();

	public abstract void updateList();

}