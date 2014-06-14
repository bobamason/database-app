package com.bobamason.database1;

import java.text.DecimalFormat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TotalFragment extends Fragment {
	TextView costText, valueText, profitText, countText;

	private DataBaseAdapter dbAdapter;

	private DecimalFormat formatter;

	private Activity activity;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
		dbAdapter = ((MainActivity) activity).getDbAdapter();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.total_frag_layout, container,
				false);
		costText = (TextView) view.findViewById(R.id.total_cost_tv);
		valueText = (TextView) view.findViewById(R.id.total_value_tv);
		profitText = (TextView) view.findViewById(R.id.profit_tv);
		countText = (TextView) view.findViewById(R.id.count_tv);
		formatter = new DecimalFormat("#,##0.00");
		TotalFragment.TotalTask totalTask = new TotalTask();
		totalTask.execute();
		return view;
	}

	private class TotalTask extends AsyncTask<Void, Void, Void> {

		private float cost;

		private float value;

		private float profit;

		private ProgressDialog pd;

		private int count;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(activity);
			pd.setMessage("Calculating");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}

		@Override
		protected Void doInBackground(Void... p1) {
			cost = dbAdapter.getTotalCost();
			value = dbAdapter.getTotalValue();
			count = dbAdapter.getCount();
			profit = value - cost;
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			costText.setText("$ " + formatter.format(cost));
			valueText.setText("$ " + formatter.format(value));
			if (profit < 0f) {
				profitText.setText("-$ " + formatter.format(Math.abs(profit)));
			} else {
				profitText.setText("$ " + formatter.format(profit));
			}
			countText.setText(count + " entries");
			pd.hide();
		}
	}
}
