package com.example.sufferqr.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.sufferqr.R;

import java.util.HashMap;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class QRDetailSectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2,R.string.tab_text_3};
    private final Context mContext;

    Bundle myHereBundle;

    QRDetailGeneralFragment qrDetailGeneralFragment;
    QRDetailImageFragment qrDetailImageFragment;
    QRDetailLocationFragment qrDetailLocationFragment;

    /**
     * setup page adapter
     * @param context context
     * @param fm fragment manger
     * @param myBundle activity to context transferring
     */
    public QRDetailSectionsPagerAdapter(Context context, FragmentManager fm, Bundle myBundle) {
        super(fm);
        mContext = context;
        myHereBundle = myBundle;

    }

    /**
     * return correct fragment
     * @param position currently clicked location
     */
    @NonNull
    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        qrDetailGeneralFragment = new QRDetailGeneralFragment(myHereBundle);
        qrDetailImageFragment = new QRDetailImageFragment(myHereBundle);
        qrDetailLocationFragment = new QRDetailLocationFragment(myHereBundle);


        if(position == 0) {
            return qrDetailGeneralFragment;
        } else if (position==1){
            return qrDetailImageFragment;
        } else if (position==2) {
            return qrDetailLocationFragment;
        } else {
            return qrDetailGeneralFragment;
        }
    }

    /**
     * return position
     */
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    /**
     * return count
     */
    @Override
    public int getCount() {
        // Show 2 total pages.
        return 3;
    }

    /**
     * when other than new mode ,sync to ui
     * @param generalView view info
     * @param imageView view info
     * @param userName username
     * @param data all data in hashpmap from firebase
     */
    public void infoCallBack(View generalView,View imageView, String userName, HashMap<String, Object> data){
        qrDetailGeneralFragment.ActivityCallBack(generalView, userName,data);
        qrDetailLocationFragment.ActivityCallBack(userName,data);
        qrDetailImageFragment.ActivityCallBack(imageView,userName,data);
    }

}