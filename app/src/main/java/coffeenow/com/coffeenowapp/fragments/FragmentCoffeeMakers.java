package coffeenow.com.coffeenowapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import coffeenow.com.coffeenowapp.R;
import coffeenow.com.coffeenowapp.adapters.CoffeeMakersAdapter;
import coffeenow.com.coffeenowapp.models.CoffeeMaker;
import coffeenow.com.coffeenowapp.tasks.FetchCoffeeMakersTask;

public class FragmentCoffeeMakers extends Fragment {

    private static final String LOG_TAG = FragmentCoffeeMakers.class.getSimpleName();
    private CoffeeMakersAdapter mCoffeeMakersAdapter;
    private Context mContext;
    private CheckBox mMyCoffeeMakers;

    public FragmentCoffeeMakers() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateCoffeeMakers(mMyCoffeeMakers.isChecked());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.coffee_maker_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            updateCoffeeMakers(mMyCoffeeMakers.isChecked());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        if (fab != null) {
            fab.show();
            fab.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_input_add));
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getFragmentManager().beginTransaction()
                            .replace(R.id.content, new FragmentAddCoffeeMaker())
                            .addToBackStack("add-coffee-maker")
                            .commit();
                }
            });
        }

        View rootView = inflater.inflate(R.layout.fragment_coffee_makers, container, false);

        mMyCoffeeMakers = (CheckBox) rootView.findViewById(R.id.myCoffeeMakers);
        mMyCoffeeMakers.setChecked(false);
        mMyCoffeeMakers.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCoffeeMakers(mMyCoffeeMakers.isChecked());
            }
        });

        ArrayList<CoffeeMaker> coffeeMakerList = new ArrayList<>();

        mCoffeeMakersAdapter = new CoffeeMakersAdapter(getContext(), coffeeMakerList);

        ListView listView = (ListView) rootView.findViewById(R.id.listview_coffeemakers);
        listView.setAdapter(mCoffeeMakersAdapter);

        return rootView;
    }

    private void updateCoffeeMakers(boolean showAll) {
        FetchCoffeeMakersTask task = new FetchCoffeeMakersTask(mContext, mCoffeeMakersAdapter, showAll);
        task.execute();
    }
}
