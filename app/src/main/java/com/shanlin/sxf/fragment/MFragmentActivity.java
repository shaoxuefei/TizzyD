package com.shanlin.sxf.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.shanlin.sxf.R;

public class MFragmentActivity extends AppCompatActivity implements View.OnClickListener {

    private FrameLayout mFrameLayout;
    private Button mTab01,mTab02,mTab03;
    private HomePageFragment homePageFragment;
    private TalkFragment talkFragment;
    private PersonFragment personFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mfragment);
        initFragment();
        initView();
    }
    private void initView(){
        mFrameLayout= (FrameLayout) findViewById(R.id.mFrameLayout);
        mTab01= (Button) findViewById(R.id.mTab01);
        mTab02= (Button) findViewById(R.id.mTab02);
        mTab03= (Button) findViewById(R.id.mTab03);

        mTab01.setOnClickListener(this);
        mTab02.setOnClickListener(this);
        mTab03.setOnClickListener(this);

        setDefaultFragment();
    }

    private void initFragment(){
        homePageFragment=new HomePageFragment();
        talkFragment=new TalkFragment();
        personFragment=new PersonFragment();


    }

    private void setDefaultFragment(){
       FragmentManager fragmentManager=getFragmentManager();
       FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();//开启Fragment的事务
//        fragmentTransaction.add(R.id.mFrameLayout,homePageFragment).commit();
        fragmentTransaction.replace(R.id.mFrameLayout,homePageFragment,"Home");
        fragmentTransaction.commit();

    }

    /**
     * FragmentTransaction---提交事务相同对象只能 提交一次，即commit方法只能执行一次，故不能定义成全局变量
     * @param v
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        FragmentManager fragmentManager=getFragmentManager();
        Fragment home = fragmentManager.findFragmentByTag("Home");
        Fragment talk = fragmentManager.findFragmentByTag("Talk");
        Fragment person = fragmentManager.findFragmentByTag("Person");
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();//开启Fragment的事务
        int cleanBeforeEntryCount = fragmentManager.getBackStackEntryCount();
        Log.e("aa","cleanBeforeCount:"+cleanBeforeEntryCount);
        if(home!=null){
            //回退--退出后退栈栈
            home.getFragmentManager().popBackStack();
        }
        if(talk!=null){
            //回退--退出后退栈栈
            talk.getFragmentManager().popBackStack();
        }
        if(person!=null){
            //回退--退出后退栈栈
            person.getFragmentManager().popBackStack();
        }

        int clearAfterEntryCount = fragmentManager.getBackStackEntryCount();
        Log.e("aa","clearAfterCount:"+clearAfterEntryCount);
        switch (id){
            case R.id.mTab01:
                if(homePageFragment==null){
                    homePageFragment=new HomePageFragment();
                }
                fragmentTransaction.replace(R.id.mFrameLayout,homePageFragment,"Home");
//                homePageFragment.tvHomePage.setOnClickListener(this);
                break;
            case R.id.mTab02:
                if(talkFragment==null){
                    talkFragment=new TalkFragment();
                }
                fragmentTransaction.replace(R.id.mFrameLayout,talkFragment,"Talk");
                break;
            case R.id.mTab03:
                if(personFragment==null){
                    personFragment=new PersonFragment();
                }
                fragmentTransaction.replace(R.id.mFrameLayout,personFragment,"Person");
                break;
            case R.id.tvHomePage:
                //在已存在的即在Back后退栈中的-在其他view中ID也可以找到
                Toast.makeText(this,"HomeFragment",Toast.LENGTH_SHORT).show();
                break;
        }
//        fragmentTransaction.addToBackStack(null);//加入后退栈
        fragmentTransaction.commit();
    }
}
