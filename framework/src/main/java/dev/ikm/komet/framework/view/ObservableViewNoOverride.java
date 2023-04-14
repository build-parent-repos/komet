/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * You may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributions from 2013-2017 where performed either by US government
 * employees, or under US Veterans Health Administration contracts.
 *
 * US Veterans Health Administration contributions by government employees
 * are work of the U.S. Government and are not subject to copyright
 * protection in the United States. Portions contributed by government
 * employees are USGovWork (17USC §105). Not subject to copyright.
 *
 * Contribution by contractors to the US Veterans Health Administration
 * during this period are contractually contributed under the
 * Apache License, Version 2.0.
 *
 * See: https://www.usa.gov/government-works
 *
 * Contributions prior to 2013:
 *
 * Copyright (C) International Health Terminology Standards Development Organisation.
 * Licensed under the Apache License, Version 2.0.
 *
 */


package dev.ikm.komet.framework.view;

//~--- JDK imports ------------------------------------------------------------

import javafx.beans.property.ListProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import dev.ikm.tinkar.coordinate.view.ViewCoordinate;
import dev.ikm.tinkar.coordinate.view.ViewCoordinateRecord;

//~--- non-JDK imports --------------------------------------------------------

//~--- classes ----------------------------------------------------------------

/**
 * The Class ObservableManifoldCoordinateImpl.
 *
 * @author kec
 */
public class ObservableViewNoOverride extends ObservableViewBase {

    public ObservableViewNoOverride(ViewCoordinate viewRecord, String name) {
        super(viewRecord, name);
    }

    public ObservableViewNoOverride(ViewCoordinate viewRecord) {
        super(viewRecord);
    }

    public ViewProperties makeOverridableViewProperties() {
        return new ViewProperties(new ObservableViewWithOverride(this), this);
    }

    @Override
    protected ObservableStampCoordinateBase makeStampCoordinateObservable(ViewCoordinate viewRecord) {
        return new ObservableStampCoordinateNoOverride(viewRecord.stampCoordinate());
    }

    @Override
    protected ObservableNavigationCoordinateNoOverride makeNavigationCoordinateObservable(ViewCoordinate viewRecord) {
        return new ObservableNavigationCoordinateNoOverride(viewRecord.navigationCoordinate());
    }


    @Override
    protected ListProperty<ObservableLanguageCoordinateBase> makeLanguageCoordinateListProperty(ViewCoordinate viewRecord) {
        ObservableList<ObservableLanguageCoordinateBase> languageCoordinateList = FXCollections.observableArrayList();
        viewRecord.languageCoordinateIterable().forEach(languageCoordinateRecord ->
                languageCoordinateList.add(new ObservableLanguageCoordinateNoOverride(languageCoordinateRecord)));

        ListProperty<ObservableLanguageCoordinateBase> languageListProperty = new SimpleEqualityBasedListProperty<>(this,
                "", languageCoordinateList);
        return languageListProperty;
    }

    @Override
    protected ObservableLogicCoordinateBase makeLogicCoordinateObservable(ViewCoordinate viewRecord) {
        return new ObservableLogicCoordinateNoOverride(viewRecord.logicCoordinate());
    }

    @Override
    protected ObservableEditCoordinateBase makeEditCoordinateObservable(ViewCoordinate viewRecord) {
        return new ObservableEditCoordinateNoOverride(viewRecord);
    }

    public void removeOverrides() {
        // nothing to do, this coordinate cannot be overridden.
    }

    @Override
    public void setExceptOverrides(ViewCoordinateRecord viewRecord) {
        setValue(viewRecord.toViewCoordinateRecord());
    }

    @Override
    public ViewCoordinateRecord getOriginalValue() {
        return getValue();
    }


    @Override
    protected ViewCoordinateRecord baseCoordinateChangedListenersRemoved(ObservableValue<? extends ViewCoordinateRecord> observable,
                                                                         ViewCoordinateRecord oldValue, ViewCoordinateRecord newValue) {
        this.stampCoordinateObservable.setValue(newValue.stampCoordinate());
        this.languageCoordinates.setAll(newValue.languageCoordinates().stream()
                .map(languageCoordinate -> new ObservableLanguageCoordinateNoOverride(languageCoordinate)).toList());
        this.navigationCoordinateObservable.setValue(newValue.navigationCoordinate());
        this.logicCoordinateObservable.setValue(newValue.logicCoordinate());
        return newValue;
    }

}


