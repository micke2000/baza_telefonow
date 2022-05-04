package com.example.baza_telefonow;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "word_table")
public class Word {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long mId;
    @NonNull
    @ColumnInfo(name = "model")
    public String model;
    @NonNull
    @ColumnInfo(name = "andVer")
    public String mAnroidVer;
    @NonNull
    @ColumnInfo(name = "producent")
    public String mProducent;
    @NonNull
    @ColumnInfo(name = "www")
    public String mWWW;

    public Word() {

    }


    public Word(@NonNull String word, @NonNull String prod, @NonNull String andver,@NonNull String www) {
        model = word;
        mProducent = prod;
        mAnroidVer = andver;
        mWWW = www;
    }
    public Word(@NonNull Long id,@NonNull String word, @NonNull String prod, @NonNull String andver,@NonNull String www) {
        model = word;
        mProducent = prod;
        mAnroidVer = andver;
        mWWW = www;
        mId = id;
    }


    public long getId() {
        return mId;
    }

    @NonNull
    public String getWord() {
        return model;
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    @NonNull
    public String getmProducent() {
        return mProducent;
    }

    @NonNull
    public String getmAnroidVer() {
        return mAnroidVer;
    }

    @NonNull
    public String getmWWW() {
        return mWWW;
    }

    public void setmWWW(@NonNull String mWWW) {
        this.mWWW = mWWW;
    }

    public void setmAnroidVer(@NonNull String mAnroidVer) {
        this.mAnroidVer = mAnroidVer;
    }

    public void setmProducent(@NonNull String mProducent) {
        this.mProducent = mProducent;
    }
}
