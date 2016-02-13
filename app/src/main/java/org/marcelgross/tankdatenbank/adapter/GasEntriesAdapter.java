package org.marcelgross.tankdatenbank.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.marcelgross.tankdatenbank.R;
import org.marcelgross.tankdatenbank.entity.GasEntry;
import org.marcelgross.tankdatenbank.util.Round;

import java.util.List;

/**
 * Created by marcelgross on 13.02.16.
 */
public class GasEntriesAdapter extends RecyclerView.Adapter<GasEntriesAdapter.EntryViewHolder> {

    private Context context;
    private List<GasEntry> entries;
    private OnEntryClickListener listener;

    public interface OnEntryClickListener {
        void onClickListener(int id);
    }

    public GasEntriesAdapter( Context context, List<GasEntry> entries, OnEntryClickListener listener ) {
        this.context = context;
        this.entries = entries;
        this.listener = listener;
    }

    @Override
    public EntryViewHolder onCreateViewHolder( ViewGroup parent, int viewType ) {
        int layout = R.layout.card_gas_entrie;
        View view = LayoutInflater.from( parent.getContext() ).inflate( layout, parent, false );
        return new EntryViewHolder( view );

    }

    @Override
    public void onBindViewHolder( EntryViewHolder holder, int position ) {
        holder.assignData( entries.get( position ) );


    }

    @Override
    public int getItemCount() {
        return entries == null ? 0 : entries.size();
    }

    public class EntryViewHolder extends RecyclerView.ViewHolder {
        public final TextView date;
        public final TextView prizeLiter;
        public final TextView prize;
        public final View view;

        public EntryViewHolder( View view ) {
            super( view );
            date = (TextView) view.findViewById( R.id.date );
            prizeLiter = (TextView) view.findViewById( R.id.prize_liter );
            prize = (TextView) view.findViewById( R.id.prize );
            this.view = view;

        }

        public void assignData( final GasEntry entry ) {
            date.setText( String.format( "%d.%d.%d", entry.getDay(), entry.getMonth(), entry.getYear() ) );
            prizeLiter.setText( context.getString( R.string.prize_liter_with_currency, Round.roundToString( entry.getPrice_liter() ) ));
            prize.setText( context.getString( R.string.prize_with_currency, Round.roundToString( entry.getPrice_liter() * entry.getLiter() ) ));
            view.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick( View v ) {
                    listener.onClickListener(entry.getId());
                }
            } );
        }

    }
}
