package com.bobamason.database1;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class EntryListFragment extends Fragment {

	private Activity activity;

	private ListView listView;

	private int sortPos;

	private int orderPos;

	private EntryListFragment.OnListItemSelected onListItemSelected;

	private Spinner sortBySpinner, orderBySpinner;

	private static final String[] SORT_COLUMNS = { "Name", "Category",
			"Description", "Cost", "Est. Value" };

	private DataBaseAdapter dbAdapter;

	private boolean isSearch;

	private String currentSearch;

	private SharedPreferences prefs;

	private static final String SORT_KEY = "sortkey";

	private static final String ORDER_KEY = "orderkey";

	public interface OnListItemSelected {
		public void listItemSelected(Entry entry);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
		dbAdapter = ((MainActivity) activity).getDbAdapter();
		if (!(activity instanceof EntryListFragment.OnListItemSelected)) {
			throw new ClassCastException(activity.toString()
					+ " must implement AllEntryListFragment.OnListItemSelected");
		}
		this.onListItemSelected = (EntryListFragment.OnListItemSelected) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.list_frag_layout, container,
				false);
		listView = (ListView) view.findViewById(R.id.listView);
		Bundle args = getArguments();
		if (args != null) {
			currentSearch = args.getString("searchString");
			isSearch = true;
			listView.setAdapter(new EntryListAdapter(activity,
					R.layout.entry_row_layout, new ArrayList<Entry>()));
		} else {
			isSearch = false;
			listView.setAdapter(new EntryListAdapter(activity,
					R.layout.entry_row_layout, new ArrayList<Entry>()));
		}
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos,
					long id) {
				onListItemSelected.listItemSelected((Entry) parent.getAdapter()
						.getItem(pos));
			}

		});

		prefs = PreferenceManager.getDefaultSharedPreferences(activity);

		sortBySpinner = (Spinner) activity.findViewById(R.id.sortby_spinner);
		sortBySpinner.setAdapter(new ArrayAdapter<String>(activity,
				R.layout.spinner_item, SORT_COLUMNS));
		sortBySpinner.setOnItemSelectedListener(mOnItemSelectedListener);
		sortPos = prefs.getInt(SORT_KEY, 0);
		sortBySpinner.setSelection(sortPos);

		orderBySpinner = (Spinner) activity.findViewById(R.id.orderby_spinner);
		orderBySpinner.setAdapter(new ArrayAdapter<String>(activity,
				R.layout.spinner_item,
				new String[] { "Ascending", "Descending" }));
		orderBySpinner.setOnItemSelectedListener(mOnItemSelectedListener);
		orderPos = prefs.getInt(ORDER_KEY, 0);
		orderBySpinner.setSelection(orderPos);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	public void updateSearchList(String str) {
		currentSearch = str;
		EntryListFragment.SearchListTask searchTask = new SearchListTask();
		searchTask.execute(str);
	}

	public void updateAllList() {
		EntryListFragment.AllListTask allTask = new AllListTask();
		allTask.execute();
	}

	private AdapterView.OnItemSelectedListener mOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long arg0) {
			int id = parent.getId();
			if (id == R.id.sortby_spinner) {
				sortPos = pos;
				SharedPreferences.Editor editor = prefs.edit();
				editor.putInt(SORT_KEY, sortPos);
				editor.apply();
				EntryListFragment.SortListTask sortTask = new SortListTask();
				sortTask.execute(sortPos, orderPos);
			} else if (id == R.id.orderby_spinner) {
				orderPos = pos;
				SharedPreferences.Editor editor = prefs.edit();
				editor.putInt(ORDER_KEY, orderPos);
				editor.apply();
				EntryListFragment.SortListTask sortTask = new SortListTask();
				sortTask.execute(sortPos, orderPos);
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
		}

	};

	private class EntryListAdapter extends BaseAdapter {

		private Context context;

		private ArrayList<Entry> list;

		private LayoutInflater inflater;

		private int resId;

		private DecimalFormat formatter;

		public EntryListAdapter(Context context, int resId,
				ArrayList<Entry> list) {
			this.context = context;
			this.list = list;
			this.resId = resId;
			init();
		}

		private void init() {
			inflater = (LayoutInflater) context
					.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
			formatter = new DecimalFormat("#,##0.00");
		}

		public void updateList(ArrayList<Entry> list) {
			this.list = list;
			this.notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int i) {
			return list.get(i);
		}

		@Override
		public long getItemId(int i) {
			return list.get(i).getId();
		}

		@Override
		public View getView(int pos, View convertView, ViewGroup parent) {
			View row = convertView;
			ViewHolder holder = null;
			if (row == null) {
				row = inflater.inflate(resId, parent, false);
				holder = new ViewHolder(row);
				row.setTag(holder);
			} else {
				holder = (ViewHolder) row.getTag();
			}
			Entry entry = list.get(pos);
			if (holder != null) {
				holder.category.setText(entry.getCategory());
				holder.name.setText(entry.getName());
				holder.decsription.setText(entry.getDescription());
				holder.cost.setText("$ " + formatter.format(entry.getCost()));
				holder.value.setText("$ " + formatter.format(entry.getValue()));
			}
			return row;
		}

		private class ViewHolder {
			TextView category;
			TextView name;
			TextView cost;
			TextView value;
			TextView decsription;

			ViewHolder(View v) {
				name = (TextView) v.findViewById(R.id.name_row);
				category = (TextView) v.findViewById(R.id.category_row);
				decsription = (TextView) v.findViewById(R.id.description_row);
				cost = (TextView) v.findViewById(R.id.cost_row);
				value = (TextView) v.findViewById(R.id.value_row);
			}
		}
	}

	private class AllListTask extends AsyncTask<Void, Void, ArrayList<Entry>> {

		private ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(activity);
			pd.setMessage("Loading List");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}

		@Override
		protected ArrayList<Entry> doInBackground(Void... args) {
			ArrayList<Entry> entries = dbAdapter.getAllEntries();
			return entries;
		}

		@Override
		protected void onPostExecute(ArrayList<Entry> result) {
			super.onPostExecute(result);
			EntryListAdapter adapter = (EntryListAdapter) listView.getAdapter();
			if (adapter != null)
				adapter.updateList(result);
			pd.dismiss();
		}
	}

	private class SearchListTask extends
			AsyncTask<String, Void, ArrayList<Entry>> {

		private ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(activity);
			pd.setMessage("Loading Search Results");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}

		@Override
		protected ArrayList<Entry> doInBackground(String... args) {
			ArrayList<Entry> entries = dbAdapter.getSearchEntries(args[0]);
			return entries;
		}

		@Override
		protected void onPostExecute(ArrayList<Entry> result) {
			super.onPostExecute(result);
			EntryListAdapter adapter = (EntryListAdapter) listView.getAdapter();
			if (adapter != null)
				adapter.updateList(result);
			pd.dismiss();
		}
	}

	private class SortListTask extends
			AsyncTask<Integer, Void, ArrayList<Entry>> {

		private ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(activity);
			pd.setMessage("Sorting");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}

		@Override
		protected ArrayList<Entry> doInBackground(Integer... args) {
			ArrayList<Entry> entries = new ArrayList<Entry>();
			if (isSearch)
				entries = dbAdapter.setSorting(args[0], args[1], currentSearch);
			else
				entries = dbAdapter.setSorting(args[0], args[1]);
			return entries;
		}

		@Override
		protected void onPostExecute(ArrayList<Entry> result) {
			super.onPostExecute(result);
			EntryListAdapter adapter = (EntryListAdapter) listView.getAdapter();
			if (adapter != null)
				adapter.updateList(result);
			pd.dismiss();
		}
	}
}
