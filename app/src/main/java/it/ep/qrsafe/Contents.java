package it.ep.qrsafe;

import android.provider.ContactsContract;

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
public final class Contents {
    public static final String URL_KEY = "URL_KEY";
    public static final String NOTE_KEY = "NOTE_KEY";

    public static final String[] PHONE_KEYS = {
            ContactsContract.Intents.Insert.PHONE, ContactsContract.Intents.Insert.SECONDARY_PHONE,
            ContactsContract.Intents.Insert.TERTIARY_PHONE
    };
    public static final String[] PHONE_TYPE_KEYS = {
            ContactsContract.Intents.Insert.PHONE_TYPE,
            ContactsContract.Intents.Insert.SECONDARY_PHONE_TYPE,
            ContactsContract.Intents.Insert.TERTIARY_PHONE_TYPE
    };
    public static final String[] EMAIL_KEYS = {
            ContactsContract.Intents.Insert.EMAIL, ContactsContract.Intents.Insert.SECONDARY_EMAIL,
            ContactsContract.Intents.Insert.TERTIARY_EMAIL
    };
    public static final String[] EMAIL_TYPE_KEYS = {
            ContactsContract.Intents.Insert.EMAIL_TYPE,
            ContactsContract.Intents.Insert.SECONDARY_EMAIL_TYPE,
            ContactsContract.Intents.Insert.TERTIARY_EMAIL_TYPE
    };

    private Contents() {
    }

    public static final class Type {

        public static final String CARD_TYPE = "MECARD";

        public static final String TEXT = "TEXT_TYPE";

        public static final String EMAIL = "EMAIL_TYPE";

        public static final String PHONE = "PHONE_TYPE";

        public static final String SMS = "SMS_TYPE";


        public static final String CONTACT = "CONTACT_TYPE";


        public static final String LOCATION = "LOCATION_TYPE";

        public static final String TEL = "TEL";

        public static final String ADR = "ADR";

        public static final String ORG = "ORG";

        public static final String EMAIL_STR = "EMAIL";

        public static final String NOTE = "NOTE";

        private Type() {
        }
    }
}
