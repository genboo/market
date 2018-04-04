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

import ru.devsp.apps.market.R;
import ru.devsp.apps.market.model.objects.Category;

/**
 * Адаптер для списка категорий
 * Created by gen on 19.12.2017.
 */

public class CategoriesListAdapter extends RecyclerViewAdapter<Category, CategoriesListAdapter.Holder> {


    public CategoriesListAdapter(List<Category> items) {
        super(items);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_category, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Category item = mItems.get(position);

        holder.name.setText(item.title);
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
        TextView name;
        ImageView pic;

        Holder(View itemView) {
            super(itemView);
            block = itemView.findViewById(R.id.cv_item);
            name = itemView.findViewById(R.id.tv_category_title);
            pic = itemView.findViewById(R.id.iv_category_pic);
        }

    }

}
