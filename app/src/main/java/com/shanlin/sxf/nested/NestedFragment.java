package com.shanlin.sxf.nested;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.shanlin.sxf.PageItemView;
import com.shanlin.sxf.R;
import com.shanlin.sxf.SwipeRefreshLayout;
import com.shanlin.sxf.Utils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author : SXF
 * @ date   : 2018/3/21
 * e-mail  : shaoxf@go-goal.com
 * desc    :
 */

public class NestedFragment extends Fragment {
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    Unbinder unbinder;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.relative)
    RelativeLayout relativeLayout;
    @BindView(R.id.nestScrollView)
    NestedScrollView nestScrollView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    int scaledTouchTapSlop;
    private ArrayList<PageItemView> arrayList;
    private String[] tabTitle = new String[]{"Tab01", "Tab02"};
    int imageHeight;
    PageItemView pageItemView01;
    PageItemView pageItemView02;
    int top;
    float startX, startY;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nested_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    public static NestedFragment newInstance() {

        Bundle args = new Bundle();

        NestedFragment fragment = new NestedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void initView() {
        scaledTouchTapSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

        arrayList = new ArrayList<>();
        pageItemView01 = new PageItemView(getContext());
        pageItemView02 = new PageItemView(getContext());
        pageItemView01.recyclerView.setNestedScrollingEnabled(false);
        pageItemView02.recyclerView.setNestedScrollingEnabled(false);
        pageItemView01.setClick(false);
        pageItemView02.setClick(false);
        arrayList.add(pageItemView01);
        arrayList.add(pageItemView02);
        ViewpagerAdapter viewpagerAdapter = new ViewpagerAdapter();
        viewPager.setAdapter(viewpagerAdapter);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(viewPager);
        relativeLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressLint("NewApi")
            @Override
            public void onGlobalLayout() {
                top = tabLayout.getTop();
                int linearHeight = relativeLayout.getHeight();
                imageHeight = image.getHeight();
                int tabHeight = tabLayout.getHeight();
                int screenHeight = Utils.getInstance().getMetrics(getContext()).heightPixels;
                int navigationBarHeight = Utils.getInstance().getNavigationBarHeight(getContext());
//                Log.e("aa", "屏幕高度" + screenHeight + "虚拟键盘高度" + navigationBarHeight);
                int screenWidth = Utils.getInstance().getMetrics(getContext()).widthPixels;
                float density = Utils.getInstance().getMetrics(getContext()).density;
                int statusBarHeight = Utils.getInstance().getStatusBarHeight(getContext());
                /**
                 * 这种设置滑动位置--需要设置ViewPager的高度  也就是滚动到某个地点的高度（基本超出屏幕）
                 */
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewPager.getLayoutParams();
                layoutParams.height = screenHeight - tabHeight - statusBarHeight;
                layoutParams.width = screenWidth;
                relativeLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        //是以nestScrollView手势为主--当按下手势的时候其默认影响消费的是最外部的View---但是在最外部的View中进行了分配响应--内/外--所以不管内外--都会先走外部的响应滑动事件
        nestScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (refreshLayout != null) {
                    refreshLayout.setEnabled(nestScrollView.getScrollY() == 0);
                }
                if (scrollY < top) {
                    pageItemView01.recyclerView.setNestedScrollingEnabled(false);
                    pageItemView02.recyclerView.setNestedScrollingEnabled(false);
                } else {
                    pageItemView01.recyclerView.setNestedScrollingEnabled(true);
                    pageItemView02.recyclerView.setNestedScrollingEnabled(true);
                }
            }
        });

        pageItemView01.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });


        pageItemView01.recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_HOVER_MOVE:
                        float x = event.getX();
                        float y = event.getY();
                        float absx = Math.abs(x - startX);
                        float absY = Math.abs(y - startY);
                        if (absx > scaledTouchTapSlop && absx > 3 * absY) {
                            return false;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        break;
                }
                return false;
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
