package com.example.restora;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.ObservableSnapshotArray;

import models.Address;

public class AddressAdapter extends FirestoreRecyclerAdapter<Address,AddressViewHolder> {
    AddressButtonsInterface listener;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AddressAdapter(@NonNull FirestoreRecyclerOptions<Address> options,AddressButtonsInterface listener) {
        super(options);
        this.listener=listener;
    }

    @Override
    protected void onBindViewHolder(@NonNull AddressViewHolder holder, int position, @NonNull Address model) {
        String s=model.fullName+"\n"+model.mobileNo+"\n"+model.pincode+"\n"+model.addressLine1+"\n"+model.addressLine2+
                "\n"+model.landmark+"\n"+model.townCity+"\n"+model.state;
        holder.addressFull.setText(s);
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address,parent,false);
        AddressViewHolder addressViewHolder=new AddressViewHolder(view);
        addressViewHolder.addressEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObservableSnapshotArray<Address> snapshots=getSnapshots();
                String addressId=snapshots.getSnapshot(addressViewHolder.getBindingAdapterPosition()).getId();
                listener.editAddress(addressId);
            }
        });
        addressViewHolder.addressRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObservableSnapshotArray<Address> snapshots=getSnapshots();
                String addressId=snapshots.getSnapshot(addressViewHolder.getBindingAdapterPosition()).getId();
                listener.addressRadioClick(addressId);
                Toast.makeText(parent.getContext(), "address selected",Toast.LENGTH_SHORT).show();
            }
        });
        return addressViewHolder;
    }
}

class AddressViewHolder extends RecyclerView.ViewHolder{
    RadioButton addressRadio; Button addressEdit;
    TextView addressFull;
    public AddressViewHolder(@NonNull View itemView) {
        super(itemView);
        addressRadio=itemView.findViewById(R.id.addressRadioButton);
        addressEdit=itemView.findViewById(R.id.addressEdit);
        addressFull=itemView.findViewById(R.id.addressFull);
    }
}
interface AddressButtonsInterface{
    void editAddress(String addressId);
    void addressRadioClick(String addressId);

}
