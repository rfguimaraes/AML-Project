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
 * @date 29-08-2014                                                            *
 * @version 0.8                                                                *
 ******************************************************************************/

package aml.extras4aml;

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
public class DescendantAnalyserMatcher implements SecondaryMatcher {

    private AML aml;
    private RelationshipMap relationshipMap;

    public DescendantAnalyserMatcher() {
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
            MappingRelation parentsRelation;
            int nMaps = 0;
            double simMaps = 0.0;
            double conf = 0.0;


            parentsRelation = hardEvaluation(a, sourceIds, targetIds);

            Pair<Integer, Double> res;
            res = mappingsSourceTarget(a, sourceIds, targetIds);
            simMaps = res.getSecond();
            nMaps = res.getFirst();


            sourceParents = relationshipMap.getAncestors(c.getSourceId(),
                    1, -1);
            targetParents = relationshipMap.getAncestors(c.getTargetId(),
                    1, -1);

            for (Integer sourceId : sourceParents) {
                for (Integer targetId : targetParents) {

                    if (parentsRelation == MappingRelation.UNKNOWN) {
                        parentsRelation = softEvaluation(sourceId, targetId,
                                nMaps);
                    }
                    conf = simMaps;

                    if (parentsRelation != MappingRelation.UNKNOWN) {
                        b.add(sourceId, targetId, conf, parentsRelation);
                    }
                }
            }
        }

        return b;
    }

    private MappingRelation hardEvaluation(Alignment a, Set<Integer> sourceIds,
                                           Set<Integer> targetIds) {
        boolean targetImpliesSource = allSourcesHaveTargets(a, sourceIds,
                targetIds);
        boolean sourceImpliesTarget = allTargetsHaveSources(a, sourceIds,
                targetIds);

        if (targetImpliesSource && !sourceImpliesTarget) {
            return MappingRelation.SUPERCLASS;
        } else if (targetImpliesSource && sourceImpliesTarget) {
            return MappingRelation.EQUIVALENCE;
        } else if (!targetImpliesSource && sourceImpliesTarget) {
            return MappingRelation.SUBCLASS;
        } else {
            return MappingRelation.UNKNOWN;
        }
    }

    private MappingRelation softEvaluation(int sourceId, int targetId,
                                           int nMaps) {
        int sChildren = relationshipMap.getDescendants
                (sourceId, 1, -1).size();
        int tChildren = relationshipMap.getDescendants
                (targetId, 1, -1).size();

        if (isNear(sChildren, tChildren,
                3) && isNear(sChildren, nMaps,
                sChildren / 3) && isNear(tChildren, nMaps,
                tChildren / 3)) {
            return MappingRelation.EQUIVALENCE;
        } else if (isNear(nMaps, sChildren,
                sChildren / 2) && tChildren > sChildren) {
            return MappingRelation.SUPERCLASS;
        } else if (isNear(nMaps, tChildren,
                sChildren / 2) && sChildren > tChildren) {
            return MappingRelation.SUBCLASS;
        } else {
            return MappingRelation.UNKNOWN;
        }
    }

    private boolean isNear(int a, int b, double thresh) {
        if (b <= a + thresh && b >= a - thresh)
            return true;
        else if (a <= b + thresh && a >= b - thresh)
            return true;
        else
            return false;
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
        return new Pair<Integer, Double>(mp, sim / mp);
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
