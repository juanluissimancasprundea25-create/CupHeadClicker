package com.juanluis.cupheadclicker.ui.ranking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.juanluis.cupheadclicker.R;
import com.juanluis.cupheadclicker.ui.data.db.PlayerEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter of the ranking list. The ViewHolder pattern reuses the row views
 * when the user scrolls, so the list uses little memory.
 *
 * @author Juan Luis Simancas Prundea
 * @version 1.0
 */
public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.PlayerViewHolder> {

    private final List<PlayerEntity> players = new ArrayList<>();
    private final String highlightedUsername;

    /**
     * @param highlightedUsername player whose row is highlighted (can be null)
     */
    public RankingAdapter(String highlightedUsername) {
        this.highlightedUsername = highlightedUsername;
    }

    /**
     * Replaces the data of the list.
     *
     * @param newPlayers players to show
     */
    public void submitList(List<PlayerEntity> newPlayers) {
        players.clear();
        if (newPlayers != null) {
            players.addAll(newPlayers);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ranking, parent, false);
        return new PlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {
        PlayerEntity player = players.get(position);
        boolean highlighted = player.username.equals(highlightedUsername);
        holder.bind(player, position + 1, highlighted);
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    /** Holds the views of one row. */
    static class PlayerViewHolder extends RecyclerView.ViewHolder {

        private final TextView positionText;
        private final TextView nameText;
        private final TextView levelText;
        private final TextView scoreText;

        PlayerViewHolder(@NonNull View itemView) {
            super(itemView);
            positionText = itemView.findViewById(R.id.text_position);
            nameText = itemView.findViewById(R.id.text_player_name);
            levelText = itemView.findViewById(R.id.text_player_level);
            scoreText = itemView.findViewById(R.id.text_player_score);
        }

        void bind(PlayerEntity player, int position, boolean highlighted) {
            positionText.setText(itemView.getContext().getString(R.string.ranking_position, position));
            nameText.setText(player.username);
            levelText.setText(itemView.getContext().getString(R.string.ranking_level, player.level));
            scoreText.setText(String.valueOf(player.score));
            itemView.setBackgroundResource(highlighted
                    ? R.color.mustard_light : android.R.color.transparent);
        }
    }
}
