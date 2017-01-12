package com.apps.darkstorm.swrpg;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.darkstorm.swrpg.load.LoadCharacters;
import com.apps.darkstorm.swrpg.load.LoadMinions;
import com.apps.darkstorm.swrpg.sw.Character;
import com.apps.darkstorm.swrpg.sw.Minion;

import java.util.ArrayList;

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
        final TabLayout tabs = (TabLayout)top.findViewById(R.id.tabs);
        ViewPager pager = (ViewPager)top.findViewById(R.id.pager);
        final CharacerList charList = CharacerList.newInstance(handle);
        final MinionList miniList = MinionList.newInstance(handle);
        FragmentPagerAdapter adap = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch(position){
                    case 0:
                        return charList;
                    case 1:
                        return miniList;
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
        FloatingActionButton fab = (FloatingActionButton)getActivity().findViewById(R.id.uni_fab);
        fab.hide();
        FloatingActionButton add = (FloatingActionButton)top.findViewById(R.id.gm_add_fab);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(tabs.getSelectedTabPosition()){
                    case 0:
                        LoadCharacters lc = new LoadCharacters(getActivity());
                        ArrayList<Integer> taken = new ArrayList<>();
                        for(Character chara:lc.characters)
                            taken.add(chara.ID);
                        int id = 0;
                        for (int i = 0;i<taken.size();i++){
                            if(taken.get(i)==id){
                                id++;
                                i = -1;
                            }
                        }
                        getChildFragmentManager().beginTransaction().replace
                                (R.id.fragment_holder,CharacterEditMain.newInstance(id)).commit();
                        charList.loadChars();
                        break;
                    case 1:
                        LoadMinions lm = new LoadMinions(getActivity());
                        ArrayList<Integer> takenList = new ArrayList<>();
                        for(Minion chara:lm.minions)
                            takenList.add(chara.ID);
                        int newID = 0;
                        for (int i = 0;i<takenList.size();i++){
                            if(takenList.get(i)==newID){
                                newID++;
                                i = -1;
                            }
                        }
                        getChildFragmentManager().beginTransaction().replace
                                (R.id.fragment_holder,MinionEditMain.newInstance(newID)).commit();
                        miniList.loadMinions();
                        break;
                }
            }
        });
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
