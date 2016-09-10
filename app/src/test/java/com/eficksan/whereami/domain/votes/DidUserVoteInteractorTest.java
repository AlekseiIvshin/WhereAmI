package com.eficksan.whereami.domain.votes;

import com.eficksan.whereami.data.votes.FirebaseDatabaseVotesRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.ValueEventListener;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.Collection;

import rx.Subscriber;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Aleksei Ivshin
 * on 10.09.2016.
 */
@RunWith(Parameterized.class)
public class DidUserVoteInteractorTest {

    DidUserVoteInteractor didUserVoteInteractor;
    FirebaseDatabaseVotesRepository mockVotesRepository = Mockito.mock(FirebaseDatabaseVotesRepository.class);

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {true, true}, // User already voted for message
                {false, true}, // User already voted against message
        });
    }

    @Parameterized.Parameter
    public boolean testUsersVote;

    @Parameterized.Parameter(value = 1)
    public boolean testDidUserVotedBefore;


    @After
    public void tearDown() {
        if (didUserVoteInteractor != null) {
            didUserVoteInteractor.unsubscribe();
        }
    }

    @Test
    public void shouldProvideDidUserVoteBefore() {
        // Given
        didUserVoteInteractor = new DidUserVoteInteractor(mockVotesRepository);
        final DataSnapshot snapshot = mock(DataSnapshot.class);
        when(snapshot.getValue(Boolean.class)).thenReturn(testUsersVote);
        doNothing().when(mockVotesRepository).canVoteMessage(anyString(), any(ValueEventListener.class));
        final String testMessageId = "test_message_id";

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] arguments = invocation.getArguments();
                ((ValueEventListener) arguments[1]).onDataChange(snapshot);
                return new Object();
            }
        }).when(mockVotesRepository).canVoteMessage(anyString(), any(ValueEventListener.class));

        Subscriber<Boolean> subscriber = new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Boolean aBoolean) {
                // Then
                Assert.assertEquals(testDidUserVotedBefore, aBoolean);
            }
        };

        // When
        didUserVoteInteractor.execute(testMessageId, subscriber);
    }

    @Test
    public void shouldReturnErrorIfDataIsNotAvailable() {
        // Given
        didUserVoteInteractor = new DidUserVoteInteractor(mockVotesRepository);
        final DataSnapshot snapshot = mock(DataSnapshot.class);
        when(snapshot.getValue(Boolean.class)).thenReturn(testUsersVote);
        doNothing().when(mockVotesRepository).canVoteMessage(anyString(), any(ValueEventListener.class));
        final String testMessageId = "test_message_id";

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] arguments = invocation.getArguments();
                ((ValueEventListener) arguments[1]).onCancelled(DatabaseError.fromException(new IllegalArgumentException("test")));
                return new Object();
            }
        }).when(mockVotesRepository).canVoteMessage(anyString(), any(ValueEventListener.class));

        Subscriber<Boolean> subscriber = new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                // Thenx
                Assert.assertTrue(e instanceof DatabaseException);
            }

            @Override
            public void onNext(Boolean aBoolean) {
            }
        };

        // When
        didUserVoteInteractor.execute(testMessageId, subscriber);
    }

    @Test
    public void shouldReturnFalseWhenUserDidNotVoteBefore() {
        // Given
        didUserVoteInteractor = new DidUserVoteInteractor(mockVotesRepository);
        final DataSnapshot snapshot = mock(DataSnapshot.class);
        when(snapshot.getValue(Boolean.class)).thenReturn(null);
        doNothing().when(mockVotesRepository).canVoteMessage(anyString(), any(ValueEventListener.class));
        final String testMessageId = "test_message_id";

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] arguments = invocation.getArguments();
                ((ValueEventListener) arguments[1]).onDataChange(snapshot);
                return new Object();
            }
        }).when(mockVotesRepository).canVoteMessage(anyString(), any(ValueEventListener.class));

        Subscriber<Boolean> subscriber = new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Boolean aBoolean) {
                // Then
                Assert.assertFalse(aBoolean);
            }
        };

        // When
        didUserVoteInteractor.execute(testMessageId, subscriber);
    }
}
