package com.makadown.friends;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by usuario on 11/04/2016.
 */
public class EditActivity extends FragmentActivity
{
    private final String LOG_TAG = EditActivity.class.getSimpleName();

    /**
     * Si bien los textos son de clase EditText, se usa TextView, que es
     * la clase de donde ésta se hereda.
     * */
    private TextView mNameTextView, mEmailTextView, mPhoneTextView;
    private Button mButton;
    private ContentResolver mContentResolver;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        mNameTextView = (TextView) findViewById(R.id.friendName);
        mEmailTextView = (TextView) findViewById(R.id.friendEmail);
        mPhoneTextView = (TextView) findViewById(R.id.friendPhone);

        mContentResolver = EditActivity.this.getContentResolver();

        Intent intent = getIntent();
        final String _id  = intent.getStringExtra( FriendsContract.FriendsColumns.FRIENDS_ID );
        String name = intent.getStringExtra( FriendsContract.FriendsColumns.FRIENDS_NAME  );
        String email = intent.getStringExtra( FriendsContract.FriendsColumns.FRIENDS_EMAIL );
        String phone = intent.getStringExtra( FriendsContract.FriendsColumns.FRIENDS_PHONE  );

        mNameTextView.setText(name);
        mEmailTextView.setText(email);
        mPhoneTextView.setText(phone);

        mButton = (Button) findViewById(R.id.saveButton);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                    ContentValues values = new ContentValues();
                    values.put(FriendsContract.FriendsColumns.FRIENDS_NAME, mNameTextView.getText().toString());
                    values.put(FriendsContract.FriendsColumns.FRIENDS_EMAIL, mEmailTextView.getText().toString());
                    values.put(FriendsContract.FriendsColumns.FRIENDS_PHONE, mPhoneTextView.getText().toString());

                    Uri uri = FriendsContract.Friends.buildFriendUri( _id );
                    int recordsUpdated = mContentResolver.update( uri , values, null, null );

                    Log.d(LOG_TAG, "number of records updated is " + recordsUpdated );

                Intent intent = new Intent(EditActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch ( item.getItemId() )
        {
            case  android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                break;
        }
        return true;
    }
}
