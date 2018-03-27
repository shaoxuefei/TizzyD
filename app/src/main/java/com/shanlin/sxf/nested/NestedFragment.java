package com.shanlin.sxf.nested;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.shanlin.sxf.PageItemView;
import com.shanlin.sxf.R;
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
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    @BindView(R.id.nestScrollView)
    NestedScrollView nestScrollView;
    private ArrayList<PageItemView> arrayList;
    private String[] tabTitle = new String[]{"Tab01", "Tab02"};
    int imageHeight;
    PageItemView pageItemView01;
    PageItemView pageItemView02;

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

        linearLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                int linearHeight = linearLayout.getHeight();
                imageHeight = image.getHeight();
                int tabHeight = tabLayout.getHeight();
                int screenHeight = Utils.getInstance().getMetrics(getContext()).heightPixels;
                int navigationBarHeight = Utils.getInstance().getNavigationBarHeight(getContext());
//                Log.e("aa", "屏幕高度" + screenHeight + "虚拟键盘高度" + navigationBarHeight);
                int screenWidth = Utils.getInstance().getMetrics(getContext()).widthPixels;
                float density = Utils.getInstance().getMetrics(getContext()).density;
                int statusBarHeight = Utils.getInstance().getStatusBarHeight(getContext());
                /**
                 * 这种设置滑动位置--需要设置ViewPager的高度  也就是滚动到某个地点的高度（基本超出屏幕）  为了实现NesterdScrollView的滑动
                 * +5  是为了临近点做手势交换判断
                 */
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewPager.getLayoutParams();
                layoutParams.height = screenHeight - tabHeight - statusBarHeight + 5;
                layoutParams.weight = screenWidth;
                linearLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        nestScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                Log.e("aa", "scrollY" + scrollY);
                if (scrollY > imageHeight) {
                    pageItemView01.recyclerView.setNestedScrollingEnabled(true);
                    pageItemView02.recyclerView.setNestedScrollingEnabled(true);
                } else {
                    pageItemView01.recyclerView.setNestedScrollingEnabled(false);
                    pageItemView02.recyclerView.setNestedScrollingEnabled(false);
                }
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
