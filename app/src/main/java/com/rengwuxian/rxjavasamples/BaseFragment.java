// (c)2016 Flipboard Inc, All Rights Reserved.

package com.rengwuxian.rxjavasamples;

import android.app.AlertDialog;
import android.app.Fragment;

import butterknife.OnClick;
import rx.Subscription;

public abstract class BaseFragment extends Fragment {
    protected Subscription subscription;

//雷同代码抽取 不同代码有子类实现 大家都需要的  模块   abstract抽象类多加使用提取共有方法
    @OnClick(R.id.tipBt)
    void tip() {
        new AlertDialog.Builder(getActivity())
                .setTitle(getTitleRes())
                .setView(getActivity().getLayoutInflater().inflate(getDialogRes(), null))
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unsubscribe();  //解除绑定
    }

    protected void unsubscribe() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }


    protected abstract int getDialogRes(); // 由每个子类来实现不同的地方

    protected abstract int getTitleRes();  // protected abstract int getDialogRes(); 内容不同通过


}
