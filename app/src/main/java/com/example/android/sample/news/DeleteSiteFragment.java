package com.example.android.sample.news;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

/**
 * 登録サイトを削除するダイアログ
 */

public class DeleteSiteFragment extends DialogFragment {

    private static final String RESULT_KEY_SITE_ID = "siteId";

    //このダイアログフラグメントのインスタンスを生成して返す
    public static DeleteSiteFragment newInstance(
            Fragment target,int requestCode,long siteId){

        //このダイアログフラグメントのインスタンスを生成する
        DeleteSiteFragment fragment = new DeleteSiteFragment();

        //削除対象のサイトのIDをBundleに詰める
        Bundle args = new Bundle();
        args.putLong(RESULT_KEY_SITE_ID,siteId);
        fragment.setArguments(args);

        //Fragment#setTargetFragment()で、結果を返す対象のフラグメントを指定する
        fragment.setTargetFragment(target,requestCode);

        return fragment;
    }

    //ダイアログを表示
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        //AlertDialogを作るためのビルダー
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //表示するダイアログを設定する
        builder.setTitle(R.string.dialog_delete_title)//タイトル
                .setMessage(R.string.dialog_delete_message)//メッセージ
                .setPositiveButton(R.string.dialog_button_positive,//yesボタン
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialogInterface, int id) {
                                //[yes]が押された時の処理
                                //結果を返す先のフラグメント
                                Fragment fragment = getTargetFragment();

                                if(fragment != null){
                                    //受け取っていた「削除対象のid」をそのまま返す
                                    Bundle args = getArguments();
                                    Intent data = new Intent();
                                    data.putExtras(args);

                                    //結果を返す先のフラグメントのonActivityResult()を呼ぶ
                                    fragment.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,data);
                                }
                            }
                        })
                .setNegativeButton(R.string.dialog_button_cancel,//キャンセルボタン
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialogInterface, int id) {
                                //結果を返す先のフラグメントに、キャンセルされたという
                                //結果コードを返す
                                Fragment fragment = getTargetFragment();

                                if(fragment != null){
                                    fragment.onActivityResult(getTargetRequestCode(),Activity.RESULT_CANCELED,null);
                                }
                            }
                        });
        //ビルダーからダイアログを返して生成して返す
        return builder.create();

    }

}
