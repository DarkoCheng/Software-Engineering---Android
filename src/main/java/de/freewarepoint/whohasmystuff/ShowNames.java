package de.freewarepoint.whohasmystuff;

import android.database.Cursor;
import android.os.Bundle;

import de.freewarepoint.whohasmystuff.AbstractListFragment;
import de.freewarepoint.whohasmystuff.AddObject;
import de.freewarepoint.whohasmystuff.R;


/**
 * Created by steven on 3/20/17.
 */
public class ShowNames extends ListLentObjects {


    String phoneNum;
    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        this.phoneNum = args.getString("key");
    }
    @Override
    protected int getIntentTitle() {
        return R.string.name_title;
    }

    @Override
    protected Cursor getDisplayedObjects() {
        return mDbHelper.fetchName(phoneNum);
    }

}
