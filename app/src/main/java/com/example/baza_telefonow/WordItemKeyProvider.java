package com.example.baza_telefonow;

import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemKeyProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordItemKeyProvider extends ItemKeyProvider<Long> {
    private Map<Long,Integer> mKeyToPosition;
    private List<Word> mWordList;
    public WordItemKeyProvider() {
        super(SCOPE_MAPPED);
        mWordList=null;
    }
    public void setWords(List<Word> wordList)
    {
        this.mWordList = wordList;
        mKeyToPosition=new HashMap<>(mWordList.size());
        for (int i=0;i<mWordList.size();++i)
            mKeyToPosition.put(mWordList.get(i).getId(),i);
    }
    @Nullable
    @Override
    public Long getKey(int position) {
        return mWordList.get(position).getId();
    }
    @Override
    public int getPosition(Long key) { return mKeyToPosition.get(key); }
}