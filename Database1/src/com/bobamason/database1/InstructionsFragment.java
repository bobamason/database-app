package com.bobamason.database1;

import android.support.v4.app.*;
import android.view.*;
import android.os.*;
import android.widget.*;
import android.app.Activity;

public class InstructionsFragment extends Fragment {

	private ImageButton nextButton;

	private ImageView mImageView;

	private int presses = 0;

	private InstructionsFragment.OnInstructionsFinished onInstructionsFinished;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		onInstructionsFinished = (InstructionsFragment.OnInstructionsFinished) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.instruction_layout, container,
				false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Activity activity = getActivity();
		mImageView = (ImageView) activity.findViewById(R.id.image_view);
		nextButton = (ImageButton) activity.findViewById(R.id.next_button);
		nextButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				presses++;
				switch (presses) {
				case 1:
					mImageView.setImageResource(R.drawable.instruction1);
					break;
				case 2:
					mImageView.setImageResource(R.drawable.instruction2);
					break;
				case 3:
					mImageView.setImageResource(R.drawable.instruction3);
					break;
				case 4:
					nextButton.setImageResource(R.drawable.exit_button);
					mImageView.setImageResource(R.drawable.instruction4);
					break;
				case 5:
					onInstructionsFinished.instructionsFinished();
					break;
				default:
					break;
				}
			}
		});
	}

	public interface OnInstructionsFinished {
		public void instructionsFinished();
	}
}
