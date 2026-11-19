package vn.edu.vhu.app_eat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import vn.edu.vhu.app_eat.R;
import vn.edu.vhu.app_eat.model.ChatMessage;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private final List<ChatMessage> messageList;

    public ChatAdapter(List<ChatMessage> messageList) {
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatMessage msg = messageList.get(position);

        if (msg.isFromUser()) {
            holder.layoutUserMsg.setVisibility(View.VISIBLE);
            holder.layoutDriverMsg.setVisibility(View.GONE);
            holder.tvUserMsg.setText(msg.getMessage());
            holder.tvUserTime.setText(msg.getTime() + " • Đã nhận");
        } else {
            holder.layoutDriverMsg.setVisibility(View.VISIBLE);
            holder.layoutUserMsg.setVisibility(View.GONE);
            holder.tvDriverMsg.setText(msg.getMessage());
            holder.tvDriverTime.setText(msg.getTime());

            try {
                Glide.with(holder.itemView.getContext())
                        .load("https://images.unsplash.com/photo-1535713875002-d1d0cf377fde")
                        .circleCrop()
                        .into(holder.ivDriverChatAvt);
            } catch (Exception ignored) {}
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layoutDriverMsg, layoutUserMsg;
        TextView tvDriverMsg, tvDriverTime, tvUserMsg, tvUserTime;
        ImageView ivDriverChatAvt;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutDriverMsg = itemView.findViewById(R.id.layoutDriverMsg);
            layoutUserMsg = itemView.findViewById(R.id.layoutUserMsg);
            tvDriverMsg = itemView.findViewById(R.id.tvDriverMsg);
            tvDriverTime = itemView.findViewById(R.id.tvDriverTime);
            tvUserMsg = itemView.findViewById(R.id.tvUserMsg);
            tvUserTime = itemView.findViewById(R.id.tvUserTime);
            ivDriverChatAvt = itemView.findViewById(R.id.ivDriverChatAvt);
        }
    }
}
