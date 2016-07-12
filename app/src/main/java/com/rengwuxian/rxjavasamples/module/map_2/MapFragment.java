// (c)2016 Flipboard Inc, All Rights Reserved.

package com.rengwuxian.rxjavasamples.module.map_2;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.rengwuxian.rxjavasamples.BaseFragment;
import com.rengwuxian.rxjavasamples.network.Network;
import com.rengwuxian.rxjavasamples.R;
import com.rengwuxian.rxjavasamples.adapter.ItemListAdapter;
import com.rengwuxian.rxjavasamples.model.Item;
import com.rengwuxian.rxjavasamples.util.GankBeautyResultToItemsMapper;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MapFragment extends BaseFragment {
    private int page = 0;

    @Bind(R.id.pageTv)
    TextView pageTv;
    @Bind(R.id.previousPageBt)
    Button previousPageBt;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.gridRv)
    RecyclerView gridRv;

    ItemListAdapter adapter = new ItemListAdapter();

    // 、、map处理最后想要的和服务器返回不同数据的  中转器 实现对数据的加工处理 gson 获取最迟数据转成的对象
    // 通过map方法经过包装处理
    // 给dialog显示 类中包含dialog就行 dialog 回调 和observable 之间通信还是用借口 让observable实现他人的接口 他人干某事时他能响应


    // observer实现流水信息 类似handle
    Observer<List<Item>> observer = new Observer<List<Item>>() { // 可以对observer 进行封装处理写个抽象类处理相同的方法 抽象处理不同的方法 还能q
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(getActivity(), R.string.loading_failed, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNext(List<Item> images) {
            swipeRefreshLayout.setRefreshing(false);
            pageTv.setText(getString(R.string.page_with_number, page));
            adapter.setItems(images);
        }
    };

    @OnClick(R.id.previousPageBt)
    void previousPage() {
        loadPage(--page);
        if (page == 1) {
            previousPageBt.setEnabled(false);
        }
    }

    @OnClick(R.id.nextPageBt)
    void nextPage() {
        loadPage(++page);
        if (page == 2) {
            previousPageBt.setEnabled(true);
        }
    }

    private void loadPage(int page) { // 加载 不同页面数据
        swipeRefreshLayout.setRefreshing(true);
        unsubscribe();
        subscription = Network.getGankApi()
                .getBeauties(10, page)
                .map(GankBeautyResultToItemsMapper.getInstance()) //单列维护转化 就是一个对象 这个对象call方法对数据进行转换
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);// 最后由观察者 在主线程 实现 拿到数据后的处理
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, view);

        gridRv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        gridRv.setAdapter(adapter);
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        swipeRefreshLayout.setEnabled(false);
        return view;
    }

    @Override
    protected int getDialogRes() {
        return R.layout.dialog_map;
    }

    @Override
    protected int getTitleRes() {
        return R.string.title_map;
    }
}
