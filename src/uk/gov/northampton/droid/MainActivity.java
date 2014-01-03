package uk.gov.northampton.droid;

import uk.gov.northampton.droid.fragments.*;
import uk.gov.northampton.droid.lib.PostCodeDialogFragment.PostCodeDialogListener;
import uk.gov.northampton.droid.lib.SocialWebViewActivity;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.SyncStateContract.Constants;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Window;

import com.actionbarsherlock.view.*;
import com.google.android.gms.auth.GoogleAuthUtil;

public class MainActivity extends SherlockFragmentActivity implements ActionBar.TabListener, PostCodeDialogListener {

    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    SharedPreferences sharedPrefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportProgressBarIndeterminateVisibility(false);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setHorizontalFadingEdgeEnabled(true);
        mViewPager.setFadingEdgeLength(25);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            actionBar.addTab(actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
        
        getAccountDetails();
        
        
        
        try { 
        	// handle the image sharing intent from android os
	        Intent intent = getIntent();
	        if(intent.getType().indexOf("image/*") != -1) {
	        	// if an image launched the app, set the current tab to report it
	        	mViewPager.setCurrentItem(1, true);
	        	
	        	/*
	        	 *  TODO send the photo path data to the fragment 
	        	 *  so that the image can be prepopulated
	        	 */
	        	
	        }
        } catch(NullPointerException e) {
        	e.printStackTrace();
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu m){
		MenuInflater inflater = getSupportMenuInflater();
    	inflater.inflate(R.menu.overflow, m);
    	return true;
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
    	Intent intent = new Intent();
    	if(item.getItemId() == R.id.contactUs){
    		intent.setClass(this, Contact.class);
    		startActivity(intent);
    	}else if(item.getItemId() == R.id.settings){
    		intent.setClass(this, Settings.class);
    		startActivity(intent);
    	}
		return super.onOptionsItemSelected(item);
	}
	
	/*
	 * Get the users google account to save them 
	 * putting in an email address
	 */
	public void getAccountDetails() {
		
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		String email = sharedPrefs.getString(Settings.NBC_EMAIL, null);
		
		if(email == null) {
			AccountManager mAccountManager = AccountManager.get(this);
		    Account[] accounts = mAccountManager.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
		    
		    if (accounts.length > 0) {
		        email = accounts[0].name;
		        
		        if(email.length() > 0) {
			    	Editor editor = sharedPrefs.edit();
				    editor.putString(Settings.NBC_EMAIL, email);
				    editor.commit();
			    }		        
		    }
		}
	}



	public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
        	switch (i){
        	case 0: return new SocialWebViewActivity();
        	case 1: return new Report();
        	case 2: return new Find();
        	}
        	return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
            	case 0: return getString(R.string.tab_social);
                case 1: return getString(R.string.tab_report);
                case 2: return getString(R.string.tab_find);
            }
            return null;
        }
    }

	@Override
	public void onTabSelected(Tab tab,
			android.support.v4.app.FragmentTransaction ft) {
        mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab,
			android.support.v4.app.FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabReselected(Tab tab,
			android.support.v4.app.FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}	
	
	@Override
	public void onFinishPostCodeDialog(String postCode) {
		if(postCode != null){
			sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			Editor editor = sharedPrefs.edit();
			editor.putString(Settings.NBC_POST_CODE, postCode);
			editor.commit();
		}
		Log.i("FIND IT","New Post Code: " + postCode);
	}
	
}
