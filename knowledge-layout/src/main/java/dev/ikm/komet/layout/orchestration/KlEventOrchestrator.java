package dev.ikm.komet.layout.orchestration;

import dev.ikm.komet.layout.event.KlPerformanceEvent;
import dev.ikm.komet.layout.event.KlRequestEvent;
import org.eclipse.collections.api.list.ImmutableList;

/**
 * KlEventOrchestrator is responsible for orchestrating a series of request events
 * based on the provided performance event.
 */
public interface KlEventOrchestrator {

    /**
     * Orchestrates a series of request events based on the given performance event.
     *
     * @param klPerformanceEvent the performance event that triggers the orchestration
     * @return an immutable list of request events generated by the orchestration process
     */
    ImmutableList<KlRequestEvent> orchestrate(KlPerformanceEvent klPerformanceEvent);
}