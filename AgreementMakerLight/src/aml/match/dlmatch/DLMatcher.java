/******************************************************************************
 * Copyright 2014-2014 Ricardo F. Guimarães                                    *
 *                                                                             *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may     *
 * not use this file except in compliance with the License. You may obtain a   *
 * copy of the License at http://www.apache.org/licenses/LICENSE-2.0           *
 *                                                                             *
 * Unless required by applicable law or agreed to in writing, software         *
 * distributed under the License is distributed on an "AS IS" BASIS,           *
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.    *
 * See the License for the specific language governing permissions and         *
 * limitations under the License.                                              *
 *                                                                             *
 *******************************************************************************
 * Matches ontologies using a reasoner to find semantic correspondences        *
 * between entities. Largely based in the algorithms presented in:             *
 * http://doi.ieeecomputersociety.org/10.1109/IITA.2007.95                     *
 *                                                                             *
 * @author Ricardo F. Guimarães                                                *
 * @date 23-08-2014                                                            *
 * @version 0.12                                                               *
 ******************************************************************************/

package aml.match.dlmatch;

import aml.AML;
import aml.match.Alignment;
import aml.match.Mapping;
import aml.match.SecondaryMatcher;
import aml.ontology.RelationshipMap;

import java.util.Iterator;
import java.util.Set;

/**
 * Created by rickfg on 8/22/14.
 */
public class DLMatcher implements SecondaryMatcher {

    private AML aml;
    private RelationshipMap relationshipMap;

    public DLMatcher() {
        aml = AML.getInstance();
        relationshipMap = aml.getRelationshipMap();
    }

    @Override
    public Alignment extendAlignment(Alignment a, double thresh) {
        Iterator<Mapping> it = a.iterator();

        while (it.hasNext()) {
            Mapping c = it.next();
            Set<Integer> ancestors;
            ancestors = relationshipMap.getAncestors(c.getSourceId(), 1);

            for (Integer ancestor : ancestors) {
                a.add(ancestor, c.getTargetId(), 0.5,
                        AML.MappingRelation.SUPERCLASS);
            }

        }
        return a;
    }
}
