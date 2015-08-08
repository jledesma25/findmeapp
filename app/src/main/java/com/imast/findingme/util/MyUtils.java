package com.imast.findingme.util;

import android.widget.ArrayAdapter;
import android.widget.Spinner;

import app.imast.com.findingme.model.District;

/**
 * Created by lmcornejov on 08/08/2015.
 */
public class MyUtils {

    public static void SelectSpinnerItemByValue(Spinner spnr, int value)
    {
        ArrayAdapter<District> adapter = (ArrayAdapter<District>) spnr.getAdapter();
        for (int position = 0; position < adapter.getCount(); position++)
        {

            if(adapter.getItem(position).getId() == value)
            {
                spnr.setSelection(position);
                return;
            }
        }
    }

}
