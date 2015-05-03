package coffee.com.coffee;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class ShowCoffeeDetails extends Activity {

    private TextView textView1;
    private TextView textView2;
    private TextView textView3;

    private ProgressDialog pDialog;
    private NetworkImageView networkImageView1;
    private ImageLoader imageLoader;
    private ObjectMapper mapper;

    private String COFFEE_URL;

    //Constants for finding number of days for last update
    private static final int SECOND = 1000;
    private static final int MINUTE = 60 * SECOND;
    private static final int HOUR = 60 * MINUTE;
    private static final int DAY = 24 * HOUR;
    private static final long WEEK = 7 * DAY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_coffee_details);

        //initialize the variables and layout resources
        initializeVariables();

        //Retrieve the values from passed from previous activity
        String coffeeObject = getIntent().getStringExtra("coffeeObject");
        COFFEE_URL = getIntent().getStringExtra("CoffeeUrl");

        //Reconstruct the coffee object from json string
        try {
            Coffee coffee = mapper.readValue(coffeeObject, Coffee.class);
            //Display the name of the coffee
            textView1.setText(coffee.getName());
            //Fetch the url of image if any
            String image_url = coffee.getImage_url();
            if(image_url!=null && image_url.length()!=0){
                //If image is present then load the image
                networkImageView1.setImageUrl(image_url, imageLoader);
            }else{
                //image is not present so hide the network image View
                networkImageView1.setVisibility(View.GONE);
            }
            //The data we need to download from net is in the form of Json Object.
            //So lets make JSON Object Request.
            JsonObjectRequest coffeeReq = new JsonObjectRequest(COFFEE_URL,new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    try {
                        //Retrieve the description and last update info from downloaded json object
                        String desc = response.getString("desc");
                        String last_update = response.getString("last_updated_at");
                        //Display the description
                        textView2.setText(desc);

                        //Get the current time and subtract it from last updated time
                        //Code for last update info start here
                        Calendar calender = Calendar.getInstance();
                        long currentTime = calender.getTimeInMillis();
                        Date date = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
                        try {
                            date = sdf.parse(last_update);
                            calender.setTime(date);
                            long oldTime = calender.getTimeInMillis();
                            long ms = currentTime - oldTime;
                            SetLastUpdatedStatus(ms);
                        } catch (ParseException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        //Code for last update info ends here

                    }catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),
                                "Error: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),
                            error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            // Adding request to request queue
            MyRequestQueue.getInstance(this.getApplicationContext()).addToRequestQueue(coffeeReq);
        }catch(IOException e){
            e.printStackTrace();
        }finally {
            hidePDialog();
        }

    }

    private void initializeVariables(){
        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making network request
        pDialog.setMessage("Loading. Please Wait...");
        pDialog.show();

        textView1 = (TextView)findViewById(R.id.textView1);
        textView2 = (TextView)findViewById(R.id.textView2);
        textView3 = (TextView)findViewById(R.id.textView3);
        networkImageView1 = (NetworkImageView)findViewById(R.id.networkImageView1);

        mapper = new ObjectMapper();
        imageLoader = MyRequestQueue.getInstance(getApplicationContext()).getImageLoader();
    }

    private void SetLastUpdatedStatus(long ms){
        StringBuffer text = new StringBuffer("");
        if (ms > WEEK) {
            text.append(ms / WEEK).append(" weeks ");
        }else
        if (ms > DAY) {
            text.append(ms / DAY).append(" days ");
        }else
        if (ms > HOUR) {
            text.append(ms / HOUR).append(" hours ");
        }else
        if (ms > MINUTE) {
            text.append(ms / MINUTE).append(" minutes ");
        }else
        if (ms > SECOND) {
            text.append(ms / SECOND).append(" seconds ");
        }
        textView3.setText("last updated: "+text.toString()+" ago");
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
        getMenuInflater().inflate(R.menu.menu_show_coffee_details, menu);
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
