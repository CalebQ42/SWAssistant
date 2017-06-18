package com.apps.darkstorm.swrpg.assistant;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.darkstorm.swrpg.assistant.sw.Editable;

public class EditFragment extends Fragment {
    public EditFragment() {}
    public static EditFragment newInstance(Editable ed) {
        EditFragment fragment = new EditFragment();
        fragment.ed = ed;
        return fragment;
    }
    Handler parentHandle = null;
    public static EditFragment newInstance(Editable ed,Handler parentHandle) {
        EditFragment fragment = new EditFragment();
        fragment.ed = ed;
        fragment.parentHandle = parentHandle;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_editable_edit, container, false);
    }

    Editable ed;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final FloatingActionButton fab = (FloatingActionButton)getActivity().findViewById(R.id.fab);
        TabLayout taby = (TabLayout)view.findViewById(R.id.tabLayout);
        ViewPager pager = (ViewPager)view.findViewById(R.id.pager);
        taby.setupWithViewPager(pager);
        FragmentPagerAdapter adap = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch(position){
                    case 0:
                        return EditGeneral.newInstance(ed,parentHandle);
                    case 1:
                        return NotesFragment.newInstance(ed);
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
                        return getString(R.string.general);
                    case 1:
                        return getString(R.string.notes);
                    default:
                        return "";
                }
            }
        };
        pager.setAdapter(adap);
        taby.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()){
                    case 0:
                        fab.hide();
                        break;
                    case 1:
                        if(getChildFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":" + 1)
                                .getChildFragmentManager().findFragmentById(R.id.content_notes) instanceof NotesListFragment){
                            fab.show();
                        }else{
                            fab.hide();
                        }
                        break;
                }
            }
            public void onTabUnselected(TabLayout.Tab tab) {}
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ed!= null)
            ed.startEditing(getActivity());
        else
            getFragmentManager().popBackStack();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (ed != null)
            ed.stopEditing();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof OnCharacterEditInteractionListener)) {
            throw new RuntimeException(context.toString()
                    + " must implement OnCharacterEditInteractionListener");
        }
    }

    interface OnCharacterEditInteractionListener {}
}
