package aml;

import java.util.List;

import aml.util.Dictionary;
import aml.util.DictionaryWord;

public class AMLExtrasTest {
    public static void main(String[] args) {
        long time = System.currentTimeMillis() / 1000;
        Dictionary d = new Dictionary();
        time = System.currentTimeMillis() / 1000 - time;
        System.out.println("Dict" + " loaded in " + time + " seconds");
        System.out.println(d.translate("en", "dog", "pt"));
        System.out.println(d.translate("pt", "cachorro", "en"));
        System.out.println(d.translate("en", "dog", "es"));
        System.out.println(d.translate("es", "perro", "pt"));
        System.out.println(d.translate("pt", "saudade", "en"));
        System.out.println(d.translate("pt", "saudade", "ko"));
        System.out.println(d.translate("pt", "saudade", "jp"));
        System.out.println(d.getAllTranslations("en", "dog"));
        System.out.println();

    }
}
