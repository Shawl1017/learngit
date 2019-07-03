package cn.itcast.travel.service;

public interface FavoriteService {
     void addFavorite(String rid, int uid);


    public boolean isFavorite(String rid, int uid);
}
