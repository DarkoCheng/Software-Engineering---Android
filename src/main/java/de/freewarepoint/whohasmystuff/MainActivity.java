package de.freewarepoint.whohasmystuff;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Menu;
import android.widget.SearchView;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    public static final int ACTION_ADD = 1;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mPlanetTitles;

    public static final String LOG_TAG = "WhoHasMyStuff";
    public static final String FIRST_START = "FirstStart";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        if (findViewById(R.id.mainActivity) != null) {
            if (savedInstanceState != null) {
                return;
            }

            ListLentObjects firstFragment = new ListLentObjects();

            firstFragment.setArguments(getIntent().getExtras());

            getFragmentManager().beginTransaction()
                    .add(R.id.mainActivity, firstFragment).commit();
        }
        mTitle = mDrawerTitle = getTitle();
        mPlanetTitles = getResources().getStringArray(R.array.menus);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.ic_sidebar, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mPlanetTitles));
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        break;
                    case 1:
                        Fragment newFragment = new CategoryListFragment();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.mainActivity, newFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        mDrawerLayout.closeDrawers();
                        break;
                    case 2:
                        Fragment newFragment1 = new NameListFragment();
                        FragmentTransaction transaction1 = getFragmentManager().beginTransaction();
                        transaction1.replace(R.id.mainActivity, newFragment1);
                        transaction1.addToBackStack(null);
                        transaction1.commit();
                        mDrawerLayout.closeDrawers();
                        mDrawerLayout.closeDrawers();
                        break;
                    case 3:
                        Fragment newFragment2 = new DateListFragment();
                        FragmentTransaction transaction2 = getFragmentManager().beginTransaction();
                        transaction2.replace(R.id.mainActivity, newFragment2);
                        transaction2.addToBackStack(null);
                        transaction2.commit();
                        mDrawerLayout.closeDrawers();
                        break;
                    case 4:
                        break;
                    case 5:
                        Bundle dataBundle = new Bundle();
                        dataBundle.putInt("item", 0);
                        Fragment newFragment3 = new SortResults();
                        newFragment3.setArguments(dataBundle);
                        FragmentTransaction transaction3 = getFragmentManager().beginTransaction();
                        transaction3.replace(R.id.mainActivity, newFragment3);
                        transaction3.addToBackStack(null);
                        transaction3.commit();
                        mDrawerLayout.closeDrawers();
                        break;
                    case 6:
                        Bundle dataBundle1 = new Bundle();
                        dataBundle1.putInt("item", 1);
                        Fragment newFragment4 = new SortResults();
                        newFragment4.setArguments(dataBundle1);
                        FragmentTransaction transaction4 = getFragmentManager().beginTransaction();
                        transaction4.replace(R.id.mainActivity, newFragment4);
                        transaction4.addToBackStack(null);
                        transaction4.commit();
                        mDrawerLayout.closeDrawers();
                        break;
                    case 7:
                        Bundle dataBundle2 = new Bundle();
                        dataBundle2.putInt("item", 2);
                        Fragment newFragment5 = new SortResults();
                        newFragment5.setArguments(dataBundle2);
                        FragmentTransaction transaction5 = getFragmentManager().beginTransaction();
                        transaction5.replace(R.id.mainActivity, newFragment5);
                        transaction5.addToBackStack(null);
                        transaction5.commit();
                        mDrawerLayout.closeDrawers();
                        break;
                }
            }
        });
        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_sidebar,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.search, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();

        ComponentName cn = new ComponentName(this, SearchActivity.class);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(cn));
        searchView.setQueryHint(getResources().getString(R.string.search_hint));
        int x = menu.size();
        return super.onCreateOptionsMenu(menu);
    }
    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        Intent i;
        // Handle action buttons
        switch (item.getItemId()) {
            default:

                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
}
