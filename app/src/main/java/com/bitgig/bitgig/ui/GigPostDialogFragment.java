package com.bitgig.bitgig.ui;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.bitgig.bitgig.R;
import com.bitgig.bitgig.model.Conversation;
import com.bitgig.bitgig.model.GigParse;

import java.util.ArrayList;

/**
 * Created by irvin on 2/7/2015.
 */

public class GigPostDialogFragment extends DialogFragment {


    /** The Conversation list. */
    private ArrayList<Conversation> convList;

    /** The chat adapter. */
    private ChatAdapter adp;

    /** The Editext to compose the message. */
    private EditText txt;

    private Context mAppContext;
    private boolean mWasPaused = false;
    private static final String TAG = "GIG_POST_FRAGMENT";

    public GigPostDialogFragment(){
        // Empty constructor required for DialogFragment
    }

    @Override
    public void onStart()
    {
        super.onStart();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        // safety check
        if (getDialog() == null)
            return;


        int dialogWidth = WindowManager.LayoutParams.MATCH_PARENT; // specify a value here
        int dialogHeight = WindowManager.LayoutParams.MATCH_PARENT; // specify a value here

        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);

        // ... other stuff you want to do in your onStart() method
    }

    @Override
    public void onPause() {
        super.onPause();
        mWasPaused = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mWasPaused) {
            mWasPaused = false;
            Log.d(TAG, "Reloading data as a result of onResume()");

        /*            mSessionsObserver.cancelPendingCallback();
            mTagsObserver.cancelPendingCallback();
            reloadSessionData(false);*/

        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.activity_gigpost_full, null);

        loadConversationList();
        ListView list = (ListView) v.findViewById(R.id.list);
        adp = new ChatAdapter();
        list.setAdapter(adp);
        list.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        list.setStackFromBottom(true);

        txt = (EditText) v.findViewById(R.id.txt);
        txt.setInputType(InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        Button yay = (Button)v.findViewById(R.id.btn_yay);
        yay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GigParse post = new GigParse();
                String str = "{Currency:'USD',Description:'Bring Uwe a Soda',TxHash:'',Status:'Accepted',User:'Uwe Cerron'}";
                post.setText(str);
                post.saveInBackground();
                GigPostDialogFragment.this.dismiss();
            }
        });

     v.findViewById(R.id.btnSend).setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {

             for (int i = 0; i < 100000; i++){
                 System.out.println("");
             }
             sendMessage();
         }
     });
        return v;
    }
/*
    public interface Callbacks {
        public void onSessionSelected(String sessionId, View clickedView);
        public void onTagMetadataLoaded(TagMetadata metadata);
    }


    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onSessionSelected(String sessionId, View clickedView) {}

        @Override
        public void onTagMetadataLoaded(TagMetadata metadata) {}
    };

    private Callbacks mCallbacks = sDummyCallbacks;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAppContext = getActivity().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_sessions, container, false);
        mCollectionView = (CollectionView) root.findViewById(R.id.sessions_collection_view);
        mPreloader = new Preloader(ROWS_TO_PRELOAD);
        mCollectionView.setOnScrollListener(mPreloader);
        mEmptyView = (TextView) root.findViewById(R.id.empty_text);
        mLoadingView = root.findViewById(R.id.loading);
        return root;
    }*/


    /**
     * Call this method to Send message to opponent. The current implementation
     * simply add an auto reply message with each sent message.
     * You need to write actual logic for sending and receiving the messages.
     */
    private void sendMessage()
    {
        if (txt.length() == 0)
            return;

        InputMethodManager imm = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(txt.getWindowToken(), 0);

        String s = txt.getText().toString();
        convList.add(new Conversation(s, "12:00 AM", true, true));
        convList.add(new Conversation("Can't talk, bring me Pepsi",
                "12:00 AM", false, true));
        adp.notifyDataSetChanged();
        txt.setText(null);
    }

    /**
     * This method currently loads a dummy list of conversations. You can write the
     * actual implementation of loading conversations.
     */
    private void loadConversationList()
    {
        convList = new ArrayList<Conversation>();
        convList.add(new Conversation("hi there...", "12:45 AM", true, true));
        convList.add(new Conversation("Hello, How can I help you?", "12:47 AM",
                false, true));
        convList.add(new Conversation("What do you think about this tool?",
                "12:49 AM", true, true));
        convList.add(new Conversation("It's actually pretty good!", "12:50 AM",
                false, true));

    }

    /**
     * The Class CutsomAdapter is the adapter class for Chat ListView. The
     * currently implementation of this adapter simply display static dummy
     * contents. You need to write the code for displaying actual contents.
     */
    private class ChatAdapter extends BaseAdapter
    {

        /* (non-Javadoc)
         * @see android.widget.Adapter#getCount()
         */
        @Override
        public int getCount()
        {
            return convList.size();
        }

        /* (non-Javadoc)
         * @see android.widget.Adapter#getItem(int)
         */
        @Override
        public Conversation getItem(int arg0)
        {
            return convList.get(arg0);
        }

        /* (non-Javadoc)
         * @see android.widget.Adapter#getItemId(int)
         */
        @Override
        public long getItemId(int arg0)
        {
            return arg0;
        }

        /* (non-Javadoc)
         * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
         */
        @Override
        public View getView(int pos, View v, ViewGroup arg2)
        {
            Conversation c = getItem(pos);
            if (c.isSent())
                v = LayoutInflater.from(getActivity()).inflate(
                        R.layout.chat_item_sent, null);
            else
                v = LayoutInflater.from(getActivity()).inflate(
                        R.layout.chat_item_rcv, null);

            TextView lbl = (TextView) v.findViewById(R.id.lbl1);
            lbl.setText(c.getDate());

            lbl = (TextView) v.findViewById(R.id.lbl2);
            lbl.setText(c.getMsg());

            lbl = (TextView) v.findViewById(R.id.lbl3);
            if (c.isSuccess())
                lbl.setText("Delivered");
            else
                lbl.setText("");

            return v;
        }

    }
}
