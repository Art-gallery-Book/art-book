package com.artbook401.artbook.adapters;

import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.User;
import com.artbook401.artbook.R;
import com.artbook401.artbook.Users;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    private static final String TAG = "";
    private final List<User> usersList;
    User currentUser;
    String userName;
    List<String> following;


    public interface OnTaskItemClickListener {
        void onItemClicked(int position);
    }

    public UsersAdapter(List<User> usersList) {
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.userscard, parent, false);
        getUser();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = usersList.get(position);
        holder.userName.setText(user.getName());
        Amplify.Storage.getUrl(
                user.getProfileImage(),
                result -> {
                    Log.i("MyAmplifyApp", "Successfully generated: " + result.getUrl());
                    runOnUiThread(() -> {
                        Picasso.get().load(String.valueOf(result.getUrl())).into(holder.userImage);
                    });
                },
                error -> Log.e("MyAmplifyApp", "URL generation failure", error)
        );




        holder.follow.setOnClickListener(view -> {
            if(following == null){
                following = new ArrayList<>();
            }
            else {
                following=currentUser.getFollowing();
            }
            following.add(user.getId());
            User myUser = User.builder().name(currentUser.getName()).id(currentUser.getId()).following(following)
        .build();
        Amplify.API.mutate(ModelMutation.update(myUser),
                response -> Log.i("MyAmplifyApp", "Todo with id: " + response.getData().getId()),
                error -> Log.e("MyAmplifyApp", "Create failed", error)
        );

        });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView userName;
        ImageView userImage;
        Button follow;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName=itemView.findViewById(R.id.userNameF);
            userImage= itemView.findViewById(R.id.userIMG);
            follow=itemView.findViewById(R.id.followBTN);

        }
    }

    public void getUser(){
        getUserName();
        Amplify.API.query(ModelQuery.list(User.class , User.NAME.eq(userName)),
                success -> {
                    Log.i(TAG, "onCreate: hi queryyyyyyyyyyyyyyyyy" + success.getData());
                    for (User user:success.getData()) {
                        currentUser=user;
                        Log.i(TAG, "onCreate: hi useeeeeeeeeer");

                    }
                    Log.i(TAG, "success ");
                },
                error->{ Log.i(TAG, "error " + error);}
        );

    }

    public void getUserName(){
        userName = Amplify.Auth.getCurrentUser().getUsername();
    }

}
