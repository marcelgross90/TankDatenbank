package org.marcelgross.tankdatenbank.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.marcelgross.tankdatenbank.Globals;
import org.marcelgross.tankdatenbank.R;
import org.marcelgross.tankdatenbank.activity.EditEntryActivity;
import org.marcelgross.tankdatenbank.adapter.GasEntriesAdapter;
import org.marcelgross.tankdatenbank.database.EntryDBHelper;
import org.marcelgross.tankdatenbank.entity.GasEntry;

import java.util.List;


public class EntriesListFragment extends Fragment implements GasEntriesAdapter.OnEntryClickListener {

    private int vehicleID;
    private int year;
    private int month;
    private GasEntriesAdapter adapter;
    private EntryDBHelper entryDBHelper;

    public static EntriesListFragment getInstance(int vehicleID, int year, int month) {
        EntriesListFragment fragment = new EntriesListFragment();
        Bundle bundle = new Bundle(  );
        bundle.putInt( Globals.VEHICLE_ID, vehicleID );
        bundle.putInt( Globals.YEAR, year );
        bundle.putInt( Globals.MONTH, month );
        fragment.setArguments( bundle );
        return fragment;
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState ) {
        View view = inflater.inflate( R.layout.fragment_entries_list, container, false );

        entryDBHelper = EntryDBHelper.getInstance( getActivity() );

        Bundle bundle = getArguments();
        vehicleID = bundle.getInt( Globals.VEHICLE_ID );
        year = bundle.getInt( Globals.YEAR );
        month = bundle.getInt( Globals.MONTH );

        List<GasEntry> entries = entryDBHelper.readAllEntriesByIdAndYearAndMonth( vehicleID, year, month );

        RecyclerView recyclerView = (RecyclerView) view.findViewById( R.id.entries_list );
        adapter = new GasEntriesAdapter( getActivity(), entries, this );
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( getContext() );
        recyclerView.setLayoutManager( linearLayoutManager );
        recyclerView.setAdapter( adapter );

        return view;
    }

    @Override
    public void onClickListener(int id) {
        Intent intent = new Intent( getActivity(), EditEntryActivity.class );
        intent.putExtra( Globals.VEHICLE_ID, vehicleID );
        intent.putExtra( Globals.ENTRY_ID,  id);
        startActivity( intent );
    }
}
