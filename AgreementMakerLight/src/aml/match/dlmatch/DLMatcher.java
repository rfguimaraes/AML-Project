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
 * @date 25-08-2014                                                            *
 * @version 0.27                                                               *
 ******************************************************************************/

package aml.match.dlmatch;

import aml.AML;
import aml.match.Alignment;
import aml.match.Mapping;
import aml.match.SecondaryMatcher;
import aml.ontology.RelationshipMap;
import aml.settings.MappingRelation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
        Alignment b = new Alignment(a);
        Iterator<Mapping> it = a.iterator();

        while (it.hasNext()) {
            Mapping c = it.next();
            Set<Integer> sourceIds, targetIds;
            sourceIds = relationshipMap.getSiblings(c.getSourceId());
            targetIds = relationshipMap.getSiblings(c.getTargetId());
            Set<Integer> sourceParents, targetParents;

            //If all siblings in source have targets who are siblings.
            if (allSourceHaveTargets(a, sourceIds, targetIds)) {
                sourceParents = relationshipMap.getParents(c.getSourceId());
                targetParents = relationshipMap.getParents(c.getTargetId());

                for (Integer sourceId : sourceParents) {
                    for (Integer targetId : targetParents) {
                        b.add(sourceId, targetId, thresh,
                                MappingRelation.SUPERCLASS);
                    }
                }
            }
        }
        return b;
    }

    private boolean allSourceHaveTargets(Alignment a, Set<Integer> sourceIds,
                                         Set<Integer> targetIds) {
        for (Integer sourceId : sourceIds) {
            if (!existsSourceMapping(a, sourceId, targetIds)) {
                return false;
            }
        }
        return true;
    }

    private boolean existsSourceMapping(Alignment a, int sourceId,
                                        Set<Integer> targetIds) {
        for (Integer targetId : targetIds) {
            if (a.containsMapping(sourceId, targetId)) {
                return true;
            }
        }
        return false;
    }
}