package streams.collection;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static streams.collection.ToCollection.show;

/**
 * 使用 Collectors.toMap() 的第三个和第四个参数
 */
public class ToMapHW {
    // 1. 获取展示语言名称和代号
    static void getLangSet(){
        Stream<Locale> localeStream = Stream.of(Locale.getAvailableLocales());
        Map<String, String> langNames = localeStream.collect(
                Collectors.toMap(Locale::getDisplayLanguage,
                        loc -> loc.getDisplayName(loc),
                        (existVal, newVal)-> existVal
                )
        );
        System.out.println(langNames);
    }
    // 2. 获取每个个国家使用的所有语言
    static void getCountryLangSet(){
        Stream<Locale> localeStream = Stream.of(Locale.getAvailableLocales());
        Map<String, Set<String>> countryLanguageSet = localeStream.collect(
                Collectors.toMap(
                        Locale::getDisplayCountry,
                        loc -> Collections.singleton(loc.getDisplayLanguage()),
                        (a, b)->{
                            Set<String> union = new HashSet<>(a);
                            union.addAll(b);
                            return union;
                        },
                        TreeMap::new
                )
        );
        System.out.println(countryLanguageSet.get("美国"));
    }
    public static void main(String[] args) {
        getLangSet();
    }
}
