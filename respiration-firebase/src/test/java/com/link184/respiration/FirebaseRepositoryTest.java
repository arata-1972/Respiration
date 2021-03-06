package com.link184.respiration;

import com.link184.respiration.models.TestModel;
import com.link184.respiration.repository.firebase.FirebaseGeneralRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

/**
 * Created by Ryzen on 2/25/2018.
 */

@RunWith(MockitoJUnitRunner.class)
public class FirebaseRepositoryTest {
    @Test
    public void generalRepositoryTest() throws Exception {
        FirebaseGeneralRepository<TestModel> firebaseGeneralRepository = mock(FirebaseGeneralRepository.class);
        assertNotNull(firebaseGeneralRepository);
    }
}
