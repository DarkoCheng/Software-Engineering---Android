package de.freewarepoint.whohasmystuff;

import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


public class ListLentObjects extends AbstractListFragment {

	@Override
	protected int getIntentTitle() {
		return R.string.app_name;
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
		return mDbHelper.fetchLentObjects();
	}

    @Override
    protected boolean isMarkAsReturnedAvailable() {
        return true;
    }

    @Override

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.main, menu);

    }

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {

        Intent i;

        Intent a;

        switch (item.getItemId()) {

            case R.id.addButton:

                i = new Intent(getActivity(), AddObject.class);

                i.putExtra(AddObject.ACTION_TYPE, AddObject.ACTION_ADD);

                startActivityForResult(i, ACTION_ADD);

                break;

        }

        return true;

    }


    boolean optionsMenuAvailable() {
        return true;
    }




}
