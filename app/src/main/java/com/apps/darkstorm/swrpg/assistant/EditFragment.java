package com.apps.darkstorm.swrpg.assistant;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.apps.darkstorm.swrpg.assistant.dice.DiceHolder;
import com.apps.darkstorm.swrpg.assistant.sw.Editable;

public class EditFragment extends Fragment {
    public EditFragment() {}
    public static EditFragment newInstance(Editable c) {
        EditFragment fragment = new EditFragment();
        fragment.c = c;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_editable_edit, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.dice_roll).setVisible(true);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.dice_roll){AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
            final View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_dice_roll,null);
            b.setView(view);
            view.findViewById(R.id.instant_recycler).setVisibility(View.GONE);
            view.findViewById(R.id.instant_dice_text).setVisibility(View.GONE);
            view.findViewById(R.id.fab_space).setVisibility(View.GONE);
            view.findViewById(R.id.dice_reset).setVisibility(View.GONE);
            view.findViewById(R.id.dice_label).setVisibility(View.GONE);
            final DiceHolder dh = new DiceHolder();
            final DiceRollFragment.DiceList dl = new DiceRollFragment.DiceList(getActivity(),dh);
            RecyclerView r = (RecyclerView)view.findViewById(R.id.dice_recycler);
            r.setAdapter(dl);
            r.setLayoutManager(new LinearLayoutManager(getActivity()));
            b.setPositiveButton(R.string.roll_text, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dh.roll().showDialog(getActivity());
                    dialog.cancel();
                }
            }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            b.show();
            return true;
        }else
            return super.onOptionsItemSelected(item);
    }

    Editable c;

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
                        return EditGeneral.newInstance(c);
                    case 1:
                        return NotesFragment.newInstance(c);
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
                //TODO: use resource strings
                switch(position){
                    case 0:
                        return "General";
                    case 1:
                        return "Notes";
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
        if(((SWrpg)getActivity().getApplication()).prefs.getBoolean(getString(R.string.google_drive_key),false))
            c.startEditing(getActivity(),((SWrpg)getActivity().getApplication()).charsFold.getDriveId());
        else{
            c.startEditing(getActivity());
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof OnCharacterEditInteractionListener)) {
            throw new RuntimeException(context.toString()
                    + " must implement OnCharacterEditInteractionListener");
        }
    }

    public interface OnCharacterEditInteractionListener {}
}
