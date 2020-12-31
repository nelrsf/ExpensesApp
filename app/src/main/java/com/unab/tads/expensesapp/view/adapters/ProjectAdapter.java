package com.unab.tads.expensesapp.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.unab.tads.expensesapp.R;
import com.unab.tads.expensesapp.databinding.ItemProjectBinding;
import com.unab.tads.expensesapp.model.entities.Project;
import com.unab.tads.expensesapp.view.adapters.eventlisteners.EditItemProjectListener;
import com.unab.tads.expensesapp.view.adapters.eventlisteners.RemoveItemProjectListener;

import java.util.ArrayList;


public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {

    private ArrayList<Project> projects;
    private OnItemClickListener onItemClickListener;
    private RemoveItemProjectListener removeItemProjectListener;
    private EditItemProjectListener editItemProjectListener;

    public ProjectAdapter(ArrayList<Project> projects){
        this.projects = projects;
    }

    public void setProjects(ArrayList<Project> projects) {
        this.projects = projects;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setRemoveItemProjectListener(RemoveItemProjectListener removeItemProjectListener){
        this.removeItemProjectListener = removeItemProjectListener;
    }

    public void setEditItemProjectListener(EditItemProjectListener editItemProjectListener){
        this.editItemProjectListener = editItemProjectListener;
    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProjectBinding itemProjectBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_project, parent, false);
        return new ProjectViewHolder(itemProjectBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) {
        Project myProject = projects.get(position);
        holder.onBind(myProject);
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }

    public class ProjectViewHolder extends RecyclerView.ViewHolder {

        private ItemProjectBinding itemProjectBinding;

        public ProjectViewHolder(@NonNull ItemProjectBinding itemView) {
            super(itemView.getRoot());
            itemProjectBinding = itemView;
        }

        public void onBind(Project myProject) {
            itemProjectBinding.setProject(myProject);
            if(removeItemProjectListener!=null){
                itemProjectBinding.ibRemoveProject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                         removeItemProjectListener.onClickRemoveButton(myProject);
                    }
                });
            }
            if(editItemProjectListener!=null){
                itemProjectBinding.ibEditProject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editItemProjectListener.onClickEditButton(myProject, getAdapterPosition());
                    }
                });
            }
            if(onItemClickListener!=null){
                itemProjectBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(myProject);
                    }
                });
            }
        }
    }

    public interface OnItemClickListener{
        void onItemClick(Project project);
    }
}
