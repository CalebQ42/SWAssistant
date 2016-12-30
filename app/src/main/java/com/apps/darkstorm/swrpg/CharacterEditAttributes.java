package com.apps.darkstorm.swrpg;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.apps.darkstorm.swrpg.StarWars.Character;
import com.apps.darkstorm.swrpg.UI.Char.SetupCharAttr;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class CharacterEditAttributes extends Fragment {
    private OnCharEditInteractionListener mListener;
    public Character chara;
    public CharacterEditAttributes() {}

    public static CharacterEditAttributes newInstance(Character chara){
        CharacterEditAttributes fragment = new CharacterEditAttributes();
        fragment.chara = chara;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View top = inflater.inflate(R.layout.fragment_character_edit_attributes, container, false);
        chara.showHideCards(top);
        new SetupCharAttr().setup(top,chara);
        final FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.universeFab);
        fab.hide();
        if (getActivity().getSharedPreferences(getString(R.string.preference_key),Context.MODE_PRIVATE)
                .getBoolean(getString(R.string.ads_key),true)) {
            AdView ads = new AdView(getContext());
            ads.setAdSize(AdSize.BANNER);
            LinearLayout.LayoutParams adLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            adLayout.weight = 0;
            adLayout.topMargin = (int)(5*getResources().getDisplayMetrics().density);
            adLayout.gravity = Gravity.CENTER_HORIZONTAL;
            ads.setLayoutParams(adLayout);
            if (BuildConfig.APPLICATION_ID.equals("com.apps.darkstorm.swrpg"))
                ads.setAdUnitId(getString(R.string.free_banner_ad_id));
            else
                ads.setAdUnitId(getString(R.string.paid_banner_ad_id));
            AdRequest adRequest = new AdRequest.Builder().addKeyword("Star Wars").build();
            ads.loadAd(adRequest);
            LinearLayout topLinLay = (LinearLayout)top.findViewById(R.id.top_lay);
            topLinLay.addView(ads,0);
        }
        return top;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCharEditInteractionListener) {
            mListener = (OnCharEditInteractionListener) context;
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
    public interface OnCharEditInteractionListener {}
}
