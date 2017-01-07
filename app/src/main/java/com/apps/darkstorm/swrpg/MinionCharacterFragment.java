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

import com.apps.darkstorm.swrpg.sw.Minion;

public class MinionCharacterFragment extends Fragment {

    private OnMinionCharacterListInteraction mListener;

    public MinionCharacterFragment() {}

    public static MinionCharacterFragment newInstance() {
        return new MinionCharacterFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View top = inflater.inflate(R.layout.fragment_minion_character, container, false);
        final Handler handle = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                if(msg.obj instanceof Character){
                    getFragmentManager().beginTransaction().replace(R.id.content_main,CharacterEditMain.newInstance((Character)msg.obj))
                            .setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out,
                                    android.R.anim.fade_in,android.R.anim.fade_out).addToBackStack("").commit();
                }else if(msg.obj instanceof Minion){
                    getFragmentManager().beginTransaction().replace(R.id.content_main,MinionEditMain.newInstance((Minion)msg.obj))
                            .setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out,
                                    android.R.anim.fade_in,android.R.anim.fade_out).addToBackStack("").commit();
                }
            }
        };
        TabLayout tab = (TabLayout)top.findViewById(R.id.tabLayout);
        ViewPager pager = (ViewPager)top.findViewById(R.id.pager);
        tab.setupWithViewPager(pager);
        FragmentPagerAdapter adap = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public android.support.v4.app.Fragment getItem(int position) {
                switch(position){
                    case 0:
                        return CharacerList.newInstance(handle);
                    case 1:
                        return MinionList.newInstance(handle);
                }
                return null;
            }

            @Override
            public int getCount() {
                return 2;
            }
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
        return top;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMinionCharacterListInteraction) {
            mListener = (OnMinionCharacterListInteraction) context;
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
    public interface OnMinionCharacterListInteraction {}
}
