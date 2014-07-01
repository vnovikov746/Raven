package com.example.raven.adapters;

import com.example.raven.R;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;


public class HistoryCursorAdapter extends CursorAdapter
{
	private LayoutInflater inflater;
	
	public HistoryCursorAdapter(Context context, Cursor c)
	{
		super(context,c);
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public void bindView(View view, Context context, Cursor c)
	{
		// c.getString(0) = txt
		// c.getString(1) = TranslatedTxt
		// c.getString(2) = time
		// c.getString(3) = phone
		// c.getInt(4) = received or sent
		// c.getInt(5) = read or not
		// c.getInt(6) = sent or not
		
		TextView phone = (TextView)view.findViewById(R.id.phone);
		TextView time = (TextView)view.findViewById(R.id.time);
		TextView txt = (TextView)view.findViewById(R.id.txt);
		
		phone.setText(c.getString(3));
		time.setText(c.getString(2));			
		txt.setText(c.getString(0));
	}
	
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent)
	{
		return inflater.inflate(R.layout.message_item_view, parent, false);
	}
}
