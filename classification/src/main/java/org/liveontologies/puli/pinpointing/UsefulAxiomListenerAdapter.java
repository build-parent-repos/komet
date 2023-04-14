package org.liveontologies.puli.pinpointing;

/*-
 * #%L
 * Proof Utility Library
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2014 - 2021 Live Ontologies Project
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

public class UsefulAxiomListenerAdapter<A>
		extends FailingAxiomPinpointingListener<A> {

	private final UsefulAxiomListener<A> delegate_;

	public UsefulAxiomListenerAdapter(UsefulAxiomListener<A> delegate) {
		this.delegate_ = delegate;
	}

	@Override
	public void usefulAxiom(A axiom) {
		delegate_.usefulAxiom(axiom);
	}

	@Override
	public void computationComplete() {
		delegate_.computationComplete();
	}

}
