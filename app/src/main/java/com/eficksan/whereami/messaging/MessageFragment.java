package com.eficksan.whereami.messaging;

import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.os.ResultReceiver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.eficksan.whereami.R;
import com.eficksan.whereami.geo.Constants;
import com.eficksan.whereami.routing.Router;
import com.eficksan.whereami.routing.Routing;

import java.lang.ref.WeakReference;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 */
public class MessageFragment extends Fragment {

    private Router mRouter;
    private Location mMessageLocation;

    @Bind(R.id.input_layout_message)
    TextInputLayout mMessageInputLayout;
    @Bind(R.id.input_message)
    EditText mMessage;
    @Bind(R.id.create_message)
    FloatingActionButton mCreateMessage;

//TODO: add broadcastreciver for listen message creation result. + checks is created current message

    public MessageFragment() {
        // Required empty public constructor
    }

    public static MessageFragment newInstance(Location location) {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.EXTRA_LOCATION_DATA, location);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMessageLocation = getArguments().getParcelable(Constants.EXTRA_LOCATION_DATA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_message, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Router) {
            mRouter = (Router) context;
        } else {
            throw new RuntimeException("Wrong type of conext. Expected " + Router.class.getName());
        }
    }

    @Override
    public void onDetach() {
            mRouter = null;
        super.onDetach();
    }

    @OnClick(R.id.create_message)
    public void handleCreateMessage() {
        final String message = mMessage.getText().toString();

        if (message.length() > 0) {
            WAIMessagingService.createMessage(getActivity(), new LocationMessage(mMessageLocation, message));
        }
    }

    @OnTextChanged(R.id.input_message)
    public void handleMessageTextChanged(EditText messageView) {
        if (messageView.getText().length() > 0 ) {
            mCreateMessage.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        } else {
            mCreateMessage.setBackgroundColor(getResources().getColor(R.color.colorAccent_inactive));
        }
    }

    private void handleMessageCreated() {
        if (mRouter != null) {
            Bundle args = new Bundle();
            args.putParcelable(Constants.EXTRA_LOCATION_DATA, mMessageLocation);
            mRouter.showScreen(Routing.MAP_SCREEN, args);
        }
    }
}
