package de.freewarepoint.whohasmystuff;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;


/**
 * Created by steven on 2/21/17.
 */
//Not completed, need to alter how the program queries the dates
public class ShowDates extends ListLentObjects {


    String name;
    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        this.name = args.getString("key");
    }
    @Override
    protected int getIntentTitle() {
        return R.string.date_title;
    }


    @Override
    protected Cursor getDisplayedObjects() {
        Log.d("name",name);
        return mDbHelper.fetchDates(name);
    }

}
