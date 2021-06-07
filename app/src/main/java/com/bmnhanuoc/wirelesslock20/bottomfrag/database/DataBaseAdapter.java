package com.bmnhanuoc.wirelesslock20.bottomfrag.database;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bmnhanuoc.wirelesslock20.R;
import com.bmnhanuoc.wirelesslock20.service.ApiService;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataBaseAdapter extends RecyclerView.Adapter<DataBaseAdapter.ViewHolder> {


    private ArrayList<DataBase> dataBases= new ArrayList<>();
    private Context context;

    public DataBaseAdapter(ArrayList<DataBase> dataBases, Context context) {
        this.dataBases = dataBases;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public DataBaseAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item,parent,false);
        return new DataBaseAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull DataBaseAdapter.ViewHolder holder, int position) {
        holder.id.setText(dataBases.get(position).getId());
        holder.name.setText(dataBases.get(position).getName());
        Picasso.get().load(dataBases.get(position).getUrl()).into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(v.getContext()).create(); //Read Update
                alertDialog.setTitle("Delete");
                alertDialog.setMessage("Do you want to delete " + holder.name.getText() + " from database?");

                alertDialog.setButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String idtosend = holder.id.getText().toString();
                        deleteUser(idtosend);
                        dataBases.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());
                        alertDialog.cancel();
                    }
                });

                alertDialog.show();  //<-- See This!
            }
        });
    }
    public void deleteUser(String idtosend){
        DeleteUser deleteUser = new DeleteUser(idtosend);
        ApiService.apiService.deleteUser(deleteUser).enqueue(new Callback<DeleteUser>() {
            @Override
            public void onResponse(Call<DeleteUser> call, Response<DeleteUser> response) {
                if (response.isSuccessful()){
                    Toast.makeText(context.getApplicationContext(), "Success",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<DeleteUser> call, Throwable t) {
                Toast.makeText(context.getApplicationContext(), "Failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataBases.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView id,name;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView3);
            id = itemView.findViewById(R.id.textView);
            name = itemView.findViewById(R.id.textView2);
        }
    }
}
