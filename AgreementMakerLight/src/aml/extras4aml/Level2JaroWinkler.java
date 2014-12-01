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
 * @date 30-11-2014                                                            *
 * @version 2.2e                                                               *
 ******************************************************************************/

package aml.extras4aml;
import java.lang.String;
import uk.ac.shef.wit.simmetrics.tokenisers.TokeniserWhitespace;
import uk.ac.shef.wit.simmetrics.similaritymetrics.JaroWinkler;

import java.util.ArrayList;

/**
 * Created by ricardo on 20/08/14.
 */
public class Level2JaroWinkler {

    /**
     * Computes the similarity between two Strings
     * @param st1: the first string to compare
     * @param st2: the second string to compare
     * @return the ISub similarity between st1 and st2
     */
    public static double stringSimilarity(String st1, String st2) {
        TokeniserWhitespace tk = new TokeniserWhitespace();

        return stringSimilarity(tk.tokenizeToArrayList(st1),
                tk.tokenizeToArrayList(st2));
    }

    private static double stringSimilarity(ArrayList<String> strings1,
                                         ArrayList<String> strings2) {
        double sum = 0;
        JaroWinkler baseScore = new JaroWinkler();

        for (String token1 : strings1) {
            double max = baseScore.getSimilarity(token1, strings2.get(0));
            for (String token2 : strings2) {
                double score = baseScore.getSimilarity(token1, token2);
                max = Math.max(max, score);
            }
            sum += max;
        }
        return sum/strings1.size();
    }
}
