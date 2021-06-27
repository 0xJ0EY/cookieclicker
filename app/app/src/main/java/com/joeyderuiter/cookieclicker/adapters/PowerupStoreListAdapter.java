package com.joeyderuiter.cookieclicker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.joeyderuiter.cookieclicker.GameActivity;
import com.joeyderuiter.cookieclicker.R;
import com.joeyderuiter.cookieclicker.models.game.Powerup;
import com.joeyderuiter.cookieclicker.models.game.ShopPowerup;
import com.joeyderuiter.cookieclicker.models.messages.PurchasePowerup;
import com.joeyderuiter.cookieclicker.models.user.Player;
import com.joeyderuiter.cookieclicker.models.user.PlayerPowerup;
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

    private Player player;

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

                this.player = player;

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

        String cost = powerup.getCost() + " cookies";
        holder.getPowerupCost().setText(cost);

        String amount = String.valueOf(this.getAmountOfPowerup(powerup.getPowerup()));
        holder.getPowerupAmount().setText(amount);

        holder.getPurchasePowerup().setOnClickListener(event -> {
            gameActivity.sendWsMessage(new PurchasePowerup(powerup.getPowerup()));

            this.updateStoreInventory();
        });

        holder.getPowerupImage().setImageResource(nameToResourceId(powerup.getResource()));
    }

    private int nameToResourceId(String name) {
        switch (name) {
            case "bronze_dagger":
                return R.drawable.bronze_dagger;
            case "steel_dagger":
                return R.drawable.steel_dagger;
            case "mithril_dagger":
                return R.drawable.mithril_dagger;
            case "adamant_dagger":
                return R.drawable.adamant_dagger;
            case "rune_dagger":
                return R.drawable.rune_dagger;
            case "dragon_dagger":
                return R.drawable.dragon_dagger;
            default:
                return R.drawable.bronze_dagger;
        }
    }

    private int getAmountOfPowerup(Powerup powerup) {
        if (player == null) return 0;

        for (PlayerPowerup playerPowerup : player.getPowerups()) {
            if (playerPowerup.getPowerup().getId() == powerup.getId())
                return playerPowerup.getAmount();
        }

        return 0;
    }

    @Override
    public int getItemCount() {
        return shopPowerups.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Getter
        private final TextView powerupName;

        @Getter
        private final TextView powerupCost;

        @Getter
        private final TextView powerupAmount;

        @Getter
        private final Button purchasePowerup;

        @Getter
        private final ImageView powerupImage;

        public ViewHolder(View itemView) {
            super(itemView);

            powerupName = (TextView) itemView.findViewById(R.id.powerup_name);
            powerupCost = (TextView) itemView.findViewById(R.id.powerup_cost);
            powerupAmount = (TextView) itemView.findViewById(R.id.powerup_amount);
            powerupImage = (ImageView) itemView.findViewById(R.id.powerup_image);
            purchasePowerup = (Button) itemView.findViewById(R.id.purchase_powerup);
        }
    }
}
