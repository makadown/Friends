package com.makadown.friends;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by usuario on 08/04/2016.
 */
public class FriendsCustomAdapter extends ArrayAdapter<Friend>
{
    private LayoutInflater mLayoutInflater;
    private static FragmentManager sFragmentManager;

    public FriendsCustomAdapter(Context context,  FragmentManager fragmentManager ) {
        super(context, android.R.layout.simple_list_item_2 );
        mLayoutInflater =  (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        sFragmentManager = fragmentManager;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view;
        if ( convertView == null )
        {
            view = mLayoutInflater.inflate( R.layout.custom_friend, parent,  false );
        }
        else
        {
            view = convertView;
        }

        final Friend friend = getItem( position );
        final int _id = friend.getId();
        final String name = friend.getName();
        final String email = friend.getEmail();
        final String phone = friend.getPhone();

        ((TextView) view.findViewById( R.id.friend_name ) ).setText(name);
        ((TextView) view.findViewById( R.id.friend_email ) ).setText(email);
        ((TextView) view.findViewById( R.id.friend_phone ) ).setText(phone);

        Button editButton = (Button) view.findViewById(R.id.edit);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent( getContext(), EditActivity.class );
                intent.putExtra( FriendsContract.FriendsColumns.FRIENDS_ID, String.valueOf( _id ) );
                intent.putExtra( FriendsContract.FriendsColumns.FRIENDS_NAME, String.valueOf( name ) );
                intent.putExtra( FriendsContract.FriendsColumns.FRIENDS_EMAIL, String.valueOf( email ) );
                intent.putExtra( FriendsContract.FriendsColumns.FRIENDS_PHONE, String.valueOf( phone ) );

                getContext().startActivity(intent);
            }
        });


        Button deleteButton = (Button) view.findViewById(R.id.delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FriendsDialog dialog = new FriendsDialog();
                Bundle args = new Bundle();
                args.putString(FriendsDialog.DIALOG_TYPE, FriendsDialog.DELETE_RECORD );
                args.putInt(FriendsContract.FriendsColumns.FRIENDS_ID,  _id );
                args.putString(FriendsContract.FriendsColumns.FRIENDS_NAME, name );
                dialog.setArguments(args);
                dialog.show( sFragmentManager, "delete-record" );
            }
        });
        return view;
    }


    public void setData( List<Friend> friends)
    {
     clear();
        if ( friends != null )
        {
            for (Friend friend : friends)
            {
                add(friend);
            }
        }
    }
}
