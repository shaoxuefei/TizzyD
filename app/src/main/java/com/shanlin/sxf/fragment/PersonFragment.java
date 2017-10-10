package com.shanlin.sxf.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shanlin.sxf.R;

/**
 * @Description:
 * @Auther: Sxf
 * @Date: 2017/10/10
 */

public class PersonFragment extends Fragment {
    Fragment personFragment;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.person_fragment, container,false);
        View viewById = inflate.findViewById(R.id.tvPerson);
        final HomePageFragment homePageFragment=new HomePageFragment();
        viewById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                personFragment = fragmentManager.findFragmentByTag("Person");
                FragmentTransaction fragmentTransaction =fragmentManager.beginTransaction();
                fragmentTransaction.hide(personFragment);
                fragmentTransaction.add(R.id.mFrameLayout,homePageFragment,"Home");
//                fragmentTransaction.replace(R.id.frameLayout,homePageFragment,"Home");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        return inflate;
    }

    @Override
    public void onResume() {
        super.onResume();
//        getFragmentManager().beginTransaction().show(personFragment);
    }
}
