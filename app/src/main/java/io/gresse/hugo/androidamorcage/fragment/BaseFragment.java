package io.gresse.hugo.androidamorcage.fragment;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import io.gresse.hugo.androidamorcage.AmorcageApplication;
import io.realm.Realm;

/**
 * A base fragment for all fragment that use Realm
 * <p/>
 * Created by Hugo Gresse on 28/09/15.
 */
public abstract class BaseFragment extends Fragment {

    protected Realm mRealm;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        mRealm = ((AmorcageApplication) getActivity().getApplication()).buildDatabase();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(createView(inflater, container, savedInstanceState), container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @LayoutRes
    public abstract int createView(LayoutInflater inflater, ViewGroup container,
                                   Bundle savedInstanceState);

}
