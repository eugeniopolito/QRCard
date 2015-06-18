package it.ep.qrsafe;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Toast;

/**
 * Created by eugenio on 05/06/15.
 */
public class CreateContactDialog extends DialogFragment {

    private static Context context;
    private static Contact contact;

    public static CreateContactDialog newInstance(Context ctx, Contact c, int title) {
        CreateContactDialog frag = new CreateContactDialog();
        context = ctx;
        contact = c;
        Bundle args = new Bundle();
        args.putInt(ctx.getString(R.string.add_contact), title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int title = getArguments().getInt(getString(R.string.add_contact));

        return new AlertDialog.Builder(getActivity())
                .setMessage(contact.getName())
                .setTitle(title)
                .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                try {
                                    context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, Utils.createContact(contact));
                                    Toast.makeText(context.getApplicationContext(), getString(R.string.contact_added), Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    Toast.makeText(context.getApplicationContext(), getString(R.string.cannot_add_contact), Toast.LENGTH_LONG).show();
                                }

                            }
                        }
                )
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //((FragmentAlertDialog)getActivity()).doNegativeClick();
                            }
                        }
                )
                .create();
    }

}
