package com.xynxs.main.dialog;

import java.io.File;

import com.xynxs.main.BaseActivity;
import com.xynxs.main.R;
import com.xynxs.main.util.Const;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;

public class PictureSelectDialog extends Dialog implements android.view.View.OnClickListener {

	private BaseActivity activity;

	public PictureSelectDialog(BaseActivity activity) {
		super(activity, R.style.BottomDialogStyle);
		this.activity = activity;

		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.dialog_bottom_select, null);

		addContentView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		setContentView(layout);

		layout.findViewById(R.id.bottom_dialog_btn_01).setOnClickListener(this);
		layout.findViewById(R.id.bottom_dialog_btn_02).setOnClickListener(this);
		layout.findViewById(R.id.bottom_dialog_btn_cancel).setOnClickListener(this);

		Window win = getWindow();
		win.setGravity(Gravity.BOTTOM);
		WindowManager.LayoutParams lp = win.getAttributes();

		lp.width = activity.getScreenWidth();

		onWindowAttributesChanged(lp);

		setCancelable(true);
		setCanceledOnTouchOutside(true);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		dismiss();
		//拍照
		if (id == R.id.bottom_dialog_btn_01) {
			File file = new File(activity.getAppDownloadDir()+Const.TAKE_PIC_FILE_NAME);
			try{
				if(!file.exists()){
					file.createNewFile();
				}
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
				activity.startActivityForResult(intent, Const.PIC_TAKE_CODE);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		if (id == R.id.bottom_dialog_btn_02) {
			Intent intent= new Intent();
			intent.setAction(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");
			activity.startActivityForResult(Intent.createChooser(intent, "选择图片"), Const.PIC_SELECT_CODE);
		}
		if (id == R.id.bottom_dialog_btn_cancel) {
			
		}
	}

}
