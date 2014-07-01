package com.example.raven.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.raven.R;

public class NewMessageAdapter extends CursorAdapter
{
	private LayoutInflater inflater;
	
	public NewMessageAdapter(Context context, Cursor c)
	{
		super(context,c);
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public void bindView(View view, Context context, Cursor c)
	{
		// c.getString(1) = name
		// c.getString(2) = phone
		// c.getString(3) = type
		
		TextView name = (TextView)view.findViewById(R.id.ccontName);
		TextView phone = (TextView)view.findViewById(R.id.ccontNo);
		TextView type = (TextView)view.findViewById(R.id.ccontType);
		
		name.setText(c.getString(1));
		phone.setText(c.getString(2));
		type.setText(c.getString(3));
	}
	
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent)
	{
		return inflater.inflate(R.layout.custcont_view, parent, false);
	}
}
