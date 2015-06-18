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
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Eugenio Polito
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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
