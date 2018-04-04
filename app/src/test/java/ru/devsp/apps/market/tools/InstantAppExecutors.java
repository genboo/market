package ru.devsp.apps.market.tools;

import java.util.concurrent.Executor;

/**
 *
 * Created by gen on 15.09.2017.
 */

public class InstantAppExecutors extends AppExecutors {
    private static Executor instant = command -> command.run();

    public InstantAppExecutors() {
        super(instant, instant, instant);
    }
}