package de.freewarepoint.whohasmystuff;

/**
 * Created by steven on 2017-03-20.
 */

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Activity;


import de.freewarepoint.whohasmystuff.database.OpenLendDbAdapter;

public class CategoryChoice extends Activity {
    protected OpenLendDbAdapter mDbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        alertSimpleListView();

    }
    public void alertSimpleListView() {

        final CharSequence[] items = { "By Type", "By Name", "By Date" };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Bundle dataBundle = new Bundle();

        builder.setOnCancelListener(new AlertDialog.OnCancelListener()
        {
            @Override
            public void onCancel(DialogInterface dialogInterface) {

                finish();
            }

        });

        builder.setTitle("Select by:");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                if (item == 0)
                {
                    //This is the listfragment for choosing a category
                    Fragment newFragment = new CategoryListFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.mainActivity, newFragment);
                    transaction.commit();
                    dialog.dismiss();

                }
                else if(item == 1)
                {
                    //This is the list fragment for selecting by name
                    Fragment newFragment = new NameListFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.mainActivity, newFragment);
                    transaction.commit();
                    dialog.dismiss();
                }
                else if (item ==2)
                {
                    //This is the list fragment for selecting by data (still in progress)
                    Fragment newFragment = new DateListFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.mainActivity, newFragment);
                    transaction.commit();
                    dialog.dismiss();

                }

            }
        }).show().setCanceledOnTouchOutside(false);

    }

}