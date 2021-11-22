package com.rianezza.volley_retrofit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class user_adapter extends ArrayAdapter<user> {

    public user_adapter(Context context, List<user> objects) {super(context, 0, objects);}

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        user user = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user, parent, false);
        }

        TextView fullname = convertView.findViewById(R.id.user_fullname);
        TextView email = convertView.findViewById(R.id.user_email);
        TextView id = convertView.findViewById(R.id.user_id);

        fullname.setText(user.getUser_fullname());
        email.setText(user.getUser_email());
        id.setText(String.valueOf(user.getId()));

        return convertView;
    }
}
