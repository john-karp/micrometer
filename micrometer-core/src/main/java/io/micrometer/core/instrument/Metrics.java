package io.micrometer.core.instrument;

import io.micrometer.core.instrument.composite.CompositeMeterRegistry;

import java.util.Collection;
import java.util.Map;
import java.util.function.ToDoubleFunction;

/**
 * @author Jon Schneider
 */
public class Metrics {
    public static final CompositeMeterRegistry globalRegistry = new CompositeMeterRegistry();

    public static void addRegistry(MeterRegistry registry) {
        globalRegistry.add(registry);
    }

    public static void removeRegistry(MeterRegistry registry) {
        globalRegistry.remove(registry);
    }

    /**
     * Build a new Counter, which is registered with this registry once {@link Counter.Builder#create()} is called.
     *
     * @param name The name of the counter (which is the only requirement for a new counter).
     * @return The builder.
     */
    public static Counter.Builder counterBuilder(String name) {
        return globalRegistry.counterBuilder(name);
    }

    /**
     * Tracks a monotonically increasing value.
     */
    public static Counter counter(String name, Iterable<Tag> tags) {
        return globalRegistry.counter(name, tags);
    }

    /**
     * Tracks a monotonically increasing value.
     */
    public static Counter counter(String name, String... tags) {
        return globalRegistry.counter(name, tags);
    }

    /**
     * Build a new Distribution Summary, which is registered with this registry once {@link DistributionSummary.Builder#create()} is called.
     *
     * @param name The name of the distribution summary (which is the only requirement for a new distribution summary).
     * @return The builder.
     */
    public static DistributionSummary.Builder summaryBuilder(String name) {
        return globalRegistry.summaryBuilder(name);
    }

    /**
     * Measures the sample distribution of events.
     */
    public static DistributionSummary summary(String name, Iterable<Tag> tags) {
        return globalRegistry.summary(name, tags);
    }

    /**
     * Measures the sample distribution of events.
     */
    public static DistributionSummary summary(String name, String... tags) {
        return globalRegistry.summary(name, tags);
    }

    /**
     * Build a new Timer, which is registered with this registry once {@link Timer.Builder#create()} is called.
     *
     * @param name The name of the timer (which is the only requirement for a new timer).
     * @return The builder.
     */
    public static Timer.Builder timerBuilder(String name) {
        return globalRegistry.timerBuilder(name);
    }

    /**
     * Measures the time taken for short tasks.
     */
    public static Timer timer(String name, Iterable<Tag> tags) {
        return globalRegistry.timer(name, tags);
    }

    /**
     * Measures the time taken for short tasks.
     */
    public static Timer timer(String name, String... tags) {
        return globalRegistry.timer(name, tags);
    }

    static class More {
        /**
         * Measures the time taken for short tasks.
         */
        public LongTaskTimer longTaskTimer(String name, Iterable<Tag> tags) {
            return globalRegistry.more().longTaskTimer(name, tags);
        }

        /**
         * Measures the time taken for short tasks.
         */
        public LongTaskTimer longTaskTimer(String name, String... tags) {
            return globalRegistry.more().longTaskTimer(name, tags);
        }

        /**
         * Build a new LongTaskTimer, which is registered with this registry once {@link LongTaskTimer.Builder#create()} is called.
         *
         * @param name The name of the timer (which is the only requirement for a new timer).
         * @return The builder.
         */
        public LongTaskTimer.Builder longTaskTimerBuilder(String name) {
            return globalRegistry.more().longTaskTimerBuilder(name);
        }

        /**
         * Tracks a monotonically increasing value, automatically incrementing the counter whenever
         * the value is observed.
         */
        public <T> T counter(String name, Iterable<Tag> tags, T obj, ToDoubleFunction<T> f) {
            return globalRegistry.more().counter(name, tags, obj, f);
        }

        /**
         * Tracks a number, maintaining a weak reference on it.
         */
        public <T extends Number> T counter(String name, Iterable<Tag> tags, T number) {
            return globalRegistry.more().counter(name, tags, number);
        }
    }

    private static final More more = new More();

    /**
     * Access to less frequently used meter types and patterns.
     */
    public static More more() {
        return more;
    };

    public static MeterRegistry register(String name, Iterable<Tag> tags, Meter.Type type, Iterable<Measurement> measurements) {
        return globalRegistry.register(name, tags, type, measurements);
    }

