package com.example.alecksjohanssen.newyorktimes;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by AlecksJohanssen on 3/16/2016.
 */
public class ArticleArrayAdapter extends ArrayAdapter<Article> {
    public ArticleArrayAdapter(Context context, List<Article> articles)
    {
        super(context,android.R.layout.simple_list_item_1,articles);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get data item for position
        Article article = this.getItem(position);
        //check to see if existing view being resused
        if(convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(getContext());
                    convertView = inflater.inflate(R.layout.item_article_result, parent, false);

            }

        //not using a recycled view -> inflate the layout
        ImageView imageView = (ImageView) convertView.findViewById(R.id.ivImage);
        //clear out recycled image from convertview from last time
        imageView.setImageResource(0);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        tvTitle.setText(article.getHeadLine());
        //popular the thumbnail image
        //remote download the image
        String thumbnail = article.getThumbNail();
        if (!TextUtils.isEmpty(thumbnail))
        {
            Picasso.with(getContext()).load(thumbnail).into(imageView);
        }
        return convertView;
    }
}
