package de.freewarepoint.whohasmystuff;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import de.freewarepoint.whohasmystuff.database.OpenLendDbAdapter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class NameListFragment extends ListFragment implements OnItemClickListener {
    private OpenLendDbAdapter mDbHelper;
    //Stores the phone numbers of each Lendee that occurs when names are filtered.
    Bundle phoneNums = new Bundle();

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.categoryfragment, container, false);
        getActivity().setTitle("Names");

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        mDbHelper = OpenLendDbAdapter.getInstance(getActivity());
        mDbHelper.open();
        super.onActivityCreated(savedInstanceState);

        //Clear the phone numbers bundle to allow ArrayAdapter to be populated with names
        phoneNums.clear();

        setListAdapter(contactNameAdapter());
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
        Bundle bundle=new Bundle();
        bundle.putString("key",phoneNums.getString(parent.getItemAtPosition(position).toString()));
        Fragment newFragment = new ShowNames();
        newFragment.setArguments(bundle);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.mainActivity, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private ArrayAdapter<String> contactNameAdapter() {
        ContentResolver cr = getActivity().getContentResolver();
        List<String> names = new ArrayList<String>();

        long cId;
        String name;
        String phoneNum;

        Bundle bTemp = new Bundle();

        Cursor allItems = mDbHelper.fetchLentObjects();
        int columnIndex = allItems.getColumnIndex(OpenLendDbAdapter.KEY_LENDEE_PHONE_NUM);

        while (allItems.moveToNext()) {

            //Get phone num.
            phoneNum = allItems.getString(columnIndex).trim();

            //Use phone number to get ID
            cId = ContactHelper.getContactID(phoneNum,cr);

            //If User was found in phone
            if (cId != -1)
            {
                //Get lendee's Name
                name = ContactHelper.getContactName(cId,cr);

                //Add a name=phoneNumber bundle if the name is not already in the bundle
                if (name.length() != 0 && !phoneNums.containsKey(name)) {
                    names.add(name);
                    phoneNums.putString(name,phoneNum);
                }

            }

        }
        allItems.close();

        Collections.sort(names);

        return new ArrayAdapter<String>(getActivity().getApplicationContext().getApplicationContext(),  android.R.layout.simple_list_item_1, names);
    }
    private ArrayAdapter<String> contactNameAdapterSearch() {

        List<String> names = new ArrayList<String>();

        Cursor allItems = mDbHelper.fetchAllObjects();
        int columnIndex = allItems.getColumnIndex(OpenLendDbAdapter.KEY_PERSON);
        while (allItems.moveToNext()) {
            final String name = allItems.getString(columnIndex).trim();
            if (!names.contains(name)) {
                names.add(name);
            }
        }
        allItems.close();

        Collections.sort(names);

        return new ArrayAdapter<String>(getActivity().getApplicationContext().getApplicationContext(),  android.R.layout.simple_list_item_1, names);
    }

}