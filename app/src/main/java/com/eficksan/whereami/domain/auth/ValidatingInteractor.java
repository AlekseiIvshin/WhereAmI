package com.eficksan.whereami.domain.auth;

import com.eficksan.whereami.domain.BaseInteractor;
import com.eficksan.whereami.domain.Validator;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Provides methods for unified validating values.
 */
public class ValidatingInteractor extends BaseInteractor<Observable<CharSequence>, Boolean> {

    private final Validator mValidator;

    public ValidatingInteractor(Validator mValidator) {
        super(Schedulers.computation(), AndroidSchedulers.mainThread());
        this.mValidator = mValidator;
    }

    @Override
    protected Observable<Boolean> buildObservable(Observable<CharSequence> parameter) {
        return parameter
                .debounce(300, TimeUnit.MICROSECONDS)
                .map(new Func1<CharSequence, Boolean>() {
                    @Override
                    public Boolean call(CharSequence email) {
                        return mValidator.validate(email.toString());
                    }
                });
    }
}
