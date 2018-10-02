package com.example.firdous.tuksup.SubjectBuilder.RecyclerAdapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.firdous.tuksup.CustomComponents.LetterImageView;
import com.example.firdous.tuksup.Listeners.OnLessonSelectedListener;
import com.example.firdous.tuksup.R;
//import com.example.firdous.tuksup.SubjectBuilder.AddSubjectActivity.OnLessonSelectedListener;
import com.example.firdous.tuksup.models.Lesson;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Lesson} and makes a call to the
 * specified {@link OnLessonSelectedListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyLessonRecyclerViewAdapter extends RecyclerView.Adapter<MyLessonRecyclerViewAdapter.ViewHolder> {

    private final List<Lesson> mValues;
    private final OnLessonSelectedListener mListener;
    Context context;

    public MyLessonRecyclerViewAdapter(Context context, List<Lesson> items, OnLessonSelectedListener listener) {
        mValues = items;
        mListener = listener;
        this.context  = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_lesson_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Resources res = context.getResources();

;
        Lesson item = mValues.get(position);
        holder.mItem = item;
        holder.itemId = item.getId();
        holder.mDay.setText(res.getStringArray(R.array.Weekdays)[item.getWeekDayId()]);
        holder.mLectureNumber.setText(String.valueOf(item.getLectureNumber()));
        String lType =res.getStringArray(R.array.Lecture_Type)[item.getLectureTypeId()];
        holder.mLectureType.setText(lType);
        holder.mTime.setText(res.getStringArray(R.array.Sessions)[item.getSessionId()]);//res.getStringArray(R.array.Sessions)[item.getSessionId()]);
// holder.mIdView.setText(mValues.get(position).id);
//        holder.mContentView.setText(mValues.get(position).content);

        holder.circleLogo.setOval(true);
        holder.circleLogo.setLetter(lType.charAt(0));

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(res.getColor(R.color.colorAccent));
        holder.circleLogo.setmBackgroundPaint(paint);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.OnLessonSelected(holder.mItem);
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
        public final TextView mLectureType;
        public final TextView mLectureNumber;
        public final TextView mTime;
        public final TextView mDay;
        public Lesson mItem;
        public String itemId;
        public LetterImageView circleLogo;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            mLectureType = (TextView) view.findViewById(R.id.subject_name);
            mLectureNumber = (TextView) view.findViewById(R.id.lecture_number);
            mTime = (TextView) view.findViewById(R.id.session_number);
            mDay = (TextView) view.findViewById(R.id.subject_code);
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
