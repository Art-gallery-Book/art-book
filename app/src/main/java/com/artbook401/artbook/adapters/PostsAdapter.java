package com.artbook401.artbook.adapters;

import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Post;
import com.amplifyframework.datastore.generated.model.User;
import com.artbook401.artbook.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private final List<Post> postsList;

    public interface OnTaskItemClickListener {
        void onItemClicked(int position);
    }

    public PostsAdapter(List<Post> postsList) {
        this.postsList = postsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.postcard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = postsList.get(position);
        holder.body.setText(post.getBody());
        Amplify.Storage.getUrl(
                post.getImage(),
                result -> {
                    Log.i("MyAmplifyApp", "Successfully generated: " + result.getUrl());
                    runOnUiThread(() -> {
                        Picasso.get().load(String.valueOf(result.getUrl())).into(holder.image);
                    });
                },
                error -> Log.e("MyAmplifyApp", "URL generation failure", error)
        );

        Amplify.API.query(ModelQuery.get(User.class, post.getUserId()),
                success -> {
                    runOnUiThread(() -> {
                        holder.myUser.setText(success.getData().getName());
                    });
                },
                error -> {
                   Log.e("MyAmplifyApp", "URL generation failure", error);
                });
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView body;
        private final TextView myUser;
        private final ImageView image;


        ViewHolder(@NonNull View itemView) {
            super(itemView);
            body = itemView.findViewById(R.id.eventDate);
            myUser = itemView.findViewById(R.id.userNameText);
            image = itemView.findViewById(R.id.postImg);
        }
    }
}
