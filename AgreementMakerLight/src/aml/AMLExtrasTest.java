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
 * Test class for the Extras4AML matchers.                                     *
 *                                                                             *
 * @authors Ricardo F. Guimarães                                               *
 * @date 25-08-2014                                                            *
 * @version 1.02                                                               *
 ******************************************************************************/

package aml;

import aml.settings.MatchingAlgorithm;

public class AMLExtrasTest {
    public static void main(String[] args) {
        //Path to input ontology files (edit manually)
        String sourcePath = "store/oaei_tests/101/onto" +
                ".rdf";
        String targetPath = "store/oaei_tests/302/onto" +
                ".rdf";
        //Path to reference alignment (edit manually, or leave blank for no
        // evaluation)
        String referencePath =
                "store/oaei_tests/302/refalign.rdf";
        //Path to save output alignment (edit manually,
        // or leave blank for no evaluation)
        String outputPath = "store/oaei_tests/302/myres.rdf";

        AML aml = AML.getInstance();
        aml.openOntologies(sourcePath, targetPath);

        //Set the matching algorithm
        aml.setMatcher(MatchingAlgorithm.EXTRAS4AML);

        aml.match();
        try {
            if (!referencePath.equals("")) {
                aml.openReferenceAlignment(referencePath);
                aml.evaluate();
                System.out.println(aml.getEvaluation());
            }
            if (!outputPath.equals(""))
                aml.saveAlignmentRDF(outputPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
