package lbs.goodplace.com.View;

import lbs.goodplace.com.R;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
/**
 * 
 * <br>类描述:
 * <br>功能详细描述:
 * 
 * @author  dingzijian
 * @date  [2012-11-23]
 */
public class ImageBrowserProgressBar extends LinearLayout {
	public ImageBrowserProgressBar(Context context) {
		super(context);
	}

	public ImageBrowserProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		ProgressBar progressBar = (ProgressBar) this.findViewById(R.id.btmprogress);
		progressBar.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
		Drawable drawable = getContext().getResources().getDrawable(R.drawable.go_progress_green);
		progressBar.setIndeterminateDrawable(drawable);
	}
}