package ru.devsp.apps.market.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import ru.devsp.apps.market.R;

/**
 * Фрагмент с изображением
 * Created by gen on 15.12.2017.
 */

public class ImageFragment extends Fragment {

    private static final String ARG_URL = "url";

    private OnClickListener mListener;
    private ImageView mImage;

    public interface OnClickListener {
        void onClick();
    }

    public static ImageFragment getInstance(String url) {
        Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        ImageFragment fragment = new ImageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);
        mImage = view.findViewById(R.id.iv_image);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Glide.with(this)
                .load(getArguments().getString(ARG_URL))
                .priority(Priority.LOW)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .dontTransform()
                .crossFade()
                .into(mImage);

        mImage.setOnClickListener(v -> {
            if(mListener != null){
                mListener.onClick();
            }
        });
    }

    public void setOnClickListener(OnClickListener listener) {
        mListener = listener;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mListener = null;
    }

}
