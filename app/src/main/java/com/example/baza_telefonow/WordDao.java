package com.example.baza_telefonow;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WordDao {
    //aby zezwolić na wielokrotne dodanie identycznego rekordu
    //zmienić strategię rozwiązywania konfliktów -> IGNORE
    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert(Word word);
    @Query("DELETE FROM word_table")
    void deleteAll();
    //zapytania Room są wykonywane w osobnym wątku
    //LiveData powiadamia obserwatora w głównym wątku
    @Query("SELECT * FROM word_table ORDER BY model ASC")
    LiveData<List<Word>> getAlphabetizedWords();
    @Delete
    void deleteWord(Word word);
    @Update
    void updatePhone(Word word);
}