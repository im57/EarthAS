//recycler 뷰 위한 어댑터

package ericson.lg.mobile.earthas.ui.confusion;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ericson.lg.mobile.earthas.R;

public class ConfusionAdapter extends RecyclerView.Adapter<ConfusionAdapter.ItemViewHolder> {

    private Context context;
    private Parsing mCallback;

    private AlertDialog.Builder builder;

    private ArrayList<Confusion> confusions = new ArrayList<>();

    ConfusionAdapter(Context context, Parsing listener){
        this.context = context;
        this.mCallback = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_search, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.onBind(confusions.get(position));
    }

    @Override
    public int getItemCount() {
        return confusions.size();
    }

    void addItem(Confusion confusion) {
        // 외부에서 item을 추가시킬 함수입니다.
        confusions.add(confusion);
    }

    void clearItem(){
        confusions.clear();
    }

    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;
        private TextView tvType;

        ItemViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.text_name);
            tvType = itemView.findViewById(R.id.text_type);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //int pos = getAdapterPosition();

                    builder = new AlertDialog.Builder(context);
                    builder.setMessage(tvType.getText().toString() + " open")
                            .setCancelable(false)
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mCallback.parsingOpen(tvType.getText().toString());
                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            }).show();
                }
            });
        }

        void onBind(Confusion confusion) {
            tvName.setText(confusion.getName());
            tvType.setText(confusion.getType());
        }
    }
}
