package com.eficksan.whereami.presentation.messaging;

import android.content.Context;
import android.location.Location;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.eficksan.whereami.R;
import com.eficksan.whereami.data.messages.PlacingMessage;
import com.eficksan.whereami.domain.messages.PlaceMessageValidator;
import com.eficksan.whereami.domain.messages.PlacingMessageInteractor;
import com.eficksan.whereami.presentation.routing.Router;
import com.eficksan.whereami.presentation.routing.Screens;
import com.jakewharton.rxbinding.widget.RxTextView;

import javax.inject.Inject;

import rx.Subscriber;
import rx.functions.Action1;

/**
 * Messaging presenter component.
 * Provides logic from Interactors to View part.
 *
 * Created by Aleksei Ivshin
 * on 24.08.2016.
 */
public class PlacingMessagePresenter implements View.OnClickListener {

    @Inject
    Router router;

    @Inject
    PlacingMessageInteractor placingMessageInteractor;

    @Inject
    PlaceMessageValidator placeMessageValidator;

    @Inject
    Context context;

    private PlacingMessageView placingMessageView;

    private Location mMessageLocation;

    /**
     * Sets view to presenter.
     * @param view view
     */
    public void setView(PlacingMessageView view) {
        this.placingMessageView = view;
    }

    /**
     * On start presentation.
     * @param messageLocation current location, message will be created using this location
     */
    public void onStart(Location messageLocation) {
        this.mMessageLocation = messageLocation;
        setListeners();
        placingMessageView.messageInput.requestFocus();

        InputMethodManager keyboard = (InputMethodManager)
                context.getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.showSoftInput(placingMessageView.messageInput, 0);
    }

    /**
     * On stop presentation.
     */
    public void onStop() {
        placingMessageView.sendMessage.setOnClickListener(null);
        placingMessageInteractor.unsubscribe();
    }

    /**
     * Set event listeners to View.
     */
    private void setListeners() {
        // Subscribe to message text changes
        RxTextView.textChanges(placingMessageView.messageInput)
                .subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence messageText) {
                        boolean isMessageValid = placeMessageValidator.validate(messageText.toString());
                        if (isMessageValid) {
                            placingMessageView.hideMessageValidationError();
                            placingMessageView.setEnableSendMessage(true);
                        } else {
                            placingMessageView.showMessageValidationError(R.string.error_placing_message_message_invalid);
                            placingMessageView.setEnableSendMessage(false);
                        }
                    }
                });

        placingMessageView.sendMessage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (R.id.create_message == v.getId()) {
            // For creating message need location
            if (mMessageLocation == null) {
                placingMessageView.showError(R.string.error_placing_message_location_required);
                return;
            }
            placingMessageView.setEnableSendMessage(false);
            final String message = PlacingMessagePresenter.this.placingMessageView.messageInput.getText().toString();
            PlacingMessage placingMessage = new PlacingMessage(mMessageLocation.getLatitude(), mMessageLocation.getLongitude(), message);
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
                        placingMessageView.showSuccess(R.string.success_placing_message_message_delivered);

                        InputMethodManager keyboard = (InputMethodManager)
                                context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        keyboard.hideSoftInputFromWindow(placingMessageView.messageInput.getWindowToken(), 0);
                        router.closeScreen(Screens.MESSAGING_SCREEN);
                    } else {
                        placingMessageView.setEnableSendMessage(true);
                        placingMessageView.showError(R.string.error_placing_message_message_deliver);
                    }
                }
            });
        }
    }
}
