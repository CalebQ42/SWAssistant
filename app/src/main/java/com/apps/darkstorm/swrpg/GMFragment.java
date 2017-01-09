package com.apps.darkstorm.swrpg;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.darkstorm.swrpg.sw.Character;
import com.apps.darkstorm.swrpg.sw.Minion;

public class GMFragment extends Fragment {

    private OnGMInteractionListener mListener;

    public GMFragment() {}

    public static GMFragment newInstance() {
        GMFragment fragment = new GMFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View top = inflater.inflate(R.layout.fragment_gm, container, false);
        final Handler handle = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                System.out.println(msg.obj);
                if(msg.obj instanceof Character){
                    getChildFragmentManager().beginTransaction().replace
                            (R.id.fragment_holder,CharacterEditMain.newInstance((Character)msg.obj))
                    .commit();
                }else if(msg.obj instanceof Minion){
                    getChildFragmentManager().beginTransaction().replace
                            (R.id.fragment_holder,MinionEditMain.newInstance((Minion)msg.obj))
                            .commit();
                }
            }
        };
        TabLayout tabs = (TabLayout)top.findViewById(R.id.tabs);
        ViewPager pager = (ViewPager)top.findViewById(R.id.pager);
        FragmentPagerAdapter adap = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch(position){
                    case 0:
                        return CharacerList.newInstance(handle);
                    case 1:
                        return MinionList.newInstance(handle);
                    default:
                        return null;
                }
            }
            @Override
            public int getCount() {
                return 2;
            }
            @Override
            public CharSequence getPageTitle(int position){
                switch(position){
                    case 0:
                        return "Characters";
                    case 1:
                        return "Minions";
                    default:
                        return "";
                }
            }
        };
        pager.setAdapter(adap);
        tabs.setupWithViewPager(pager);
        return top;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnGMInteractionListener) {
            mListener = (OnGMInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnGMInteractionListener {}
}
