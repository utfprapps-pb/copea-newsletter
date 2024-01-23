package br.edu.utfpr.observers;

import io.quarkus.runtime.StartupEvent;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

@ApplicationScoped
public class AppStartup {

    void onStart(@Observes StartupEvent startupEvent) {
    }

}