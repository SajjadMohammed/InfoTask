package com.sajjad.info.Adapter;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sajjad.info.R;

class PersonViewHolder extends RecyclerView.ViewHolder {
    TextView personName, personSaying;
    ImageView personImage;
    Button editPerson, deletePerson;

    PersonViewHolder(@NonNull View itemView) {
        super(itemView);

        personName = itemView.findViewById(R.id.personName);
        personSaying = itemView.findViewById(R.id.personSaying);
        personImage = itemView.findViewById(R.id.personImage);
        editPerson= itemView.findViewById(R.id.editItem);
        deletePerson= itemView.findViewById(R.id.deleteItem);
    }
}