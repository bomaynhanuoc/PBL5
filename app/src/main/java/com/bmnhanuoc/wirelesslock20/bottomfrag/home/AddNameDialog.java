//package com.bmnhanuoc.wirelesslock20.bottomfrag.home;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.EditText;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatDialogFragment;
//
//import com.bmnhanuoc.wirelesslock20.R;
//
//import org.jetbrains.annotations.NotNull;
//
//public class AddNameDialog extends AppCompatDialogFragment {
//    private EditText editText;
//    private AddNameDialogListener listener;
//
//    @NonNull
//    @NotNull
//    @Override
//    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//
//        LayoutInflater inflater = getActivity().getLayoutInflater();
//
//        View view = inflater.inflate(R.layout.layout_dialog,null);
//
//        builder.setView(view)
//                .setTitle("Save to database")
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                })
//                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        String name = editText.getText().toString().trim();
//                        if (listener != null) {
//                            listener.applyText(name);
//                        }
//                    }
//                });
//
//        editText = view.findViewById(R.id.edit_name);
//
//        return builder.create();
//    }
//
//    @Override
//    public void onAttach(@NonNull @NotNull Context context) {
//        super.onAttach(context);
//
//        try {
//            listener = (AddNameDialogListener) context;
//        } catch (ClassCastException e) {
////            throw new ClassCastException(context.toString() +
////                    "must implement AddNameDialogListner");
//        }
//
//
//    }
//
//    public interface AddNameDialogListener{
//        void applyText(String name);
//    }
//}
