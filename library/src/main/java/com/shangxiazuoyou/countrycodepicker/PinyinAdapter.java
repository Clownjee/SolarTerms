package com.shangxiazuoyou.countrycodepicker;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.WeakHashMap;

public abstract class PinyinAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> implements View.OnClickListener {

    private static final String TAG = PinyinAdapter.class.getSimpleName();
    public static final int TYPE_OTHER = 1;
    private WeakHashMap<View, VH> holders = new WeakHashMap<>();
    public final ArrayList<PinyinEntity> entityList = new ArrayList<>();
    private OnItemClickListener listener = new OnItemClickListener() {
        @Override
        public void onItemClick(PinyinEntity entity, int position) {

        }
    };

    public PinyinAdapter(List<? extends PinyinEntity> entities) {
        if (entities == null) throw new NullPointerException("entities == null!");
        update(entities);
    }

    public void update(List<? extends PinyinEntity> entities) {
        if (entities == null) throw new NullPointerException("entities == null!");
        entityList.clear();
        entityList.addAll(entities);
        Collections.sort(entityList, new Comparator<PinyinEntity>() {
            @Override
            public int compare(PinyinEntity o1, PinyinEntity o2) {
                String pinyin = o1.getPinyin().toLowerCase();
                String anotherPinyin = o2.getPinyin().toLowerCase();
                char letter = pinyin.charAt(0);
                char otherLetter = anotherPinyin.charAt(0);
                if (isLetter(letter) && isLetter(otherLetter))
                    return pinyin.compareTo(anotherPinyin);
                else if (isLetter(letter) && !isLetter(otherLetter)) {
                    return -1;
                } else if (!isLetter(letter) && isLetter(otherLetter)) {
                    return 1;
                } else {
                    return pinyin.compareTo(anotherPinyin);
                }
            }
        });
        notifyDataSetChanged();
    }

    private boolean isLetter(char letter) {
        return 'a' <= letter && 'z' >= letter || 'A' <= letter && 'Z' >= letter;
    }

    @Override
    public final void onBindViewHolder(@NonNull VH holder, int position) {
        PinyinEntity entity = entityList.get(position);
        holders.put(holder.itemView, holder);
        holder.itemView.setOnClickListener(this);
        onBindHolder(holder, entity, position);
    }

    @Override
    @NonNull
    public final VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return onCreateHolder(parent, viewType);
    }

    public abstract VH onCreateHolder(ViewGroup parent, int viewType);

    @Override
    public int getItemViewType(int position) {
        PinyinEntity entity = entityList.get(position);
        return getViewType(entity, position);
    }

    public int getViewType(PinyinEntity entity, int position) {
        return TYPE_OTHER;
    }

    @Override
    public final int getItemCount() {
        return entityList.size();
    }

    public void onBindHolder(VH holder, PinyinEntity entity, int position) {

    }

    public interface OnItemClickListener {
        void onItemClick(PinyinEntity entity, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public final void onClick(View v) {
        VH holder = holders.get(v);
        if (holder == null) {
            Log.e(TAG, "holder is null");
            return;
        }
        int position = holder.getAdapterPosition();
        PinyinEntity pyEntity = entityList.get(position);
        listener.onItemClick(pyEntity, position);
    }
}
