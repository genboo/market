package ru.devsp.apps.market.view.comopnents;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import ru.devsp.apps.market.view.ImageFragment;

/**
 * Изображения
 * Created by gen on 15.12.2017.
 */

public class ImagePagerAdapter extends FragmentPagerAdapter {

    private String[] mItems;

    private ImageFragment.OnClickListener mListener;

    public ImagePagerAdapter(FragmentManager fm, String[] items) {
        super(fm);
        if (items == null) {
            mItems = new String[]{};
        } else {
            mItems = items;
        }
    }

    public void setItems(String[] items) {
        mItems = items;
    }

    @Override
    public Fragment getItem(int position) {
        ImageFragment fragment = ImageFragment.getInstance(mItems[position]);
        fragment.setOnClickListener(mListener);
        return fragment;
    }

    public void setOnClickListener(ImageFragment.OnClickListener listener) {
        mListener = listener;
    }

    @Override
    public int getCount() {
        return mItems.length;
    }
}