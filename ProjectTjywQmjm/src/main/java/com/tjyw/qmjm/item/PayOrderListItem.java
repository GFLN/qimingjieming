package com.tjyw.qmjm.item;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.tjyw.atom.pub.inject.From;
import com.tjyw.atom.pub.item.AtomPubFastAdapterAbstractItem;
import com.tjyw.qmjm.ClientQmjmApplication;
import com.tjyw.qmjm.R;

import java.util.List;

/**
 * Created by stephen on 25/08/2017.
 */
public class PayOrderListItem extends AtomPubFastAdapterAbstractItem<String, PayOrderListItem, PayOrderListItem.PayOrderListHolder> {

    public PayOrderListItem(String src) {
        super(src);
    }

    @Override
    public PayOrderListHolder getViewHolder(View v) {
        return new PayOrderListHolder(v);
    }

    @Override
    public int getType() {
        return R.id.atom_pub_resFasterAdapterType_PayOrderListItem;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.atom_pay_order_list_body;
    }

    @Override
    public void bindView(PayOrderListHolder holder, List<Object> payloads) {
        super.bindView(holder, payloads);
        holder.onBindView(ClientQmjmApplication.getContext(), src);
    }

    public static class PayOrderListHolder extends AtomPubFastAdapterAbstractItem.AtomPubFastAdapterItemHolder<String> {

        @From(R.id.bodyOrderNo)
        protected TextView bodyOrderNo;

        @From(R.id.bodyTitle)
        protected TextView bodyTitle;

        @From(R.id.bodyPrice)
        protected TextView bodyPrice;

        @From(R.id.bodyOrderDate)
        protected TextView bodyOrderDate;

        public PayOrderListHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onBindView(Context context, String s) {

        }
    }
}