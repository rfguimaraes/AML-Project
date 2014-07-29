/******************************************************************************
 * Copyright 2013-2013 LASIGE                                                  *
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
 * Helper class which defines the translations   							   *
 *                                                                             *
 * @authors Ricardo F. Guimarães                                               *
 * @date 28-07-2014                                                            *
 * @version 1.0                                                                *
 ******************************************************************************/

package aml.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.xpath.operations.String;

public class DictionaryWord {
    private String langCode;
    private String writtenForm;
    private HashMap<String, List<DictionaryWord>> translations;

    public DictionaryWord(String langCode, String writtenForm) {
        setLangCode(langCode);
        setWrittenForm(writtenForm);
        this.translations = new HashMap<String, List<DictionaryWord>>();
    }

    public void addTranslation(DictionaryWord transl) {
        String langCode = transl.getLangCode();
        if (!this.translations.containsKey(langCode)) {
            this.translations.put(langCode, new ArrayList<DictionaryWord>());
        }
        List<DictionaryWord> list = this.translations.get(langCode);
        list.add(transl);
    }

    public String getLangCode() {
        return langCode;
    }

    public void setLangCode(String langCode) {
        this.langCode = langCode;
    }

    public String getWrittenForm() {
        return writtenForm;
    }

    public void setWrittenForm(String writtenForm) {
        this.writtenForm = writtenForm;
    }

    public List<DictionaryWord> getTranslations() {
        List<DictionaryWord> allTranslations = new ArrayList<DictionaryWord>();
        for (String langCode : translations.keySet()) {
            allTranslations.addAll(translations.get(langCode));
        }
        return allTranslations;
    }

    public List<DictionaryWord> getTranslations(List<String> langCodes) {
        List<DictionaryWord> desiredTranslations = new ArrayList<DictionaryWord>();
        for (String langCode : langCodes) {
            desiredTranslations.addAll(translations.get(langCode));
        }
        return desiredTranslations;
    }

    public List<DictionaryWord> getTranslations(String langCode) {
        return translations.get(langCode);
    }

}
