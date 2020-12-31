package com.unab.tads.expensesapp.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.unab.tads.expensesapp.R;
import com.unab.tads.expensesapp.databinding.ItemSpendingBinding;
import com.unab.tads.expensesapp.model.entities.Spending;
import com.unab.tads.expensesapp.view.adapters.eventlisteners.EditItemSpendingListener;
import com.unab.tads.expensesapp.view.adapters.eventlisteners.RemoveItemSpendingListener;

import java.util.ArrayList;


public class SpendingAdapter extends RecyclerView.Adapter<SpendingAdapter.SpendingViewHolder> {

    private ArrayList<Spending> expenses;
    private RemoveItemSpendingListener removeItemSpendingListener;
    private EditItemSpendingListener editItemSpendingListener;

    public SpendingAdapter(ArrayList<Spending> expenses){
        this.expenses = expenses;
    }

    public void setExpenses(ArrayList<Spending> expenses) {
        this.expenses = expenses;
        notifyDataSetChanged();
    }

    public ArrayList<Spending> getExpenses(){
        return this.expenses;
    }

    public void setRemoveItemSpendingListener(RemoveItemSpendingListener removeItemSpendingListener) {
        this.removeItemSpendingListener = removeItemSpendingListener;
    }

    public void setEditItemSpendingListener(EditItemSpendingListener editItemSpendingListener) {
        this.editItemSpendingListener = editItemSpendingListener;
    }

    @NonNull
    @Override
    public SpendingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSpendingBinding itemSpendingBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_spending, parent, false);
        return new SpendingViewHolder(itemSpendingBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SpendingViewHolder holder, int position) {
        Spending mySpending = expenses.get(position);
        holder.onBind(mySpending);
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }



    public class SpendingViewHolder extends RecyclerView.ViewHolder {

        private ItemSpendingBinding itemSpendingBinding;

        public SpendingViewHolder(@NonNull ItemSpendingBinding itemView) {
            super(itemView.getRoot());
            itemSpendingBinding = itemView;
        }

        public void onBind(Spending mySpending) {
            mySpending.setRecyclerViewPosition(getAdapterPosition());
            itemSpendingBinding.setSpending(mySpending);
            String[] categoryArray = itemSpendingBinding.getRoot().getResources().getStringArray(R.array.categories_array);
            itemSpendingBinding.tvCategoryValue.setText(categoryArray[mySpending.getCategory()]);
            if(editItemSpendingListener!=null){
                itemSpendingBinding.ibEditSpending.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editItemSpendingListener.OnClickEditButton(mySpending);
                    }
                });
            }
            if(removeItemSpendingListener!=null){
                itemSpendingBinding.ibRemoveSpending.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeItemSpendingListener.OnClickRemoveButton(mySpending);
                    }
                });
            }
        }
    }
}