package com.saeed.tutorials.mediaplayer.utils;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.TextView;

/**
 * Created by nadirhussain on 22/05/2017.
 */

public class ViewScaleHandler
{
    private static final String TAG_CONSTANT = "constant";
    private static final String TAG_WIDTH_CONSTANT = "constantWidth";
    private static final String TAG_HEIGHT_CONSTANT = "constantHeight";
    // These are constants that represent device for which xml has been designed.
    private static final double standardWidth = 1080;
    private static final double standardHeight = 1920;
    private static final double standardDensity = 2.0;


    private double widthRatio = 0;
    private double heightRatio = 0;
    private static double aspectRatioFactor = 0;
    private double textScalingFactor = 0;
    private boolean isDeviceSmall = false;


    public ViewScaleHandler(Context activity) {
        int width = ScreenUtil.getScreenWidth(activity);
        int height = ScreenUtil.getScreenHeight(activity) - ScreenUtil.getStatusBarHeight(activity);
        float currentDensity = ScreenUtil.getScreenDensity(activity);

        widthRatio = (width / standardWidth);
        heightRatio = (height / standardHeight);
        aspectRatioFactor = Math.min(widthRatio, heightRatio);
        textScalingFactor = Math.min(widthRatio, heightRatio) * (standardDensity / currentDensity);
        isDeviceSmall = ScreenUtil.isSmallDevice(activity);
    }


    public void scaleRootView(View view) {
        if (view.getLayoutParams() instanceof MarginLayoutParams) {
            MarginLayoutParams marginLayoutParams = (MarginLayoutParams) view.getLayoutParams();
            String tag = "";
            if (view.getTag() != null) {
                tag = view.getTag().toString();
            }
            if (marginLayoutParams != null) {
                scaleSizeDimensionsParams(marginLayoutParams, tag);
                scaleMargins(marginLayoutParams);
                scaleViewPaddings(view);
                view.setLayoutParams(marginLayoutParams);
            }
        }

        if (view instanceof TextView) {
            scaleTextSize((TextView) view);
        }

        if (view instanceof ViewGroup) {
            int childCount = ((ViewGroup) view).getChildCount();
            for (int i = 0; i < childCount; i++) {
                scaleRootView(((ViewGroup) view).getChildAt(i));
            }
        }

    }

    private void scaleSizeDimensionsParams(MarginLayoutParams marginLayoutParams, String tag) {
        if (marginLayoutParams == null || tag.contentEquals(TAG_CONSTANT)) {
            return;
        }

        if (!tag.contentEquals(TAG_WIDTH_CONSTANT) && marginLayoutParams.width != MarginLayoutParams.WRAP_CONTENT && marginLayoutParams.width != MarginLayoutParams.MATCH_PARENT) {
            marginLayoutParams.width *= aspectRatioFactor;
        }

        if (!tag.equalsIgnoreCase(TAG_HEIGHT_CONSTANT) && marginLayoutParams.height != MarginLayoutParams.WRAP_CONTENT && marginLayoutParams.height != MarginLayoutParams.MATCH_PARENT) {
            marginLayoutParams.height *= aspectRatioFactor;
        }

    }

    private void scaleMargins(MarginLayoutParams marginLayoutParams) {
        marginLayoutParams.leftMargin *= widthRatio;
        marginLayoutParams.rightMargin *= widthRatio;
        marginLayoutParams.topMargin *= heightRatio;
        marginLayoutParams.bottomMargin *= heightRatio;
    }

    private void scaleViewPaddings(View view) {
        int paddingTop = (int) (view.getPaddingTop() * heightRatio);
        int paddingLeft = (int) (view.getPaddingLeft() * widthRatio);
        int paddingRight = (int) (view.getPaddingRight() * widthRatio);
        int paddingBottom = (int) (view.getPaddingBottom() * heightRatio);

        view.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
    }

    private void scaleTextSize(TextView textView) {
        float textSize = textView.getTextSize();
        textSize *= textScalingFactor;
        if (isDeviceSmall) {
            textSize = textSize + 1.2f;
        }
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    }

    public static double resizeDimension(int value) {
        return value * aspectRatioFactor;
    }
    public static double resizeDimension(Context activity,int value) {
        int width = ScreenUtil.getScreenWidth(activity);
        int height = ScreenUtil.getScreenHeight(activity) - ScreenUtil.getStatusBarHeight(activity);


        double widthRatio = (width / standardWidth);
        double heightRatio = (height / standardHeight);
        aspectRatioFactor = Math.min(widthRatio, heightRatio);
        return value * aspectRatioFactor;
    }
}

