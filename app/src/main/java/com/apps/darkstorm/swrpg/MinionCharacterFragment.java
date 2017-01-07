package com.apps.darkstorm.swrpg;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.apps.darkstorm.swrpg.R;

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
        TabLayout tab = (TabLayout)top.findViewById(R.id.tabLayout);
        ViewPager pager = (ViewPager)top.findViewById(R.id.pager);
        tab.setupWithViewPager(pager);
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
