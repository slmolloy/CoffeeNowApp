package coffeenow.com.coffeenowapp.fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import coffeenow.com.coffeenowapp.CoffeeNowApp;
import coffeenow.com.coffeenowapp.R;
import coffeenow.com.coffeenowapp.tasks.LoginTask;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentLogin.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentLogin#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentLogin extends Fragment implements LoginTask.OnTaskComplete {

    private static final String LOG_TAG = FragmentLogin.class.getSimpleName();
    private OnFragmentInteractionListener mListener;
    private static LoginTask.OnTaskComplete mLoginTask;
    private View mRootView;
    private TextView mUsername;
    private TextView mPassword;
    private Button mLogin;

    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private Drawable mNavIcon;

    public FragmentLogin() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FragmentLogin.
     */
    public static FragmentLogin newInstance() {
        FragmentLogin fragment = new FragmentLogin();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_login, container, false);
        mUsername = (TextView) mRootView.findViewById(R.id.login_username);
        mPassword = (TextView) mRootView.findViewById(R.id.login_password);
        mLogin = (Button) mRootView.findViewById(R.id.login_button);

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        if (fab != null) {
            fab.hide();
        }

        mDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        if (mDrawerLayout != null) {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }

        mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        if (mToolbar != null) {
            mNavIcon = mToolbar.getNavigationIcon();
            mToolbar.setNavigationIcon(null);
        }


        mLoginTask = this;
        mLogin.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginTask task = new LoginTask(mLoginTask, ((CoffeeNowApp) getContext().getApplicationContext()).getUser());
                task.execute(mUsername.getText().toString().trim(), mPassword.getText().toString().trim());
            }
        });

        return mRootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onLoginDone();
    }

    @Override
    public void onLoginComplete(boolean successful) {
        if (successful) {
            if (mDrawerLayout != null) {
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }
            if (mToolbar != null && mNavIcon != null) {
                mToolbar.setNavigationIcon(mNavIcon);
            }
            mListener.onLoginDone();
        } else {
            Toast.makeText(getActivity(), "Login failed", Toast.LENGTH_LONG).show();
        }
    }
}
