package com.link184.sample;

import android.util.Log;

import com.link184.respiration.repository.local.LocalConfiguration;
import com.link184.respiration.repository.local.LocalGeneralRepository;
import com.link184.respiration.subscribers.SingleSubscriberRespiration;
import com.link184.sample.local.User;
import com.link184.sample.main.SampleActivity;
import com.link184.sample.modules.LocalUserRepositoryBuilder;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Random;

import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import io.reactivex.observers.TestObserver;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

/**
 * Created by Ryzen on 3/2/2018.
 */

@RunWith(AndroidJUnit4.class)
public class LocalRepositoryTest {
    private final String TAG = getClass().getSimpleName();
    private final String TEST_ASSET_DB_NAME = "user_db.json";

    @Rule
    public ActivityTestRule<SampleActivity> activityTestRule = new ActivityTestRule<>(SampleActivity.class);
    private LocalGeneralRepository<User> localGeneralRepository;

    @Before
    public void prepareRepository() {
        localGeneralRepository = LocalUserRepositoryBuilder.getInstance(activityTestRule.getActivity());
    }

    @After
    public void resetDb() {
        resetRepositoryFormAssets();
    }

    private void resetRepositoryFormAssets() {
        LocalConfiguration localConfiguration = new LocalConfiguration<>(User.class);
        localConfiguration.setAssetDbFilePath(TEST_ASSET_DB_NAME);
        localConfiguration.setDatabaseChildren("userData", "user");
        localConfiguration.setContext(activityTestRule.getActivity());
        localGeneralRepository.resetRepository(localConfiguration, true);
    }

    @Test
    public void localRepositoryTest() {
        TestObserver<User> userTestObserver = new TestObserver<User>() {
            @Override
            public void onNext(User user) {
                assertNotNull("Repository emmit a null object", user != null);
            }

            @Override
            public void onError(Throwable t) {
                Log.e(TAG, "onError: ", t);
            }

            @Override
            public void onComplete() {
                super.onComplete();
            }
        };
        localGeneralRepository.subscribe(userTestObserver::onNext, userTestObserver::onError, userTestObserver::onComplete);

        User value = localGeneralRepository.getValue();
        assertNotNull(value);
    }

    @Test
    public void dbWriteTest() throws InterruptedException {
        float testRandomHeight = new Random(System.nanoTime()).nextFloat();
        User user = localGeneralRepository.getValue();
        assertNotNull(user);
        user.setHeight(testRandomHeight);
        localGeneralRepository.setValue(user);
        Thread.sleep(1_000);

        TestObserver<User> userTestObserver = new TestObserver<User>() {
            @Override
            public void onNext(User user) {
                assertNotNull("Repository emmit a null object", user);
                assertEquals("Height are not set", user.getHeight(), testRandomHeight);
            }

            @Override
            public void onError(Throwable t) {
                Log.e(TAG, "onError: ", t);
            }

            @Override
            public void onComplete() {
                super.onComplete();
            }
        };
        localGeneralRepository.subscribe(userTestObserver::onNext, userTestObserver::onError, userTestObserver::onComplete);
    }

    @Test
    public void dbDeletionTest() throws InterruptedException, IOException {
        localGeneralRepository.removeValue();
        Thread.sleep(2_000);

        TestObserver<User> userTestObserver = new TestObserver<User>() {
            @Override
            public void onNext(User user) {
                assertNull("Repository emmit a not null object", user);
            }

            @Override
            public void onError(Throwable t) {
                Log.e(TAG, "onError: ", t);
                assertNotNull(t);
            }

            @Override
            public void onComplete() {
                super.onComplete();
            }
        };
        localGeneralRepository.subscribe(new SingleSubscriberRespiration<User>() {
            @Override
            public void onSuccess(User dataSnapShot) {
                userTestObserver.onNext(dataSnapShot);
            }

            @Override
            public void onFailure(Throwable error) {
                userTestObserver.onError(error);
            }
        });
    }
}
