package com.tjyw.bbqmqd.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mikepenz.fastadapter.adapters.FooterAdapter;
import com.mikepenz.fastadapter.adapters.HeaderAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.tjyw.atom.network.conf.IApiField;
import com.tjyw.atom.network.model.Explain;
import atom.pub.fragment.AtomPubBaseFragment;
import atom.pub.inject.From;
import com.tjyw.bbqmqd.ClientQmjmApplication;
import com.tjyw.bbqmqd.R;
import com.tjyw.bbqmqd.item.ExplainFooterItem;
import com.tjyw.bbqmqd.item.ExplainHeaderItem;
import com.tjyw.bbqmqd.item.ExplainZodiacItem;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

/**
 * Created by stephen on 17-8-11.
 */
public class ExplainMasterZodiacFragment extends AtomPubBaseFragment {

    public static ExplainMasterZodiacFragment newInstance(Explain explain) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(IApiField.E.explain, explain);

        ExplainMasterZodiacFragment fragment = new ExplainMasterZodiacFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @From(R.id.explainZodiacContainer)
    protected RecyclerView explainZodiacContainer;

    protected HeaderAdapter<ExplainHeaderItem> explainHeaderAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.atom_explain_zodiac, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Explain explain = (Explain) pGetSerializableExtra(IApiField.E.explain);
        if (null == explain || null == explain.nameZodiac) {
            return ;
        }

        FastItemAdapter<ExplainZodiacItem> itemAdapter = new FastItemAdapter<ExplainZodiacItem>();
        itemAdapter.add(new ExplainZodiacItem(new Pair<Integer, String>(R.string.atom_pub_resStringExplainZodiacXi, explain.nameZodiac.shengxiaoxi)));
        itemAdapter.add(new ExplainZodiacItem(new Pair<Integer, String>(R.string.atom_pub_resStringExplainZodiacJi, explain.nameZodiac.shengxiaoji)));

        explainHeaderAdapter = new HeaderAdapter<ExplainHeaderItem>();
        explainHeaderAdapter.wrap(itemAdapter);
        explainHeaderAdapter.add(new ExplainHeaderItem(explain));

        FooterAdapter<ExplainFooterItem> footerAdapter = new FooterAdapter<ExplainFooterItem>();
        footerAdapter.wrap(explainHeaderAdapter);
        footerAdapter.add(new ExplainFooterItem(explain));

        explainZodiacContainer.setLayoutManager(new LinearLayoutManager(ClientQmjmApplication.getContext()));
        explainZodiacContainer.setAdapter(explainHeaderAdapter);
        explainZodiacContainer.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(ClientQmjmApplication.getContext())
                        .color(android.R.color.transparent)
                        .sizeResId(R.dimen.atom_pubResDimenRecyclerViewDivider8dp)
                        .build());

    }
}
