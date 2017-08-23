package com.tjyw.qmjm.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.tjyw.atom.network.conf.IApiField;
import com.tjyw.atom.network.conf.ISection;
import com.tjyw.atom.network.model.NameDefinition;
import com.tjyw.atom.network.presenter.NamingPresenter;
import com.tjyw.atom.network.presenter.listener.OnApiPostErrorListener;
import com.tjyw.atom.network.presenter.listener.OnApiPostNamingListener;
import com.tjyw.atom.pub.inject.From;
import com.tjyw.qmjm.ClientQmjmApplication;
import com.tjyw.qmjm.R;
import com.tjyw.qmjm.dialog.NamingPayWindows;
import com.tjyw.qmjm.item.NamingWordItem;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import nucleus.factory.RequiresPresenter;

/**
 * Created by stephen on 14/08/2017.
 */
@RequiresPresenter(NamingPresenter.class)
public class NamingListActivity extends BaseToolbarActivity<NamingPresenter<NamingListActivity>> implements OnApiPostErrorListener, OnApiPostNamingListener {

    protected String postSurname;

    protected int postGender;

    protected int postNameNumber;

    @From(R.id.namingListContainer)
    protected RecyclerView namingListContainer;

    protected FastItemAdapter<NamingWordItem> nameDefinitionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        postSurname = pGetStringExtra(IApiField.S.surname, null);
        if (TextUtils.isEmpty(postSurname)) {
            finish();
            return ;
        } else {
            postGender = pGetIntExtra(IApiField.G.gender, ISection.GENDER.MALE);
            postNameNumber = pGetIntExtra(IApiField.N.nameNumber, ISection.NAME_COUNT.SINGLE);
        }

        setContentView(R.layout.atom_naming_list);
        tSetToolBar(getString(R.string.atom_pub_resStringNamingList));

        nameDefinitionAdapter = new FastItemAdapter<NamingWordItem>();
        nameDefinitionAdapter.withOnClickListener(new FastAdapter.OnClickListener<NamingWordItem>() {
            @Override
            public boolean onClick(View v, IAdapter<NamingWordItem> adapter, NamingWordItem item, int position) {
                return false;
            }
        });

        namingListContainer.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        namingListContainer.setAdapter(nameDefinitionAdapter);
        namingListContainer.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(ClientQmjmApplication.getContext())
                        .color(R.color.atom_pub_resColorDivider)
                        .sizeResId(R.dimen.atom_pubResDimenRecyclerViewDividerSize)
                        .marginResId(R.dimen.atom_pubResDimenRecyclerViewDivider8dp)
                        .build());

        namingListContainer.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        NamingPayWindows.newInstance(getSupportFragmentManager());
                }
            }
        });

        getPresenter().postNameDefinition(
                postSurname,
                pGetStringExtra(IApiField.D.day, null),
                postGender,
                postNameNumber
        );
    }

    @Override
    public void postOnExplainError(int postId, Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void postOnNamingSuccess(List<NameDefinition> result) {
        List<NamingWordItem> itemList = new ArrayList<NamingWordItem>();
        int size = null == result ? 0 : result.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                itemList.add(new NamingWordItem(result.get(i)));
            }

            nameDefinitionAdapter.add(itemList);
        }
    }
}
