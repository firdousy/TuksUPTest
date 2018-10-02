package com.example.firdous.tuksup.SubjectBuilder;

import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.firdous.tuksup.CustomComponents.LetterImageView;
import com.example.firdous.tuksup.R;
import com.example.firdous.tuksup.SubjectBuilder.SubjectFragment.OnListFragmentInteractionListener;
import com.example.firdous.tuksup.SubjectBuilder.dummy.DummyContent.DummyItem;
import com.example.firdous.tuksup.models.Subject;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MySubjectRecyclerViewAdapter extends RecyclerView.Adapter<MySubjectRecyclerViewAdapter.ViewHolder> {

    private final List<Subject> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MySubjectRecyclerViewAdapter(List<Subject> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_subject, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Subject item = mValues.get(position);

        holder.mItem = item;
        holder.itemId = item.getId();
        holder.name.setText(item.getName());
        holder.code.setText(item.getCode());
        holder.lecturer.setText(item.getLecturer());

        holder.circleLogo.setOval(true);
        holder.circleLogo.setLetter(item.getName().charAt(0));

        if(item.getColor() != null && !item.getColor().trim().isEmpty()){
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.parseColor(item.getColor()));
            holder.circleLogo.setmBackgroundPaint(paint);
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView name;
        public final TextView code;
        public final TextView lecturer;
        public Subject mItem;
        public String itemId;
        public LetterImageView circleLogo;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            name = (TextView) view.findViewById(R.id.subject_name);
            code = (TextView) view.findViewById(R.id.subject_code);
            lecturer = (TextView) view.findViewById(R.id.subject_lecturer);
            circleLogo = view.findViewById(R.id.ivMain);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + itemId + "'";
        }

//        public void animate(RecyclerView.ViewHolder viewHolder) {
//            final Animation animAnticipateOvershoot = AnimationUtils.loadAnimation(context, R.anim.bounce_interpolator);
//            viewHolder.itemView.setAnimation(animAnticipateOvershoot);
//        }
    }
}
