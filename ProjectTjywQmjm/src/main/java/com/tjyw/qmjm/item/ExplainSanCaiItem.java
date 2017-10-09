package com.tjyw.qmjm.item;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.tjyw.atom.network.model.Explain;
import com.tjyw.atom.pub.inject.From;
import com.tjyw.atom.pub.item.AtomPubFastAdapterAbstractItem;
import com.tjyw.qmjm.ClientQmjmApplication;
import com.tjyw.qmjm.R;

import java.util.List;

/**
 * Created by stephen on 15/08/2017.
 */
public class ExplainSanCaiItem extends AtomPubFastAdapterAbstractItem<Explain.SanCai, ExplainSanCaiItem, ExplainSanCaiItem.DestinyFooterHolder> {

    public ExplainSanCaiItem(Explain.SanCai src) {
        super(src);
    }

    @Override
    public DestinyFooterHolder getViewHolder(View v) {
        return new DestinyFooterHolder(v);
    }

    @Override
    public int getType() {
        return R.id.atom_pub_resFasterAdapterType_ExplainDestinyFooterItem;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.atom_explain_destiny_sancai;
    }

    @Override
    public void bindView(DestinyFooterHolder holder, List<Object> payloads) {
        super.bindView(holder, payloads);
        holder.onBindView(ClientQmjmApplication.getContext(), src);
    }

    public static class DestinyFooterHolder extends AtomPubFastAdapterAbstractItem.AtomPubFastAdapterItemHolder<Explain.SanCai> {

        @From(R.id.bodyWuXing)
        protected TextView bodyWuXing;

        @From(R.id.bodyJiXiong)
        protected TextView bodyJiXiong;

        @From(R.id.bodyZongLun)
        protected TextView bodyZongLun;

        @From(R.id.bodyJiChuYun)
        protected TextView bodyJiChuYun;

        @From(R.id.bodyChengGongYun)
        protected TextView bodyChengGongYun;

        @From(R.id.bodyRenJiGuanXi)
        protected TextView bodyRenJiGuanXi;

        @From(R.id.bodyXingGe)
        protected TextView bodyXingGe;

        public DestinyFooterHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onBindView(Context context, Explain.SanCai sanCai) {
            bodyWuXing.setText(sanCai.sancai);
            bodyJiXiong.setText(sanCai.jixiong);
            bodyZongLun.setText(sanCai.zonglun);
            bodyJiChuYun.setText(sanCai.jichuyun);
            bodyChengGongYun.setText(sanCai.chenggongyun);
            bodyRenJiGuanXi.setText(sanCai.renjiyun);
            bodyRenJiGuanXi.setText(sanCai.renjiyun);
            bodyXingGe.setText(sanCai.xingge);
        }
    }
}