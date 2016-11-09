package com.apps.darkstorm.swrpg;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class GuideMain extends Fragment {
    private OnGuideInteractionListener mListener;

    public GuideMain() {}

    public static GuideMain newInstance() {
        return new GuideMain();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnGuideInteractionListener) {
            mListener = (OnGuideInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View top = inflater.inflate(R.layout.fragment_guide_main, container, false);
        TabLayout taber = (TabLayout)top.findViewById(R.id.guide_taber);
        ViewPager pager = (ViewPager)top.findViewById(R.id.guide_pager);
        taber.setupWithViewPager(pager);
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.universeFab);
        fab.hide();
        PagerAdapter adap = new PagerAdapter() {
            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            public Object instantiateItem(ViewGroup group,int position){
                View tmp;
                switch(position){
                    case 0:
                        tmp = diceView(inflater,group);
                        group.addView(tmp);
                        return tmp;
                    case 1:
                        tmp = skillBasicView(inflater,group);
                        group.addView(tmp);
                        return tmp;
                    default:
                        return null;
                }
            }

            public CharSequence getPageTitle(int position){
                switch(position){
                    case 0:
                        return "Dice";
                    case 1:
                        return "Skill Basics";
                    default:
                        return "";
                }
            }
        };
        pager.setAdapter(adap);
        return top;
    }

    public View skillBasicView(LayoutInflater inflater, ViewGroup container){
        return inflater.inflate(R.layout.guide_skill_basic,container,false);
    }

    public View diceView(LayoutInflater inflater, ViewGroup container){
        return inflater.inflate(R.layout.guide_dice,container,false);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public interface OnGuideInteractionListener {
        void onGuideInteraction();
    }
}
