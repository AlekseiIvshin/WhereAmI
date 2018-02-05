package com.eficksan.whereami.domain.auth;

import com.eficksan.whereami.data.auth.SignUpData;
import com.eficksan.whereami.data.auth.UsersDataSource;
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
import rx.Scheduler;
import rx.functions.Func1;

/**
 * Sing up interactor provides method for creating new user.
 */
public class SignUpInteractor extends BaseInteractor<SignUpData, Boolean> {

    private final FirebaseAuth mFirebaseAuth;
    private final UsersDataSource mUsersDataSource;

    public SignUpInteractor(
            FirebaseAuth firebaseAuth,
            UsersDataSource usersDataSource,
            Scheduler jobScheduler, Scheduler uiScheduler) {
        super(jobScheduler, uiScheduler);
        this.mFirebaseAuth = firebaseAuth;
        this.mUsersDataSource = usersDataSource;
    }

    @Override
    protected Observable<Boolean> buildObservable(final SignUpData parameter) {
        return Observable.just(parameter)
                .subscribeOn(jobScheduler)
                .map(new Func1<SignUpData, Task<AuthResult>>() {
                    @Override
                    public Task<AuthResult> call(SignUpData signUpData) {
                        return mFirebaseAuth.createUserWithEmailAndPassword(
                                signUpData.email,
                                signUpData.password);
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
                .flatMap(new Func1<Boolean, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Boolean isSucceed) {
                        if (isSucceed) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(parameter.userName)
                                    .build();
                            assert user != null;
                            user.updateProfile(profileUpdates);
                            return mUsersDataSource.setCurrentUserName(user.getUid(), parameter.userName);
                        }
                        return Observable.just(false);
                    }
                });
    }
}
