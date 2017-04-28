package de.freewarepoint.whohasmystuff;

import android.database.Cursor;
import android.os.Bundle;


/**
 * Created by darko on 2017-03-06.
 */

public class
SortResults extends ListLentObjects{
    int myInt;

    public void setArguments(Bundle args)
    {
        super.setArguments(args);
        this.myInt =args.getInt("item", -1);
    }


    @Override
    protected int getIntentTitle() {
        return R.string.app_name;
    }

    @Override
    boolean optionsMenuAvailable() {
        return true;
    }

    @Override
    protected int getEditAction() {
        return AddObject.ACTION_EDIT_LENT;
    }

    @Override
    protected boolean redirectToDefaultListAfterEdit() {
        return false;
    }

    @Override
    protected Cursor getDisplayedObjects() {
        if (myInt == 0)
        {
            return mDbHelper.sortByDate();
        }
        else if (myInt == 1)
        {
            return mDbHelper.sortByName();
        }
        else
        {
            return mDbHelper.sortByItem();
        }

    }


    @Override
    protected boolean isMarkAsReturnedAvailable() {
        return true;
    }




}