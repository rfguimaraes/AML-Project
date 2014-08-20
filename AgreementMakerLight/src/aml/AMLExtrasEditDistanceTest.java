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
 * Calculates the similarity of two strings using the level 2 Jaro Wrinkler    *
 * as proposed in http://secondstring.sourceforge.net/doc/iiweb03.pdf          *
 *                                                                             *
 * @author Ricardo F. Guimarães                                                *
 * @date 20-08-2014                                                            *
 * @version 0.1                                                                *
 ******************************************************************************/

package aml;

import aml.util.Level2JaroWinkler;
import uk.ac.shef.wit.simmetrics.similaritymetrics.JaroWinkler;

/**
 * Created by ricardo on 20/08/14.
 */
public class AMLExtrasEditDistanceTest {

    public static void main(String[] args) {
        String first = "afunnyhouse";
        String second = "a funny house";
        JaroWinkler original = new JaroWinkler();

        System.out.println(Level2JaroWinkler.stringSimilarity(first,
                second));
        System.out.println(original.getSimilarity(first, second));
    }

}
