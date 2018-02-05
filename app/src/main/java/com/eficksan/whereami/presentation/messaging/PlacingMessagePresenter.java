package com.eficksan.whereami.presentation.messaging;

import android.location.Location;

import com.eficksan.whereami.R;
import com.eficksan.whereami.data.messages.PlacingMessage;
import com.eficksan.whereami.domain.messages.PlaceMessageValidator;
import com.eficksan.whereami.domain.messages.PlacingMessageInteractor;
import com.eficksan.whereami.presentation.common.BasePresenter;
import com.eficksan.whereami.presentation.routing.Screens;

import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Messaging presenter component.
 * Provides logic from Interactors to View part.
 * <p/>
 * Created by Aleksei Ivshin
 * on 24.08.2016.
 */
public class PlacingMessagePresenter extends BasePresenter<PlacingMessageView> {

    private final PlacingMessageInteractor placingMessageInteractor;
    private final PlaceMessageValidator placeMessageValidator;

    private boolean mIsMessageValid;
    private String mMessage = "";

    private Location mMessageLocation;

    public PlacingMessagePresenter(
            PlacingMessageInteractor placingMessageInteractor,
            PlaceMessageValidator placeMessageValidator) {
        this.placingMessageInteractor = placingMessageInteractor;
        this.placeMessageValidator = placeMessageValidator;
    }

    @Override
    public void onViewCreated(PlacingMessageView view) {
        super.onViewCreated(view);
        setListeners();
    }

    /**
     * On stop presentation.
     */
    public void onStop() {
        mView.sendMessage.setOnClickListener(null);
        placingMessageInteractor.unsubscribe();
        super.onStop();
    }

    /**
     * Set event listeners to View.
     */
    private void setListeners() {
        // Subscribe to message text changes
        mView.getMessageValuesChannel()
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        mIsMessageValid = placeMessageValidator.validate(s);
                        if (mIsMessageValid || mMessage.isEmpty()) {
                            mView.hideMessageValidationError();
                        } else {
                            mView.showMessageValidationError(
                                    R.string.error_placing_message_message_invalid);
                        }

                        mView.setEnableSendMessage(mIsMessageValid && !mMessage.isEmpty());
                        return mIsMessageValid;
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String messageText) {
                        mMessage = messageText;
                    }
                });

        mView.getCreateMessageChannel()
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        // For creating message need location
                        if (mMessageLocation == null) {
                            mView.showError(R.string.error_placing_message_location_required);
                            return;
                        }
                        mView.setEnableSendMessage(false);
                        PlacingMessage placingMessage = new PlacingMessage(
                                mMessageLocation.getLatitude(),
                                mMessageLocation.getLongitude(),
                                mMessage);
                        placingMessageInteractor.execute(placingMessage, new PlacingSubscriber());
                    }
                });
    }

    public void setLocation(Location location) {
        this.mMessageLocation = location;
    }

    private class PlacingSubscriber extends Subscriber<Boolean> {
        @Override
        public void onCompleted() {
            placingMessageInteractor.unsubscribe();
        }

        @Override
        public void onError(Throwable e) {
            placingMessageInteractor.unsubscribe();
        }

        @Override
        public void onNext(Boolean isMessageAdded) {
            if (isMessageAdded) {
                // Message was sent. Return to previous screen.
                mView.showSuccess(R.string.success_placing_message_message_delivered);

                mRouter.closeScreen(Screens.MESSAGING_SCREEN);
            } else {
                mView.setEnableSendMessage(true);
                mView.showError(R.string.error_placing_message_message_deliver);
            }
        }
    }
}
