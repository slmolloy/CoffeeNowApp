package coffeenow.com.coffeenowapp.fragments;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.TextView;

import coffeenow.com.coffeenowapp.R;
import coffeenow.com.coffeenowapp.models.CoffeeMaker;
import coffeenow.com.coffeenowapp.tasks.AddCoffeeMakerTask;

public class FragmentAddCoffeeMaker extends Fragment implements
        LocationListener {

    private final String LOG_TAG = FragmentAddCoffeeMaker.class.getSimpleName();
    private Context mContext;
    private TextView mName;
    private RadioGroup mLocation;
    private TextView mVolume;
    private CheckBox mPrivate;

    private Location mLastLocation = null;

    public FragmentAddCoffeeMaker() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        try {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1, this);
            mLastLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } catch (SecurityException e) {
            Log.e(LOG_TAG, "Error", e);
        }
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_add_coffee_maker, container, false);
        mName = (TextView) rootView.findViewById(R.id.create_coffee_maker_name);
        mLocation = (RadioGroup) rootView.findViewById(R.id.create_coffee_maker_location_group);
        mVolume = (TextView) rootView.findViewById(R.id.create_coffee_maker_volume);
        mPrivate = (CheckBox) rootView.findViewById(R.id.create_coffee_maker_private);

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        if (fab != null) {
            fab.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_menu_save));
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CoffeeMaker cm = new CoffeeMaker(mName.getText().toString());
                    switch (mLocation.getCheckedRadioButtonId()) {
                        case R.id.create_coffee_maker_location_home:
                            cm.setLocation("home");
                            break;
                        case R.id.create_coffee_maker_location_office:
                            cm.setLocation("office");
                            break;
                        case R.id.create_coffee_maker_location_other:
                        default:
                            cm.setLocation("other");
                    }
                    cm.setVolume(Integer.parseInt(mVolume.getText().toString()));
                    cm.setPrivate(mPrivate.isChecked());
                    if (mLastLocation != null) {
                        cm.setLatitude(mLastLocation.getLatitude());
                        cm.setLongitude(mLastLocation.getLongitude());
                    }

                    AddCoffeeMakerTask task = new AddCoffeeMakerTask(mContext, cm);
                    task.execute(cm);
                    getFragmentManager().popBackStack();
                }
            });
        }

        return rootView;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            mLastLocation = location;
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
