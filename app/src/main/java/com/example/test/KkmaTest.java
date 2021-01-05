package com.example.test;

import org.snu.ids.kkma.constants.POSTag;
import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.snu.ids.kkma.ma.MExpression;
import org.snu.ids.kkma.ma.MorphemeAnalyzer;
import org.snu.ids.kkma.ma.Sentence;

import java.util.List;


public class KkmaTest {
    public static void main(String[] args){
        // maTest();
        // extractTest();
        String text = "매일두통에시달리다";
        System.out.println(posTag(text));
    }

    public static void maTest()
    {
        String string = "꼬꼬마형태소분석기를테스트할것입니다.";
        try {
            MorphemeAnalyzer ma = new MorphemeAnalyzer();
            ma.createLogger(null);
            List<MExpression> ret = ma.analyze(string);
            ret = ma.postProcess(ret);
            ret = ma.leaveJustBest(ret);
            List<Sentence> stl = ma.divideToSentences(ret);
            for( int i = 0; i < stl.size(); i++ ) {
                Sentence st = stl.get(i);
                System.out.println("=============================================  " + st.getSentence());
                for( int j = 0; j < st.size(); j++ ) {
                    System.out.println(st.get(j));
                }
            }
            ma.closeLogger();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void extractTest()
    {
        String string = "매일두통에시달리다";
        KeywordExtractor ke = new KeywordExtractor();
        KeywordList kl = ke.extractKeyword(string, false);
        for( int i = 0; i < kl.size(); i++ ){
            Keyword kwrd = kl.get(i);
            if( kwrd.isTagOf(POSTag.NNG) ){
                System.out.println(kwrd.getString());
            }
            else if( kwrd.isTagOf(POSTag.VV) || kwrd.isTagOf(POSTag.VA) ) {
                System.out.println(kwrd.getString()+"다");
            }
        }
    }

    private static String posTag(String text) {
        String result = "";
        KeywordExtractor ke = new KeywordExtractor();
        KeywordList kl = ke.extractKeyword(text, false);
        for( int i = 0; i < kl.size(); i++ ){
            Keyword kwrd = kl.get(i);
            if( kwrd.isTagOf(POSTag.NNG) ){
                result += kwrd.getString() + " ";
            }
            else if( kwrd.isTagOf(POSTag.VV) || kwrd.isTagOf(POSTag.VA) ) {
                result += kwrd.getString() + "다 ";
            }
        }
        return result;
    }

}



