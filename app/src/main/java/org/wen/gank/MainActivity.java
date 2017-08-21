package org.wen.gank;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tabLayout) TabLayout tabLayout;
    @BindView(R.id.viewPager) ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.main_title);
        }

        CommonTabAdapter tabAdapter =
            new CommonTabAdapter.Builder(this, getSupportFragmentManager()).add(
                CategoryFragment.ANDROID, CategoryFragment.getInstance(CategoryFragment.ANDROID))
                .add(CategoryFragment.IOS, CategoryFragment.getInstance(CategoryFragment.IOS))
                .add(CategoryFragment.FRONT, CategoryFragment.getInstance(CategoryFragment.FRONT))
                .into(viewPager);
        viewPager.setOffscreenPageLimit(tabAdapter.getCount());

        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_search) {
            Toast.makeText(this, "search", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.menu_collection) {
            CollectionsActivity.navigato(this);
        }
        return super.onOptionsItemSelected(item);
    }
}
