package com.example.raven.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.raven.R;
import com.example.raven.db.Constants;

public class ChatCursorAdapter extends CursorAdapter
{
	private LayoutInflater inflater;
	
	public ChatCursorAdapter(Context context, Cursor c)
	{
		super(context,c);
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public void bindView(View view, Context context, Cursor c)
	{
		// c.getString(1) = phone
		// c.getInt(2) = received or sent
		// c.getString(3) = Txt
		// c.getInt(4) = TranslatedTxt
		// c.getInt(5) = read or not
		// c.getInt(6) = sent or not
		// c.getString(7) = time
		
		TextView txt = (TextView)view.findViewById(R.id.txt);

		txt.setText(c.getString(3));
	}
	
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent)
	{
		return inflater.inflate(R.layout.chat_message, parent, false);
	}
}
