package com.apps.darkstorm.swrpg.assistant;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class GuideMain extends Fragment {

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
        if (!(context instanceof OnGuideInteractionListener)){
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_guide_main, container, false);
    }

    public void onViewCreated(final View top,final Bundle saved){
        TabLayout taber = (TabLayout)top.findViewById(R.id.guide_taber);
        ViewPager pager = (ViewPager)top.findViewById(R.id.guide_pager);
        taber.setupWithViewPager(pager);
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
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
                        tmp = getActivity().getLayoutInflater().inflate(R.layout.guide_dice,group,false);
                        group.addView(tmp);
                        return tmp;
                    case 1:
                        tmp = getActivity().getLayoutInflater().inflate(R.layout.guide_skill_basic,group,false);
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
        if (((SWrpg)getActivity().getApplication()).prefs.getBoolean(getString(R.string.ads_key),true)) {
            AdView ads = new AdView(getActivity());
            ads.setAdSize(AdSize.BANNER);
            LinearLayout.LayoutParams adLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            adLayout.weight = 0;
            adLayout.gravity = Gravity.CENTER_HORIZONTAL;
            ads.setLayoutParams(adLayout);
            ads.setAdUnitId(getString(R.string.free_banner_ad_id));
            AdRequest adRequest = new AdRequest.Builder().addKeyword("Star Wars").addKeyword("Tabletop Roleplay").addKeyword("RPG").build();
            ads.loadAd(adRequest);
            LinearLayout topLinLay = (LinearLayout)top.findViewById(R.id.top_lay);
            topLinLay.addView(ads,topLinLay.getChildCount());
        }
    }

    public interface OnGuideInteractionListener {}
}
