package com.makadown.friends;


import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by usuario on 08/04/2016.
 */
public class FriendsSearchListLoader extends AsyncTaskLoader<List<Friend>> {
    private static final String LOG_TAG = FriendsSearchListLoader.class.getSimpleName();
    private List<Friend> mFriends;
    private ContentResolver mContentResolver;
    private Cursor mCursor;
    private String mfilterText;

    public FriendsSearchListLoader(Context context, Uri uri,
                                   ContentResolver contentResolver, String filterText)
    {
        super(context);
        mContentResolver = contentResolver;
        mfilterText = filterText;
    }

    @Override
    public List<Friend> loadInBackground() {
        String[] projection =
                {BaseColumns._ID,
                FriendsContract.FriendsColumns.FRIENDS_NAME,
                FriendsContract.FriendsColumns.FRIENDS_EMAIL, FriendsContract.FriendsColumns.FRIENDS_PHONE};
        List<Friend> entries = new ArrayList<Friend>();

        String selection = FriendsContract.FriendsColumns.FRIENDS_NAME + " LIKE '" + mfilterText + "%'";
        mCursor = mContentResolver.query( FriendsContract.URI_TABLE, projection,
                selection, null, null);

        if ( mCursor != null )
        {

            if ( mCursor.moveToFirst() )
            {
                do
                {
                    int _id = mCursor.getInt( mCursor.getColumnIndex( BaseColumns._ID ) );
                    String name = mCursor.getString( mCursor.getColumnIndex(FriendsContract.FriendsColumns.FRIENDS_NAME ) );
                    String email = mCursor.getString( mCursor.getColumnIndex(FriendsContract.FriendsColumns.FRIENDS_EMAIL ) );
                    String phone = mCursor.getString( mCursor.getColumnIndex(FriendsContract.FriendsColumns.FRIENDS_PHONE ) );

                    Friend friend = new Friend( _id, name, email, phone );
                    entries.add(friend);

                }while ( mCursor.moveToNext() );
            }
        }

        return entries;
    }

    @Override
    public void deliverResult(List<Friend> friends) {
        if ( isReset() )
        {
            if ( friends != null )
            {
                mCursor.close();
            }
        }

        List<Friend> oldFriendList = mFriends;
        if ( mFriends == null || mFriends.size() == 0  )   // en video está nomas | en vez de ||
        {
            Log.d( LOG_TAG, "++++++++ No se regresaron datos"   );
        }

        mFriends = friends;
        if ( isStarted() )
        {
            super.deliverResult(friends);
        }

        if( oldFriendList != null && oldFriendList != friends )
        {
            mCursor.close();
        }



    }

    @Override
    protected void onStartLoading() {

        if ( mFriends != null )
        {
            deliverResult( mFriends );
        }

        if ( takeContentChanged() || mFriends == null  )
        {
            forceLoad();
        }
    }


    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        onStopLoading();

        if ( mCursor!=null )
        {
            mCursor.close();
        }

        mFriends = null;
    }

    @Override
    public void onCanceled(List<Friend> friends) {
        super.onCanceled(friends);
        if ( mCursor!=null )
        {
            mCursor.close();
        }

    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
    }
}
