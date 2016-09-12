package com.eficksan.whereami.domain.votes;

import com.eficksan.whereami.data.votes.FirebaseDatabaseVotesRepository;
import com.eficksan.whereami.data.votes.VotesDataSource;
import com.eficksan.whereami.domain.BaseInteractor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * Interactor for checking user available to vote message.
 */
public class DidUserVoteInteractor extends BaseInteractor<String, Boolean> {

    private final VotesDataSource dataSource;
    private final FirebaseAuth auth;

    public DidUserVoteInteractor(VotesDataSource dataSource, FirebaseAuth auth) {
        super(Schedulers.computation(), AndroidSchedulers.mainThread());
        this.dataSource = dataSource;
        this.auth = auth;
    }

    @Override
    protected Observable<Boolean> buildObservable(String parameter) {
        return dataSource.fetchUserMessageVote(parameter,auth.getCurrentUser().getUid())
                .map(new Func1<Boolean, Boolean>() {
                    @Override
                    public Boolean call(Boolean aBoolean) {
                        return aBoolean != null;
                    }
                });
    }

}
