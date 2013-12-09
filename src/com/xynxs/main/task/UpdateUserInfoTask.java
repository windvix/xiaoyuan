package com.xynxs.main.task;

import java.io.File;

import com.xynxs.main.BaseActivity;
import com.xynxs.main.MyInfoActivity;
import com.xynxs.main.bean.User;
import com.xynxs.main.util.Const;
import com.xynxs.main.util.ServerHelper;

public class UpdateUserInfoTask extends BaseTask {

	private boolean pictureSelected;

	private User user;

	private String result;
	
	public UpdateUserInfoTask(MyInfoActivity activity, boolean pictureSelected, User user) {
		super(activity);
		this.pictureSelected = pictureSelected;
		this.user = user;
	}

	@Override
	protected void doInBackground() {
		if (!isCancelled() && user != null) {
			boolean upLoadOk = false;
			// 如果有图片,则先上传图片
			if (pictureSelected) {
				File file = new File(getActivity().getAppDownloadDir() + user.getId() + Const.TEMP_JPG);
				if (file.exists()) {
					getHelper().uploadImg(file, user.getId(), Const.USER_HEAD_JPG_NAME);
					upLoadOk = true;
				}
			}
			// 上传成功,则更新头像
			if (upLoadOk) {
				user.setHead_img(ServerHelper.getUploadImgURL(user.getId(), Const.USER_HEAD_JPG_NAME));
			}			
			if (!isCancelled()) {
				result = (String)getHelper().updateUserInfo(user.getName(),user.getCollege_name(),user.getGender(),user.getCity_name(), user.getHead_img(), user.getEntranceYear(), user.getLabel(), user.getSign(), user.getPassword(), user.getBirthDay(), user.getQQ(), user.getEmail(), user.getTel(), user.getAddress(), user.getDepartment(), user.getRealName(), user.getId());
			}
		}
	}

	@Override
	protected void onPostExecute() {
		MyInfoActivity act = (MyInfoActivity)getActivity();
		act.submitResult(result);
	}

}
