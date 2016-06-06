package coffeenow.com.coffeenowapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import coffeenow.com.coffeenowapp.CoffeeNowApp;
import coffeenow.com.coffeenowapp.R;
import coffeenow.com.coffeenowapp.models.CoffeeMaker;
import coffeenow.com.coffeenowapp.models.User;
import coffeenow.com.coffeenowapp.tasks.RemoveCoffeeMakerTask;

public class CoffeeMakersAdapter extends ArrayAdapter<CoffeeMaker> {

    public CoffeeMakersAdapter(Context context, ArrayList<CoffeeMaker> coffeeMakers) {
        super(context, 0, coffeeMakers);
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        CoffeeMaker cm = getItem(position);
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item_coffee_maker, null);
        }

        TextView tv = (TextView) view.findViewById(R.id.list_item_coffee_maker_textView);
        tv.setText(cm.toString());

        ImageView iv = (ImageView) view.findViewById(R.id.delete_coffee_maker);
        User user = ((CoffeeNowApp) getContext().getApplicationContext()).getUser();
        if (!user.getId().equals(cm.getOwner())) {
            iv.setVisibility(View.INVISIBLE);
        } else {
            iv.setVisibility(View.VISIBLE);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeCoffeeMaker(parent, v);
                }
            });
        }

        return view;
    }

    public void removeCoffeeMaker(ViewGroup parent, View view) {
        ListView lv = (ListView) parent;
        int position = lv.getPositionForView(view);
        CoffeeMaker cm = getItem(position);
        remove(cm);

        RemoveCoffeeMakerTask task = new RemoveCoffeeMakerTask(getContext());
        task.execute(cm.getId());
    }
}
