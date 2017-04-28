package de.freewarepoint.whohasmystuff;

import android.database.Cursor;
import android.os.Bundle;


/**
 * Created by steven on 2/15/17.
 */
public class ShowCategories extends ListLentObjects {

    int position;
    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        this.position = args.getInt("key", -1);
    }
    @Override
    protected int getIntentTitle() {
        return R.string.category_title;
    }


    @Override
    protected Cursor getDisplayedObjects() {
        return mDbHelper.fetchType(position);
    }


}
