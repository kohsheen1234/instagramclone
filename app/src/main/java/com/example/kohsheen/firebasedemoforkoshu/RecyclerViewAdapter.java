package com.example.kohsheen.firebasedemoforkoshu;

/**
 * Created by kohsheen on 18/1/18.
 */

        import android.content.Context;
        import android.support.v7.widget.RecyclerView;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.bumptech.glide.Glide;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.database.DatabaseReference;

        import java.util.ArrayList;
        import java.util.List;



/**
 * Created by AndroidJSon.com on 6/18/2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{


    Context context;
    List<Post> MainImageUploadInfoList;
    User user;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    public String userid;



    public RecyclerViewAdapter(Context context, List<Post> TempList) {

        this.MainImageUploadInfoList = TempList;

        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_items, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        firebaseAuth = FirebaseAuth.getInstance();

        Post UploadInfo = MainImageUploadInfoList.get(position);

        holder.imageNameTextView.setText(UploadInfo.getTitle());
        holder.imageDescriptionTextview.setText(UploadInfo.getDescription());
       // userid = databaseReference.child(firebaseAuth.getCurrentUser().getUid()).toString();

        holder.usernametextview.setText(firebaseAuth.getCurrentUser().getDisplayName());

        //Loading image from Glide library.
        Glide.with(context)
                .load(UploadInfo.getUrl())
                .override(2700,2700)

                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {

        return MainImageUploadInfoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView imageNameTextView;
        public TextView imageDescriptionTextview;
        public TextView usernametextview;


        public ViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.imageView);

            imageNameTextView = (TextView) itemView.findViewById(R.id.ImageNameTextView);
            imageDescriptionTextview=(TextView)itemView.findViewById(R.id.ImageDescriptionTextView);
            usernametextview=(TextView)itemView.findViewById(R.id.UserNameTextView);

        }
    }
}