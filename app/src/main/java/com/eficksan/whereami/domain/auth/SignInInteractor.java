package com.eficksan.whereami.domain.auth;

import android.support.annotation.NonNull;

import com.eficksan.whereami.data.auth.SignInData;
import com.eficksan.whereami.domain.BaseInteractor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * Created by Aleksei_Ivshin on 9/6/16.
 */
public class SignInInteractor extends BaseInteractor<SignInData, Boolean> {

    private final FirebaseAuth mFirebaseAuth;
    private PublishSubject<Boolean> signInChannel;

    public SignInInteractor(FirebaseAuth mFirebaseAuth) {
        super(Schedulers.computation(), AndroidSchedulers.mainThread());
        this.mFirebaseAuth = mFirebaseAuth;
    }

    @Override
    protected Observable<Boolean> buildObservable(final SignInData parameter) {
        signInChannel = PublishSubject.create();
        signInChannel.subscribeOn(jobScheduler)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mFirebaseAuth.signInWithEmailAndPassword(parameter.email, parameter.password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        signInChannel.onNext(task.isSuccessful());
                                        signInChannel.onCompleted();
                                    }
                                });
                    }
                });
        return signInChannel;
    }
}
