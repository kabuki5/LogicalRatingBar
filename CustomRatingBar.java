package com.discoverinktattoo.discoverink.cviews;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.discoverinktattoo.discoverink.R;

import java.util.ArrayList;

/**
 * Doonamis
 * Created by Ramon on 20/12/16.
 */

public class CustomRatingBar extends LinearLayout implements View.OnClickListener {

    private int maxItems = 5;
    private boolean clickable;
    private float rating;
    private Context mContext;
    private ArrayList<ImageView> mImages;

    public CustomRatingBar(Context context) {
        super(context);
        this.mContext = context;
        populateRating();
    }

    public CustomRatingBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        populateRating();
    }

    public CustomRatingBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        populateRating();
    }

    public void setMaxItems(int maxItems) {
        this.maxItems = maxItems;
    }

    public void setRating(float rating) {
        this.rating = rating;
        populateRating();
    }

    public void setRatingEnabled(boolean clickable) {
        this.clickable = clickable;
        populateRating();
    }


    private void populateRating() {
        removeAllViews();
        mImages = new ArrayList<>();
        int full = R.drawable.machine_big;
        int empty = R.drawable.machine_big_void;
        int half = R.drawable.machine_big_half;

        float iconSize = mContext.getResources().getDimension(R.dimen.rating_bar_item);
        LayoutParams params = new LayoutParams((int) iconSize, (int) iconSize);

        int integerPart = (int) rating;
        double fractional = rating - integerPart;

        ImageView imageView;

        for (int i = 0; i < maxItems; i++) {
            imageView = new ImageView(mContext);
            imageView.setTag(i);
            imageView.setLayoutParams(params);
            if (clickable)
                imageView.setOnClickListener(this);
            mImages.add(imageView);
            int resource;

            if (integerPart > i)
                resource = full;
            else if (fractional > 0) {
                if (fractional < 0.250)
                    resource = empty;
                else if (fractional > 0.250 && fractional < 0.750)
                    resource = half;
                else
                    resource = full;
                fractional = 0;
            } else {
                resource = empty;
            }
            imageView.setImageResource(resource);
            addView(imageView);
        }

    }


    @Override
    public void onClick(View view) {
        int position = (int) view.getTag();
        rating = position + 1;
        for (int i = 0; i < mImages.size(); i++) {
            ImageView imageView = mImages.get(i);
            if (i <= position) {
                imageView.setImageResource(R.drawable.machine_big);
            } else {
                imageView.setImageResource(R.drawable.machine_big_void);
            }
        }
    }

    public float getRating() {
        return rating;
    }


}
