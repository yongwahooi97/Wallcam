package cn.easyar.samples.helloarvideo;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by OoiYongWah on 27/12/2017.
 */

public class CategoryAdapter extends ArrayAdapter<Category> {
    public CategoryAdapter(Activity context, int resource, List<Category> list) {
        super(context, resource, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Category category = getItem(position);

        LayoutInflater inflater  = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.category_record, parent, false);

        //TextView textViewCode, textViewCredit;
        ImageView imageView2;
        TextView textViewDescription;

        //textViewCode = (TextView)rowView.findViewById(R.id.textViewCode);
        imageView2 = (ImageView) rowView.findViewById(R.id.imageViewCategory);
        textViewDescription = (TextView) rowView.findViewById(R.id.textViewDescription);
        //textViewCredit = (TextView)rowView.findViewById(R.id.textViewCredit);

        //textViewCode.setText(textViewCode.getText() + ":" +category.getId());
        textViewDescription.setText(category.getDescription() + " - " + category.getPrice());
        Picasso.with(getContext()).load(category.getUrl()).into(imageView2);
        //textViewCredit.setText(textViewCredit.getText() + ":" + category.getDescription());
        return rowView;
    }
}
