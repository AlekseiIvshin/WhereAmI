package com.eficksan.whereami.domain.auth;

import com.eficksan.whereami.domain.Validator;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Matchers;
import org.mockito.Mockito;

import rx.Subscriber;
import rx.subjects.PublishSubject;

/**
 * Created by Aleksei Ivshin
 * on 06.09.2016.
 */
@RunWith(JUnit4.class)
public class ValidatingInteractorTest {

    ValidatingInteractor validatingInteractor;

    @After
    public void tearDown() {
        if (validatingInteractor!=null) {
            validatingInteractor.unsubscribe();
        }
    }

    @Test
    public void shouldReturnTrueWhenValidatorReturnsTrue() {
        // Given
        Validator validator = Mockito.mock(Validator.class);
        Mockito.when(validator.validate(Matchers.anyString())).thenReturn(true);
        validatingInteractor = new ValidatingInteractor(validator);
        PublishSubject<CharSequence> textChannel = PublishSubject.create();
        Subscriber<Boolean> validationSubscriber = new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Boolean aBoolean) {
                Assert.assertTrue(aBoolean);
            }
        };
        validatingInteractor.execute(textChannel, validationSubscriber);

        // When
        textChannel.onNext("test");
    }
    @Test
    public void shouldReturnFalseWhenValidatorReturnsFalse() {
        // Given
        Validator validator = Mockito.mock(Validator.class);
        Mockito.when(validator.validate(Matchers.anyString())).thenReturn(false);
        validatingInteractor = new ValidatingInteractor(validator);
        PublishSubject<CharSequence> textChannel = PublishSubject.create();
        Subscriber<Boolean> validationSubscriber = new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Boolean aBoolean) {
                Assert.assertFalse(aBoolean);
            }
        };
        validatingInteractor.execute(textChannel, validationSubscriber);

        // When
        textChannel.onNext("test");
    }
}
