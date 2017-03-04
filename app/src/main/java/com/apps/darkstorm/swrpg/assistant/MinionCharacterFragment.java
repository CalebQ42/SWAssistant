package com.apps.darkstorm.swrpg.assistant;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.darkstorm.swrpg.assistant.load.LoadCharacters;
import com.apps.darkstorm.swrpg.assistant.load.LoadMinions;
import com.apps.darkstorm.swrpg.assistant.sw.Character;
import com.apps.darkstorm.swrpg.assistant.sw.Minion;
import com.apps.darkstorm.swrpg.assistant.R;

import java.util.ArrayList;

public class MinionCharacterFragment extends Fragment {

    private OnMinionCharacterListInteraction mListener;

    public MinionCharacterFragment() {}

    public static MinionCharacterFragment newInstance() {
        return new MinionCharacterFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_minion_character, container, false);
    }

    public void onViewCreated(final View top,Bundle saved){
        final Handler handle = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                if(msg.obj instanceof Character){
                    getFragmentManager().beginTransaction().replace(R.id.content_main,CharacterEditMain.newInstance((Character)msg.obj))
                            .setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out,
                                    android.R.animator.fade_in,android.R.animator.fade_out).addToBackStack("").commit();
                }else if(msg.obj instanceof Minion){
                    getFragmentManager().beginTransaction().replace(R.id.content_main,MinionEditMain.newInstance((Minion)msg.obj))
                            .setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out,
                                    android.R.animator.fade_in,android.R.animator.fade_out).addToBackStack("").commit();
                }
            }
        };
        final TabLayout tab = (TabLayout)top.findViewById(R.id.tabLayout);
        ViewPager pager = (ViewPager)top.findViewById(R.id.pager);
        tab.setupWithViewPager(pager);
        FragmentPagerAdapter adap = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch(position){
                    case 0:
                        return CharacterList.newInstance(handle);
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
        FloatingActionButton fab = (FloatingActionButton)getActivity().findViewById(R.id.uni_fab);
        fab.setImageResource(R.drawable.add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tab.getSelectedTabPosition() == 0){
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
                    if(getActivity()!=null)
                        getFragmentManager().beginTransaction().replace(R.id.content_main,CharacterEditMain.newInstance(id))
                                .setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out,
                                        android.R.animator.fade_in,android.R.animator.fade_out).addToBackStack("").commit();
                }else{
                    LoadMinions lc = new LoadMinions(getActivity());
                    ArrayList<Integer> taken = new ArrayList<>();
                    for(Minion chara:lc.minions)
                        taken.add(chara.ID);
                    int id = 0;
                    for (int i = 0;i<taken.size();i++){
                        if(taken.get(i)==id){
                            id++;
                            i = -1;
                        }
                    }
                    getFragmentManager().beginTransaction().replace(R.id.content_main,MinionEditMain.newInstance(id))
                            .setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out,
                                    android.R.animator.fade_in,android.R.animator.fade_out).addToBackStack("").commit();
                }
            }
        });
        fab.show();
        pager.setAdapter(adap);
        top.requestFocus();
        top.setFocusableInTouchMode(true);
    }

    public void onResume(){
        super.onResume();
        if(((SWrpg)getActivity().getApplication()).prefs.getBoolean(getString(R.string.google_drive_key),false)){
            if(((SWrpg)getActivity().getApplication()).gac==null ||!((SWrpg)getActivity().getApplication()).gac.isConnected())
                ((MainActivity)getActivity()).gacMaker();
        }
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
