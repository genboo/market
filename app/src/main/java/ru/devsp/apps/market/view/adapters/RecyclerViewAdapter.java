package ru.devsp.apps.market.view.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Абстрактный адаптер для списков
 * Created by gen on 28.09.2017.
 */

public abstract class RecyclerViewAdapter<T, H extends RecyclerView.ViewHolder> extends  RecyclerView.Adapter<H>  {

    private static final int DEFAULT_CLICK_DELAY = 100;

    private OnItemClickListener mOnItemClickListener;
    List<T> mItems;

    public interface OnItemClickListener {
        void click(int position);
    }

    RecyclerViewAdapter(List<T> items) {
        if (items == null) {
            mItems = new ArrayList<>();
        } else {
            mItems = items;
        }
    }

    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }

    public void setItems(List<T> items) {
        mItems = items;
    }

    public List<T> getItems(){
        return mItems;
    }

    public T getItem(int position){
        if(position >= mItems.size()){
            return null;
        }
        return mItems.get(position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    void onItemClick(View view, int position){
        if (position != RecyclerView.NO_POSITION && mOnItemClickListener != null) {
            view.postDelayed(() -> mOnItemClickListener.click(position), DEFAULT_CLICK_DELAY);
        }
    }

}
