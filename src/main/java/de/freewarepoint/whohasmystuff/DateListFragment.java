package de.freewarepoint.whohasmystuff;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.util.Log;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import de.freewarepoint.whohasmystuff.database.OpenLendDbAdapter;

public class DateListFragment extends ListFragment implements OnItemClickListener {
    private OpenLendDbAdapter mDbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.categoryfragment, container, false);
        getActivity().setTitle("Dates");

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        mDbHelper = OpenLendDbAdapter.getInstance(getActivity());
        mDbHelper.open();
        super.onActivityCreated(savedInstanceState);
        setListAdapter(contactNameAdapter());
        getListView().setOnItemClickListener(this);
    }
//Will be replacecd to more adequately display the dates
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
        Bundle bundle=new Bundle();
        //Need to find a better way to bundle the date for query
        bundle.putString("key",contactNameAdapterSearch().getItem(position));
        Fragment newFragment = new ShowDates();
        newFragment.setArguments(bundle);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.mainActivity, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private ArrayAdapter<String> contactNameAdapter() {

        List<String> names = new ArrayList<String>();

        Cursor allItems = mDbHelper.fetchAllObjects();
        int columnIndex = allItems.getColumnIndex(OpenLendDbAdapter.KEY_DATE);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat df2 = new SimpleDateFormat("MMMM yyyy");
        while (allItems.moveToNext()) {
            String name = allItems.getString(columnIndex).trim();
            //Turn the string into a data, then back with the format to display by month
            try
            {
                Date date = df.parse(name);
                name = df2.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (!names.contains(name)) {
                    names.add(name);
            }
        }
        allItems.close();
        Collections.reverse(names);
        return new ArrayAdapter<String>(getActivity().getApplicationContext().getApplicationContext(),  android.R.layout.simple_list_item_1, names);
    }
    private ArrayAdapter<String> contactNameAdapterSearch() {

        List<String> names = new ArrayList<String>();



        Cursor allItems = mDbHelper.fetchAllObjects();
        int columnIndex = allItems.getColumnIndex(OpenLendDbAdapter.KEY_DATE);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat df2 = new SimpleDateFormat("yyyy-MM");
        while (allItems.moveToNext()) {
            String name = allItems.getString(columnIndex).trim();
            //Changes the date into a format that can be used to search the database
            try
            {
                Date date = df.parse(name);
                name = df2.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (!names.contains(name)) {
                names.add(name);
            }
        }
        allItems.close();
        Collections.reverse(names);

        return new ArrayAdapter<String>(getActivity().getApplicationContext().getApplicationContext(),  android.R.layout.simple_list_item_1, names);
    }

}