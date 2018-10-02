package com.example.firdous.tuksup.SubjectBuilder.RecyclerAdapters;

import android.content.ClipData;
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
import android.widget.Toast;

import com.example.firdous.tuksup.Listeners.OnBuildingSelectedListener;
import com.example.firdous.tuksup.Listeners.OnLessonSelectedListener;
import com.example.firdous.tuksup.R;
import com.example.firdous.tuksup.SubjectBuilder.AddSubjectActivity;
//import com.example.firdous.tuksup.SubjectBuilder.AddSubjectActivity.OnLessonSelectedListener;
import com.example.firdous.tuksup.models.Building;
import com.example.firdous.tuksup.models.Lesson;

import java.util.ArrayList;
import java.util.List;

import io.grpc.internal.SharedResourceHolder;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Lesson} and makes a call to the
 * specified {@link OnLessonSelectedListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class BuildingSuggestionsAdapter extends ArrayAdapter{

    private final LayoutInflater mInflater;
    private final Context mContext;
    private final List<Building> items;
    private final int mResource;
    private final OnBuildingSelectedListener mListener;

    //    private ListFilter listFilter = new ListFilter();
    private List<Building> suggestions, temp;

    public BuildingSuggestionsAdapter(@NonNull Context context, @LayoutRes int resource,
                           @NonNull List objects, OnBuildingSelectedListener listener) {
        super(context, resource, 0, objects);

        mContext = context;
        mInflater = LayoutInflater.from(context);
        mResource = resource;
        mListener = listener;
        items = objects;
        this.suggestions =new ArrayList<>(objects);
        this.temp =new ArrayList<>(objects);
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

    @Override
    public Filter getFilter() {
        return myFilter;
    }

    Filter myFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            Building item = (Building) resultValue;
            //fire event listener
            //Toast.makeText(mContext,"Selected Item "+item.getName(),Toast.LENGTH_LONG).show();

            mListener.OnBuildingSelected(item);

            return item.getName();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (Building item : temp) {
                    if (item.getName().toLowerCase().startsWith(constraint.toString().toLowerCase()) ||
                            item.getShortName().toLowerCase().startsWith(constraint.toString().toLowerCase()) ) {
                        suggestions.add(item);
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<Building> c = (ArrayList) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (Building item : c) {
                    add(item);
                    notifyDataSetChanged();
                }
            }
            else{
                clear();
                notifyDataSetChanged();
            }
        }
    };

}