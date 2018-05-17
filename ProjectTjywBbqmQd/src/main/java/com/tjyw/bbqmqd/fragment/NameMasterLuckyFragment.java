package com.tjyw.bbqmqd.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tjyw.atom.network.conf.IApiField;
import com.tjyw.atom.network.conf.ICode;
import com.tjyw.atom.network.model.PayService;
import com.tjyw.atom.network.param.ListRequestParam;
import com.tjyw.atom.network.presenter.NamingPresenter;
import com.tjyw.atom.network.presenter.listener.OnApiPayPostListener;
import com.tjyw.atom.network.services.HttpPayServices;
import com.tjyw.bbqmqd.R;
import com.tjyw.bbqmqd.activity.BaseActivity;
import com.tjyw.bbqmqd.factory.IClientActivityLaunchFactory;

import atom.pub.inject.From;
import nucleus.factory.RequiresPresenter;

/**
 * Created by stephen on 17-8-11.
 */
@RequiresPresenter(NamingPresenter.class)
public class NameMasterLuckyFragment extends BaseFragment<NamingPresenter<NameMasterRecommendFragment>> implements OnApiPayPostListener.PostPayListVipListener {

    public static NameMasterLuckyFragment newInstance(ListRequestParam param) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(IApiField.P.param, param.clone());

        NameMasterLuckyFragment fragment = new NameMasterLuckyFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @From(R.id.payServiceContainer)
    protected ViewGroup payServiceContainer;

    @From(R.id.payServiceClose)
    protected ImageView payServiceClose;

    @From(R.id.payServiceName)
    protected ImageView payServiceName;

    @From(R.id.payServicePrice)
    protected TextView payServicePrice;

    @From(R.id.payServiceOldPrice)
    protected TextView payServiceOldPrice;

    @From(R.id.payServiceBuy)
    protected ImageView payServiceBuy;

    @From(R.id.payServiceSuit)
    protected ImageView payServiceSuit;

    protected ListRequestParam listRequestParam;

    protected PayService payService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.atom_pay_service, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        payServiceContainer.setBackgroundResource(android.R.color.transparent);
        payServiceClose.setVisibility(View.GONE);

        listRequestParam = (ListRequestParam) pGetSerializableExtra(IApiField.P.param);
        if (null != listRequestParam) {
            maskerShowProgressView(false);
            getPresenter().postPayListVipDiscount(
                    HttpPayServices.VIP_ID.TJJM,
                    listRequestParam.surname,
                    listRequestParam.day
            );
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ICode.SECTION.PAY:
                switch (resultCode) {
                    case ICode.PAY.ALIPAY_SUCCESS:
                    case ICode.PAY.WX_SUCCESS:
                        if (null != data) {
                            listRequestParam.orderNo = data.getStringExtra(IApiField.O.orderNo);
                            IClientActivityLaunchFactory.launchNamingListActivity((BaseActivity) getActivity(), listRequestParam);
                        }
                }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.payServiceBuy:
                IClientActivityLaunchFactory.launchPayOrderActivity(this, listRequestParam, payService);
                break ;
            case R.id.payServiceSuit:
                maskerShowProgressView(true);
                getPresenter().postPayListVipDiscount(
                        HttpPayServices.VIP_ID.NEW_SUIT,
                        listRequestParam.surname,
                        listRequestParam.day
                );
            default:
                super.onClick(v);
        }
    }

    @Override
    public void postOnPayListVipSuccess(int type, PayService payService) {
        maskerHideProgressView();
        switch (payService.id) {
            case HttpPayServices.VIP_ID.NEW_SUIT:
                IClientActivityLaunchFactory.launchPayOrderActivity(this, listRequestParam, payService);
                return ;
            default:
                this.payService = payService;
        }

        payServiceBuy.setOnClickListener(this);
        payServiceSuit.setOnClickListener(this);

        payServicePrice.setText(String.valueOf(payService.money));

        payServiceOldPrice.setText(payService.oldMoney);
        payServiceOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);

        payServiceName.setImageResource(R.drawable.atom_pub_png_pay_window_lucky);
    }
}
