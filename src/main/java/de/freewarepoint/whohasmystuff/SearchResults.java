package de.freewarepoint.whohasmystuff;

import android.database.Cursor;
import android.os.Bundle;

public class SearchResults extends AbstractListFragment {
    String query;

    public void setArguments(Bundle args)
    {
        super.setArguments(args);
        this.query = args.getString("key");
    }

    @Override
    protected int getIntentTitle(){ return R.string.search_title;}

    @Override
    boolean optionsMenuAvailable(){ return false;}

    @Override
    protected int getEditAction(){ return AddObject.ACTION_EDIT_LENT;}

    @Override
    protected boolean redirectToDefaultListAfterEdit(){ return true;}

    @Override
    protected Cursor getDisplayedObjects(){ return mDbHelper.fetchSearchObjects(query);}

    @Override
    protected boolean isMarkAsReturnedAvailable(){ return false;}
}
