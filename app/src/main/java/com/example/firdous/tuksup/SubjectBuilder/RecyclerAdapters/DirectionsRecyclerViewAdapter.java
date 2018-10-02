package com.example.firdous.tuksup.SubjectBuilder.RecyclerAdapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.firdous.tuksup.CustomComponents.LetterImageView;
import com.example.firdous.tuksup.Listeners.OnLessonSelectedListener;
import com.example.firdous.tuksup.R;
import com.example.firdous.tuksup.models.Lesson;

import java.util.List;

public class DirectionsRecyclerViewAdapter extends RecyclerView.Adapter<DirectionsRecyclerViewAdapter.ViewHolder> {


    Context context;
    List<String> items;

    public DirectionsRecyclerViewAdapter(Context context, List<String> items) {
        this.items = items;
        this.context  = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_direction_info, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //BIND DATA
        Resources res = context.getResources();

        holder.info.setText(Html.fromHtml(items.get(position), Html.FROM_HTML_MODE_LEGACY));

        String p = (position+1) + "";
        holder.num.setLetter(p.charAt(0));

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(res.getColor(R.color.colorPrimary));
        holder.num.setmBackgroundPaint(paint);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView info;
        LetterImageView num;


        public ViewHolder(View view) {
            super(view);

            info = view.findViewById(R.id.directionInfo);
            num = view.findViewById(R.id.ivMain);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + info + "'";
        }

//        public void animate(RecyclerView.ViewHolder viewHolder) {
//            final Animation animAnticipateOvershoot = AnimationUtils.loadAnimation(context, R.anim.bounce_interpolator);
//            viewHolder.itemView.setAnimation(animAnticipateOvershoot);
//        }
    }
}
