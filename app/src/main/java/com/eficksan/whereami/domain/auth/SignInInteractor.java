package com.eficksan.whereami.domain.auth;

import com.eficksan.whereami.data.auth.SignInData;
import com.eficksan.whereami.domain.BaseInteractor;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Aleksei_Ivshin on 9/6/16.
 */
public class SignInInteractor extends BaseInteractor<SignInData, Boolean> {

    private final FirebaseAuth mFirebaseAuth;

    public SignInInteractor(FirebaseAuth mFirebaseAuth) {
        super(Schedulers.computation(), AndroidSchedulers.mainThread());
        this.mFirebaseAuth = mFirebaseAuth;
    }

    @Override
    protected Observable<Boolean> buildObservable(final SignInData parameter) {
        return Observable.just(parameter)
                .subscribeOn(jobScheduler)
                .map(new Func1<SignInData, Task<AuthResult>>() {
                    @Override
                    public Task<AuthResult> call(SignInData signInData) {
                        return mFirebaseAuth.signInWithEmailAndPassword(signInData.email, signInData.password);
                    }
                })
                .map(new Func1<Task<AuthResult>, Boolean>() {
                    @Override
                    public Boolean call(Task<AuthResult> authResultTask) {
                        try {
                            Tasks.await(authResultTask, 5, TimeUnit.SECONDS);
                            return authResultTask.isSuccessful();
                        } catch (ExecutionException | InterruptedException | TimeoutException e) {
                            e.printStackTrace();
                            return false;
                        }
                    }
                });
    }
}
