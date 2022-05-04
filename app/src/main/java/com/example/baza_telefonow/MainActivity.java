package com.example.baza_telefonow;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.selection.Selection;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private WordViewModel mWordViewModel;
    private ActivityResultLauncher<Intent> mActivityResultLauncher;
    private ActivityResultLauncher<Intent> mUpdateResultLauncher;
    private FloatingActionButton mMainFab;
    private Button buttonClearAll;
    private WordListAdapter mAdapter;
    private SelectionTracker<Long> mSelectionTracker;
    private FloatingActionButton mDeleteFab;
    private FloatingActionButton mUpdateFab;
    private boolean mIsMainFabAdd = true;
    private WordItemKeyProvider mWordItemKeyProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        mAdapter = new WordListAdapter(this,mWordItemKeyProvider);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mWordViewModel = new ViewModelProvider(this)
                .get(WordViewModel.class);
        //Observer::onChanged -> lambda
        mWordViewModel.getAllWords().observe(this, words -> {
            mAdapter.setWordList(words);
        });
        mActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        String producer = result.getData().getStringExtra("producer");
                        String andoirdVer = result.getData().getStringExtra("andver");
                        String www = result.getData().getStringExtra("www");
                        Word word = new Word(result.getData()
                                .getStringExtra(
                                        NewWordActivity.EXTRA_REPLY),producer,andoirdVer,www);
                        mWordViewModel.insert(word);
                    }
                });
        mUpdateResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        String model = result.getData().getStringExtra("model");
                        Long id = result.getData().getLongExtra("id",-1);
                        String producer = result.getData().getStringExtra("producer");
                        String andoirdVer = result.getData().getStringExtra("andver");
                        String www = result.getData().getStringExtra("www");
                        Word word = new Word(id,model,producer,andoirdVer,www);
                        mWordViewModel.updateWord(word);
                    }
                });
        mMainFab = findViewById(R.id.fabMain);
        buttonClearAll = findViewById(R.id.button_clear_all);
        buttonClearAll.setOnClickListener(view -> {
            mWordViewModel.deleteAll();
        });
        mMainFab.setOnClickListener(view -> mainFabClicked());
        mWordItemKeyProvider = new WordItemKeyProvider();
        mAdapter = new WordListAdapter(this,mWordItemKeyProvider);
        recyclerView.setAdapter(mAdapter);
        mSelectionTracker = new SelectionTracker.Builder<>(
                "word-selection",
                recyclerView,
                mWordItemKeyProvider,
                //odczytuje szczegóły wybranego elementu
                new WordItemDetailsLookup(recyclerView),
//magazyn na klucze
                StorageStrategy.createLongStorage()).build();
        mAdapter.setSelectionTracker(mSelectionTracker);
        mDeleteFab = findViewById(R.id.fabDelete);
        mUpdateFab = findViewById(R.id.fabUpdate);
        mDeleteFab.setOnClickListener(view -> deleteSelection());
        mUpdateFab.setOnClickListener(view -> updateSelection());
        mSelectionTracker.addObserver(
                new SelectionTracker.SelectionObserver<Long>() {
                    @Override
                    public void onSelectionChanged() {
                        //wyświetlamy/chowamy przycisk kasowania
                        updateFabs();
                        if(mSelectionTracker.getSelection().size() > 1){
                            mUpdateFab.setVisibility(View.GONE);
                        }
                        super.onSelectionChanged();
                    }
                    @Override
                    public void onSelectionRestored() {
                        //wyświetlamy/chowamy przycisk kasowania
                        updateFabs();
                        super.onSelectionRestored();
                    }
                    @Override
                    public void onItemStateChanged(@NonNull Long key,
                                                   boolean selected) {
                        super.onItemStateChanged(key, selected);
                    }});
    }

    private void updateSelection() {
        Selection<Long> selection=mSelectionTracker.getSelection();
        int wordPosition=-1;
        List<Word> wordList=mWordViewModel.getAllWords().getValue();
        wordPosition=mWordItemKeyProvider.getPosition(selection.iterator().next());
        Intent intent = new Intent(
                MainActivity.this, UpdatePhoneActivity.class);
        Word updatedWord = wordList.get(wordPosition);
        String model = updatedWord.getWord();
        String producer = updatedWord.getmProducent();
        String andVer = updatedWord.getmAnroidVer();
        String www = updatedWord.getmWWW();
        Long id = updatedWord.getId();
        intent.putExtra("model",model);
        intent.putExtra("id",id);
        intent.putExtra("producer",producer);
        intent.putExtra("andver",andVer);
        intent.putExtra("www",www);
        mUpdateResultLauncher.launch(intent);
    }

    private void mainFabClicked() {
            if (mIsMainFabAdd) {
            Intent intent = new Intent(
                    MainActivity.this, NewWordActivity.class);
            mActivityResultLauncher.launch(intent);
            } else {
                mSelectionTracker.clearSelection();
            }
        }
    private void deleteSelection() {
        Selection<Long> selection=mSelectionTracker.getSelection();
        int wordPosition=-1;
        List<Word> wordList=mWordViewModel.getAllWords().getValue();
        //przeglądamy identyfikatory z zaznaczenia i kasujemy elementy
        for (long wordId:selection) {
            wordPosition=mWordItemKeyProvider.getPosition(wordId);
            mWordViewModel.deleteWord(wordList.get(wordPosition));
        }
    }
    private void updateFabs() {
        if (mSelectionTracker.hasSelection())
        {
            mDeleteFab.setVisibility(View.VISIBLE);
            mUpdateFab.setVisibility(View.VISIBLE);
            mUpdateFab.setImageDrawable(getDrawable(R.drawable.ic_update_foreground));
            mMainFab.setImageDrawable(
                    getDrawable(R.drawable.ic_baseline_cancel_24));
            mIsMainFabAdd=false;
        }
        else
        {
            mDeleteFab.setVisibility(View.GONE);
            mUpdateFab.setVisibility(View.GONE);
            mMainFab.setImageDrawable(
                    getDrawable(R.drawable.ic_baseline_add_24));
            mIsMainFabAdd=true;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        //stan zaznaczenia trzeba zachować
        mSelectionTracker.onSaveInstanceState(outState);
    }
    @Override
    protected void onRestoreInstanceState(
            @NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //stan zaznaczenia trzeba przywrócić
        mSelectionTracker.onRestoreInstanceState(savedInstanceState);
    }
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.clear_data)
//        {
//            Toast.makeText(this,"Clearing the data...",
//                    Toast.LENGTH_SHORT).show();
//            mWordViewModel.deleteAll();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}