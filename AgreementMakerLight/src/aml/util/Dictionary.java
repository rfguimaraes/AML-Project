/*******************************************************************************
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
 * wikt2dict                                                                   *
 *                                                                             *
 * @authors Ricardo F. Guimarães                                               *
 * @date 22-10-2014                                                            *
 * @version 1.1                                                                *
 ******************************************************************************/

package aml.util;

import org.semanticweb.elk.util.collections.Pair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Dictionary {

    // The dictionary produced by wik2dict
    HashMap<Pair<String, String>, DictionaryWord> data;

    public Dictionary(String dictionariesPath, String filename) {
        // Setup the wik2dict output file location
        System.setProperty("dictionary.dir", dictionariesPath);
        String path = new File(dictionariesPath).getAbsolutePath();
        data = new HashMap<Pair<String, String>, DictionaryWord>();
        FileReader file = null;
        try {
            file = new FileReader(path + "/" + filename);
            BufferedReader reader = new BufferedReader(file);
            String line = "";
            while ((line = reader.readLine()) != null) {
                String[] values = line.split("\\t", -1);
                DictionaryWord first = getWord(values[0], values[1]);
                DictionaryWord second = getWord(values[2], values[3]);
                add(first, second);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (file != null) {
                try {
                    file.close();
                } catch (IOException e) {
                    // TODO: handle this
                }
            }
        }
    }

    public Dictionary(String sourceLang, String targetLang, boolean diff) {
        // Setup the wik2dict output file location
        System.setProperty("dictionary.dir", "store/knowledge/dictionary/");
        String path = new File("store/knowledge/dictionary/").getAbsolutePath();
        data = new HashMap<Pair<String, String>, DictionaryWord>();
        FileReader file = null;
        if(sourceLang.compareTo(targetLang) > 0) {
            String temp = sourceLang;
            sourceLang = targetLang;
            targetLang = temp;
        }
        try {
            String filename = sourceLang + "_" + targetLang + ".wikt.txt";
            file = new FileReader(path + "/" + filename);
            BufferedReader reader = new BufferedReader(file);
            String line = "";
            while ((line = reader.readLine()) != null) {
                String[] values = line.split("\\t", -1);
                DictionaryWord first = getWord(sourceLang, values[0]);
                DictionaryWord second = getWord(targetLang, values[1]);
                add(first, second);
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Could not open file");
        } finally {
            if (file != null) {
                try {
                    file.close();
                } catch (IOException e) {
                    // TODO: handle this
                }
            }
        }
    }

    private DictionaryWord getWord(String srcLang, String writtenForm) {
        Pair<String, String> key;
        key = new Pair<String, String>(srcLang, writtenForm);
        if (!data.containsKey(key)) {
            return new DictionaryWord(srcLang, writtenForm);
        } else {
            return data.get(key);
        }
    }

    private void add(DictionaryWord firstWord, DictionaryWord secondWord) {
        Pair<String, String> firstPair, secondPair;
        firstPair = new Pair<String, String>(firstWord.getLangCode(),
                firstWord.getWrittenForm());
        secondPair = new Pair<String, String>(secondWord.getLangCode(),
                secondWord.getWrittenForm());
        firstWord.addTranslation(secondWord);
        secondWord.addTranslation(firstWord);

        if (!data.containsKey(firstPair)) {
            data.put(firstPair, firstWord);
        }
        if (!data.containsKey(secondPair)) {
            data.put(secondPair, secondWord);
        }
    }

    public List<DictionaryWord> translate(String srcLang, String writtenForm,
                                          String tgtLang) {
        Pair<String, String> key;
        key = new Pair<String, String>(srcLang, writtenForm);
        if (data.containsKey(key)) {
            return data.get(key).getTranslations(tgtLang);
        } else
            return new ArrayList<DictionaryWord>();
    }

    public List<DictionaryWord> getAllTranslations(String srcLang,
                                                   String writtenForm) {
        Pair<String, String> key = new Pair<String, String>(srcLang,
                writtenForm);
        if (!data.containsKey(key)) return new ArrayList<DictionaryWord>();
        return data.get(key).getTranslations();
    }
}
