package fcm.test.crud_test.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fcm.test.crud_test.R;
import fcm.test.crud_test.model.data.GetPost;

public class GetListAdapter extends RecyclerView.Adapter<GetListAdapter.ViewHolder> implements OnItemLongClickListener {

    List<GetPost> list = new ArrayList<>();
    OnItemLongClickListener listener;

    Context context;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String name = list.get(position).getName();
        String title = list.get(position).getTitle();
        String content = list.get(position).getContent();
        String writeAt = list.get(position).getWriteAt();

        if(!list.get(position).getImageName().equals("deletetd")) {
            Glide.with(context).load("http://220.90.237.33:7004/list/image/" + list.get(position).getImageName())
                    .into(holder.imageView);
        }

        holder.name.setText(name);
        holder.title.setText(title);
        holder.content.setText(content);
        holder.writeAt.setText(writeAt);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onItemLongClick(ViewHolder viewHolder, View view, int position) {
        if(listener != null) {
            listener.onItemLongClick(viewHolder, view, position);
        }
    }

    public GetPost getPost(int position) {
        return list.get(position);
    }

    public void setList(List<GetPost> list) {
        Collections.reverse(list);

        this.list = list;
        notifyDataSetChanged();
    }

    public void setListener(OnItemLongClickListener listener) {
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView title;
        TextView content;
        TextView writeAt;

        ImageView imageView;

        public ViewHolder(@Nullable View view) {
            super(view);

            view.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                if(listener != null) {
                    listener.onItemLongClick(ViewHolder.this, v, position);

                    return true;
                }

                return false;
            });

            name = view.findViewById(R.id.list_name);
            title = view.findViewById(R.id.list_title);
            content = view.findViewById(R.id.list_content);
            writeAt = view.findViewById(R.id.list_write_at);
            imageView = view.findViewById(R.id.list_image);
        }
    }
}
