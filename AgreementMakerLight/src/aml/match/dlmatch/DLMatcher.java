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
 * Actually using the RelationshipMap to produce the same effect.              *
 * @author Ricardo F. Guimarães                                                *
 * @date 26-08-2014                                                            *
 * @version 0.4                                                                *
 ******************************************************************************/

package aml.match.dlmatch;

import aml.AML;
import aml.match.Alignment;
import aml.match.Mapping;
import aml.match.SecondaryMatcher;
import aml.ontology.RelationshipMap;
import aml.settings.MappingRelation;
import org.semanticweb.elk.util.collections.Pair;

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
        Alignment b = new Alignment(a);
        Iterator<Mapping> it = a.iterator();

        while (it.hasNext()) {
            Mapping c = it.next();
            Set<Integer> sourceIds, targetIds;
            sourceIds = relationshipMap.getSiblings(c.getSourceId());
            targetIds = relationshipMap.getSiblings(c.getTargetId());
            Set<Integer> sourceParents, targetParents;
            boolean sourceImpliesTarget, targetImpliesSource;
            MappingRelation parentsRelation;

            targetImpliesSource = allSourcesHaveTargets(a, sourceIds,
                    targetIds);
            sourceImpliesTarget = allTargetsHaveSources(a, sourceIds,
                    targetIds);

            //If all siblings in source have targets who are siblings.

            if(targetImpliesSource && !sourceImpliesTarget) {
                parentsRelation = MappingRelation.SUPERCLASS;
                System.out.println("Super");
            }
            else if(targetImpliesSource && sourceImpliesTarget) {
                parentsRelation = MappingRelation.EQUIVALENCE;
                System.out.println("Equiv");
            }
            else if(!targetImpliesSource && sourceImpliesTarget) {
                parentsRelation = MappingRelation.SUBCLASS;
                System.out.println("Sub");
            }
            else {
                parentsRelation = MappingRelation.UNKNOWN;
                System.out.println("Unk");
            }
            Pair <Integer, Double> res;
            res = mappingsSourceTarget(a, sourceIds, targetIds);
            if (res.getFirst() >= 2*sourceIds.size() / 3 && res.getSecond() >
                    0.5) {
                parentsRelation = MappingRelation.SUPERCLASS;
            }

            if (parentsRelation != MappingRelation.UNKNOWN) {
                sourceParents = relationshipMap.getAncestors(c.getSourceId(),
                        1);
                targetParents = relationshipMap.getAncestors(c.getSourceId(),
                        1);

                for (Integer sourceId : sourceParents) {
                    for (Integer targetId : targetParents) {
                        b.add(sourceId, targetId, thresh,
                                parentsRelation);
                    }
                }
            }
        }
        return b;
    }

    private boolean allSourcesHaveTargets(Alignment a, Set<Integer> sourceIds,
                                         Set<Integer> targetIds) {
        for (Integer sourceId : sourceIds) {
            if (!existsSourceMapping(a, sourceId, targetIds)) {
                return false;
            }
        }
        return true;
    }

    private Pair<Integer, Double> mappingsSourceTarget(Alignment a,
                                             Set<Integer> sourceIds,
                                          Set<Integer> targetIds) {
        int mp = 0;
        double sim = 0;
        for (Integer sourceId : sourceIds) {
            for (Integer targetId : targetIds) {
                double t = a.getSimilarity(sourceId, targetId);
                if (t > 0.0) {
                    mp++;
                    sim += t;
                }
            }
        }
        return new Pair<Integer, Double>(mp, sim/mp);
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

    private boolean allTargetsHaveSources(Alignment a, Set<Integer> sourceIds,
                                          Set<Integer> targetIds) {
        for (Integer targetId : targetIds) {
            if (!existsTargetMapping(a, targetId, sourceIds)) {
                return false;
            }
        }
        return true;
    }

    private boolean existsTargetMapping(Alignment a, int targetId,
                                        Set<Integer> sourceIds) {
        for (Integer sourceId : sourceIds) {
            if (a.containsMapping(sourceId, targetId)) {
                return true;
            }
        }
        return false;
    }
}
