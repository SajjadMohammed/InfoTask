package com.sajjad.info.Adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.sajjad.info.AddUpdateFragment;
import com.sajjad.info.InfoActivity;
import com.sajjad.info.R;
import com.sajjad.info.SQLiteHelper;
import com.sajjad.info.ShowInfoActivity;

import java.util.ArrayList;
import java.util.List;

public class PersonRecyclerAdapter extends RecyclerView.Adapter<PersonViewHolder> {

    private List<PersonInfo> personInfos,baseResouce;
    ;
    private SQLiteHelper sqLiteHelper;
    private Context context;

    public PersonRecyclerAdapter(Context context,List<PersonInfo> personInfos) {
        this.context = context;
        this.personInfos = personInfos;
        baseResouce = personInfos;
    }

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.info_item, parent, false);
        return new PersonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PersonViewHolder holder, final int position) {

        final String personName = personInfos.get(position).getPersonName();
        final String personSaying = personInfos.get(position).getPersonSaying();
        byte[] personPicture = personInfos.get(position).getPersonPicture();

        holder.personName.setText(personName);
        holder.personSaying.setText(personSaying);
        new LoadImageAsync(holder).execute(personPicture);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pair[] pairs = new Pair[3];
                pairs[0] = new Pair<View, String>(holder.personName, holder.personName.getTransitionName());
                pairs[1] = new Pair<View, String>(holder.personSaying, holder.personSaying.getTransitionName());
                pairs[2] = new Pair<View, String>(holder.personImage, holder.personImage.getTransitionName());

                ActivityOptions activityOptions =
                        ActivityOptions.makeSceneTransitionAnimation((Activity) view.getContext(), pairs);
                Intent showIntent = new Intent(view.getContext(), ShowInfoActivity.class);
                // sending id into show activity for showing info.
                showIntent.putExtra("personId", personInfos.get(position).getId());
                view.getContext().startActivity(showIntent, activityOptions.toBundle());
            }
        });
        holder.editPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddUpdateFragment addUpdateFragment = new AddUpdateFragment();
                Bundle bundle = new Bundle();
                // sending id into dialog fragment for updating
                bundle.putInt("personId", personInfos.get(position).getId());
                addUpdateFragment.setArguments(bundle);
                addUpdateFragment.show(((FragmentActivity) view.getContext()).getSupportFragmentManager(), null);
            }
        });
        holder.deletePerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // remove item from database
                sqLiteHelper = new SQLiteHelper(view.getContext());
                sqLiteHelper.removePerson(personInfos.get(position).getId());
                // remove item from list
                removePerson(position);
                ((InfoActivity)context).sliderPagerAdapter=new SliderPagerAdapter(sqLiteHelper.getLastThree());
                ((InfoActivity)context).sliderPager.setAdapter(((InfoActivity)context).sliderPagerAdapter);

            }
        });
    }

    public void searchPerson(String newText) {
        personInfos = new ArrayList<>();
        if (newText.isEmpty()) {
            personInfos = baseResouce;
        } else {
            for (PersonInfo person : baseResouce) {
                if (person.getPersonName().toLowerCase().trim().contains(newText.toLowerCase().trim())) {
                    personInfos.add(person);
                }
            }
        }
        notifyDataSetChanged();
    }

    private void removePerson(int position) {
        personInfos.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return personInfos.size();
    }
}