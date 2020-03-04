package com.shanlin.sxf;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shanlin.sxf.diyview.MyViewItemFragment;
import com.shanlin.sxf.diyview.NoScrollViewPager;
import com.shanlin.sxf.diyview.TreasureViewFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainUIActivity extends AppCompatActivity {
    @BindView(R.id.viewPager)
    NoScrollViewPager viewPager;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;


    MainFragment mainFragment;

    private String[] strings = new String[]{"Tab01", "Tab02", "Tab03", "Tab04"};
    private int[] tabRes = new int[]{R.mipmap.tab01, R.mipmap.tab02, R.mipmap.tab03, R.mipmap.tab02};

    private ViewPagerAdapter viewPagerAdapter;

    private List<Fragment> fragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            String saveId = savedInstanceState.getString("saveId");
            if (!TextUtils.isEmpty(saveId)) {
                Toast.makeText(MainUIActivity.this, saveId, Toast.LENGTH_SHORT).show();
            }
        }
        setContentView(R.layout.activity_main_ui);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        //tabLayout
        for (int i = 0; i < strings.length; i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            View inflate = LayoutInflater.from(this).inflate(R.layout.tab_item_layout, null);
            ImageView tabIv = inflate.findViewById(R.id.tab_iv);
            TextView tabTv = inflate.findViewById(R.id.tab_tv);
            tabIv.setBackgroundResource(tabRes[i]);
            tabTv.setText(strings[i]);
            if (i == 0) {
                tabTv.setTextColor(Color.RED);
            }
            tab.setCustomView(inflate);
            tabLayout.addTab(tab);
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View customView = tab.getCustomView();
                ((TextView) customView.findViewById(R.id.tab_tv)).setTextColor(getResources().getColor(R.color.red));
                viewPager.setCurrentItem(tab.getPosition(), false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //上一个被点击的位置、也就是类似于记住了 lastPosition
                View customView = tab.getCustomView();
                ((TextView) customView.findViewById(R.id.tab_tv)).setTextColor(getResources().getColor(R.color.white));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        fragmentList = new ArrayList<>();
        mainFragment = new MainFragment();
        fragmentList.add(mainFragment);
        fragmentList.add(RecyclerViewDecorationFragment.newInstance());
        fragmentList.add(TreasureViewFragment.newInstance());
        fragmentList.add(MyViewItemFragment.newInstance());
        viewPager.setAdapter(viewPagerAdapter);
        //这里不设置预加载个数--试着用懒加载--不写默认会渲染1个View
        //设置预加载的数量、、这样不会导致切换Tab的时候、页面重现渲染加载、因为未设置的时候默认只加载相邻的一个，
        // 这样会导致之前加载的Fragment进行onDestroyView(),可以理解为一个容器、容量有限、新的进老的出（直接销毁）。老的进需要再创建
        viewPager.setOffscreenPageLimit(fragmentList.size() - 1);
        tabLayout.getTabAt(0).select();

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 0x31) {
            if (data != null) {
                byte[] imageBytes = data.getByteArrayExtra("imageByte");
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                mainFragment.image.setImageBitmap(bitmap);
            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        Bundle bundle = new Bundle();
        bundle.putString("saveId", "HelloWorld");
        onSaveInstanceState(bundle);
    }

    //Activity-不会主动调用--需要手动调用--一般在onPause()之前调用
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Log.e("aa", "onSaveInstance-Activity");
    }

    //AppCompatActivity
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("aa", "onSaveInstance_AppCompatActivity");
    }

}
