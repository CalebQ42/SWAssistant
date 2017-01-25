package com.apps.darkstorm.swrpg;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.darkstorm.swrpg.sw.Character;

public class CharacterEditMain extends Fragment {
    private OnFragmentInteractionListener mListener;
    Character chara;

    public CharacterEditMain() {}

    public static CharacterEditMain newInstance(Character character) {
        CharacterEditMain fragment = new CharacterEditMain();
        fragment.chara = character;
        return fragment;
    }

    public static CharacterEditMain newInstance(int Id){
        CharacterEditMain fragment = new CharacterEditMain();
        Character tmp = new Character(Id);
        fragment.chara = tmp;
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            if(!((SWrpg)getActivity().getApplication()).hasShortcut(chara))
                ((SWrpg)getActivity().getApplication()).addShortcut(chara,getActivity());
            else
                ((SWrpg)getActivity().getApplication()).updateShortcut(chara,getActivity());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View top = inflater.inflate(R.layout.fragment_character_edit_main, container, false);
        TabLayout taby = (TabLayout)top.findViewById(R.id.tabLay);
        final FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.uni_fab);
        taby.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()){
                    case 0:
                        fab.hide();
                        break;
                    case 1:
                        fab.show();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        ViewPager pager = (ViewPager)top.findViewById(R.id.viewPage);
        taby.setupWithViewPager(pager);
        FragmentPagerAdapter adap = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch(position){
                    case 0:
                        return CharacterEditAttributes.newInstance(chara);
                    case 1:
                        return CharacterEditNotes.newInstance(chara);
                    default:
                        return null;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch(position){
                    case 0:
                        return "Attributes";
                    case 1:
                        return "Notes";
                    default:
                        return "";
                }
            }
        };
        pager.setAdapter(adap);
        top.setFocusableInTouchMode(true);
        top.requestFocus();
        return top;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void onResume(){
        super.onResume();
        SharedPreferences pref = getActivity().getSharedPreferences(getString(R.string.preference_key),Context.MODE_PRIVATE);
        if (pref.getBoolean(getString(R.string.google_drive_key),false) && ((SWrpg)getActivity().getApplication()).gac != null){
            chara.startEditing(getActivity(), ((SWrpg)getActivity().getApplication()).charsFold.getDriveId());
        }else{
            chara.startEditing(getActivity());
        }
    }

    public void onPause(){
        super.onPause();
        chara.stopEditing();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
    }
}
