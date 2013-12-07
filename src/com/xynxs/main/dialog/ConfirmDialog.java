package com.xynxs.main.dialog;

import com.xynxs.main.MainActivity;
import com.xynxs.main.R;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

public class ConfirmDialog extends Dialog{

	private View rootView;
	
	public ConfirmDialog(MainActivity activity, String positiveBtnText, String negativeBtnText, String msg, String title) {
		super(activity, R.style.Dialog);
		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.dialog_confirm, null);
		addContentView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		setContentView(layout);

		setCancelable(true);
		setCanceledOnTouchOutside(true);
		this.rootView = layout;
		
		((TextView)rootView.findViewById(R.id.dialog_message)).setText(msg);
		((TextView)rootView.findViewById(R.id.dialog_title)).setText(title);
		((Button)rootView.findViewById(R.id.positiveButton)).setText(positiveBtnText);
		((Button)rootView.findViewById(R.id.negativeButton)).setText(negativeBtnText);
	}
	
	
	public Button getPositiveBtn(){
		return (Button)rootView.findViewById(R.id.positiveButton);
	}
	
	public Button getNegativeBtn(){
		return (Button)rootView.findViewById(R.id.negativeButton);
	}
}
