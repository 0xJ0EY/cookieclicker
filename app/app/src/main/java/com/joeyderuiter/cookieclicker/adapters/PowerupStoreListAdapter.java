package com.joeyderuiter.cookieclicker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.joeyderuiter.cookieclicker.R;
import com.joeyderuiter.cookieclicker.models.game.ShopPowerup;
import com.joeyderuiter.cookieclicker.services.StoreService;

import java.util.ArrayList;

public class PowerupStoreListAdapter extends RecyclerView.Adapter<PowerupStoreListAdapter.ViewHolder>  {
    private static final String TAG = "PowerupStoreListAdapter";
    private final ArrayList<ShopPowerup> shopPowerups;
    private final StoreService storeService;

    public PowerupStoreListAdapter(StoreService storeService) {
        this.storeService = storeService;
        this.shopPowerups = new ArrayList<>();

        this.updateStoreInventory();
    }

    private void updateStoreInventory() {
        shopPowerups.clear();

        shopPowerups.addAll(storeService.FetchShopPowerups());

        notifyDataSetChanged();
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

        holder.powerupName.setText(powerup.getPowerup().getName());
    }

    @Override
    public int getItemCount() {
        return shopPowerups.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView powerupName;

        public ViewHolder(View itemView) {
            super(itemView);

            powerupName = (TextView) itemView.findViewById(R.id.powerup_name);
        }
    }
}
