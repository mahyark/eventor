package com.eventor.ui.listItem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.eventor.R;
import com.eventor.model.Activity;

public class ListItemFragment extends Fragment {

    private String TAG = ListItemFragment.class.getSimpleName();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_listitem, container, false);
        final TextView vTitle = root.findViewById(R.id.text_Title);
        final TextView vCategory = root.findViewById(R.id.text_Category);
        final TextView vType = root.findViewById(R.id.text_Type);
        final TextView vStreet = root.findViewById(R.id.text_Street);
        final TextView vPostcode = root.findViewById(R.id.text_Postcode);
        final TextView vCity = root.findViewById(R.id.text_City);

        Activity currentActivity = (Activity) getArguments().getSerializable("selectedActivity");
        vTitle.setText(currentActivity.name);
        vCategory.setText(currentActivity.category);
        vType.setText(currentActivity.type);
        vStreet.setText(currentActivity.street + " " + currentActivity.housenumber);
        vPostcode.setText(Integer.toString(currentActivity.postcode));
        vCity.setText(currentActivity.city);

        return root;
    }
}