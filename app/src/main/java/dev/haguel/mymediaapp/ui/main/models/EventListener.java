package dev.haguel.mymediaapp.ui.main.models;

public interface EventListener {
        public void onSearchClickListener(String search, int choice);
        public void onFavoriteClickListener(Media clickedItem);
        public void onMediaClickedListener(Media clickedItem);
}