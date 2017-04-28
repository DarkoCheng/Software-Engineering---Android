package de.freewarepoint.whohasmystuff;

import android.graphics.Region;
import android.net.Uri;
import android.os.Bundle;
import de.freewarepoint.whohasmystuff.database.OpenLendDbAdapter;

import java.util.Date;

public class LentObject {

    public LentObject() {
        // Empty constructor
    }

    public LentObject(Bundle bundle) {
        description = bundle.getString(OpenLendDbAdapter.KEY_DESCRIPTION);
        lenderPhoneNum = bundle.getString(OpenLendDbAdapter.KEY_LENDER_PHONE_NUM);
        lendeePhoneNum = bundle.getString(OpenLendDbAdapter.KEY_LENDEE_PHONE_NUM);
        type = bundle.getInt(OpenLendDbAdapter.KEY_TYPE);
        date = new Date(bundle.getLong(OpenLendDbAdapter.KEY_DATE));
        modificationDate = new Date(bundle.getLong(OpenLendDbAdapter.KEY_MODIFICATION_DATE));
        personName = bundle.getString(OpenLendDbAdapter.KEY_PERSON);
        personKey = bundle.getString(OpenLendDbAdapter.KEY_PERSON_KEY);
    }

    public int id;
    public String description;
    public String lenderPhoneNum;
    public String lendeePhoneNum;
    public int type;
    public Date date;
    public Date modificationDate;
    public String personName;
    public String personKey;
    public boolean returned;
    public Uri calendarEventURI;

}
