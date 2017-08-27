package com.tjyw.qmjm.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.fastadapter.listeners.ClickEventHook;
import com.tjyw.atom.network.IllegalRequestException;
import com.tjyw.atom.network.conf.IApiField;
import com.tjyw.atom.network.conf.ISection;
import com.tjyw.atom.network.presenter.IPost;
import com.tjyw.atom.network.presenter.NamingPresenter;
import com.tjyw.atom.network.presenter.listener.OnApiFavoritePostListener;
import com.tjyw.atom.network.presenter.listener.OnApiPostErrorListener;
import com.tjyw.atom.network.presenter.listener.OnApiPostNamingListener;
import com.tjyw.atom.network.result.RNameDefinition;
import com.tjyw.atom.pub.inject.From;
import com.tjyw.qmjm.ClientQmjmApplication;
import com.tjyw.qmjm.R;
import com.tjyw.qmjm.dialog.NamingPayWindows;
import com.tjyw.qmjm.factory.IClientActivityLaunchFactory;
import com.tjyw.qmjm.item.NamingWordItem;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import nucleus.factory.RequiresPresenter;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by stephen on 14/08/2017.
 */
@RequiresPresenter(NamingPresenter.class)
public class NamingListActivity extends BaseToolbarActivity<NamingPresenter<NamingListActivity>>
        implements OnApiPostErrorListener, OnApiPostNamingListener, OnApiFavoritePostListener.PostFavoriteAddListener {

    protected String postSurname;

    protected int postGender;

    protected int postNameNumber;

    @From(R.id.namingListContainer)
    protected RecyclerView namingListContainer;

    protected FastItemAdapter<NamingWordItem> nameDefinitionAdapter;

    protected RNameDefinition.Param postParam;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

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

        immersionBarWith()
                .fitsSystemWindows(true)
                .statusBarColor(R.color.colorPrimary)
                .statusBarDarkFont(true)
                .init();

        nameDefinitionAdapter = new FastItemAdapter<NamingWordItem>();
        nameDefinitionAdapter.withOnClickListener(new FastAdapter.OnClickListener<NamingWordItem>() {
            @Override
            public boolean onClick(View v, IAdapter<NamingWordItem> adapter, NamingWordItem item, int position) {
                if (null != postParam) {
                    IClientActivityLaunchFactory.launchExplainMasterActivity(
                            NamingListActivity.this,
                            postParam.surname,
                            item.src.getGivenName(),
                            postParam.day,
                            postParam.gender
                    );
                }
                return true;
            }
        }).withItemEvent(new ClickEventHook<NamingWordItem>() {
            @Nullable
            @Override
            public View onBind(@NonNull RecyclerView.ViewHolder viewHolder) {
                if (viewHolder instanceof NamingWordItem.NamingWordHolder) {
                    return ((NamingWordItem.NamingWordHolder) viewHolder).getNameWordCollect();
                } else {
                    return super.onBind(viewHolder);
                }
            }

            @Override
            public void onClick(View v, int position, FastAdapter<NamingWordItem> fastAdapter, NamingWordItem item) {
                if (null != postParam) {
                    maskerShowProgressView(true);
                    getPresenter().postFavoriteAdd(
                            postParam.surname,
                            item.src.getGivenName(),
                            postParam.day,
                            postParam.gender
                    );
                }
            }
        });

        namingListContainer.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        namingListContainer.setAdapter(nameDefinitionAdapter);
        namingListContainer.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(ClientQmjmApplication.getContext())
                        .color(R.color.atom_pub_resColorDivider)
                        .sizeResId(R.dimen.atom_pubResDimenRecyclerViewDividerSize)
                        .marginResId(R.dimen.atom_pubResDimenRecyclerViewDivider16dp)
                        .showLastDivider()
                        .build());

        namingListContainer.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        postParam.nameNumber = postNameNumber;
                        NamingPayWindows.newInstance(getSupportFragmentManager(), postParam);
                }
            }
        });

        maskerOnClick(null);
    }

    @Override
    public void maskerOnClick(View view) {
        maskerShowProgressView(false);
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
        switch (postId) {
            case IPost.Naming:
                if (throwable instanceof IllegalRequestException) {
                    maskerShowMaskerLayout(throwable.getMessage(), R.string.atom_pub_resStringRetry);
                } else {
                    maskerShowMaskerLayout(getString(R.string.atom_pub_resStringNetworkBroken), R.string.atom_pub_resStringRetry);
                }
                break ;
            default:
                maskerHideProgressView();
        }
    }

    @Override
    public void postOnNamingSuccess(RNameDefinition result) {
        maskerHideProgressView();
        if (null != result) {
            postParam = result.param;
            int size = result.size();
            if (size > 0) {
                List<NamingWordItem> itemList = new ArrayList<NamingWordItem>();
                for (int i = 0; i < size; i++) {
                    itemList.add(new NamingWordItem(result.get(i)));
                }

                nameDefinitionAdapter.add(itemList);
            }
        }
    }

    @Override
    public void postOnFavoriteAddSuccess() {
        nameDefinitionAdapter.notifyAdapterDataSetChanged();
    }
}
