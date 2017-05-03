package com.example.hp.contactsapp;

        import android.content.Intent;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.support.design.widget.FloatingActionButton;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Pair;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.CheckBox;
        import android.widget.CompoundButton;
        import android.widget.ListView;
        import android.widget.RadioButton;
        import android.widget.SimpleAdapter;
        import android.widget.Spinner;
        import android.widget.Toast;

        import org.json.JSONException;

        import java.io.IOException;
        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;
        import java.util.concurrent.ExecutionException;

public class AccueilActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener {
    SimpleAdapter adapter=null;
    ListView lv = null;
    CheckBox rdbAll = null,rdbDelete=null;
    List<Map<String,String>> list = new ArrayList<Map<String, String>>();
    ArrayList<Pair<String,String>> listAdd = new ArrayList<Pair<String, String>>();
    public static final int ADD_REQUEST=1,ADD_RESPONSE=1,UPDATE_REQUEST=2,UPDATE_RESPONSE=2;
    int validation = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);
        //=========== BTN NEW ===============
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(AccueilActivity.this, FormActivity.class), ADD_REQUEST);
            }
        });
        //=========== GET ALL ===============

        refrech();




    }




    public void refrech(){
        lv= (ListView) findViewById(R.id.lvContact);
        GetAsyncTask t = new GetAsyncTask();
        t.execute();
        try {
            list= t.get();
            if(list.get(0).get("error")!=null){
                Map<String,String> error = list.get(0);
                // Toast.makeText(this, error.get("error"), Toast.LENGTH_LONG).show();
            }
        } catch (InterruptedException e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        } catch (ExecutionException e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
        String[] from={"id","nomPrenom"};
        int[] to={R.id.txvId,R.id.txvNomPrenom};
        adapter = new SimpleAdapter(this,list,R.layout.item,from,to);
        lv.setAdapter(adapter);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode== ADD_REQUEST && resultCode==ADD_RESPONSE){
            Pair<String,String> el = new Pair<>("nomPrenom",data.getStringExtra("nomPrenom"));
            listAdd.add(el);
            el = new Pair<>("tel",data.getStringExtra("tel"));
            listAdd.add(el);
            el = new Pair<>("email",data.getStringExtra("email"));
            listAdd.add(el);
            el = new Pair<>("statut",data.getStringExtra("statut"));
            listAdd.add(el);
            AddAsyncTask add = new AddAsyncTask();
            add.execute(listAdd);
            refrech();
        }
        if(requestCode== UPDATE_REQUEST && resultCode==UPDATE_RESPONSE){
            // Toast.makeText(this, data.getStringExtra("id"), Toast.LENGTH_SHORT).show();
            Pair<String,String> el = new Pair<>("id",data.getStringExtra("id"));
            listAdd.add(el);
            el = new Pair<>("nomPrenom",data.getStringExtra("nomPrenom"));
            listAdd.add(el);
            el = new Pair<>("tel",data.getStringExtra("tel"));
            listAdd.add(el);
            el = new Pair<>("email",data.getStringExtra("email"));
            listAdd.add(el);
            el = new Pair<>("statut",data.getStringExtra("statut"));
            listAdd.add(el);
            //  Toast.makeText(this,data.getStringExtra("statut"), Toast.LENGTH_LONG).show();
            UpdateAsyncTask update = new UpdateAsyncTask();
            update.execute(listAdd);
            refrech();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent it = new Intent(AccueilActivity.this,FormActivity.class);

        it.putExtra("id",list.get(position).get("id"));
        it.putExtra("nomPrenom",list.get(position).get("nomPrenom"));
        it.putExtra("tel",list.get(position).get("tel"));
        it.putExtra("email",list.get(position).get("email"));
        it.putExtra("statut",list.get(position).get("statut"));
        startActivityForResult(it,UPDATE_REQUEST);





    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        View vi = null;
        for (int i = 0; i < lv.getCount(); i++) {
            vi=adapter.getView(i,lv.getChildAt(i),lv);

            CheckBox rd = (CheckBox) vi.findViewById(R.id.rdDelete);
            rd.setVisibility(View.VISIBLE);
            rd.setChecked(false);
        }
        CheckBox rd = (CheckBox) view.findViewById(R.id.rdDelete);
        rd.setChecked(true);


        return true;
    }


    //================== TACHE GET ALL ===============================
    class GetAsyncTask extends AsyncTask<Void,Void,ArrayList<Map<String,String>>> {
        public static final String URL="http://192.168.43.22:81/ecoleCI/contacts/index/json";
        ArrayList<Map<String,String>> error = new ArrayList<>();
        String v="";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Map<String, String>> doInBackground(Void... params) {
            try {
                return Actions.getData(URL);
            } catch (IOException e) {
                Map<String,String> element= new HashMap<>();
                v  = e.toString();
                element.put("msg",e.toString());
                error.add(element);
                return error;
            } catch (JSONException e) {
                Map<String,String> element= null;
                v  = e.toString();
                element.put("msg",e.toString());
                error.add(element);
                return error;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Map<String, String>> maps) {
            super.onPostExecute(maps);
            if(!v.equals(""))
                Toast.makeText(AccueilActivity.this,v, Toast.LENGTH_SHORT).show();
        }
    }
    //==================== TACHE ADD ===============================
    class AddAsyncTask extends AsyncTask<ArrayList<Pair<String,String>>,Void,String>{
        public static final String URL="http://192.168.43.22:81/ecoleCI/contacts/create/json";
        String msg="";

        @Override
        protected String doInBackground(ArrayList<Pair<String, String>>... params) {
            try {
                Actions.save(params[0],URL);
                msg="Contact ajouter avec success !";
            } catch (IOException e) {
                msg= e.toString();
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(AccueilActivity.this,s, Toast.LENGTH_LONG).show();
            super.onPostExecute(s);
        }
    }
    //================== TACHE UPDATE ===============================
    class UpdateAsyncTask extends AsyncTask<ArrayList<Pair<String,String>>,Void,String>{
        public static final String URL="http://192.168.43.22:81/ecoleCI/contacts/update/json";
        String msg="";
        @Override
        protected String doInBackground(ArrayList<Pair<String, String>>... params) {
            try {
                msg=Actions.save(params[0],URL);

            } catch (IOException e) {
                msg= e.toString();
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(AccueilActivity.this,s, Toast.LENGTH_LONG).show();
            super.onPostExecute(s);
        }
    }
    // ================== TACHE DELETE ===============================

}
