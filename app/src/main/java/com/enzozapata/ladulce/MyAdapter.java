package com.enzozapata.ladulce;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.enzozapata.ladulce.data.models.Posts;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<Posts> values;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public  TextView txtHeader;
        public TextView txtFooter;
        public TextView txtPrecio;
        public com.androidnetworking.widget.ANImageView imgView;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            txtHeader = (TextView) v.findViewById(R.id.titulo);
            txtFooter = (TextView) v.findViewById(R.id.fecha_pub);
            txtPrecio = (TextView) v.findViewById(R.id.precio);
            imgView = (com.androidnetworking.widget.ANImageView) v.findViewById(R.id.icon);
        }
    }

    public void add(int position, Posts item) {
        values.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        values.remove(position);
        notifyItemRemoved(position);
    }
    public void ver(Context context, int position){
        Intent intent = new Intent(context, activity_post.class);
        String message = values.get(position).titulo;
        intent.putExtra(EXTRA_MESSAGE, message);
        context.startActivity(intent);
    }
    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(List<Posts> myDataset) {
        values = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.row_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Context context = holder.txtHeader.getContext();
        Posts post = values.get(position);
        holder.txtHeader.setText(post.titulo);
        holder.txtHeader.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ver(context,position);
            }
        });
        holder.imgView.setDefaultImageResId(R.mipmap.loading);
        holder.imgView.setImageUrl(post.icon);
        holder.txtPrecio.setText(post.precio);
        holder.txtFooter.setText(post.fecha_pub);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return values.size();
    }

}