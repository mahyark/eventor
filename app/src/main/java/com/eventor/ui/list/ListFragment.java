package com.eventor.ui.list;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eventor.AppDatabase;
import com.eventor.MainActivity;
import com.eventor.R;
import com.eventor.ui.listItem.ListItemFragment;
import com.eventor.model.Activity;

import org.json.JSONObject;

public class ListFragment extends Fragment {
    private AppDatabase db;
    private List<String> activities;
    private ArrayList<String> listItems;
    private ArrayAdapter<String> adapter;
    private ListView listView;
    private EditText editText;
    private final static int REQUEST_CODE_1 = 1;

    public void searchItem(String textToSearch){
        listItems.clear();
        listItems.addAll(activities);
        for(String a:activities){
            if(!a.toLowerCase().contains(textToSearch.toLowerCase())){
                listItems.remove(a);
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void initList(){
        db = AppDatabase.getDatabase(getActivity());
        List<String> items = new ArrayList<String>(db.activityDao().getActivityNames());
        activities = items;
        listItems = new ArrayList<String>(activities);
        adapter = new ArrayAdapter<String>(getContext(), R.layout.list_item, R.id.txtitem, listItems);
        listView.setAdapter(adapter);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        listView=(ListView)view.findViewById(R.id.listview);
        editText=(EditText)view.findViewById(R.id.txtsearch);
        initList();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                db = AppDatabase.getDatabase(getActivity());
                String[] aProps = listView.getItemAtPosition(position).toString().split("~");
                Activity selectedActivity = new Activity(db.activityDao().getActivity(aProps[0].trim(), aProps[1].trim()));

                Bundle bundle = new Bundle();
                bundle.putSerializable("selectedActivity", selectedActivity);

                Fragment frag = new ListItemFragment();
                frag.setArguments(bundle);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.nav_host_fragment, frag);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    // reset listview
                    initList();
                } else {
                    // perform search
                    searchItem(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        return view;
    }
}