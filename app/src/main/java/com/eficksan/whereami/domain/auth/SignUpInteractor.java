package com.eficksan.whereami.domain.auth;

import com.eficksan.whereami.data.auth.SignUpData;
import com.eficksan.whereami.data.auth.UsersRepository;
import com.eficksan.whereami.domain.BaseInteractor;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Sing up interactor provides method for creating new user.
 */
public class SignUpInteractor extends BaseInteractor<SignUpData, Boolean> {

    private final FirebaseAuth mFirebaseAuth;
    private final UsersRepository mUsersRepository;

    public SignUpInteractor(FirebaseAuth mFirebaseAuth, UsersRepository mUsersRepository) {
        super(Schedulers.computation(), AndroidSchedulers.mainThread());
        this.mFirebaseAuth = mFirebaseAuth;
        this.mUsersRepository = mUsersRepository;
    }

    @Override
    protected Observable<Boolean> buildObservable(final SignUpData parameter) {
        return Observable.just(parameter)
                .subscribeOn(jobScheduler)
                .map(new Func1<SignUpData, Task<AuthResult>>() {
                    @Override
                    public Task<AuthResult> call(SignUpData signUpData) {
                        return mFirebaseAuth.createUserWithEmailAndPassword(signUpData.email, signUpData.password);
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
                })
                //TODO: Check on user is auto sign in when user registered
//                .doOnNext(new Action1<Boolean>() {
//                    @Override
//                    public void call(Boolean aBoolean) {
//                        // Sign in user
//                        Task<AuthResult> authResultTask = mFirebaseAuth.signInWithEmailAndPassword(parameter.email, parameter.password);
//                        try {
//                            Tasks.await(authResultTask);
//                        } catch (ExecutionException | InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                })
                .doOnNext(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean isSucceed) {
                        if (isSucceed) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(parameter.userName)
                                    .build();
                            assert user != null;
                            user.updateProfile(profileUpdates);
                            mUsersRepository.setCurrentUserName(parameter.userName);
                        }
                    }
                })
                .doOnNext(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean isSucceed) {
                        if (isSucceed) {
                            mUsersRepository.setCurrentUserName(parameter.userName);
                        }
                    }
                });
    }
}
