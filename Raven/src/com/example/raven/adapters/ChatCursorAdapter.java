package com.example.raven.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.Gravity;
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
	
	@SuppressWarnings("deprecation")
	public ChatCursorAdapter(Context context, Cursor c)
	{
		super(context, c);
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public void bindView(View view, Context context, Cursor c)
	{
		// c.getString(1) = txt
		// c.getString(2) = translated txt
		// c.getString(3) = time
		// c.getInt(4) = receivrd or sent
		// c.getInt(5) = read or not
		// c.getInt(6) = sent or not
		
		TextView txt = (TextView) view.findViewById(R.id.chatMessage);
		if(c.getInt(4) == Constants.RECEIVED)
		{
			txt.setGravity(Gravity.LEFT);
			txt.setBackgroundColor(Color.rgb(0, 191, 255));
		}
		else
		{
			txt.setGravity(Gravity.RIGHT);
			txt.setBackgroundColor(Color.rgb(255, 187, 255));
		}
		txt.setText(c.getString(1));
	}
	
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent)
	{
		return inflater.inflate(R.layout.chat_item_view, parent, false);
	}
}
