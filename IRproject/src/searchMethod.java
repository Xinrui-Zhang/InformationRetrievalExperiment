/**
 * @创建人 柳靖洋
 * @创建时间 2021/5/10
 * @描述
 */
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.*;
public class searchMethod {

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

    private TreeMap<String, Integer> AND(TreeMap<String, Integer> result, TreeMap<String, Integer> next) {
        TreeMap<String, Integer> docId = new TreeMap<String, Integer>();
        Iterator i = result.entrySet().iterator();
        Iterator j = next.entrySet().iterator();
        while (i.hasNext() && j.hasNext()) {
            Map.Entry rentry = ( Map.Entry)i.next();
            Map.Entry nentry = (Map.Entry)j.next();
            String resultID=(String)rentry.getKey();
            String nextID=(String)nentry.getKey();
            int val = resultID.compareTo(nextID);
            switch (val) {
                case 0: //相等
                    docId.put((String)nentry.getKey(),(Integer)nentry.getValue());
                    i.next();
                    j.next();
                    break;
                case -1: //result[i] < next[j]
                    i.next();
                    break;
                case 1: //result[i] > next[j]
                    j.next();
                    break;
            }
        }
        return docId;
    }

    private TreeMap<String, Integer> ANDNOT(TreeMap<String, Integer> result, TreeMap<String, Integer> next) {

        TreeMap<String, Integer> docId = new TreeMap<String, Integer>();
        Iterator i = result.entrySet().iterator();
        Iterator j = next.entrySet().iterator();
        while (i.hasNext() && j.hasNext()) {
            Map.Entry rentry = (Map.Entry)i.next();
            Map.Entry nentry = (Map.Entry)j.next();
            String resultID=(String)rentry.getKey();
            String nextID=(String)nentry.getKey();
            int val = resultID.compareTo(nextID);
            switch (val) {
                case 0: //相等
                    i.next();
                    j.next();
                    break;
                case -1: //result[i] < next[j]
                case 1: //result[i] > next[j]
                    docId.put((String)rentry.getKey(),(Integer)rentry.getValue());
                    docId.put((String)nentry.getKey(),(Integer)nentry.getValue());
                    i.next();
                    j.next();
                    break;
            }
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
        Iterator i = p1.entrySet().iterator();
        Iterator j = p2.entrySet().iterator();
        while (i.hasNext() && j.hasNext()) {
            Map.Entry rentry = (Map.Entry)i.next();
            Map.Entry nentry = (Map.Entry)j.next();
            String p1ID=(String)rentry.getKey();
            String p2ID=(String)nentry.getKey();
            int val = p1ID.compareTo(p2ID);
            switch (val) {
                //相等
                case 0 :{
                    docId.put((String) nentry.getKey(), (Integer) nentry.getValue());
                    i.next();
                    j.next();
                }
                //result[i] < next[j]
                case -1 :{
                    docId.put((String) rentry.getKey(), (Integer) rentry.getValue());
                    i.next();
                }
                //result[i] > next[j]
                case 1 :{
                    docId.put((String) nentry.getKey(), (Integer) nentry.getValue());
                    j.next();
                }
            }
        }
        return docId;
    }

};
