package com.eficksan.whereami.presentation.messaging;

import android.content.Context;
import android.location.Location;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.eficksan.whereami.R;
import com.eficksan.whereami.data.messages.PlacingMessage;
import com.eficksan.whereami.domain.messages.PlaceMessageValidator;
import com.eficksan.whereami.domain.messages.PlacingMessageInteractor;
import com.eficksan.whereami.presentation.common.BasePresenter;
import com.eficksan.whereami.presentation.routing.Screens;
import com.jakewharton.rxbinding.widget.RxTextView;

import rx.Subscriber;
import rx.functions.Action1;

/**
 * Messaging presenter component.
 * Provides logic from Interactors to View part.
 * <p/>
 * Created by Aleksei Ivshin
 * on 24.08.2016.
 */
public class PlacingMessagePresenter extends BasePresenter<PlacingMessageView> implements View.OnClickListener {

    private final PlacingMessageInteractor placingMessageInteractor;
    private final PlaceMessageValidator placeMessageValidator;
    private final Context context;

    private Location mMessageLocation;

    public PlacingMessagePresenter(
            PlacingMessageInteractor placingMessageInteractor,
            PlaceMessageValidator placeMessageValidator,
            Context context) {
        this.placingMessageInteractor = placingMessageInteractor;
        this.placeMessageValidator = placeMessageValidator;
        this.context = context;
    }

    /**
     * On start presentation.
     *
     * @param messageLocation current location, message will be created using this location
     */
    public void onStart(Location messageLocation) {
        this.mMessageLocation = messageLocation;
        setListeners();
        mView.messageInput.requestFocus();

        InputMethodManager keyboard = (InputMethodManager)
                context.getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.showSoftInput(mView.messageInput, 0);
    }

    /**
     * On stop presentation.
     */
    public void onStop() {
        super.onStop();
        mView.sendMessage.setOnClickListener(null);
        placingMessageInteractor.unsubscribe();
    }

    /**
     * Set event listeners to View.
     */
    private void setListeners() {
        // Subscribe to message text changes
        RxTextView.textChanges(mView.messageInput)
                .subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence messageText) {
                        boolean isMessageValid = placeMessageValidator.validate(messageText.toString());
                        if (isMessageValid) {
                            mView.hideMessageValidationError();
                            mView.setEnableSendMessage(true);
                        } else {
                            mView.showMessageValidationError(
                                    R.string.error_placing_message_message_invalid);
                            mView.setEnableSendMessage(false);
                        }
                    }
                });

        mView.sendMessage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (R.id.create_message == v.getId()) {
            // For creating message need location
            if (mMessageLocation == null) {
                mView.showError(R.string.error_placing_message_location_required);
                return;
            }
            mView.setEnableSendMessage(false);
            final String message = PlacingMessagePresenter.this.mView.messageInput.getText().toString();
            PlacingMessage placingMessage = new PlacingMessage(
                    mMessageLocation.getLatitude(),
                    mMessageLocation.getLongitude(),
                    message);
            placingMessageInteractor.execute(placingMessage, new Subscriber<Boolean>() {
                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable e) {
                }

                @Override
                public void onNext(Boolean isMessageAdded) {
                    if (isMessageAdded) {
                        // Message was sent. Return to previous screen.
                        mView.showSuccess(R.string.success_placing_message_message_delivered);

                        InputMethodManager keyboard = (InputMethodManager)
                                context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        keyboard.hideSoftInputFromWindow(mView.messageInput.getWindowToken(), 0);
                        mRouter.closeScreen(Screens.MESSAGING_SCREEN);
                    } else {
                        mView.setEnableSendMessage(true);
                        mView.showError(R.string.error_placing_message_message_deliver);
                    }
                }
            });
        }
    }
}
