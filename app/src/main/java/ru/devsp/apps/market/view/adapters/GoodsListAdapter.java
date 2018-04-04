package ru.devsp.apps.market.view.adapters;


import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;
import java.util.Locale;

import ru.devsp.apps.market.R;
import ru.devsp.apps.market.model.objects.Good;

/**
 * Адаптер для списка товаров в категории
 * Created by gen on 19.12.2017.
 */

public class GoodsListAdapter extends RecyclerViewAdapter<Good, GoodsListAdapter.Holder> {

    private String mPricePattern;

    public GoodsListAdapter(List<Good> items, String pricePattern) {
        super(items);
        mPricePattern = pricePattern;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_good, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Good item = mItems.get(position);

        holder.name.setText(item.title);
        holder.price.setText(String.format(Locale.getDefault(), mPricePattern, item.price));
        Glide.with(holder.name.getContext())
                .load(item.image)
                .priority(Priority.LOW)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .dontTransform()
                .crossFade()
                .into(holder.pic);

        holder.block.setOnClickListener(v -> onItemClick(v, holder.getAdapterPosition()));
    }

    class Holder extends RecyclerView.ViewHolder {
        CardView block;
        TextView price;
        TextView name;
        ImageView pic;

        Holder(View itemView) {
            super(itemView);
            block = itemView.findViewById(R.id.cv_item);
            name = itemView.findViewById(R.id.tv_good_title);
            price = itemView.findViewById(R.id.tv_good_price);
            pic = itemView.findViewById(R.id.iv_good_pic);
        }

    }

}
