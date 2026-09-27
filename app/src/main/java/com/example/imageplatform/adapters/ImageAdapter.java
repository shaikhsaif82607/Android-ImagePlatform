package com.example.imageplatform.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.*;
import android.widget.*;

import androidx.recyclerview.widget.RecyclerView;

import com.example.imageplatform.Detail;
import com.example.imageplatform.Image;
import com.example.imageplatform.R;
import com.example.imageplatform.DBHelper;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    Context context;
    List<Image> list;
    int userId;
    boolean isProfile; // 🔥 show delete button only in profile

    // ✅ NORMAL MODE
    public ImageAdapter(Context context, List<Image> list, int userId) {
        this.context = context;
        this.list = list;
        this.userId = userId;
        this.isProfile = false;
    }

    // ✅ PROFILE MODE
    public ImageAdapter(Context context, List<Image> list, int userId, boolean isProfile) {
        this.context = context;
        this.list = list;
        this.userId = userId;
        this.isProfile = isProfile;
    }

    // ✅ FALLBACK (SAFE)
    public ImageAdapter(List<Image> list) {
        this.list = list;
        this.userId = -1;
        this.isProfile = false;
    }

    // 🔥 IMPORTANT METHOD (FIX YOUR ERROR)
    public void setProfileMode(boolean isProfile) {
        this.isProfile = isProfile;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        ImageButton deleteBtn;

        public ViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.image);
            title = view.findViewById(R.id.title);
            deleteBtn = view.findViewById(R.id.deleteBtn);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) context = parent.getContext();

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_image, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Image img = list.get(position);

        holder.title.setText(img.getTitle());

        // ✅ SAFE IMAGE LOAD
        try {
            if (img.getImageUri() != null && !img.getImageUri().isEmpty()) {
                holder.image.setImageURI(Uri.parse(img.getImageUri()));
            } else {
                holder.image.setImageResource(img.getImage());
            }
        } catch (Exception e) {
            holder.image.setImageResource(android.R.drawable.ic_menu_gallery);
        }

        // 🔥 DELETE BUTTON (PROFILE ONLY)
        if (isProfile && holder.deleteBtn != null) {
            holder.deleteBtn.setVisibility(View.VISIBLE);

            holder.deleteBtn.setOnClickListener(v -> {

                DBHelper db = new DBHelper(context);

                int postId = img.getId();

                if (postId > 0) { // ✅ SAFE CHECK

                    db.deletePost(postId);

                    int pos = holder.getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        list.remove(pos);
                        notifyItemRemoved(pos);
                    }

                    Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(context, "Cannot delete", Toast.LENGTH_SHORT).show();
                }
            });

        } else if (holder.deleteBtn != null) {
            holder.deleteBtn.setVisibility(View.GONE);
        }

        // ✅ CLICK → DETAIL PAGE
        holder.itemView.setOnClickListener(v -> {

            if (userId == -1) return;

            Intent intent = new Intent(context, Detail.class);
            intent.putExtra("title", img.getTitle());

            if (img.getImageUri() != null && !img.getImageUri().isEmpty()) {
                intent.putExtra("imageUri", img.getImageUri());
            } else {
                intent.putExtra("image", img.getImage());
            }

            intent.putExtra("userId", userId);

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}