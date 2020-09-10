package com.example.workncardio;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.databases.FoodInfo;
import com.example.workncardio.dummy.FoodContent.FoodItem;

import java.util.ArrayList;
import java.util.List;


public class MyFoodRecyclerViewAdapter extends RecyclerView.Adapter<MyFoodRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<FoodInfo> mValues;
    private LongClickListener longClickListener;

    public MyFoodRecyclerViewAdapter(ArrayList<FoodInfo> mValues, LongClickListener longClickListener) {
        this.mValues = mValues;
        this.longClickListener = longClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_calorie_food, parent, false);
        return new ViewHolder(view,longClickListener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.Fname.setText(mValues.get(position).getName());
        holder.Fcal.setText(String.valueOf(mValues.get(position).getKcal()));
        switch(position%5){
            case 1:holder.cardView.setCardBackgroundColor(holder.cardView.getResources().getColor(R.color.Grey1,null));
            break;
            case 2:holder.cardView.setCardBackgroundColor(holder.cardView.getResources().getColor(R.color.Grey2,null));
                break;
            case 3:holder.cardView.setCardBackgroundColor(holder.cardView.getResources().getColor(R.color.Grey3,null));
                break;
            case 4:holder.cardView.setCardBackgroundColor(holder.cardView.getResources().getColor(R.color.Grey4,null));
                break;
            case 0:holder.cardView.setCardBackgroundColor(holder.cardView.getResources().getColor(R.color.Grey5,null));
                break;

        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        public final View mView;
        public final TextView Fname;
        public final TextView Fcal;
        public CardView cardView;
        LongClickListener longClickListener;

        public ViewHolder(View view,LongClickListener longClickListener) {
            super(view);
            mView = view;
            Fname = (TextView) view.findViewById(R.id.FOName);
            Fcal = (TextView) view.findViewById(R.id.Fcal);
            cardView=view.findViewById(R.id.card);
            this.longClickListener=longClickListener;

            itemView.setOnLongClickListener(this);
        }

        @Override
        public String toString() {
            return "ViewHolder{" +
                    "mView=" + mView +
                    ", Fname=" + Fname +
                    ", Fcal=" + Fcal +
                    '}';
        }

        @Override
        public boolean onLongClick(View view) {
            longClickListener.onLongClickListener(getAdapterPosition());
            return true;
        }
    }

    public interface LongClickListener{
        void onLongClickListener(int position);
    }
}