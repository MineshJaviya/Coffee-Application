package coffee.com.coffee;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String API_KEY = "WuVbkuUsCXHPx3hsQzus4SE";
    private static final String COFFEE_URL = "https://coffeeapi.percolate.com/api/coffee/";
    private ProgressDialog pDialog;
    private List<Coffee> coffeeList;
    private ListView listView;
    private CustomListAdapter adapter;
    private ObjectMapper mapper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize all the declared variables and layout resources
        initializeVariables();

        //Build the url to make a request
        String requestUrl=COFFEE_URL+"?api_key="+API_KEY;
        //The available json data is in the form of array. So lets make jsonarray request
        JsonArrayRequest coffeeReq = new JsonArrayRequest(requestUrl,
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    //Log the response we got
                    //Log.d(TAG, response.toString());
                    //As we received the response, hide the progressdialog
                    hidePDialog();

                    //Parse the obtained JSON Array data
                    try{
                        //Generate the json string from the obtained response
                        String json = response.toString();
                        //populate the coffeeList using mapper and json
                        coffeeList = mapper.readValue(json, new TypeReference<List<Coffee>>() {});
                        int i=0;
                        //Remove the empty elements from the list
                        while(i<coffeeList.size()){
                            if(coffeeList.get(i).getName()==null || coffeeList.get(i).getName().length()==0){
                                coffeeList.remove(i);
                                i--;
                            }
                            i++;
                        }
                        //set up the list adapter from the coffeelist we just generated
                        adapter = new CustomListAdapter(MainActivity.this, coffeeList,getApplicationContext());
                        listView.setAdapter(adapter);
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    hidePDialog();
                }
        });

        // Adding request to request queue
        MyRequestQueue.getInstance(this.getApplicationContext()).addToRequestQueue(coffeeReq);
    }

    private void initializeVariables(){

        coffeeList = new ArrayList<Coffee>();
        listView = (ListView)findViewById(R.id.listView);
        //set up listView's onclick listener
        SetUpListViewClickListener();

        //instantiate the object mapper to be used for parsing json data
        mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);

        //instantiate the progressDialog
        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making network request
        pDialog.setMessage("Loading. Please Wait...");
        pDialog.show();
    }

    private void SetUpListViewClickListener(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                //Retrieve the object that is clicked on list
                Coffee coffee = coffeeList.get(position);

                //Generate the intent with corresponding values from the user object
                Intent intent = new Intent(MainActivity.this, ShowCoffeeDetails.class);
                try {
                    String coffeeString = mapper.writeValueAsString(coffee);
                    intent.putExtra("coffeeObject", coffeeString);
                    intent.putExtra("CoffeeUrl",COFFEE_URL+coffee.getId()+"/?api_key="+API_KEY);
                } catch (JsonProcessingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                //Start the activity to show the map
                startActivity(intent);
            }
        });
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }
}
