package flowers.parag.com.myflowersapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Parag on 09/04/16.
 */
public class FlowersAdapter extends RecyclerView.Adapter<FlowersAdapter.ViewHolder> implements  RecyclerView.OnItemTouchListener{

    public ArrayList<Flowers> objects;
    private Context mContext;
    GestureDetector mGestureDetector;
    OnclickInterface listener;

    Picasso imageLoader;

    public FlowersAdapter(Context context,ArrayList<Flowers> flowersArrayList,OnclickInterface listener){

        this.objects = flowersArrayList;
        imageLoader = Picasso.with(context);
        this.mContext = context;
        this.listener = listener;

        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }


    public void setImage(ImageView i, String url) {
        imageLoader.load(url).into(i);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(mContext)
                .inflate(R.layout.flowers_layout, parent, false);

        ViewHolder vh = new ViewHolder(v);


        return vh;
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
        if(holder.flowerImage!=null) {
            holder.flowerImage.setImageResource(0);
            holder.shareButton.setOnClickListener(null);
            holder.bookmarkButton.setOnClickListener(null);
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final Flowers f = objects.get(position);
        holder.flowerName.setText(f.getName());
        setImage(holder.flowerImage,f.getUrl());


           if(Bookmarks.getInstance(mContext).checkBookmarkExists(f.getName()) >0){
               holder.bookmarkButton.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_bookmark_black_24dp));
           }
           else{
               holder.bookmarkButton.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_bookmark_border_black_24dp));
           }



        holder.flowerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(view,f.getUrl());
            }
        });

        holder.bookmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(":click","click start");
                if(Bookmarks.getInstance(mContext).checkBookmarkExists(f.getName()) >0){
                    Bookmarks.getInstance(mContext).deleteBookmark(f.getName());
                    holder.bookmarkButton.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_bookmark_border_black_24dp));
                }
                else{
                    Bookmarks.getInstance(mContext).insertBookmark(f.getName(),f.getUrl());
                    holder.bookmarkButton.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_bookmark_black_24dp));
                }

                Log.d(":click","click finish");

            }
        });

        holder.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent sendIntent = new Intent();
                sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, f.getName()+" : "+f.getUrl());
                sendIntent.setType("text/plain");
                mContext.startActivity(sendIntent);
            }
        });

    }



    @Override
    public int getItemCount() {
        return objects.size();
    }


    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        View childView = view.findChildViewUnder(e.getX(), e.getY());
        if (childView != null  && mGestureDetector.onTouchEvent(e)) {
            onItemClick(view.getChildPosition(childView));
            return true;
        }
        return false;
    }

    public void onItemClick(int position){

    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView flowerName;
        ImageView flowerImage;
        ImageView shareButton;
        ImageView bookmarkButton;


        public ViewHolder(View convertView) {
            super(convertView);
            this.flowerImage = (ImageView) convertView.findViewById(R.id.flowerImage);
            this.flowerName = (TextView) convertView.findViewById(R.id.flowerName);
            this.shareButton = (ImageView)convertView.findViewById(R.id.share);
            this.bookmarkButton = (ImageView)convertView.findViewById(R.id.bookmark);

        }

    }



}
