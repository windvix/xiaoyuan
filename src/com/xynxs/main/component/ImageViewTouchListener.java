package com.xynxs.main.component;


import com.xynxs.main.BaseActivity;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public abstract class ImageViewTouchListener implements OnTouchListener {

	private BaseActivity act;

	public ImageViewTouchListener(BaseActivity act) {
		this.act = act;
	}

	private static final String TAG = "Touch";
	// These matrices will be used to move and zoom image
	Matrix matrix = new Matrix();
	Matrix savedMatrix = new Matrix();
	// We can be in one of these 3 states
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	int mode = NONE;

	// Remember some things for zooming
	PointF start = new PointF();
	PointF mid = new PointF();
	float oldDist = 1f;

	public Matrix getMatrix(int imgHeight, int imgWidth) {
		if (imgWidth < act.getScreenWidth()) {
			matrix.postTranslate((act.getScreenWidth() - imgWidth) / 2.0f, ((act.getScreenHeight() - act.getTitleBarHeight()) / 2.0f) - (imgHeight / 2.0f));
		} else {
			matrix.postTranslate(0, ((act.getScreenHeight() - act.getTitleBarHeight()) / 2.0f) - (imgHeight / 2.0f));
		}
		return matrix;
	}

	public void initMatrix(ImageView view, Bitmap bitmap) {
		if (view != null && bitmap != null) {
			int imgHeight = view.getHeight();
			int imgWidth = view.getWidth();
			int bWidth = bitmap.getWidth();
			int bHeight = bitmap.getHeight();

			int gapW = imgWidth - bWidth;
			int gapH = imgHeight - bHeight;

			float pointX = (imgWidth - bWidth) / 2.0f;
			if (pointX < 0) {
				pointX = -pointX;
			}
			//matrix.postTranslate(pointX, 0);

			// 将图片自动绽放到框的大小
			float size_w = (imgWidth / (1.0f * bWidth));
			float size_h = (imgHeight / (1.0f * bHeight));
			if (size_h > size_w) {
				size_w = size_h;
			}
			matrix.postScale(size_w, size_w);

			view.setImageBitmap(bitmap);
			view.setImageMatrix(matrix);
			view.setOnTouchListener(this);

		}
	}

	private long downTime = 0;
	private float downX = 0;
	private float downY = 0;

	public abstract void touchUp();

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		ImageView view = (ImageView) v;
		// Log.e("view_width",
		// view.getImageMatrix()..toString()+"*"+v.getWidth());
		// Dump touch event to log
		dumpEvent(event);

		// Handle touch events here...
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			downTime = System.currentTimeMillis();
			downX = event.getX();
			downY = event.getY();
			matrix.set(view.getImageMatrix());
			savedMatrix.set(matrix);
			start.set(event.getX(), event.getY());
			// Log.d(TAG, "mode=DRAG");
			mode = DRAG;

			// Log.d(TAG, "mode=NONE");
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			oldDist = spacing(event);
			// Log.d(TAG, "oldDist=" + oldDist);
			if (oldDist > 10f) {
				savedMatrix.set(matrix);
				midPoint(mid, event);
				mode = ZOOM;
				// Log.d(TAG, "mode=ZOOM");
			}
			break;
		case MotionEvent.ACTION_UP:
			long now = System.currentTimeMillis();
			float upX = event.getX();
			float upY = event.getY();
			if (now - downTime < 700 && downX == upX && upY == downY) {
				touchUp();
				break;
			}
		case MotionEvent.ACTION_POINTER_UP:
			mode = NONE;
			// Log.e("view.getWidth", view.getWidth() + "");
			// Log.e("view.getHeight", view.getHeight() + "");

			break;
		case MotionEvent.ACTION_MOVE:
			if (mode == DRAG) {
				// ...
				matrix.set(savedMatrix);
				postTranslate(matrix,event.getX() - start.x, event.getY() - start.y);
			} else if (mode == ZOOM) {
				float newDist = spacing(event);
				Log.d(TAG, "newDist=" + newDist);
				if (newDist > 10f) {
					matrix.set(savedMatrix);
					float scale = newDist / oldDist;
					matrix.postScale(scale, scale, mid.x, mid.y);
				}
			}
			break;
		}

		view.setImageMatrix(matrix);
		return true; // indicate event was handled
	}

	//不要移动到屏幕之外
	private void postTranslate(Matrix matrix, float dx, float dy){
		matrix.postTranslate(dx, dy);
	}
	
	private void dumpEvent(MotionEvent event) {
		String names[] = { "DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE", "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?" };
		StringBuilder sb = new StringBuilder();
		int action = event.getAction();
		int actionCode = action & MotionEvent.ACTION_MASK;
		sb.append("event ACTION_").append(names[actionCode]);
		if (actionCode == MotionEvent.ACTION_POINTER_DOWN || actionCode == MotionEvent.ACTION_POINTER_UP) {
			sb.append("(pid ").append(action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
			sb.append(")");
		}
		sb.append("[");
		for (int i = 0; i < event.getPointerCount(); i++) {
			sb.append("#").append(i);
			sb.append("(pid ").append(event.getPointerId(i));
			sb.append(")=").append((int) event.getX(i));
			sb.append(",").append((int) event.getY(i));
			if (i + 1 < event.getPointerCount())
				sb.append(";");
		}
		sb.append("]");
		// Log.d(TAG, sb.toString());
	}

	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}

}