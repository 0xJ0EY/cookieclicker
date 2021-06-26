package com.joeyderuiter.cookieclicker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.joeyderuiter.cookieclicker.GameActivity;
import com.joeyderuiter.cookieclicker.R;
import com.joeyderuiter.cookieclicker.models.game.ShopPowerup;
import com.joeyderuiter.cookieclicker.models.messages.PurchasePowerup;
import com.joeyderuiter.cookieclicker.models.user.Player;
import com.joeyderuiter.cookieclicker.services.AuthService;
import com.joeyderuiter.cookieclicker.services.StoreService;
import com.joeyderuiter.cookieclicker.viewmodels.GameViewModel;

import org.w3c.dom.Text;

import java.util.ArrayList;

import lombok.Getter;

public class PowerupStoreListAdapter extends RecyclerView.Adapter<PowerupStoreListAdapter.ViewHolder>  {
    private static final String TAG = "PowerupStoreListAdapter";
    private final ArrayList<ShopPowerup> shopPowerups;

    private final GameActivity gameActivity;

    private final StoreService storeService;
    private final AuthService authService;

    private int amountOfCookies = 0;

    private final GameViewModel gameViewModel;

    public PowerupStoreListAdapter(
            GameActivity gameActivity,
            StoreService storeService,
            AuthService authService,
            GameViewModel gameViewModel
    ) {
        this.gameActivity = gameActivity;
        this.storeService = storeService;
        this.authService = authService;

        this.shopPowerups = new ArrayList<>();

        this.gameViewModel = gameViewModel;

        this.updateStoreInventory();
        this.setupRefresh();
    }

    private void updateStoreInventory() {
        shopPowerups.clear();

        shopPowerups.addAll(storeService.FetchShopPowerups());

        notifyDataSetChanged();
    }

    // Not the most efficient, but does the job :^)
    private void setupRefresh() {
        this.gameViewModel.getPlayers().observe(this.gameActivity, players -> {
            for (Player player : players.getPlayers()) {
                if (!authService.isCurrentPlayer(player)) continue;

                if (this.amountOfCookies != player.getCookies()) {
                    notifyDataSetChanged();

                    this.amountOfCookies = player.getCookies();
                }
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View powerupListItemView = inflater.inflate(R.layout.component_game_store_list_item, parent, false);

        return new ViewHolder(powerupListItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PowerupStoreListAdapter.ViewHolder holder, int position) {
        ShopPowerup powerup = shopPowerups.get(position);

        boolean enabled = amountOfCookies >= powerup.getCost();
        holder.getPurchasePowerup().setEnabled(enabled);

        holder.getPowerupName().setText(powerup.getPowerup().getName());

        int cost = powerup.getCost();
        holder.getPowerupCost().setText(Integer.toString(cost));

        holder.getPurchasePowerup().setOnClickListener(event -> {
            gameActivity.sendWsMessage(new PurchasePowerup(powerup.getPowerup()));

            this.updateStoreInventory();
        });
    }

    @Override
    public int getItemCount() {
        return shopPowerups.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Getter
        private TextView powerupName;

        @Getter
        private TextView powerupCost;

        @Getter
        private Button purchasePowerup;

        public ViewHolder(View itemView) {
            super(itemView);

            powerupName = (TextView) itemView.findViewById(R.id.powerup_name);
            powerupCost = (TextView) itemView.findViewById(R.id.powerup_cost);
            purchasePowerup = (Button) itemView.findViewById(R.id.purchase_powerup);
        }
    }
}
