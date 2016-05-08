package coffeenow.com.coffeenowapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import coffeenow.com.coffeenowapp.R;
import coffeenow.com.coffeenowapp.models.CoffeeMaker;
import coffeenow.com.coffeenowapp.tasks.FetchCoffeeMakersTask;

public class FragmentCoffeeMakers extends Fragment {

    private final String LOG_TAG = FragmentCoffeeMakers.class.getSimpleName();
    private ArrayAdapter<CoffeeMaker> mCoffeeMakerAdapter;
    private Context mContext;

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
        updateCoffeeMakers();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.coffee_maker_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            updateCoffeeMakers();
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
        }

        View rootView = inflater.inflate(R.layout.fragment_coffee_makers, container, false);

        List<CoffeeMaker> coffeeMakerList = new ArrayList<>();

        mCoffeeMakerAdapter = new ArrayAdapter<>(
                getActivity(),
                R.layout.list_item_coffee_maker,
                R.id.list_item_coffee_maker_textView,
                coffeeMakerList);

        ListView listView = (ListView) rootView.findViewById(R.id.listview_coffeemakers);
        listView.setAdapter(mCoffeeMakerAdapter);


        return rootView;
    }

    private void updateCoffeeMakers() {
        FetchCoffeeMakersTask task = new FetchCoffeeMakersTask(mContext, mCoffeeMakerAdapter);
        task.execute();
    }
}
