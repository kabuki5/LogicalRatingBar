package com.redarbor.computrabajo.crosscutting.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.redarbor.computrabajo.R;

import java.util.ArrayList;

/**
 * Created by ramon on 07/03/2017.
 */
public class CustomRatingBar extends LinearLayout implements View.OnClickListener {

    private static final long ANIMATION_SPEED = 150;

    public interface OnRatingChangeListener {
        void onRatingChanged(View view, float rating);
    }

    private int maxItems = 5;
    private boolean clickable;
    private float mRating;
    private Context mContext;
    private ArrayList<ImageView> mImages;
    private Drawable mFullImageResource;
    private Drawable mEmptyImageResource;
    private float mIconSize;
    private Drawable mLastImageFull;
    private Drawable mLastImageEmpty;
    private OnRatingChangeListener mCallback;
    private boolean isInAnimation;


    public CustomRatingBar(Context context) {
        super(context);
        this.mContext = context;
        populateRating();
    }

    public CustomRatingBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        obtainData(attrs);
        populateRating();
    }

    public CustomRatingBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        obtainData(attrs);
        populateRating();
    }


    private void obtainData(AttributeSet attrs) {
        mFullImageResource = mContext.getResources().getDrawable(R.drawable.s_f);
        mEmptyImageResource = mContext.getResources().getDrawable(R.drawable.s_e);

        TypedArray array = mContext.obtainStyledAttributes(attrs, R.styleable.CustomRatingBar, 0, 0);

        try {
            mFullImageResource = array.getDrawable(R.styleable.CustomRatingBar_imageFull);
            mEmptyImageResource = array.getDrawable(R.styleable.CustomRatingBar_imageEmpty);
            mRating = array.getInteger(R.styleable.CustomRatingBar_initRating, 0);
            maxItems = array.getInteger(R.styleable.CustomRatingBar_maxItems, 5);
            clickable = array.getBoolean(R.styleable.CustomRatingBar_clickable, false);
            mIconSize = array.getDimension(R.styleable.CustomRatingBar_iconSize, mContext.getResources().getDimension(R.dimen.rating_bar_item));
            mLastImageFull = array.getDrawable(R.styleable.CustomRatingBar_lastImageFull);
            mLastImageEmpty = array.getDrawable(R.styleable.CustomRatingBar_lastImageEmpty);

            if (mLastImageFull == null)
                mLastImageFull = mFullImageResource;
            if (mLastImageEmpty == null)
                mLastImageEmpty = mEmptyImageResource;
        } finally {
            array.recycle();
        }
        setupMeasurements();
    }

    private void setupMeasurements() {
        setGravity(Gravity.CENTER);
//        setBackgroundColor(getResources().getColor(R.color.colorAccent));
    }

    public void setMaxItems(int maxItems) {
        this.maxItems = maxItems;
    }

    public void setRating(float rating) {
        this.mRating = rating;
        populateRating();
    }

    public void setRatingEnabled(boolean clickable) {
        this.clickable = clickable;
        populateRating();
    }

    private void populateRating() { //TODO => Manage half values
        setGravity(Gravity.CENTER_HORIZONTAL);
        removeAllViews();
        mImages = new ArrayList<>();

        FrameLayout.LayoutParams imageParams = new FrameLayout.LayoutParams((int) mIconSize, (int) mIconSize);
        imageParams.gravity = Gravity.CENTER;
        int margin = (int) (mIconSize / 2);
        imageParams.setMargins(0, margin, 0, margin);

        LayoutParams frameLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
        int integerPart = (int) mRating;
//        double fractional = mRating - integerPart;

        ImageView imageView;
        FrameLayout frameLayout;

        boolean isLastItem = false;
        for (int i = 0; i < maxItems; i++) {
            if (i == maxItems - 1)
                isLastItem = true;
            else
                isLastItem = false;
            //creating image outer container
            frameLayout = new FrameLayout(mContext);
            frameLayout.setLayoutParams(frameLayoutParams);
            //creating ImageView
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(imageParams);
            //adding imageView to layout container
            frameLayout.addView(imageView);
            frameLayout.setTag(i);
            //setting click callback
            if (clickable)
                frameLayout.setOnClickListener(this);
            //adding
            mImages.add(imageView);
            Drawable resource;

            if (integerPart > i)
                if (isLastItem)
                    resource = mLastImageFull;
                else
                    resource = mFullImageResource;
          /*  else if (fractional > 0) {
                if (fractional < 0.250)
                    resource = mEmptyImageResource;
                else if (fractional > 0.250 && fractional < 0.750)
                    resource = half;
                else
                    resource = mFullImageResource;
                fractional = 0;
            }*/
            else {
                if (isLastItem)
                    resource = mLastImageEmpty;
                else
                    resource = mEmptyImageResource;
            }
            imageView.setImageDrawable(resource);
            addView(frameLayout);
        }
    }


    @Override
    public void onClick(View view) {
        if(isInAnimation)
            return;
        Drawable resource;
        int position = (int) view.getTag();
        mRating = position + 1;
        for (int i = 0; i < mImages.size(); i++) {
            ImageView imageView = mImages.get(i);
            if (i <= position) {
                if (i == mImages.size() - 1)
                    resource = mLastImageFull;
                else
                    resource = mFullImageResource;
            } else {
                if (i == mImages.size() - 1)
                    resource = mLastImageEmpty;
                else
                    resource = mEmptyImageResource;

            }
            imageView.setImageDrawable(resource);
        }
        if (mCallback != null)
            mCallback.onRatingChanged(CustomRatingBar.this, mRating);
    }

    public float getRating() {
        return mRating;
    }

    private int animCounter = 0;
    private boolean reverseCount;
    private Drawable animResource;

    public void animateViews() {
        if(isInAnimation)
            return;
        animCounter = 0;
        reverseCount = false;

        animResource = mContext.getResources().getDrawable(R.drawable.s_f);
        isInAnimation = true;
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (animCounter == -1)
                    return;
                if (animCounter == mImages.size() - 1 && !reverseCount) {
                    reverseCount = true;
                    animResource = mLastImageFull;
                    animCounter++;
                } else if (animCounter == mImages.size() - 1 && reverseCount) {
                    animResource = mLastImageEmpty;
                }

                if (animCounter == mImages.size())
                    mImages.get(animCounter - 1).setImageDrawable(animResource);
                else
                    mImages.get(animCounter).setImageDrawable(animResource);

//                if (animCounter == mImages.size())
//                    animateImageViewChange(mContext, mImages.get(animCounter - 1), animResource);
//                else
//                    animateImageViewChange(mContext, mImages.get(animCounter), animResource);
                if (reverseCount) {
                    animCounter--;
                    animResource = mEmptyImageResource;
                } else
                    animCounter++;

                if (animCounter != -1)
                    handler.postDelayed(this, ANIMATION_SPEED);
                else {
                    isInAnimation = false;
                }
            }
        }, 500);
    }



    public void setOnRatingChangeListener(OnRatingChangeListener listener) {
        this.mCallback = listener;
    }

    public static void animateImageViewChange(Context c, final ImageView v, final Drawable new_image) {
        final Animation anim_out = AnimationUtils.loadAnimation(c, R.anim.fade_out);
        final Animation anim_in = AnimationUtils.loadAnimation(c, R.anim.fade_in);
        anim_out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                v.setImageDrawable(new_image);
                anim_in.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                    }
                });
                v.startAnimation(anim_in);
            }
        });
        v.startAnimation(anim_out);
    }
}