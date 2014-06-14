package com.bobamason.database1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.util.*;
import android.support.v7.app.*;
import android.content.*;
import android.preference.*;

public class MainActivity extends ActionBarActivity implements
		EntryListFragment.OnListItemSelected,
		EditEntryFragment.OnEditEntryListener,
		InstructionsFragment.OnInstructionsFinished {

	private FragmentManager fragmentManager;

	private DataBaseAdapter dbAdapter;

	private SearchView mSearchView;

	private static final String LIST_TAG = "list";

	private static final String ADD_TAG = "add";

	private static final String EDIT_TAG = "edit";

	private static final String SEARCH_TAG = "search";

	private static final String TOTAL_TAG = "total";

	private ActionBar actionBar;

	private SharedPreferences prefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		fragmentManager = getSupportFragmentManager();
		dbAdapter = new DataBaseAdapter(this);

		actionBar = getSupportActionBar();
		prefs = PreferenceManager.getDefaultSharedPreferences(this);

		FragmentTransaction transaction = fragmentManager.beginTransaction();
		if (prefs.getBoolean("firstTime", true)) {
			actionBar.hide();
			InstructionsFragment instFrag = new InstructionsFragment();
			transaction.add(R.id.fragment_holder, instFrag);
			transaction.commit();
		} else {
			EntryListFragment listFrag = new EntryListFragment();
			transaction.add(R.id.fragment_holder, listFrag, LIST_TAG);
			transaction.commit();
		}
	}

	@Override
	public void instructionsFinished() {
		actionBar.show();
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean("firstTime", false);
		editor.apply();
		EntryListFragment listFrag = new EntryListFragment();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.replace(R.id.fragment_holder, listFrag, LIST_TAG);
		transaction.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.xml.menu, menu);
		MenuItem searchItem = menu.findItem(R.id.menu_search);
		mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
		mSearchView
				.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

					@Override
					public boolean onQueryTextSubmit(String str) {
						EntryListFragment searchFrag = (EntryListFragment) fragmentManager
								.findFragmentByTag(SEARCH_TAG);
						if (searchFrag == null || !searchFrag.isVisible()) {
							searchFrag = new EntryListFragment();
							Bundle bundle = new Bundle();
							bundle.putString("searchString", str);
							searchFrag.setArguments(bundle);
							FragmentTransaction transaction = fragmentManager
									.beginTransaction();
							transaction.replace(R.id.fragment_holder,
									searchFrag, SEARCH_TAG);
							transaction.addToBackStack(null);
							transaction.commit();
						} else {
							searchFrag.updateSearchList(str);
						}
						return true;
					}

					@Override
					public boolean onQueryTextChange(String str) {
						return true;
					}

				});
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_search:
			return true;
		case R.id.menu_add:
			Fragment addFrag = fragmentManager.findFragmentByTag(ADD_TAG);
			if (addFrag == null || !addFrag.isVisible()) {
				FragmentTransaction transaction = fragmentManager
						.beginTransaction();
				transaction.replace(R.id.fragment_holder,
						new EditEntryFragment(), ADD_TAG);
				transaction.addToBackStack(null);
				transaction.commit();
			}
			return true;
		case R.id.menu_source:
			String url = "https://github.com/bobm3/database-app/tree/master/Database1";
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(url));
			startActivity(intent);
			return true;
		case R.id.menu_total:
			Fragment totalFrag = fragmentManager.findFragmentByTag(TOTAL_TAG);
			if (totalFrag == null || !totalFrag.isVisible()) {
				FragmentTransaction transaction = fragmentManager
						.beginTransaction();
				transaction.replace(R.id.fragment_holder, new TotalFragment(),
						TOTAL_TAG);
				transaction.addToBackStack(null);
				transaction.commit();
			}
			return true;
		case R.id.menu_reference:
			String refUrl = "http://developer.android.com/reference/packages.html";
			Intent refIntent = new Intent(Intent.ACTION_VIEW);
			refIntent.setData(Uri.parse(refUrl));
			startActivity(refIntent);
			return true;
		case R.id.menu_exit:
			finish();
			return true;
		default:
			return false;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void addEntry(Entry entry) {
		hideKeyboard();
		fragmentManager.popBackStack();
		EntryListFragment listFrag = new EntryListFragment();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.replace(R.id.fragment_holder, listFrag, LIST_TAG);
		transaction.commit();
		((EntryListFragment) fragmentManager.findFragmentByTag(LIST_TAG))
				.updateAllList();
	}

	@Override
	public void listItemSelected(Entry entry) {
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		EditEntryFragment editFragment = new EditEntryFragment();
		editFragment.setArguments(Entry.entryToBundle(entry));
		transaction.replace(R.id.fragment_holder, editFragment, EDIT_TAG);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	@Override
	public void editEntry(Entry entry) {
		hideKeyboard();
		fragmentManager.popBackStack();
		((EntryListFragment) fragmentManager.findFragmentByTag(LIST_TAG))
				.updateAllList();
	}

	@Override
	public void deleteEntry(Entry entry) {
		hideKeyboard();
		fragmentManager.popBackStack();
		((EntryListFragment) fragmentManager.findFragmentByTag(LIST_TAG))
				.updateAllList();
	}

	private void hideKeyboard() {
		InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		try {
			mInputMethodManager.hideSoftInputFromWindow(getCurrentFocus()
					.getWindowToken(), 0);
		} catch (NullPointerException e) {
			Log.e("hide keyboard", e.toString());
			e.printStackTrace();
		}
	}

	public DataBaseAdapter getDbAdapter() {
		// TODO Auto-generated method stub
		return dbAdapter;
	}
}
