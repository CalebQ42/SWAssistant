package com.apps.darkstorm.swrpg;

import android.content.Context;
import android.content.SharedPreferences;
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

import com.apps.darkstorm.swrpg.StarWars.Character;
import com.apps.darkstorm.swrpg.StarWars.Minion;

import java.util.ArrayList;

public class MinCharList extends Fragment {

    private OnMinCharInteractionListener mListener;

    Handler gm = null;

    public MinCharList() {
    }

    public static MinCharList newInstance() {
        MinCharList fragment = new MinCharList();
        return fragment;
    }

    public static MinCharList newInstance(Handler handle) {
        MinCharList fragment = new MinCharList();
        fragment.gm = handle;
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
        final View top = inflater.inflate(R.layout.fragment_min_char_list, container, false);
        final FloatingActionButton fab = (FloatingActionButton)getActivity().findViewById(R.id.universeFab);
        final SharedPreferences pref = getActivity().getSharedPreferences(getString(R.string.preference_key),Context.MODE_PRIVATE);
        fab.show();
        fab.setImageResource(R.drawable.ic_add);
        final ViewPager pager = (ViewPager)top.findViewById(R.id.pager);
        TabLayout tabs = (TabLayout)top.findViewById(R.id.tabs);
        tabs.setupWithViewPager(pager);
        final Handler handle = new Handler(Looper.getMainLooper()){
            public void handleMessage(Message in){
                if (gm != null){
                    Message out = gm.obtainMessage();
                    out.obj = in.obj;
                    gm.sendMessage(out);
                }else {
                    if (in.obj instanceof Character) {
                        Character chara = (Character) in.obj;
                        getFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                                android.R.anim.fade_in, android.R.anim.fade_out).replace(R.id.content_navigation, CharacterEditMain.newInstance(chara))
                                .addToBackStack("Editing " + chara.name).commit();
                    } else if (in.obj instanceof Minion) {
                        Minion minion = (Minion) in.obj;
                        getFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                                android.R.anim.fade_in, android.R.anim.fade_out).replace(R.id.content_navigation, MinionEditMain.newInstance(minion))
                                .addToBackStack("Editing " + minion.name).commit();
                    }
                }
            }
        };
        final CharacterList charList = CharacterList.newInstance(handle,gm!=null);
        final MinionList minList = MinionList.newInstance(handle,gm!=null);
        final FragmentPagerAdapter adap = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch(position){
                    case 0:
                        return charList;
                    case 1:
                        return minList;
                    default:
                        return null;
                }
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
        pager.setAdapter(adap);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pager.getCurrentItem()==0){
                    final SharedPreferences pref = getActivity().getSharedPreferences(getString(R.string.preference_key),Context.MODE_PRIVATE);
                    if (!(pref.getBoolean(getString(R.string.cloud_key),false) && pref.getBoolean(getString(R.string.sync_key),true) &&
                            (((SWrpg)getActivity().getApplication()).gac == null ||
                                    !((SWrpg)getActivity().getApplication()).gac.isConnected()))){
                        ArrayList<Integer> has = new ArrayList<>();
                        for (Character chara : charList.chars) {
                            has.add(chara.ID);
                        }
                        int max = 0;
                        while (has.contains(max)) {
                            max++;
                        }
                        getFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                                android.R.anim.fade_in, android.R.anim.fade_out).replace(R.id.content_navigation, CharacterEditMain.newInstance(max))
                                .addToBackStack("Creating a new Character").commit();
                        fab.hide();
                    }
                }else{
                    final SharedPreferences pref = getActivity().getSharedPreferences(getString(R.string.preference_key),Context.MODE_PRIVATE);
                    if (!(pref.getBoolean(getString(R.string.cloud_key),false) && pref.getBoolean(getString(R.string.sync_key),true) &&
                            (((SWrpg)getActivity().getApplication()).gac == null ||
                                    !((SWrpg)getActivity().getApplication()).gac.isConnected()))){
                        ArrayList<Integer> has = new ArrayList<>();
                        for (Minion minion : minList.minions) {
                            has.add(minion.ID);
                        }
                        int max = 0;
                        while (has.contains(max)) {
                            max++;
                        }
                        getFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                                android.R.anim.fade_in, android.R.anim.fade_out).replace(R.id.content_navigation, MinionEditMain.newInstance(max))
                                .addToBackStack("Creating a new Minion").commit();
                        fab.hide();
                    }
                }
            }
        });
        return top;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMinCharInteractionListener) {
            mListener = (OnMinCharInteractionListener) context;
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

    public interface OnMinCharInteractionListener {
        void onMinCharInteraction();
    }
}
