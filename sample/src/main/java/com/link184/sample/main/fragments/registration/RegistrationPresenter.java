package com.link184.sample.main.fragments.registration;

import com.link184.respiration.repository.firebase.FirebaseGeneralRepository;
import com.link184.sample.SampleApplication;
import com.link184.sample.firebase.SamplePublicModel;

import javax.inject.Inject;

public class RegistrationPresenter {
    private RegistrationView view;

    @Inject
    FirebaseGeneralRepository<SamplePublicModel> publicRepository;

    RegistrationPresenter(RegistrationView registrationView) {
        this.view = registrationView;
        ((SampleApplication) view.getFragment().getActivity().getApplication())
                .getAppComponent().inject(this);
    }

    public void pushAccountToFirebase(String account, String password) {
        publicRepository.getFirebaseAuth().createUserWithEmailAndPassword(account, password);
    }
}
