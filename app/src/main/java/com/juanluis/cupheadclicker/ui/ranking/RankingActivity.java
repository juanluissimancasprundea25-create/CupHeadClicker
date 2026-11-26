package com.juanluis.cupheadclicker.ui.ranking;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.juanluis.cupheadclicker.R;
import com.juanluis.cupheadclicker.ui.data.repository.PlayerRepository;

/**
 * Ranking screen: list of players that can be sorted by score, level or name.
 *
 * @author Juan Luis Simancas Prundea
 * @version 1.0
 */
public class RankingActivity extends AppCompatActivity {

    private RankingViewModel viewModel;
    private RankingAdapter adapter;
    private ProgressBar progressBar;
    private TextView emptyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        viewModel = new ViewModelProvider(this).get(RankingViewModel.class);

        progressBar = findViewById(R.id.progress_ranking);
        emptyText = findViewById(R.id.text_ranking_empty);

        RecyclerView recyclerView = findViewById(R.id.recycler_ranking);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RankingAdapter(viewModel.getCurrentUsername());
        recyclerView.setAdapter(adapter);

        setUpSortSpinner();

        viewModel.getRanking().observe(this, players -> {
            adapter.submitList(players);
            boolean empty = players == null || players.isEmpty();
            emptyText.setVisibility(empty ? View.VISIBLE : View.GONE);
        });
        viewModel.isLoading().observe(this, isLoading ->
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE));
    }

    /** The spinner options follow the same order as {@link PlayerRepository.SortOrder}. */
    private void setUpSortSpinner() {
        Spinner spinner = findViewById(R.id.spinner_sort);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.ranking_sort_options, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viewModel.loadRanking(PlayerRepository.SortOrder.values()[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Nothing to do.
            }
        });
    }
}
