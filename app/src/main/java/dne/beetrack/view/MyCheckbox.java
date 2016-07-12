package dne.beetrack.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import dne.beetrack.R;

/**
 * Created by loipn on 7/12/2016.
 */
public class MyCheckbox extends ImageView {

    private boolean isChecked;

    public MyCheckbox(Context context, AttributeSet attrs) {
        super(context, attrs);
        isChecked = true;
        setIsChecked(isChecked);
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
        if (this.isChecked) {
            setImageResource(R.drawable.ic_ticker_green);
        } else {
            setImageResource(R.drawable.ic_ticker_gray);
        }
    }

    public void toggleCheck() {
        isChecked = !isChecked;
        if (isChecked) {
            setImageResource(R.drawable.ic_ticker_green);
        } else {
            setImageResource(R.drawable.ic_ticker_gray);
        }
    }

    public boolean getIsChecked() {
        return isChecked;
    }
}
