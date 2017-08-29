package com.shanlin.sxf;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class Main3Activity extends AppCompatActivity {
    private TabLayout tabLayout;
    private CoordinatorLayout coordinatorlayout;
    private AppBarLayout appBarlayout;
    private Toolbar toolbar;
    private float density;
    private TextView tv_title;
    private CollapsingToolbarLayout collapsToolLayout;
    private ViewPager viewPager;
    private String[] tabTitle=new String[]{"Tab01","Tab02"};
    private ArrayList<PageItemView> arrayList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        initView();
        initPageView();
    }

    private void initView(){
        DisplayMetrics metrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        density = metrics.density;

        collapsToolLayout= (CollapsingToolbarLayout) findViewById(R.id.collapsToolLayout);
        collapsToolLayout.setTitle("Title");
        tv_title= (TextView) findViewById(R.id.tv_title);
        toolbar= (Toolbar) findViewById(R.id.toolBar);
        coordinatorlayout= (CoordinatorLayout) findViewById(R.id.coordinatorlayout);
        appBarlayout= (AppBarLayout) findViewById(R.id.
                appBarlayout);
        tabLayout= (TabLayout) findViewById(R.id.tabLayout);
//        tabLayout.addTab(tabLayout.newTab().setText("Tab01"));
//        tabLayout.addTab(tabLayout.newTab().setText("Tab02"));
//        tabLayout.addTab(tabLayout.newTab().setText("Tab03"));
        //设置AppbarLayout的滑动变化监听
        appBarlayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int scroll=appBarLayout.getHeight()+verticalOffset;
                int appHeight= (int) (tabLayout.getHeight()+50*density);
                    if(scroll<=appHeight){
                        Log.e("aa","visiable");
                        tv_title.setVisibility(View.VISIBLE);
                    }else {
                        Log.e("aa","Gone");
                        tv_title.setVisibility(View.GONE);
                    }
            }
        });
    }
   private void initPageView(){
       viewPager= (ViewPager) findViewById(R.id.viewPager);
       PageItemView pageItemView01=new PageItemView(this);
       PageItemView pageItemView02=new PageItemView(this);
//       PageItemView pageItemView03=new PageItemView(this);
       arrayList.add(pageItemView01);
       arrayList.add(pageItemView02);
//       arrayList.add(pageItemView03);
       ViewpagerAdapter viewpagerAdapter=new ViewpagerAdapter();
       viewPager.setAdapter(viewpagerAdapter);
       tabLayout.setTabMode(TabLayout.MODE_FIXED);
       tabLayout.setupWithViewPager(viewPager);

   }


    class ViewpagerAdapter extends PagerAdapter{

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
            return view==object;
        }
        //获取Tablayout的标题头
        @Override
        public CharSequence getPageTitle(int position) {
            if(position<tabTitle.length) {
                return tabTitle[position];
            }
            return "Tab";
        }
    }
}
