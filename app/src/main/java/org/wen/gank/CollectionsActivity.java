package org.wen.gank;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import butterknife.ButterKnife;
import org.wen.gank.mvp.MvpActivity;

/**
 * created by Jiahui.wen 2017-08-21
 */
public class CollectionsActivity extends MvpActivity<CollectionsView, CollectionsPresenter>
    implements CollectionsView {

    public static void navigato(Context context) {
        Intent intent = new Intent(context, CollectionsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected CollectionsPresenter createPresenter() {
        return new CollectionsPresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collections);
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }
}
