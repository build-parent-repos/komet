package dev.ikm.komet.framework.panel;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;
import javafx.scene.control.ScrollPane;
import dev.ikm.komet.framework.observable.ObservableEntity;
import dev.ikm.komet.framework.observable.ObservableEntitySnapshot;
import dev.ikm.komet.framework.view.ViewProperties;
import dev.ikm.tinkar.common.flow.FlowSubscriber;
import dev.ikm.tinkar.common.util.broadcast.Subscriber;
import dev.ikm.tinkar.entity.Entity;
import dev.ikm.tinkar.terms.EntityFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class ComponentPanel
        extends ComponentPanelAbstract
        implements ChangeListener<EntityFacade> {
    private static final Logger LOG = LoggerFactory.getLogger(ComponentPanel.class);
    private final ScrollPane scrollPane = new ScrollPane(componentPanelBox);
    private final SimpleObjectProperty<EntityFacade> componentProperty;
    private final WeakChangeListener<EntityFacade> weakComponentChangedListener = new WeakChangeListener(this);
    private final Subscriber<Integer> invalidationSubscriber;

    {
        this.scrollPane.setFitToWidth(true);
        this.scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        this.scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        this.componentDetailPane.setCenter(this.scrollPane);
    }

    public ComponentPanel(SimpleObjectProperty<EntityFacade> componentProperty, ViewProperties viewProperties) {
        super(viewProperties);
        this.componentProperty = componentProperty;
        this.componentProperty.addListener(this.weakComponentChangedListener);
        Platform.runLater(() -> changed(this.componentProperty, null, this.componentProperty.getValue()));
        this.invalidationSubscriber = new FlowSubscriber<>(nid -> {
            if (this.componentProperty.get() != null) {
                if (getReferencedNids().contains(nid)) {
                    // component has changed, need to update.
                    EntityFacade entityFacade = this.componentProperty.get();
                    Platform.runLater(() -> componentProperty.set(null));
                    if (entityFacade != null) {
                        Platform.runLater(() -> componentProperty.set(Entity.provider().getEntityFast(entityFacade)));
                    }
                }
            }
        });
        Entity.provider().addSubscriberWithWeakReference(this.invalidationSubscriber);
    }

    @Override
    public void changed(ObservableValue<? extends EntityFacade> observable, EntityFacade oldValue, EntityFacade newValue) {
        referencedNids.clear();
        if (newValue != null) {
            referencedNids.add(newValue.nid());
            Platform.runLater(() -> {
                getComponentPanelBox().getChildren().clear();
                ObservableEntitySnapshot entitySnapshot = ObservableEntity.getSnapshot(newValue.nid(), viewProperties.calculator());
                getComponentPanelBox().getChildren().add(makeComponentPanel(entitySnapshot, componentProperty).getComponentDetailPane());
            });
        }
    }

    @Override
    public <C extends EntityFacade> Optional<C> getComponent() {
        return Optional.ofNullable((C) componentProperty.getValue());
    }
}
