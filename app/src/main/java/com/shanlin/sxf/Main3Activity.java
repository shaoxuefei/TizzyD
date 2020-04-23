package com.shanlin.sxf;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Main3Activity extends AppCompatActivity {
    private TabLayout tabLayout;
    private AppBarLayout appBarlayout;
    private float density;
    private TextView tv_title;
    private CollapsingToolbarLayout collapsToolLayout;
    private ViewPager viewPager;
    private String[] tabTitle = new String[]{"Tab01", "Tab02"};
    private ArrayList<PageItemView> arrayList = new ArrayList<>();
    private Button btnStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        initView();
        initPageView();
    }

    private void initView() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        density = metrics.density;

//        collapsToolLayout = findViewById(R.id.collapsToolLayout);
//        collapsToolLayout.setTitle("Title");
        appBarlayout = findViewById(R.id.appBarlayout);
        tabLayout = findViewById(R.id.tabLayout);
        btnStr = findViewById(R.id.btnStr);
        appBarlayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                Toast.makeText(Main3Activity.this,verticalOffset+"",Toast.LENGTH_SHORT).show();
                testUtils.toastMsg();
                /**
                 layout_collapseMode:pin属性已经是默认是隐藏  会在对应的CollapsingToolbarLayout滑动到底部的时候现实出来
                 */

                /**
                 *如果外部不用CollapsingToolbarLayout这个父布局的话  可以这样判断隐藏
                 */
//                int scroll = appBarLayout.getHeight() + verticalOffset;
//                int appHeight = (int) (tabLayout.getHeight() + 50 * density);
//                if (scroll <= appHeight) {
//                    Log.e("aa", "visiable");
//                    tv_title.setVisibility(View.VISIBLE);
//                } else {
//                    Log.e("aa", "Gone");
//                    tv_title.setVisibility(View.GONE);
//                }
            }
        });
    }
    TestUtils testUtils;
    private void initPageView() {
        viewPager = findViewById(R.id.viewPager);
        PageItemView pageItemView01 = new PageItemView(this);
        PageItemView pageItemView02 = new PageItemView(this);
        testUtils=new TestUtils();
        testUtils.setListArray(arrayList);
        arrayList=new ArrayList<>();
        arrayList.add(pageItemView01);
        arrayList.add(pageItemView02);
        ViewpagerAdapter viewpagerAdapter = new ViewpagerAdapter();
        viewPager.setAdapter(viewpagerAdapter);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            //已經點擊的Position
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Toast.makeText(Main3Activity.this, tab.getText(), Toast.LENGTH_SHORT).show();
            }

            //未點擊的Position
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                CharSequence text = tab.getText();
            }

            //重复点击回调的position
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Toast.makeText(Main3Activity.this, tab.getText(), Toast.LENGTH_SHORT).show();
            }
        });


        btnStr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PageItemView pageItemView = arrayList.get(0);
                pageItemView.updateData();
            }
        });

    }


    class ViewpagerAdapter extends PagerAdapter {



        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(arrayList.get(position));
            return arrayList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(arrayList.get(position));
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        //获取Tablayout的标题头
        @Override
        public CharSequence getPageTitle(int position) {
            if (position < tabTitle.length) {
                return tabTitle[position];
            }
            return "Tab";
        }
    }
}
