package me.ns.androidplayground.room.ds;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by shintaro.nosaka on 2017/05/19.
 */
@Entity
public class Directory {

    @PrimaryKey
    public String path;

}
