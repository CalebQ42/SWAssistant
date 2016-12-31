package com.apps.darkstorm.swrpg;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MinCharList extends Fragment {

    private OnMinCharInteractionListener mListener;

    public MinCharList() {
    }

    public static MinCharList newInstance() {
        MinCharList fragment = new MinCharList();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        final CharacterList charList = CharacterList.newInstance();
        final MinionList minList = MinionList.newInstance();
        FragmentStatePagerAdapter adap = new FragmentStatePagerAdapter(getFragmentManager()) {
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
                    charList.fabAction(fab);
                }else{
                    minList.fabAction(fab);
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
