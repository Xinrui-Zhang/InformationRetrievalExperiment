/**
 * @创建人 柳靖洋
 * @创建时间 2021/5/10
 * @描述
 */
import java.text.CollationKey;
import java.text.Collator;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.*;
public class searchMethod {
    private Poet poet;
    public TreeMap<String, Integer> query(String q, TreeMap<Character, Term> terms){
        TreeMap<String, Integer> result = new TreeMap<>();
        for(int i = 0; i < q.length(); i++){
            switch (q.charAt(i)){
                case '+':
                    if(terms.containsKey(q.charAt(i+1)))
                        result = OR(result,terms.get(q.charAt(i+1)).getDoc());
                    i++;
                    break;
                case '*':
                    if(terms.containsKey(q.charAt(i+1)))
                        result = AND(result,terms.get(q.charAt(i+1)).getDoc());
                    i++;
                    break;
                case '-':
                    if(terms.containsKey(q.charAt(i+1)))
                        result = ANDNOT(result,terms.get(q.charAt(i+1)).getDoc());
                    i++;
                    break;
                default:
                    if(terms.containsKey(q.charAt(i)))
                        result = terms.get(q.charAt(i)).getDoc();
                    break;

            }
        }
        
        return result;
    }


    private TreeMap<String, Integer> AND(TreeMap<String, Integer> p1, TreeMap<String, Integer> p2) {
        TreeMap<String, Integer> docId = new TreeMap<String, Integer>();
        int i = 0, j = 0;
        String p1ID="",p2ID="";
        while (i<p1.keySet().size() && j<p2.keySet().size()){
            p1ID=(String)p1.keySet().toArray()[i];
            p2ID=(String)p2.keySet().toArray()[j];
            int val = p1ID.compareTo(p2ID);
            //相等
            if (val == 0) {
                docId.put((String) p1ID, (Integer) p1.values().toArray()[i]);
                i++;
                j++;
            } else if (val < 0) {
                i++;

            } else if (val > 0) {
                j++;
            }
        }
        return docId;
    }

    private TreeMap<String, Integer> ANDNOT(TreeMap<String, Integer> p1, TreeMap<String, Integer> p2) {

        TreeMap<String, Integer> docId = new TreeMap<String, Integer>();
        int i = 0, j = 0;
        String p1ID="",p2ID="";
        while (i<p1.keySet().size() && j<p2.keySet().size()){
            p1ID=(String)p1.keySet().toArray()[i];
            p2ID=(String)p2.keySet().toArray()[j];
            int val = p1ID.compareTo(p2ID);
            //相等
            if (val == 0) {
                i++;
                j++;
            } else if (val < 0) {
                docId.put((String) p1ID, (Integer) p1.values().toArray()[i]);
                i++;
            } else if (val > 0) {
                j++;
            }
        }
        while(i<p1.keySet().size()){
            p1ID=(String)p1.keySet().toArray()[i];
            docId.put((String) p1ID, (Integer) p1.values().toArray()[i]);
            i++;
        }

        return docId;
    }

    private TreeMap<String, Integer> AndAll( TreeMap<String, Integer> result,ArrayList<TreeMap<String, Integer>> next)
    {

       next.sort(new Comparator<TreeMap<String, Integer>>() {
           @Override
           public int compare(TreeMap<String, Integer> o1, TreeMap<String, Integer> o2) {
               int dif=o1.size()-o2.size();
               if(dif>0){
                   return 1;
               }else if(dif<0){
                   return -1;
               }
               return 0;
           }
       });


        int i=0;
        while(i<next.size())
        {
            result=AND(result,next.get(i));
        }
        return result;
    }
    
    private TreeMap<String, Integer> OR(TreeMap<String, Integer> p1, TreeMap<String, Integer> p2){
        TreeMap<String, Integer> docId = new TreeMap<String, Integer>();
        int i = 0, j = 0;
        String p1ID="",p2ID="";
        while (i<p1.keySet().size() && j<p2.keySet().size()){
            p1ID=(String)p1.keySet().toArray()[i];
            p2ID=(String)p2.keySet().toArray()[j];
            int val = p1ID.compareTo(p2ID);
            //相等
            if (val == 0) {
                docId.put((String) p1ID, (Integer) p1.values().toArray()[i]);
                i++;
                j++;
            } else if (val < 0) {
                docId.put((String) p1ID, (Integer) p1.values().toArray()[i]);
                i++;

            } else if (val > 0) {
                docId.put((String) p2ID, (Integer) p2.values().toArray()[j]);
                j++;
            }
        }

        while(i<p1.keySet().size()){
            p1ID=(String)p1.keySet().toArray()[i];
            docId.put((String) p1ID, (Integer) p1.values().toArray()[i]);
            i++;
        }
        while(j<p2.keySet().size()){
            p2ID=(String)p2.keySet().toArray()[j];
            docId.put((String) p2ID, (Integer) p2.values().toArray()[j]);
            j++;
        }
        return docId;
    }

    private TreeMap<String, Integer> OrAll( TreeMap<String, Integer> result,ArrayList<TreeMap<String, Integer>> next)
    {

        next.sort(new Comparator<TreeMap<String, Integer>>() {
            @Override
            public int compare(TreeMap<String, Integer> o1, TreeMap<String, Integer> o2) {
                int dif=o1.size()-o2.size();
                if(dif<0){
                    return 1;
                }else if(dif>0){
                    return -1;
                }
                return 0;
            }
        });


        int i=0;
        while(i<next.size())
        {
            result=OR(result,next.get(i));
        }
        return result;
    }

};
