package io.gresse.hugo.androidamorcage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import io.gresse.hugo.androidamorcage.event.BusProvider;
import io.gresse.hugo.androidamorcage.fragment.NotesFragment;

/**
 * The base Main Activity that manage Fragment, NavigationDrawer and other common behaviour.
 * It act like a parent view controller to manage Fragment stack.
 */
public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        changeFragment(Fragment.instantiate(this, NotesFragment.class.getName()), true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

    @Override
    public void onBackPressed() {
        int fragments = getFragmentManager().getBackStackEntryCount();
        if (fragments == 1) {
            finish();
        }
        super.onBackPressed();
    }

    /**
     * Change the current fragment. It will pop the given fragment from backStack if it has already been instantiated
     * and wasn't garbage collected.
     *
     * @param frag            The given fragment to display
     * @param saveInBackstack if you wan't the fragment to be saved in the backStack to "back" button will me auto
     *                        managed by Android
     */
    private void changeFragment(Fragment frag, boolean saveInBackstack) {
        String backStateName = ((Object) frag).getClass().getName();

        try {
            FragmentManager manager = getSupportFragmentManager();
            boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

            if (!fragmentPopped && manager.findFragmentByTag(backStateName) == null) { //fragment not in back stack, create it.
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.container, frag, ((Object) frag).getClass().getName());

                if (saveInBackstack) {
                    Log.d(TAG, "Change Fragment : addToBackTack");
                    transaction.addToBackStack(backStateName);
                } else {
                    Log.d(TAG, "Change Fragment : NO addToBackTack");
                }

                transaction.commit();
            } else {
                Log.d(TAG, "Change Fragment : nothing to do");
                // custom effect if fragment is already instanciated
            }
        } catch (IllegalStateException exception) {
            Log.e(TAG, "Unable to commit fragment, could be activity as been killed in background. " + exception.toString());
        }
    }
}
