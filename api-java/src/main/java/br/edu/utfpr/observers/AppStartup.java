package br.edu.utfpr.observers;

import br.edu.utfpr.teste.QuartzInitializer;
import io.quarkus.runtime.StartupEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@ApplicationScoped
public class AppStartup {

    @Inject
    QuartzInitializer quartzInitializer;

    void onStart(@Observes StartupEvent startupEvent) {
        quartzInitializer.init();
    }
}
