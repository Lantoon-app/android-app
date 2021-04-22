package com.bazinga.lantoon.home.target;

import android.widget.Filter;

import com.bazinga.lantoon.home.target.model.Target;

import java.util.ArrayList;
import java.util.List;

public class TargetFilter extends Filter {

    List<Target> targetList;
    TargetListAdapter targetListAdapter;

    public TargetFilter(List<Target> targetList, TargetListAdapter targetListAdapter) {
        this.targetList = targetList;
        this.targetListAdapter = targetListAdapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        //RESULTS
        FilterResults results=new FilterResults();

        //VALIDATION
        if(constraint != null && constraint.length()>0)
        {

            //CHANGE TO UPPER FOR CONSISTENCY
            constraint=constraint.toString().toUpperCase();

            List<Target> filterTargets= new ArrayList<>();

            //LOOP THRU FILTER LIST
            for(int i=0;i<targetList.size();i++)
            {
                //FILTER
                if(targetList.get(i).getTargetstatus().equals(Integer.valueOf((String) constraint)))
                {
                    filterTargets.add(targetList.get(i));
                }
            }

            results.count=filterTargets.size();
            results.values=filterTargets;
        }else
        {
            results.count=targetList.size();
            results.values=targetList;
        }

        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        targetListAdapter.targetList= (List<Target>) results.values;
        targetListAdapter.notifyDataSetChanged();
    }
}
