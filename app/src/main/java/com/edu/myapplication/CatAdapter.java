package com.edu.myapplication;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edu.nju.myapplication.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by heleninsa on 2019-11-12.
 *
 * @author heleninsa
 */
public class CatAdapter extends RecyclerView.Adapter<CatAdapter.CatHolder> {

    private List<Breed> mData = new ArrayList<>();

    public void setData(List<Breed> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CatHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cat, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CatHolder holder, int position) {
        holder.bind(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    protected class CatHolder extends RecyclerView.ViewHolder {

        private Breed data;

        private TextView nameTv;

        public CatHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Gson gson = new Gson();
                    Intent intent = new Intent(v.getContext(), CatActivity.class);
                    intent.putExtra("data", gson.toJson(data));
                    v.getContext().startActivity(intent);
                }
            });
            nameTv = itemView.findViewById(R.id.cat_name);
        }

        public void bind(Breed data) {
            nameTv.setText(data.getName());
            this.data = data;
        }
    }

}
