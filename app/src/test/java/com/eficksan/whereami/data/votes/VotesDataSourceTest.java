package com.eficksan.whereami.data.votes;

import com.eficksan.whereami.App;
import com.eficksan.whereami.BuildConfig;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import rx.observers.TestSubscriber;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Aleksei_Ivshin on 9/12/16.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class,
        application = App.class)
public class VotesDataSourceTest {

    static String messageId = "testMessageId";
    static String userId = "userId";
    VotesDataSource votesDataSource;
    FirebaseDatabase mockDatabase;

    @Before
    public void setUp() {
        mockDatabase = mock(FirebaseDatabase.class);
        votesDataSource = new VotesDataSource(mockDatabase);
    }

    @Test
    @Ignore
    public void shouldReturnTrueWhenUserVotedForMessage() {
        // Given
        TestSubscriber<Boolean> testSubscriber = new TestSubscriber<>();

        // Mock database
        DatabaseReference mockDatabaseRef = mock(DatabaseReference.class);
        when(mockDatabaseRef.child(anyString())).thenReturn(mockDatabaseRef);

        final DataSnapshot snapshot = mock(DataSnapshot.class);
        when(snapshot.getValue(Boolean.class)).thenReturn(true);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] arguments = invocation.getArguments();
                ((ValueEventListener) arguments[0]).onDataChange(snapshot);
                return new Object();
            }
        }).when(mockDatabaseRef).addListenerForSingleValueEvent(any(ValueEventListener.class));
        when(mockDatabase.getReference()).thenReturn(mockDatabaseRef);

        // When
        votesDataSource.fetchUserMessageVote(messageId, userId).subscribe(testSubscriber);

        // Then
        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertNoErrors();
        testSubscriber.assertCompleted();
        testSubscriber.assertValue(true);
    }

    @Test
    @Ignore
    public void shouldReturnFalseWhenUserVotedAgainstMessage() {
        // Given
        TestSubscriber<Boolean> testSubscriber = new TestSubscriber<>();

        // Mock database
        DatabaseReference mockDatabaseRef = mock(DatabaseReference.class);
        when(mockDatabaseRef.child(anyString())).thenReturn(mockDatabaseRef);

        final DataSnapshot snapshot = mock(DataSnapshot.class);
        when(snapshot.getValue(Boolean.class)).thenReturn(false);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] arguments = invocation.getArguments();
                ((ValueEventListener) arguments[0]).onDataChange(snapshot);
                return new Object();
            }
        }).when(mockDatabaseRef).addListenerForSingleValueEvent(any(ValueEventListener.class));
        when(mockDatabase.getReference()).thenReturn(mockDatabaseRef);

        // When
        votesDataSource.fetchUserMessageVote(messageId, userId).subscribe(testSubscriber);

        // Then
        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertNoErrors();
        testSubscriber.assertCompleted();
        testSubscriber.assertValue(false);
    }

    @Test
    @Ignore
    public void shouldReturnNullWhenUserDidNotVoteBefore() {
        // Given
        TestSubscriber<Boolean> testSubscriber = new TestSubscriber<>();

        // Mock database
        DatabaseReference mockDatabaseRef = mock(DatabaseReference.class);
        when(mockDatabaseRef.child(anyString())).thenReturn(mockDatabaseRef);

        final DataSnapshot snapshot = mock(DataSnapshot.class);
        when(snapshot.getValue(Boolean.class)).thenReturn(null);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] arguments = invocation.getArguments();
                ((ValueEventListener) arguments[0]).onDataChange(snapshot);
                return new Object();
            }
        }).when(mockDatabaseRef).addListenerForSingleValueEvent(any(ValueEventListener.class));
        when(mockDatabase.getReference()).thenReturn(mockDatabaseRef);

        // When
        votesDataSource.fetchUserMessageVote(messageId, userId).subscribe(testSubscriber);

        // Then
        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertNoErrors();
        testSubscriber.assertCompleted();
        testSubscriber.assertValue(null);
    }
}
