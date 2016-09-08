package com.eficksan.whereami.presentation.message;

import com.eficksan.whereami.data.auth.User;
import com.eficksan.whereami.data.messages.PlacingMessage;
import com.eficksan.whereami.domain.messages.FindMessageInteractor;
import com.eficksan.whereami.domain.users.FindUserInteractor;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Message details presenter.
 */
public class MessageDetailsPresenter {

    @Inject
    FindUserInteractor findUserInteractor;

    @Inject
    FindMessageInteractor findMessageInteractor;

    private Subscriber<User> userSubscriber = new Subscriber<User>() {
        @Override
        public void onCompleted() {
            findUserInteractor.unsubscribe();
        }

        @Override
        public void onError(Throwable e) {
            findUserInteractor.unsubscribe();
        }

        @Override
        public void onNext(User user) {
            mDetailsView.showAuthor(user);

        }
    };

    private Subscriber<PlacingMessage> placingMessageSubscriber = new Subscriber<PlacingMessage>() {
        @Override
        public void onCompleted() {
            findMessageInteractor.unsubscribe();
        }

        @Override
        public void onError(Throwable e) {
            findMessageInteractor.unsubscribe();
        }

        @Override
        public void onNext(PlacingMessage placingMessage) {
            mDetailsView.showMessage(placingMessage);
            findUserInteractor.execute(placingMessage.userId, userSubscriber);
        }
    };

    private MessageDetailsView mDetailsView;

    public void setView(MessageDetailsView detailsView) {
        this.mDetailsView = detailsView;
    }

    public void onCreate(String messageId) {
        findMessageInteractor.execute(messageId, placingMessageSubscriber);
    }

    public void onDestroy() {
        findMessageInteractor.unsubscribe();
        findUserInteractor.unsubscribe();
    }
}
