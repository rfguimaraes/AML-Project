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
 * @date 07-10-2014                                                            *
 * @version 2.0e                                                               *
 ******************************************************************************/

package aml;

import aml.AML;
import aml.settings.MatchingAlgorithm;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;


public class AMLExtrasTest {

    public static void showFiles(File[] files) {
        for (File file : files) {
            if (file.isDirectory()){
                showFiles(file.listFiles());
            } else if (file.getName().endsWith("onto.rdf")) {
                eval(file.getParent());
            }
        }
    }

    public static void eval(String path) {

        String sourcePath = "store/oaei_tests/benchmarks/101/onto.rdf";
        String targetPath = path + "/onto.rdf";
        String referencePath = path + "/refalign.rdf";
        String outputPath = "";
        AML aml = AML.getInstance();
        aml.openOntologies(sourcePath, targetPath);
        System.out.println("e4a " + path);
        //Set the matching algorithm

        for (MatchingAlgorithm m : MatchingAlgorithm.values()){
            System.out.println("e4a " + path + " " + m.toString());
            evaluate(m, referencePath, outputPath);
        }
    }

    public static void main(String[] args)
    {
//        //Path to input ontology files (edit manually)
//        String sourcePath = "store/oaei_tests/101/onto.rdf";
//        String targetPath = "/home/bcc/rickfg/Downloads/oaei_tests/302/onto" +
//                ".rdf";
//        //Path to reference alignment (edit manually, or leave blank for no
//        // evaluation)
//        String referencePath =
//                "/home/bcc/rickfg/Downloads/oaei_tests/302/refalign.rdf";
//        //Path to save output alignment (edit manually,
//        // or leave blank for no evaluation)
//        String outputPath = "store/oaei_tests/302/myres.rdf";
//
//        AML aml = AML.getInstance();
//        aml.openOntologies(sourcePath, targetPath);
//
//        //Set the matching algorithm
//
//        for (MatchingAlgorithm m : MatchingAlgorithm.values()) {
//            evaluate(m, referencePath, outputPath);
//        }
        showFiles(new File("store/oaei_tests/benchmarks").listFiles());
    }

    private static void evaluate(MatchingAlgorithm m, String referencePath,
                                 String outputPath)
    {
        AML aml = AML.getInstance();
        aml.setMatcher(m);

        aml.match();
        try {
            if (!referencePath.equals("")) {
                aml.openReferenceAlignment(referencePath);
                aml.evaluate();
                System.out.println("e4a " + aml.getEvaluation());
            }
            if (!outputPath.equals(""))
                aml.saveAlignmentRDF(outputPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
