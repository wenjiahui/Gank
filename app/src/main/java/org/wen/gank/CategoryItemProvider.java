package org.wen.gank;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import me.drakeet.multitype.ItemViewBinder;
import org.wen.gank.model.Gank;

/**
 * created by Jiahui.wen 2017-09-20
 */
public class CategoryItemProvider
    extends ItemViewBinder<Gank, CategoryItemProvider.GankViewHolder> {

    @NonNull
    @Override
    protected CategoryItemProvider.GankViewHolder onCreateViewHolder(
        @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View itemView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        return new CategoryItemProvider.GankViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(@NonNull CategoryItemProvider.GankViewHolder holder,
        @NonNull final Gank item) {
        holder.titleView.setText(item.description());
        holder.titleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebViewActivity.go(v.getContext(), item.url(), item.description());
            }
        });
    }

    class GankViewHolder extends RecyclerView.ViewHolder {

        public TextView titleView;

        public GankViewHolder(View itemView) {
            super(itemView);
            titleView = (TextView) itemView.findViewById(android.R.id.text1);
        }
    }
}
