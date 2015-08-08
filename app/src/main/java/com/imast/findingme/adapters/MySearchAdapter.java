package com.imast.findingme.adapters;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import com.imast.findingme.Config;
import com.imast.findingme.R;
import com.imast.findingme.model.MySearch;
import com.imast.findingme.util.VolleySingleton;

import static com.imast.findingme.util.LogUtils.makeLogTag;

/**
 * Created by XKenokiX on 06/08/2015.
 */
public class MySearchAdapter extends RecyclerView.Adapter<MySearchAdapter.MySearchViewHolder> {

    private static final String TAG = makeLogTag(MySearchAdapter.class);

    private FragmentActivity fragmentActivity;

    private Context context;

    private List<MySearch> items;

    public List<MySearch> getItems() {
        return items;
    }

    public void setItems(List<MySearch> items) {
        this.items = items;
    }

    public void clearData() {
        this.items.clear();
    }

    public MySearchAdapter(FragmentActivity fragmentActivity, Context context, List<MySearch> items) {
        this.fragmentActivity = fragmentActivity;
        this.context = context;
        this.items = items;
    }

    public static class MySearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public NetworkImageView urlPetPhoto;
        public TextView txvPetName;
        public TextView txvPetInfo;

        private ClickListener clickListener;

        public MySearchViewHolder(View itemView) {
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

    public MySearchAdapter.MySearchViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_my_search_item, viewGroup, false);
        return new MySearchViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MySearchAdapter.MySearchViewHolder mySearchViewHolder, int i) {

        String base_url_photo = "http://findmewebapp-eberttoribioupc.c9.io/system/pets/photos/000/000/";
        String folder = String.format("%03d/thumb", items.get(i).getLost_pet().getPet().getId()) + "/";

        String final_url_photo = base_url_photo + folder + items.get(i).getLost_pet().getPet().getPhoto_file_name();

        mySearchViewHolder.urlPetPhoto.setImageUrl(final_url_photo, VolleySingleton.getInstance(context).getImageLoader());
        mySearchViewHolder.urlPetPhoto.setDefaultImageResId(R.drawable.ic_my_pets);
        //lostPetViewHolder.urlPetPhoto.setErrorImageResId(R.drawable.ic_sign_out);
        mySearchViewHolder.txvPetName.setText(items.get(i).getLost_pet().getPet().getName());
        mySearchViewHolder.txvPetInfo.setText(items.get(i).getLost_pet().getInfo());

        mySearchViewHolder.setClickListener(new MySearchViewHolder.ClickListener() {
            @Override
            public void onClick(View v, int position, boolean isLongClick) {
                if (!isLongClick) {
                    Config.lostPet = items.get(position).getLost_pet();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
