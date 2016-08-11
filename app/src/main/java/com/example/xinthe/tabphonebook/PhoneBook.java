package com.example.xinthe.tabphonebook;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class PhoneBook extends AppCompatActivity {

    DBHelper dbHelper;
    ListView lv;
    ArrayList<HolderPhoneBook> data;
    ImageButton add_contact;
    CustomAdapter adapter;
    TextView nocnts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_book);
        dbHelper = new DBHelper(PhoneBook.this);
        add_contact = (ImageButton) findViewById(R.id.addcntct);
        nocnts=(TextView)findViewById(R.id.nocnts);
        Intent i=getIntent();
        add_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addContact();
            }
        });
        populateList();
    }
    @Override
    public void onResume() {
        super.onResume();
        populateList();
    }

    public void addContact() {
        startActivity(new Intent(PhoneBook.this, NewOrEditContact.class));

    }
    private void populateList() {
        data = dbHelper.getPhoneBookList();
        if (data == null || data.size() == 0) {
            Toast.makeText(this, "Empty List", Toast.LENGTH_LONG).show();
            lv = (ListView) findViewById(R.id.listView);
            lv.setAdapter(new CustomAdapter(this,data));
            nocnts.setVisibility(View.VISIBLE);

        } else {
            Log.e("hey", "hello");
            lv = (ListView) findViewById(R.id.listView);
            nocnts.setVisibility(View.INVISIBLE);
            if(adapter == null){
                adapter = new CustomAdapter(this,data);
                lv.setAdapter(adapter);
            }
            else{adapter.setData(data);
                lv.setAdapter(adapter);}
            // lv.setAdapter(new CustomAdapter(this,data));
            // lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //   @Override
            //   public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //     startActivity(new Intent(MainActivity.this,EnterDetails.class).putExtra("id",data.get(position).getId()));
            //  }
            // });
        }
    }
    class CustomAdapter extends BaseAdapter {
        Context context;  ArrayList<HolderPhoneBook> data;

        CustomAdapter(Context context,ArrayList<HolderPhoneBook>data){ this.context = context;this.data = data;}

        public void setData(ArrayList<HolderPhoneBook> data){
            this.data = data;
            notifyDataSetChanged();
        }
        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return data.get(position).getId();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View layout = convertView;
            if (layout == null) {
                layout = getLayoutInflater().inflate(R.layout.row, parent, false);

            }
            TextView cntctName = (TextView) layout.findViewById(R.id.cntctname);
            TextView cntctno = (TextView) layout.findViewById(R.id.cntctno);
            ImageButton edit = (ImageButton) layout.findViewById(R.id.edit);
            ImageButton delete = (ImageButton) layout.findViewById(R.id.delete);
            HolderPhoneBook hbd = data.get(position);
            if (data.size() != 0) {
                cntctName.setText(hbd.getFname());

                cntctno.setText(hbd.getPhoneNumber());
                edit.setTag(R.id.db_id, position);
                delete.setTag(R.id.view_id, position);
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        final AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                                PhoneBook.this);

                        alertDialog2.setTitle("Confirm Delete...");


                        alertDialog2.setMessage("Are you sure you want delete this file?");




                        alertDialog2.setPositiveButton("YES",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        int pos = (int) v.getTag(R.id.view_id);
                                        boolean success = new DBHelper(PhoneBook.this).deletePhoneBookContact(data.get(pos).getId());
                                        if (success == true)
                                            Toast.makeText(getApplicationContext(),
                                                    "deleted", Toast.LENGTH_SHORT)
                                                    .show();
                                        populateList();
                                    }
                                });


                        alertDialog2.setNegativeButton("NO",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Write your code here to execute after dialog
                                        Toast.makeText(getApplicationContext(),
                                                "You clicked on NO", Toast.LENGTH_SHORT)
                                                .show();
                                        dialog.cancel();
                                    }
                                });


                        alertDialog2.show();


                    }
                });
                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = (int) v.getTag(R.id.db_id);
                        startActivity(new Intent(PhoneBook.this, NewOrEditContact.class).putExtra("id", data.get(pos).getId()));
                    }
                });
                return layout;
            } else return layout;
        }
    }




}
