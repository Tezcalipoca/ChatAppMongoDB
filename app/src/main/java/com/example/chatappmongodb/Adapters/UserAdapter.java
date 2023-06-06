package com.example.chatappmongodb.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.chatappmongodb.Models.User;
import com.example.chatappmongodb.R;
import com.example.chatappmongodb.View.ChattingScreen;
import com.example.chatappmongodb.View.MyProfileActivity;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> implements Filterable {
    ArrayList<User> listUser;
    ArrayList<User> listFilterUser;
    Context context;

    public UserAdapter(ArrayList<User> listUser, Context context) {
        this.listUser = listUser;
        this.context = context;
        this.listFilterUser = listUser;
    }

    @NonNull
    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_user,parent,false);
        return new UserViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserViewHolder holder, int position) {
        User user =listUser.get(position);
        if (user != null) {
//            if (user.getProfile().isEmpty()) {
//                holder.civAvatarItemListUser.setImageResource(R.drawable.default_avatar);
//            } else {
//
//            }
            Picasso.get().load(user.getProfile()).placeholder(R.drawable.default_avatar).into(holder.civAvatarItemListUser);
            holder.tvUserNameItemListUser.setText(user.getName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences sharedPreferences = context.getSharedPreferences("myId", Context.MODE_PRIVATE);
                    String myId = sharedPreferences.getString("myId", "");
                    String friendId = listUser.get(holder.getAdapterPosition()).get_id();
                    Intent intent = new Intent(holder.itemView.getContext(), ChattingScreen.class);
                    intent.putExtra("friendId",friendId);
                    intent.putExtra("myId",myId);
                    context.startActivity(intent);
                }
            });
            holder.ibtnMoreVertContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences sharedPreferences = context.getSharedPreferences("myId", Context.MODE_PRIVATE);
                    String myId = sharedPreferences.getString("myId", "");
                    String friendId = listUser.get(holder.getAdapterPosition()).get_id();
                    PopupMenu popupMenu = new PopupMenu(holder.itemView.getContext(), holder.ibtnMoreVertContact);
                    popupMenu.getMenuInflater().inflate(R.menu.menu_contact,popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.popupSendMessage:
                                    Intent intent= new Intent(holder.itemView.getContext(), ChattingScreen.class);
                                    intent.putExtra("friendId",friendId);
                                    intent.putExtra("myId",myId);
                                    context.startActivity(intent);
                                    break;
                                case R.id.popupViewProfile:
                                    Intent intent2 = new Intent(holder.itemView.getContext(), MyProfileActivity.class);
                                    intent2.putExtra("myId", friendId);
                                    context.startActivity(intent2);
                                    break;
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        CircleImageView civAvatarItemListUser;
        TextView tvUserNameItemListUser;
        ImageButton ibtnMoreVertContact;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            civAvatarItemListUser = itemView.findViewById(R.id.civAvatarItemListUser);
            tvUserNameItemListUser = itemView.findViewById(R.id.tvUserNameItemListUser);
            ibtnMoreVertContact = itemView.findViewById(R.id.ibtnMoreVertContact);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String strSearch = charSequence.toString();
                if (strSearch.isEmpty()) {
                    listUser = listFilterUser;
                } else {
                    ArrayList<User> list = new ArrayList<>();
                    for (User user : listFilterUser) {
                        if (user.getName().toLowerCase().trim().contains(strSearch.toLowerCase().trim())) {
                            list.add(user);
                        }
                    }
                    listUser = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = listUser;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listUser = (ArrayList<User>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
