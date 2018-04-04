package ru.devsp.apps.market.view.adapters;


import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;
import java.util.Locale;

import ru.devsp.apps.market.R;
import ru.devsp.apps.market.model.objects.Cart;

/**
 * Адаптер для списка товаров в корзине
 * Created by gen on 19.12.2017.
 */

public class CartListAdapter extends RecyclerViewAdapter<Cart, CartListAdapter.Holder> {

    private String mPricePattern;

    public CartListAdapter(List<Cart> items, String pricePattern) {
        super(items);
        mPricePattern = pricePattern;
    }

    private OnChangeCountListener mOnChangeCountListener;
    private OnDeleteListener mOnDeleteListener;

    public interface OnChangeCountListener {
        void change(int position, Cart cart);
    }

    public interface OnDeleteListener {
        void delete(int position, Cart cart);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_cart, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Cart item = mItems.get(position);

        holder.name.setText(item.good.title);
        updateCount(holder.count, item.count);
        holder.price.setText(String.format(Locale.getDefault(), mPricePattern, item.good.price));
        Glide.with(holder.name.getContext())
                .load(item.good.image)
                .priority(Priority.LOW)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .dontTransform()
                .crossFade()
                .into(holder.pic);

        holder.block.setOnClickListener(v -> onItemClick(v, holder.getAdapterPosition()));

        holder.minus.setOnClickListener(v -> {
            if (mOnChangeCountListener != null) {
                if (item.count > 1) {
                    item.count--;
                }
                updateCount(holder.count, item.count);
                mOnChangeCountListener.change(holder.getAdapterPosition(), item);
            }
        });

        holder.plus.setOnClickListener(v -> {
            if (mOnChangeCountListener != null) {
                item.count++;
                updateCount(holder.count, item.count);
                mOnChangeCountListener.change(holder.getAdapterPosition(), item);
            }
        });

        holder.delete.setOnClickListener(v ->{
            if(mOnDeleteListener != null){
                mItems.remove(holder.getAdapterPosition());
                mOnDeleteListener.delete(holder.getAdapterPosition(), item);
            }
        });

    }

    public void add(int position, Cart item){
        mItems.add(position, item);
    }

    private void updateCount(TextView textView, int count) {
        textView.setText(String.format("%s", count));
    }

    public void setOnChangeCountListener(OnChangeCountListener listener) {
        mOnChangeCountListener = listener;
    }

    public void setOnDeleteListener(OnDeleteListener listener) {
        mOnDeleteListener = listener;
    }

    class Holder extends RecyclerView.ViewHolder {
        CardView block;
        TextView name;
        TextView price;
        TextView count;
        ImageView pic;
        ImageButton minus;
        ImageButton plus;
        ImageButton delete;

        Holder(View itemView) {
            super(itemView);
            block = itemView.findViewById(R.id.cv_item);
            name = itemView.findViewById(R.id.tv_good_title);
            price = itemView.findViewById(R.id.tv_good_price);
            count = itemView.findViewById(R.id.tv_cart_count);
            pic = itemView.findViewById(R.id.iv_good_pic);
            minus = itemView.findViewById(R.id.ib_cart_count_minus);
            plus = itemView.findViewById(R.id.ib_cart_count_plus);
            delete = itemView.findViewById(R.id.ib_cart_delete);
        }

    }

}
