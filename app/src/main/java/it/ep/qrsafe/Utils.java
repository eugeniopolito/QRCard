package it.ep.qrsafe;

import android.content.ContentProviderOperation;
import android.provider.ContactsContract;

import java.util.ArrayList;

/**
 * Created by eugenio on 05/06/15.
 */
public class Utils {


    /**
     * Parse the vCard and returns a {@link Contact} object.
     *
     * @param vcard
     * @return
     */
    public static Contact parseVcard(String vcard) {
        String[] tokens = vcard.split(";");
        Contact c = new Contact();
        for (String s : tokens) {
            if (s.startsWith(Contents.Type.CARD_TYPE)) {
                c.setName(s.split(":")[2]);
            }
            if (s.startsWith(Contents.Type.ADR)) {
                c.setAddress(s.split(":")[1]);
            }
            if (s.startsWith(Contents.Type.ORG)) {
                c.setOrganization(s.split(":")[1]);
            }
            if (s.startsWith(Contents.Type.TEL)) {
                c.setPhone(s.split(":")[1]);
            }
            if (s.startsWith(Contents.Type.EMAIL_STR)) {
                c.setEmail(s.split(":")[1]);
            }
            if (s.startsWith(Contents.Type.NOTE)) {
                c.setNotes(s.split(":")[1]);
            }
        }
        return c;
    }

    /**
     * Create the contact to add to the address book.
     *
     * @param c
     * @return
     */
    public static ArrayList<ContentProviderOperation> createContact(Contact c) {
        ArrayList<ContentProviderOperation> cpo = new ArrayList<ContentProviderOperation>();
        cpo.add(ContentProviderOperation.newInsert(
                ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        if (c.getName() != null && !c.getName().isEmpty()) {
            cpo.add(ContentProviderOperation.newInsert(
                    ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(
                            ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                            c.getName()).build());
        }

        if (c.getAddress() != null && !c.getAddress().isEmpty()) {
            cpo.add(ContentProviderOperation.newInsert(
                    ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
                    .withValue(
                            ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS,
                            c.getAddress()).build());
        }


        if (c.getPhone() != null && !c.getPhone().isEmpty()) {
            cpo.add(ContentProviderOperation.
                    newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, c.getPhone())
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                    .build());
        }

        if (c.getEmail() != null && !c.getEmail().isEmpty()) {
            cpo.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Email.DATA, c.getEmail())
                    .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                    .build());
        }

        if (c.getOrganization() != null && !c.getOrganization().isEmpty()) {
            cpo.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, c.getOrganization())
                    .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
                    .build());
        }

        if (c.getNotes() != null && !c.getNotes().isEmpty()) {
            cpo.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Note.NOTE, c.getNotes())
                    .build());
        }

        return cpo;
    }

}
