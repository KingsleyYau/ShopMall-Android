package lbs.goodplace.com.component;

import lbs.goodplace.com.R;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

public class ScoreStar extends ImageView {
	private static Drawable DRAWABLE00;
	private static Drawable DRAWABLE10;
	private static Drawable DRAWABLE20;
	private static Drawable DRAWABLE30;
	private static Drawable DRAWABLE40;
	private static Drawable DRAWABLE50;

	private int star = -1;

	public ScoreStar(Context context) {
		super(context);
	}

	public ScoreStar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ScoreStar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public int getStar() {
		return this.star;
	}

	/**
	 * 
	 * @param paramInt
	 *            the valid value only contain 0, 5, 10, 15, 20, 25, 30, 35, 40,
	 *            45, 50
	 */
	public void setStar(int paramInt) {
		star = paramInt;
		if (star < 20) {
			if (DRAWABLE00 == null) {
				DRAWABLE00 = getResources().getDrawable(R.drawable.star00);
			}
			setImageDrawable(DRAWABLE00);
		}

		else if (20 <= star && star < 40) {
			if (DRAWABLE10 == null) {
				DRAWABLE10 = getResources().getDrawable(R.drawable.star10);
			}
			setImageDrawable(DRAWABLE10);
		}


		else if (40 <= star && star < 60) {
			if (DRAWABLE20 == null) {
				DRAWABLE20 = getResources().getDrawable(R.drawable.star20);
			}
			setImageDrawable(DRAWABLE20);

		}


		else if (60 <= star && star < 80) {
			if (DRAWABLE30 == null) {
				DRAWABLE30 = getResources().getDrawable(R.drawable.star30);
			}
			setImageDrawable(DRAWABLE30);

		}


		else if (80 <= star && star < 100) {
			if (DRAWABLE40 == null) {
				DRAWABLE40 = getResources().getDrawable(R.drawable.star40);
			}
			setImageDrawable(DRAWABLE40);

		}


		else if (100 <= star) {
			if (DRAWABLE50 == null) {
				DRAWABLE50 = getResources().getDrawable(R.drawable.star50);
			}
			setImageDrawable(DRAWABLE50);
		}

	}
}