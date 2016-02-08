package org.marcelgross.tankdatenbank.fragment;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import org.marcelgross.tankdatenbank.R;

public class DeleteDialogFragment extends DialogFragment {

    public interface DeleteDialog {
        void delete();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState ) {
        return new AlertDialog.Builder( getActivity() )
                .setTitle( R.string.delete_for_sure )
                .setPositiveButton( R.string.yes,
                        new DialogInterface.OnClickListener() {
                            public void onClick( DialogInterface dialog, int whichButton ) {
                                ((DeleteDialog) getActivity()).delete();
                                dialog.dismiss();
                            }
                        }
                )
                .setNegativeButton( R.string.no,
                        new DialogInterface.OnClickListener() {
                            public void onClick( DialogInterface dialog, int whichButton ) {
                                dialog.dismiss();
                            }
                        }
                )
                .create();
    }
}