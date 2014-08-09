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
 * Dictionary of translations obtain from the English Wiktionary, using        *
 * wik2dict                                                                    *
 *                                                                             *
 * @authors Ricardo F. Guimarães                                               *
 * @date 30-07-2014                                                            *
 * @version 0.41                                                            *
 ******************************************************************************/

package aml;

import aml.match.WikDictMatcher;

public class AMLExtrasTest {
    public static void main(String[] args) {
        //Path to input ontology files (edit manually)
        String sourcePath = "store/anatomy/mouse.owl";
        String targetPath = "store/anatomy/human.owl";


        AML aml = AML.getInstance();
        aml.openOntologies(sourcePath, targetPath);
        WikDictMatcher wikmatch = new WikDictMatcher();
    }
}
