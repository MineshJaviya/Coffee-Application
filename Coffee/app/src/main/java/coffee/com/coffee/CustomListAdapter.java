package coffee.com.coffee;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

public class CustomListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Coffee> coffeeList;
    private Context context;
    private ImageLoader imageLoader;
    
    
    public CustomListAdapter(Activity activity, List<Coffee> coffeeList, Context context) {
        this.activity = activity;
        this.coffeeList = coffeeList;
        this.context = context;
        this.imageLoader = MyRequestQueue.getInstance(context).getImageLoader();
    }
 
    @Override
    public int getCount() {
        return coffeeList.size();
    }
 
    @Override
    public Object getItem(int location) {
        return coffeeList.get(location);
    }
 
    @Override
    public long getItemId(int position) {
        return position;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
 
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_layout, null);
 
        if (imageLoader == null)
            imageLoader = MyRequestQueue.getInstance(context).getImageLoader();

        //Get the network image view holder
        final NetworkImageView ntworkImageView = (NetworkImageView) convertView.findViewById(R.id.networkImageView);

        //Get the holders for coffeeTitle and coffeeDescription
        TextView title = (TextView) convertView.findViewById(R.id.coffeeTitle);
        TextView desc = (TextView) convertView.findViewById(R.id.coffeeDesc);
        // getting coffee data for the row
        final Coffee coffee = coffeeList.get(position);
 
        //Retrieve image url
        String imageUrl = coffee.getImage_url();
        if(imageUrl!=null && imageUrl.length()!=0){
            //set the network image view visible and load the image from the url
            ntworkImageView.setVisibility(View.VISIBLE);
            ntworkImageView.setImageUrl(imageUrl, imageLoader);
        }else{
            //there is no image url so hide the network image view
            ntworkImageView.setVisibility(View.GONE);
        }
        //Set the title field to the name of coffee
        title.setText(coffee.getName());
        //Set the description field to the description of coffee
        desc.setText(coffee.getDesc());
 
        return convertView;
    }
}
