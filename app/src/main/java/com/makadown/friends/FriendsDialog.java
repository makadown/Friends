package com.makadown.friends;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * Created by usuario on 11/04/2016.
 */
public class FriendsDialog extends DialogFragment
{
    private static String LOG_TAG = FriendsDialog.class.getSimpleName();
    private LayoutInflater mLayoutInflater;
    public static final String DIALOG_TYPE = "command";
    public static final String DELETE_RECORD = "deleteRecord";
    public static final String DELETE_DATABASE = "deleteDatabase";
    public static final String CONFIRM_EXIT = "confirmExit";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        mLayoutInflater = getActivity().getLayoutInflater();
        final View view = mLayoutInflater.inflate( R.layout.friend_layout, null );
        String command = getArguments().getString( DIALOG_TYPE );
        if ( command.equals(DELETE_RECORD) )
        {
            final int _id = getArguments().getInt( FriendsContract.FriendsColumns.FRIENDS_ID );
            String name = getArguments().getString(FriendsContract.FriendsColumns.FRIENDS_NAME);
            TextView popupMessage = (TextView) view.findViewById(R.id.popup_message);
            popupMessage.setText("¿Estás seguro de eliminar a " + name + " de tu lista de Friends?");
            builder.setView(view).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ContentResolver contentResolver = getActivity().getContentResolver();
                    Uri uri = FriendsContract.Friends.buildFriendUri( String.valueOf( _id ) );
                    contentResolver.delete(uri, null, null);
                    Intent intent = new Intent( getActivity().getApplicationContext() , MainActivity.class );
                    intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
                    startActivity(intent);
                }
            });
        }
        else if ( command.equals(DELETE_DATABASE) )
        {
            TextView popupMessage = (TextView) view.findViewById(R.id.popup_message);
            popupMessage.setText("¿Estás seguro de eliminar toda tu lista de Friends?");
            builder.setView(view).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ContentResolver contentResolver = getActivity().getContentResolver();
                    Uri uri = FriendsContract.URI_TABLE;
                    contentResolver.delete(uri, null, null);
                    Intent intent = new Intent( getActivity().getApplicationContext() , MainActivity.class );
                    intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
                    startActivity(intent);
                }
            }).setNegativeButton( "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    /*Para cancelar no necesito hacer nada.
                    Asi que está bien dejarlo sin actividad.
                     */
                }
            }) ;
        }
        else if ( command.equals( CONFIRM_EXIT ) )
        {
            TextView popupMessage = (TextView) view.findViewById(R.id.popup_message);
            popupMessage.setText("¿Estás seguro de salir sin guardar la información?");
            builder.setView(view).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                     getActivity().finish();
                }
            }).setNegativeButton( "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    /*Para cancelar no necesito hacer nada.
                    Asi que está bien dejarlo sin actividad.
                     */
                }
            }) ;
        }
        else
        {
            Log.d( LOG_TAG, "Comando Inválido pasado como parámetro : "  + command );
        }


        return builder.create();
    }
}
