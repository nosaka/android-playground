package me.ns.androidplayground.room.ds;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by shintaro.nosaka on 2017/05/19.
 */
@Dao
public interface AppDao {

    @Query("SELECT * FROM Directory")
    List<Directory> allDirectory();

    @Query("DELETE FROM Directory")
    void deleteDirectory();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertDirectory(Directory value);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertDirectory(List<Directory> value);

    @Query("SELECT * FROM Image")
    List<Image> allImage();

    @Query("DELETE FROM Image")
    void deleteImage();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertImage(Image value);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertImage(List<Image> value);


}
