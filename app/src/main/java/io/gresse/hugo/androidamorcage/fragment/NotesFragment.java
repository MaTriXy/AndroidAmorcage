package io.gresse.hugo.androidamorcage.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.gresse.hugo.androidamorcage.R;
import io.gresse.hugo.androidamorcage.adapter.NoteRecyclerViewAdapter;
import io.gresse.hugo.androidamorcage.event.BusProvider;
import io.gresse.hugo.androidamorcage.event.note.LoadNotesEvent;
import io.gresse.hugo.androidamorcage.event.note.NotesReceivedEvent;
import io.gresse.hugo.androidamorcage.model.Note;
import io.gresse.hugo.androidamorcage.service.NoteService;
import io.realm.RealmList;

/**
 * Display the list of note.
 * <p/>
 * Created by Hugo Gresse on 28/09/15.
 */
public class NotesFragment extends BaseFragment {

    public static final  String TAG           = NotesFragment.class.getSimpleName();
    private static final int    ITEM_PER_LOAD = 50;

    @Bind(R.id.notesRecyclerView)
    public RecyclerView mRecyclerView;

    private LinearLayoutManager     mLayoutManager;
    private NoteRecyclerViewAdapter mAdapter;
    private int                     mTotalItemCount;
    private int                     mLastVisibleItem;
    private boolean                 mIsLoadingNewItems;
    private boolean                 mAllDataLoaded;
    private RealmList<Note>         mNotes;

    @Override
    public int createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return R.layout.fragment_notes;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // It's improving performance if the layout size will not change
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new NoteRecyclerViewAdapter(new RealmList<>(new Note()));

        mAdapter.setData(NoteService.getNotes(mRealm));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                mTotalItemCount = mLayoutManager.getItemCount();
                mLastVisibleItem = mLayoutManager.findLastVisibleItemPosition();

                // Scrolled to bottom. Do something here.
                if (!mIsLoadingNewItems && mLastVisibleItem == mTotalItemCount - 1 && !mAllDataLoaded) {
                    mIsLoadingNewItems = true;
                    Log.d(TAG, "Scrolled to end, load new notes");
                    BusProvider.getInstance().post(new LoadNotesEvent(mTotalItemCount, ITEM_PER_LOAD));
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        BusProvider.getInstance().register(this);

        if (mNotes == null || mNotes.isEmpty()) {
            BusProvider.getInstance().post(new LoadNotesEvent(0, ITEM_PER_LOAD));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Subscribe
    public void onNoteReceived(NotesReceivedEvent event){
        Log.d(TAG, "onNoteReceived");
        if(event.mNotes.isEmpty()){
            Log.d(TAG, "All note received");
            Toast.makeText(getActivity(), getString(R.string.note_data_allloaded), Toast.LENGTH_SHORT).show();
            mAllDataLoaded = true;
            return;
        }

        if(mNotes == null){
            mNotes = event.mNotes;
        } else {
            mIsLoadingNewItems = false;
            // Merge new notes with the old one
            Log.d(TAG, "Add new note to list");
            for(Note note : event.mNotes){
                mNotes.add(note);
            }

        }

        mAdapter.setData(mNotes);
    }
}
