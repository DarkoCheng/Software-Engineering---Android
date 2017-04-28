package de.freewarepoint.whohasmystuff;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.content.Context;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;

/**
 * Created by Niko on 3/22/2017.
 */

public class ContactHelper {

     /**********************************************
     * Method: GetContactPhone
     *
     * Purpose: To obtain a contact's mobile phone number using their contact ID.
     *
     *  Arguments:  - cID:    The contact's contact ID (long)
     *              - cr:     The activity's ContentResolver (ContentResolver)
     *
     *  Returns: - The contact's first mobilePhoneNumber
     *           - An empty string if no number was found
     *************************************************/
    public static String getContactPhoneNumber(long cID, ContentResolver cr)
    {
        String contactNum = "";
        Cursor c_phone = null;
        Cursor c_contact = null;
        int phoneNumType;

        int hasPhoneNum = 0;


        c_contact = cr.query(ContactsContract.Contacts.CONTENT_URI, null,  ContactsContract.Contacts._ID + "= " + String.valueOf(cID), null, null);

        if (c_contact.moveToFirst())
        {
            hasPhoneNum = c_contact.getInt(c_contact.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER));
            if (hasPhoneNum == 1)
            {
                c_phone = cr.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "= "+ cID,
                        null,
                        null);
                while (c_phone.moveToNext())
                {
                    phoneNumType = c_phone.getInt(c_phone.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.TYPE));
                    if (phoneNumType == ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE) {
                        contactNum = c_phone.getString(c_phone.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        break;
                    }
                }
            }
        }
        return contactNum;
    }

    /**********************************************
     * Method: getContactID
     *
     * Purpose: To obtain the contact ID of a contact with the mobile phone number phoneNum.
     *
     *  Arguments:  - phoneNum:    The Phone number of the contact who's ID you wish to obtain (String)
     *              - cr:          The activity's ContentResolver (ContentResolver)
     *
     *  Returns: - The ID of a contact who has the mobile phone number phoneNum
     *           - -1 if no contact has phoneNum as a mobile number
     *************************************************/
    public static long getContactID(String phoneNum,  ContentResolver cr)
    {
        long cID = -1;
        Cursor c_contact = null;
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNum));

        c_contact = cr.query(uri, new String[]{ContactsContract.PhoneLookup._ID},null,null,null);
        if (c_contact.moveToFirst())
        {
            cID = c_contact.getLong(0);
        }
        return cID;
    }

    /**********************************************
     * Method: getContactName
     *
     * Purpose:    To obtain the display name of a contact using their contact ID.
     *
     *  Arguments:  - cID:    The contact's contactID (long)
     *              - cr:     The activity's ContentResolver (ContentResolver)
     *
     *  Returns: - The dislay name of the contact with cID.
     *           - An empty string if no contact with cID was found
     *************************************************/
    public static String getContactName(long cID,  ContentResolver cr)
    {
        String name = "";
        Cursor c_contact = null;

        int hasPhoneNum = 0;

        c_contact = cr.query(ContactsContract.Contacts.CONTENT_URI, null,  ContactsContract.Contacts._ID + "= " + String.valueOf(cID), null, null);

        if (c_contact.moveToFirst())
        {
            name = c_contact.getString(c_contact.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
        }
        return name;

    }

    /**********************************************
     * Method: getContactPictureURI
     *
     * Purpose:    To obtain the contact picture Uri of the contact with the cotactID of cID
     *
     *  Arguments:  - cID:    The contact's contactID (long)
     *              - cr:     The activity's ContentResolver (ContentResolver)
     *
     *  Returns: - A Uri to the contact picture of the contact with ID cID
     *           - null if no contact was found with contactID
     *************************************************/
    public static Uri getContactPictureURI(long cID, ContentResolver cr)
    {
        Uri picUri = null;
        Cursor c_photo = null;

        //Check if the contact has a contact photo
        c_photo = cr.query(
                ContactsContract.Data.CONTENT_URI,
                null,
                ContactsContract.Data.CONTACT_ID + "=" + cID + " AND " + ContactsContract.Data.MIMETYPE + "='" + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE + "'",
                null,
                null);
        if(c_photo.moveToFirst()){
            picUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, cID);
        }

        return picUri;
    }

    /**********************************************
     * Method: picURIToBitmap
     *
     * Purpose:    To get  a bitmap image of a contact picture using it's Uri.
     *
     *  Arguments:  - picUri: A contact Picture URI (Uri)
     *              - cr:     The activity's ContentResolver (ContentResolver)
     *
     *  Returns: - A Bitmap image of the PicUri
     *           - null if image decoding failed
     *************************************************/
    public static Bitmap picURIToBitmap(Uri picUri, ContentResolver cr)
    {
        Bitmap contactPic = null;

        InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(cr, picUri);

        if (inputStream != null) {
            contactPic = BitmapFactory.decodeStream(inputStream);

            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return contactPic;
    }

    /**********************************************
     * Method: setContactImage
     *
     * Purpose: To set an ImageView's source to the contact picture of the contact with the contact CD
     *          cID. If no contact image was found, a defualt contact picture will be set as the image source.
     *
     *  Arguments:  - cID:    The contact's contactID (long)
     *              - cr:     The activity's ContentResolver (ContentResolver)
     *              - image:  The who's Image will be set to the contact Picture (ImageView)
     *
     *************************************************/
    public static void setContactImage(Long cID, ContentResolver cr, ImageView image)
    {
        Bitmap pic;
        Uri picUri;

        //Get the picUri, use the Uri to obtain a bitmap of the picure, and set the bitmap as the image's source
        picUri = ContactHelper.getContactPictureURI(cID, cr);
        if (picUri != null)
        {
            pic = ContactHelper.picURIToBitmap(picUri, cr);
            if (pic != null)
            {
                image.setImageBitmap(pic);
            }
            else
            {
                image.setImageResource(R.drawable.default_contact);
            }
        }
        else
        {
            image.setImageResource(R.drawable.default_contact);
        }

    }
}
