package com.example.firdous.tuksup.SubjectBuilder.RecyclerAdapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.firdous.tuksup.Listeners.OnBuildingSelectedListener;
import com.example.firdous.tuksup.Listeners.OnLessonSelectedListener;
import com.example.firdous.tuksup.R;
import com.example.firdous.tuksup.SubjectBuilder.AddSubjectActivity;
//import com.example.firdous.tuksup.SubjectBuilder.AddSubjectActivity.OnLessonSelectedListener;
import com.example.firdous.tuksup.models.Building;
import com.example.firdous.tuksup.models.Lesson;

import java.util.List;

import io.grpc.internal.SharedResourceHolder;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Lesson} and makes a call to the
 * specified {@link OnLessonSelectedListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class BuildingAdapter extends ArrayAdapter{

    private final LayoutInflater mInflater;
    private final Context mContext;
    private final List<Building> items;
    private final int mResource;

//    private ListFilter listFilter = new ListFilter();
    private List<Building> AllItems, temp;

    public BuildingAdapter(@NonNull Context context, @LayoutRes int resource,
                              @NonNull List objects) {
        super(context, resource, 0, objects);

        mContext = context;
        mInflater = LayoutInflater.from(context);
        mResource = resource;
        items = objects;
    }
    @Override
    public View getDropDownView(int position, @Nullable View convertView,
                                @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public @NonNull View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent){
        final View view = mInflater.inflate(mResource, parent, false);

        TextView name = (TextView) view.findViewById(R.id.building_name);
        TextView shortname = (TextView) view.findViewById(R.id.building_short_name);

        Building building = items.get(position);

        name.setText(building.getName());
        shortname.setText(building.getShortName());

        return view;
    }

    @Override
    public int getCount() {
        return items.size();
    }

//    @Override
//    public String getItem(int position) {
////        Log.d("CustomListAdapter",
////                dataList.get(position));
//        return items.get(position).getName();
//    }

//    @Override
//    public Filter getFilter() {
//        return myFilter;
//    }
}