    /**
     * Register a gauge that reports the value of the object after the function
     * {@code f} is applied. The registration will keep a weak reference to the object so it will
     * not prevent garbage collection. Applying {@code f} on the object should be thread safe.
     * <p>
     * If multiple gauges are registered with the same id, then the values will be aggregated and
     * the sum will be reported. For example, registering multiple gauges for active threads in
     * a thread pool with the same id would produce a value that is the overall number
     * of active threads. For other behaviors, manage it on the user side and avoid multiple
     * registrations.
     *
     * @param name Name of the gauge being registered.
     * @param tags Sequence of dimensions for breaking down the getName.
     * @param obj  Object used to compute a value.
     * @param f    Function that is applied on the value for the number.
     * @return The number that was passed in so the registration can be done as part of an assignment
     * statement.
     */
    public static <T> T gauge(String name, Iterable<Tag> tags, T obj, ToDoubleFunction<T> f) {
        return globalRegistry.gauge(name, tags, obj, f);
    }

    /**
     * Register a gauge that reports the value of the {@link java.lang.Number}.
     *
     * @param name   Name of the gauge being registered.
     * @param tags   Sequence of dimensions for breaking down the getName.
     * @param number Thread-safe implementation of {@link Number} used to access the value.
     * @return The number that was passed in so the registration can be done as part of an assignment
     * statement.
     */
    public static <T extends Number> T gauge(String name, Iterable<Tag> tags, T number) {
        return globalRegistry.gauge(name, tags, number);
    }

    /**
     * Register a gauge that reports the value of the {@link java.lang.Number}.
     *
     * @param name   Name of the gauge being registered.
     * @param number Thread-safe implementation of {@link Number} used to access the value.
     * @return The number that was passed in so the registration can be done as part of an assignment
     * statement.
     */
    public static <T extends Number> T gauge(String name, T number) {
        return globalRegistry.gauge(name, number);
    }

    /**
     * Register a gauge that reports the value of the object.
     *
     * @param name Name of the gauge being registered.
     * @param obj  Object used to compute a value.
     * @param f    Function that is applied on the value for the number.
     * @return The number that was passed in so the registration can be done as part of an assignment
     * statement.
     */
    public static <T> T gauge(String name, T obj, ToDoubleFunction<T> f) {
        return globalRegistry.gauge(name, obj, f);
    }

    /**
     * Register a gauge that reports the size of the {@link java.util.Collection}. The registration
     * will keep a weak reference to the collection so it will not prevent garbage collection.
     * The collection implementation used should be thread safe. Note that calling
     * {@link java.util.Collection#size()} can be expensive for some collection implementations
     * and should be considered before registering.
     *
     * @param name       Name of the gauge being registered.
     * @param tags       Sequence of dimensions for breaking down the getName.
     * @param collection Thread-safe implementation of {@link Collection} used to access the value.
     * @return The number that was passed in so the registration can be done as part of an assignment
     * statement.
     */
    public static <T extends Collection<?>> T gaugeCollectionSize(String name, Iterable<Tag> tags, T collection) {
        return globalRegistry.gaugeCollectionSize(name, tags, collection);
    }

    /**
     * Register a gauge that reports the size of the {@link java.util.Map}. The registration
     * will keep a weak reference to the collection so it will not prevent garbage collection.
     * The collection implementation used should be thread safe. Note that calling
     * {@link java.util.Map#size()} can be expensive for some collection implementations
     * and should be considered before registering.
     *
     * @param name Name of the gauge being registered.
     * @param tags Sequence of dimensions for breaking down the getName.
     * @param map  Thread-safe implementation of {@link Map} used to access the value.
     * @return The number that was passed in so the registration can be done as part of an assignment
     * statement.
     */
    public static <T extends Map<?, ?>> T gaugeMapSize(String name, Iterable<Tag> tags, T map) {
        return globalRegistry.gaugeMapSize(name, tags, map);
    }

    /**
     * Build a new Gauge, which is registered with this registry once {@link Gauge.Builder#create()} is called.
     *
     * @param name The name of the gauge.
     * @param obj  Object used to compute a value.
     * @param f    Function that is applied on the value for the number.
     * @return The builder.
     */
    public static <T> Gauge.Builder gaugeBuilder(String name, T obj, ToDoubleFunction<T> f) {
        return globalRegistry.gaugeBuilder(name, obj, f);
    }
}