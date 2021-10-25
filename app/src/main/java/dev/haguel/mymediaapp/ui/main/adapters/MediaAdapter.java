package dev.haguel.mymediaapp.ui.main.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import dev.haguel.mymediaapp.R;
import dev.haguel.mymediaapp.ui.main.models.Media;

public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.ViewHolder>{

    private List<Media> mediaList;
    private OnMediaClickListener mListener;


    //CONSTRUCTOR
    public MediaAdapter(List<Media> mediaList){
        this.mediaList = mediaList;
    }

    public interface OnMediaClickListener{
        void onMediaClick(int position);
        void onFavoriteClick(int position);
    }

    public void setOnMediaClickListener(OnMediaClickListener listener){
        mListener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.media_item, parent, false);
        return new ViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Media media = mediaList.get(position);
        String typeAndRatings = media.getMediaType() + " • " + media.getRatings();
        holder.tvMediaTitle.setText(media.getTitle());
        holder.tvMediaYear.setText(media.getYear());
        holder.tvMediaTypeAndRatings.setText(typeAndRatings);

        if (media.getFavorite()){
            holder.ivMediaFav.setImageResource(R.drawable.star_yellow);
        } else {
            holder.ivMediaFav.setImageResource(R.drawable.star_outline);
        }

        Picasso.get().load(media.getImageLinkPath() + media.getPosterPath())
                .placeholder(R.mipmap.preview_poster_image)
                .error(R.mipmap.preview_poster_image)
                .into(holder.ivMediaPoster);
    }

    @Override
    public int getItemCount() {
        return mediaList.size();
    }




    // INNER CLASS
    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivMediaPoster;
        ImageView ivMediaFav;
        TextView tvMediaTitle;
        TextView tvMediaYear;
        TextView tvMediaTypeAndRatings;


        public ViewHolder(@NonNull View itemView, OnMediaClickListener listener){
            super(itemView);
            ivMediaPoster = itemView.findViewById(R.id.ivMediaPoster);
            ivMediaFav = itemView.findViewById(R.id.ivMediaFav);
            tvMediaTitle = itemView.findViewById(R.id.tvMediaTitle);
            tvMediaYear = itemView.findViewById(R.id.tvMediaYear);
            tvMediaTypeAndRatings = itemView.findViewById(R.id.tvMediaTypeAndRatings);


            itemView.setOnClickListener(v -> {
                if (listener != null){
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        listener.onMediaClick(position);
                    }
                }
            });

            ivMediaFav.setOnClickListener(v -> {
                if (listener != null){
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        listener.onFavoriteClick(position);
                    }
                }
            });
        }
    }

}
