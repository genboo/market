package ru.devsp.apps.market.view.adapters;


import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import ru.devsp.apps.market.R;
import ru.devsp.apps.market.model.objects.Order;

/**
 * Адаптер для списка заказов
 * Created by gen on 19.12.2017.
 */

public class OrdersListAdapter extends RecyclerViewAdapter<Order, OrdersListAdapter.Holder> {

    private String mPricePattern;
    private String mCountPattern;
    private String mNumberPattern;

    public OrdersListAdapter(List<Order> items, String pricePattern, String countPattern, String numberPattern) {
        super(items);
        mPricePattern = pricePattern;
        mCountPattern = countPattern;
        mNumberPattern = numberPattern;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_order, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Order item = mItems.get(position);

        holder.status.setText(item.status);
        holder.price.setText(String.format(Locale.getDefault(), mPricePattern, item.price));
        holder.date.setText(item.time);
        holder.count.setText(String.format(Locale.getDefault(), mCountPattern, item.count));
        holder.number.setText(String.format(Locale.getDefault(), mNumberPattern, item.id));

        holder.block.setOnClickListener(v -> onItemClick(v, holder.getAdapterPosition()));

    }

    class Holder extends RecyclerView.ViewHolder {
        CardView block;
        TextView status;
        TextView price;
        TextView date;
        TextView number;
        TextView count;

        Holder(View itemView) {
            super(itemView);
            block = itemView.findViewById(R.id.cv_item);
            status = itemView.findViewById(R.id.tv_order_status);
            price = itemView.findViewById(R.id.tv_order_price);
            date = itemView.findViewById(R.id.tv_order_date);
            count = itemView.findViewById(R.id.tv_order_count);
            number = itemView.findViewById(R.id.tv_order_number);
        }

    }

}
