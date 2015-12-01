package io.gresse.hugo.androidamorcage.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.gresse.hugo.androidamorcage.R;
import io.gresse.hugo.androidamorcage.model.Note;
import io.realm.RealmList;

/**
 * Adapter to display the list of note
 * <p/>
 * Created by Hugo Gresse on 18/07/15.
 */
public class NoteRecyclerViewAdapter extends RecyclerView.Adapter<NoteRecyclerViewAdapter.NoteViewHolder> {

    private RealmList<Note> mDataSet;

    public NoteRecyclerViewAdapter(RealmList<Note> dataSet) {
        mDataSet = dataSet;
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_note, viewGroup, false);

        return new NoteViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder noteViewHolder, int position) {
        final Note currentNote = mDataSet.get(position);

        noteViewHolder.idTextView.setText(String.valueOf(currentNote.getId()));
        noteViewHolder.titleTextView.setText(currentNote.getTitle());
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public void add(int position, Note note) {
        mDataSet.add(position, note);
        notifyItemInserted(position);
    }

    public void remove(Note note) {
        int position = mDataSet.indexOf(note);
        mDataSet.remove(position);
        notifyItemRemoved(position);
    }

    public void setData(RealmList<Note> notes) {
        mDataSet = notes;
        notifyDataSetChanged();
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.idTextView)
        TextView idTextView;

        @Bind(R.id.titleTextView)
        TextView titleTextView;

        public NoteViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }


}
