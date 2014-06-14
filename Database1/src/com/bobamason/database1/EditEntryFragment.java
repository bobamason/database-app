package com.bobamason.database1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class EditEntryFragment extends Fragment {

	private EditText nameEditText;

	private EditText categoryEditText;

	private EditText descriptionText;

	private EditText costEditText;

	private EditText valueEditText;

	private Button doneButton;

	private Activity activity;

	private EditEntryFragment.OnEditEntryListener onEditEntryListener;

	private Entry entry;

	private boolean isEdit;

	private DataBaseAdapter dbAdapter;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
		dbAdapter = ((MainActivity) activity).getDbAdapter();
		if (!(activity instanceof EditEntryFragment.OnEditEntryListener)) {
			throw new ClassCastException(activity.toString()
					+ " must implement EditEntryFragment.OnEditEntryListener");
		}
		onEditEntryListener = (EditEntryFragment.OnEditEntryListener) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.edit_frag_layout, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		nameEditText = (EditText) activity.findViewById(R.id.name_edittext);
		categoryEditText = (EditText) activity
				.findViewById(R.id.category_edittext);
		descriptionText = (EditText) activity
				.findViewById(R.id.description_edittext);
		costEditText = (EditText) activity.findViewById(R.id.cost_edittext);
		valueEditText = (EditText) activity.findViewById(R.id.value_edittext);

		if (getArguments() != null) {
			isEdit = true;
			entry = Entry.bundleToEntry(getArguments());
			ImageButton deleteButton = (ImageButton) activity
					.findViewById(R.id.delete_button);
			deleteButton.setVisibility(View.VISIBLE);
			deleteButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					EditEntryFragment.DeleteTask deleteTask = new DeleteTask();
					deleteTask.execute(entry);
				}
			});
			ImageButton copyButton = (ImageButton) activity
					.findViewById(R.id.copy_button);
			copyButton.setVisibility(View.VISIBLE);
			copyButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					getEntryFromEditTexts();
					EditEntryFragment.AddTask addTask = new AddTask();
					addTask.execute(entry);
				}
			});
			nameEditText.setText(entry.getName());
			categoryEditText.setText(entry.getCategory());
			descriptionText.setText(entry.getDescription());
			costEditText.setText(String.valueOf(entry.getCost()));
			valueEditText.setText(String.valueOf(entry.getValue()));
		} else {
			isEdit = false;
		}

		doneButton = (Button) activity.findViewById(R.id.done_add_button);
		doneButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View p1) {
				getEntryFromEditTexts();

				if (isEdit) {
					EditEntryFragment.EditTask editTask = new EditTask();
					editTask.execute(entry);
				} else {
					EditEntryFragment.AddTask addTask = new AddTask();
					addTask.execute(entry);
				}
			}
		});
	}

	private void getEntryFromEditTexts() {
		if (entry == null)
			entry = new Entry();
		String name = nameEditText.getText().toString();
		entry.setName(name);
		String category = categoryEditText.getText().toString();
		entry.setCategory(category);
		String description = descriptionText.getText().toString();
		entry.setDescription(description);

		String costString = costEditText.getText().toString();
		if (costString.equals(""))
			costString = "0.0";
		float cost = Float.valueOf(costString);
		entry.setCost(cost);

		String valueString = valueEditText.getText().toString();
		if (valueString.equals(""))
			valueString = "0.0";
		float value = Float.valueOf(valueString);
		entry.setValue(value);
	}

	public interface OnEditEntryListener {
		public void addEntry(Entry entry);

		public void editEntry(Entry entry);

		public void deleteEntry(Entry entry);

	}

	private class AddTask extends AsyncTask<Entry, Void, Entry> {

		private ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(activity);
			pd.setMessage("Adding New Entry");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}

		@Override
		protected Entry doInBackground(Entry... args) {
			dbAdapter.insert(args[0]);
			return args[0];
		}

		@Override
		protected void onPostExecute(Entry result) {
			super.onPostExecute(result);
			onEditEntryListener.addEntry(result);
			pd.hide();
		}
	}

	private class EditTask extends AsyncTask<Entry, Void, Entry> {

		private ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(activity);
			pd.setMessage("Updating");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}

		@Override
		protected Entry doInBackground(Entry... args) {
			dbAdapter.updateEntry(args[0]);
			return args[0];
		}

		@Override
		protected void onPostExecute(Entry result) {
			super.onPostExecute(result);
			onEditEntryListener.editEntry(result);
			pd.hide();
		}
	}

	private class DeleteTask extends AsyncTask<Entry, Void, Entry> {

		private ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(activity);
			pd.setMessage("Deleting");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}

		@Override
		protected Entry doInBackground(Entry... args) {
			dbAdapter.deleteEntry(args[0]);
			return args[0];
		}

		@Override
		protected void onPostExecute(Entry result) {
			super.onPostExecute(result);
			onEditEntryListener.deleteEntry(result);
			pd.hide();
		}
	}
}
