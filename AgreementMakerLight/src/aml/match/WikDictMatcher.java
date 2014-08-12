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
 * Matches ontologies by finding literal full-name matches between their       *
 * Lexicons after extension with the data obtained from the English Wiktionary,*
 * using wikt2dict.                                                            *
 * @author Ricardo F. Guimarães                                                *
 * @date 06-08-2014                                                            *
 * @version 0.7                                                                *
 ******************************************************************************/

package aml.match;

import aml.AML;
import aml.ontology.Lexicon;
import aml.util.Dictionary;
import aml.util.DictionaryWord;
import aml.util.ISub;
import aml.util.StringParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by rickfg on 8/5/14.
 */
public class WikDictMatcher implements PrimaryMatcher, LexiconExtender {

    private final String PATH = "store/knowledge/dictionary/";
    // The type of lexical entry generated by this LexiconExtender
    private final String TYPE = "externalMatch";
    // The source of this LexiconExtender
    private final String SOURCE = "English Wiktionary (wik2dict)";
    // The confidence score of the English Wiktionary
    private final double CONFIDENCE = 0.7;
    //The dictionary
    private Dictionary wikt;
    //Links to the Lexicons
    private Lexicon sourceLex;
    private Lexicon targetLex;

    public WikDictMatcher() {

        // Setup the wik2dict output file location
        long time = System.currentTimeMillis() / 1000;
        wikt = new Dictionary(PATH, "English.txt");
        time = System.currentTimeMillis() / 1000 - time;
        System.out.println("Dictionary loaded in " + time + " seconds");
        AML aml = AML.getInstance();
        sourceLex = aml.getSource().getLexicon();
        targetLex = aml.getTarget().getLexicon();
    }

    @Override
    public Alignment match(double thresh) {
        AML aml = AML.getInstance();
        Lexicon ext1 = getExtendedLexicon(aml.getSource().getLexicon(), thresh);
        Lexicon ext2 = getExtendedLexicon(aml.getTarget().getLexicon(), thresh);
        return match(ext1, ext2, thresh);
    }

    @Override
    public void extendLexicons(double thresh) {
        AML aml = AML.getInstance();
        extendLexicon(aml.getSource().getLexicon(), thresh);
        extendLexicon(aml.getTarget().getLexicon(), thresh);
    }

    private void extendLexicon(Lexicon source, double thresh) {
        source = extendFrom(source, thresh);
    }

    private Lexicon getExtendedLexicon(Lexicon source, double thresh) {
        return extendFrom(source, thresh);
    }

    private Lexicon extendFrom(Lexicon source, double thresh) {
        Lexicon extended = new Lexicon(source);

        //Iterate through the original Lexicon names
        for (String s : source.getNames()) {
            //We don't match formulas to Wiktionary
            if (StringParser.isFormula(s))
                continue;

            List<DictionaryWord> translations = new ArrayList<DictionaryWord>();

            for (String srcLang : source.getLanguages(s)) {
                translations.addAll(wikt.getAllTranslations(srcLang, s));
            }

            if (translations.isEmpty())
                continue;

            double conf = CONFIDENCE - 0.01 * translations.size();
            if (conf < thresh)
                continue;

            Set<Integer> terms = source.getInternalClasses(s);
            //Add each term with the translation to the extension Lexicon
            for (Integer i : terms) {
                double weight = conf * source.getWeight(s, i);
                if (weight < thresh)
                    continue;
                for (DictionaryWord w : translations) {
                    extended.add(i, w.getWrittenForm(), w.getLangCode(),
                            TYPE, SOURCE, weight);
                }
            }
        }
        return extended;
    }

    private Alignment match(Lexicon source, Lexicon target, double thresh) {
        Alignment maps = new Alignment();
        Set<Integer> sources = source.getClasses();
        Set<Integer> targets = target.getClasses();
        for (Integer i : sources) {
            for (Integer j : targets) {
                double sim = mapTwoTerms(i, j);
                if (sim >= thresh)
                    maps.add(i, j, sim);
            }
        }
        return maps;
    }

    private double mapTwoTerms(int sourceId, int targetId) {
        double maxSim = 0.0;
        double sim, weight;

        Set<String> sourceNames = sourceLex.getNames();

        if (sourceNames == null || targetLex.getNames() == null)
            return 0.0;
        for (String s : sourceNames) {
            weight = sourceLex.getCorrectedWeight(s, sourceId);
            for (String lang : sourceLex.getLanguages(s, sourceId)) {
                Set<String> targetNames = targetLex.getNamesWithLanguage
                        (targetId, lang);
                if (targetNames.isEmpty())
                    continue;
                for (String t : targetNames) {
                    sim = weight * targetLex.getCorrectedWeight(t, targetId);
                    sim *= ISub.stringSimilarity(s, t);
                    if (sim > maxSim)
                        maxSim = sim;
                }
            }
        }
        return maxSim;
    }
}