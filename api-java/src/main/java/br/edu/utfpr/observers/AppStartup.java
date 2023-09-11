package br.edu.utfpr.observers;

import io.quarkus.runtime.StartupEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

@ApplicationScoped
public class AppStartup {

    void onStart(@Observes StartupEvent startupEvent) {
    }

}