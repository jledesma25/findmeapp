package com.imast.findingme.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import com.imast.findingme.Config;
import com.imast.findingme.R;
import com.imast.findingme.model.LostPet;
import com.imast.findingme.ui.fragments.LostPetInfoFragment;
import com.imast.findingme.util.VolleySingleton;

import static com.imast.findingme.util.LogUtils.makeLogTag;

/**
 * Created by aoki on 04/08/2015.
 */
public class LostPetAdapter extends RecyclerView.Adapter<LostPetAdapter.LostPetViewHolder> {

    private static final String TAG = makeLogTag(LostPetAdapter.class);

    private FragmentActivity fragmentActivity;

    private Context context;

    private List<LostPet> items;

    public List<LostPet> getItems() {
        return items;
    }

    public void setItems(List<LostPet> items) {
        this.items = items;
    }

    public void clearData() {
        this.items.clear();
    }

    public static class LostPetViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public NetworkImageView urlPetPhoto;
        public TextView txvPetName;
        public TextView txvPetInfo;

        private ClickListener clickListener;

        public LostPetViewHolder(View itemView) {
            super(itemView);
            urlPetPhoto = (NetworkImageView) itemView.findViewById(R.id.petPhoto);
            txvPetName = (TextView) itemView.findViewById(R.id.petName);
            txvPetInfo = (TextView) itemView.findViewById(R.id.petInfo);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public interface ClickListener {

            /**
             * Called when the view is clicked.
             *
             * @param v view that is clicked
             * @param position of the clicked item
             * @param isLongClick true if long click, false otherwise
             */
            public void onClick(View v, int position, boolean isLongClick);

        }

        /* Setter for listener. */
        public void setClickListener(ClickListener clickListener) {
            this.clickListener = clickListener;
        }

        @Override
        public void onClick(View v) {

            // If not long clicked, pass last variable as false.
            clickListener.onClick(v, getAdapterPosition(), false);
        }

        @Override
        public boolean onLongClick(View v) {

            // If long clicked, passed last variable as true.
            clickListener.onClick(v, getAdapterPosition(), true);
            return true;
        }

    }

    public LostPetAdapter(Context context, FragmentActivity fragmentActivity, List<LostPet> items) {
        this.context = context;
        this.fragmentActivity = fragmentActivity;
        this.items = items;
    }

    public LostPetAdapter.LostPetViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_lost_pet_item, viewGroup, false);
        return new LostPetViewHolder(v);
    }

    @Override
    public void onBindViewHolder(LostPetAdapter.LostPetViewHolder lostPetViewHolder, int i) {

        String base_url_photo = "http://findmewebapp-eberttoribioupc.c9.io/system/pets/photos/000/000/";
        String folder = String.format("%03d/thumb", items.get(i).getPet_id()) + "/";

        String final_url_photo = base_url_photo + folder + items.get(i).getPet().getPhoto_file_name();

        lostPetViewHolder.urlPetPhoto.setImageUrl(final_url_photo, VolleySingleton.getInstance(context).getImageLoader());
        lostPetViewHolder.urlPetPhoto.setDefaultImageResId(R.drawable.ic_my_pets);
        //lostPetViewHolder.urlPetPhoto.setErrorImageResId(R.drawable.ic_sign_out);
        lostPetViewHolder.txvPetName.setText(items.get(i).getPet().getName());
        lostPetViewHolder.txvPetInfo.setText(items.get(i).getInfo());

        lostPetViewHolder.setClickListener(new LostPetViewHolder.ClickListener() {
            @Override
            public void onClick(View v, int position, boolean isLongClick) {
                if (!isLongClick) {
                    Config.lostPet = items.get(position);

                    Fragment fragment = new LostPetInfoFragment();
                    FragmentTransaction fragmentTransaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
