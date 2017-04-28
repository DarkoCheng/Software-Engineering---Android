package de.freewarepoint.whohasmystuff;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import de.freewarepoint.whohasmystuff.database.OpenLendDbAdapter;

/**
 * Created by aman on 2017-03-09.
 */

public class SearchActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        handleIntent(getIntent());
    }

    public void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    public void onListItemClick(ListView l,
                                View v, int position, long id) {
        // call detail activity for clicked entry
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query =
                    intent.getStringExtra(SearchManager.QUERY);
            doSearch(query);
        }
    }

    private void doSearch(String queryStr) {
        // get a Cursor, prepare the ListAdapter
        // and set it
//        SearchResults sr = new SearchResults();
//        Cursor cursor = sr.getDisplayedObjects();
//        startManagingCursor(cursor);
//        ListAdapter adapter = new SimpleCursorAdapter(
//                this,
//                R.layout.row,
//                cursor,
//                new String[]{
//                        "description",
//                        "person",
//                        "date",
//                        "modification_date"
//                },
//                new int[]{
//                        R.id.toptext,
//                        R.id.bottomtext,
//                        R.id.date
//                }
//        );
//        setListAdapter(adapter);
        Bundle dataBundle = new Bundle();
        dataBundle.putString("key", queryStr);
        Fragment newFragment = new SearchResults();
        newFragment.setArguments(dataBundle);
        //@Override
        //onBackPressed();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.mainActivity, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }
    public void onBackPressed() {

        finish(); // finish activity

    }
}
