/**
 * @创建人 柳靖洋
 * @创建时间 2021/5/10
 * @描述
 */
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.*;
import java.util.Map.Entry;
public class searchMethod {
    public TreeMap<String,TreeMap<Character,Double>> docVector=dataProcessor.getDictionarys();
    public TreeMap<String,TreeMap<Character,Double>> docVectorMax=dataProcessor.getDictionarysMax();

    public TreeMap<String, ArrayList<Double>> query(String q, TreeMap<Character, Term> terms){
        TreeMap<String, ArrayList<Double>> result = new TreeMap<>();
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

    public TreeMap<String, Double> MLEcal(String text, Double lambda){
        TreeMap<String,Double> result =new TreeMap<String,Double>();
        TreeMap<Character, Term> dictionary = dataProcessor.getPoetTerm();
        TreeMap<String, Poet> poets = dataProcessor.getPoets();
        for(int i = 0; i < text.length(); i++){
            Term t = dictionary.get(text.charAt(i));
            for(String d : t.getDoc().keySet()){
                Double value = 1.0;
                if (result.containsKey(d)){
                    value = result.get(d);
                }
                Poet p = poets.get(d);
                double temp = 0.0;
                ArrayList<Double> doc = t.getDoc().get(d);
                temp = lambda * (doc.get(0) / p.getWordNum()) + (1-lambda) * (t.getCollFreq() / 57538);
                value *= temp;
                result.put(d, value);
            }
        }
        return result;
    }

    public TreeMap<String,Double> CosinSimilarity(String qvector)
    {
        TreeMap<String,Double> result =new TreeMap<String,Double>();//返回一个文档和对应相关性的集合
        TreeMap<Character,Double> dvector;//文档向量
        Character query;
        int i = 0;
        while(i<qvector.length())
        {   int j=0;
            query=(Character)qvector.charAt(i);
            while(j<docVector.keySet().size())//全部文档向量的集合
            {   String Docid=(String)docVector.keySet().toArray()[j];
                dvector=(TreeMap<Character,Double>)docVector.get(Docid);
                if(dataProcessor.getPoetTerm().containsKey(query)){
                    Term t = dataProcessor.getPoetTerm().get(query);
                    double dw = dvector.get(query);
                    if(t.getDoc().containsKey(Docid)){
                        double tf = (double)t.getDoc().get(Docid).get(0);
                        double idf = Math.log(1000 / (double)(t.getDocNum()+1))/Math.log(10);
                        double temp = tf / qvector.length() * idf;
                        if (result.containsKey(Docid))//如果计算过这个文档
                        {
                            double oldwf = result.get(Docid);
                            result.replace(Docid, oldwf + dw*temp);
                        } else {
                            result.put(Docid, dw*temp);
                        }
                    }
                }
                j++;
            }
            i++;
        }
        return result;
    }

    public TreeMap<String,Double> CosinSimilarityMax(String qvector)
    {
        TreeMap<String,Double> result =new TreeMap<String,Double>();//返回一个文档和对应相关性的集合
        TreeMap<Character,Double> dvector;//文档向量
        Character query;
        int i = 0;
        while(i<qvector.length())
        {   int j=0;
            query=(Character)qvector.charAt(i);
            while(j<docVectorMax.keySet().size())//全部文档向量的集合
            {   String Docid=(String)docVectorMax.keySet().toArray()[j];
                dvector=(TreeMap<Character,Double>)docVectorMax.get(Docid);
                if(dataProcessor.getPoetTerm().containsKey(query)){
                    Term t = dataProcessor.getPoetTerm().get(query);
                    double dw = dvector.get(query);
                    if(t.getDoc().containsKey(Docid)){
                        double idf = Math.log(1000 / (double)(t.getDocNum()+1))/Math.log(10);
                        double temp = (0.5+0.5*t.getDoc().get(Docid).get(0)/t.getDoc().get(Docid).get(3)) * idf;
                        if (result.containsKey(Docid))//如果计算过这个文档
                        {
                            double oldwf = result.get(Docid);
                            result.replace(Docid, oldwf + dw*temp);
                        } else {
                            result.put(Docid, dw*temp);
                        }
                    }
                }
                j++;
            }
            i++;
        }
        return result;
    }

    public TreeMap<String,Double> possbilityMethod(String query)
    {
        TreeMap<String,Double> result =new TreeMap<String,Double>();
        TreeMap<String, ArrayList<Double>> rdoc=new TreeMap<String, ArrayList<Double>>();//or的结果
        TreeMap<Character, Term> terms=dataProcessor.getPoetTerm();
        Character queryTerm;
        int i = 0;
        double maxDoc=1000.0;
        double pi=0;
        double ri=0;
        if(query.length()==1) rdoc = terms.get(query.charAt(0)).getDoc();
        else{
            for(int n = 0; n < query.length()-1; n++) {
                if(terms.containsKey(query.charAt(n))) {
                    if(rdoc.size()==0) {
                        rdoc = terms.get(query.charAt(n)).getDoc();
                    }else {
                        rdoc = OR(rdoc, terms.get(query.charAt(n + 1)).getDoc());
                    }
                }
            }//计算or
        }


        while(i<query.length())
        {   int j=0;
            queryTerm=query.charAt(i);
            while(j<rdoc.keySet().size()) {
                String Docid=(String)rdoc.keySet().toArray()[j];
                if (dataProcessor.getPoetTerm().containsKey(queryTerm)) {
                    Term t = dataProcessor.getPoetTerm().get(queryTerm);
                    double docNum = t.getDocNum();
                    pi = docNum / (maxDoc+1);
                    if(t.getDoc().containsKey(Docid))
                    {
                        double RSV=0.0;
                        double RSVi = 2*Math.log(1000 / (double)(t.getDocNum()+1));
                        RSVi+=0.5*Math.log(pi/(1-pi));
                        if (result.containsKey(Docid))//如果计算过这个文档
                        {
                            double oldRSV = result.get(Docid);
                            result.replace(Docid, oldRSV+RSVi);
                        } else {
                            result.put(Docid, RSVi);
                        }

                    }

                }
                j++;
            }
            i++;
        }
        return result;
    }

    private TreeMap<String, ArrayList<Double>> AND(TreeMap<String, ArrayList<Double>> p1, TreeMap<String, ArrayList<Double>> p2) {
        TreeMap<String, ArrayList<Double>> docId = new TreeMap<String, ArrayList<Double>>();
        int i = 0, j = 0;
        String p1ID="",p2ID="";
        while (i<p1.keySet().size() && j<p2.keySet().size()){
            p1ID=(String)p1.keySet().toArray()[i];
            p2ID=(String)p2.keySet().toArray()[j];
            int val = p1ID.compareTo(p2ID);
            //相等
            if (val == 0) {
                ArrayList<Double> temp = new ArrayList<>();
                temp.add( (p1.get(p1ID).get(0)+p1.get(p1ID).get(0))/2.0);
                temp.add( (p1.get(p1ID).get(1)+p1.get(p1ID).get(1))/2.0);
                temp.add( (p1.get(p1ID).get(2)+p1.get(p1ID).get(2))/2.0);
                docId.put((String) p1ID, temp);
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

    private TreeMap<String, ArrayList<Double>> ANDNOT(TreeMap<String, ArrayList<Double>> p1, TreeMap<String, ArrayList<Double>> p2) {

        TreeMap<String, ArrayList<Double>> docId = new TreeMap<String, ArrayList<Double>>();
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
                docId.put((String) p1ID, p1.get(p1ID));
                i++;
            } else if (val > 0) {
                j++;
            }
        }
        while(i<p1.keySet().size()){
            p1ID=(String)p1.keySet().toArray()[i];
            docId.put((String) p1ID,p1.get(p1ID));
            i++;
        }
        return docId;
    }

    private TreeMap<String, ArrayList<Double>> AndAll( TreeMap<String, ArrayList<Double>> result,ArrayList<TreeMap<String, ArrayList<Double>>> next)
    {

       next.sort(new Comparator<TreeMap<String, ArrayList<Double>>>() {
           @Override
           public int compare(TreeMap<String, ArrayList<Double>> o1, TreeMap<String, ArrayList<Double>> o2) {
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
    
    private TreeMap<String, ArrayList<Double>> OR(TreeMap<String, ArrayList<Double>> p1, TreeMap<String, ArrayList<Double>> p2){
        TreeMap<String, ArrayList<Double>> docId = new TreeMap<String, ArrayList<Double>>();
        int i = 0, j = 0;
        String p1ID="",p2ID="";
        while (i<p1.keySet().size() && j<p2.keySet().size()){
            p1ID=(String)p1.keySet().toArray()[i];
            p2ID=(String)p2.keySet().toArray()[j];
            int val = p1ID.compareTo(p2ID);
            //相等
            if (val == 0) {
                ArrayList<Double> temp = new ArrayList<>();
                temp.add( (p1.get(p1ID).get(0)+p1.get(p1ID).get(0))/2.0);
                temp.add( (p1.get(p1ID).get(1)+p1.get(p1ID).get(1))/2.0);
                temp.add( (p1.get(p1ID).get(2)+p1.get(p1ID).get(2))/2.0);
                docId.put((String) p1ID, temp);
                i++;
                j++;
            } else if (val < 0) {
                docId.put((String) p1ID, p1.get(p1ID));
                i++;

            } else if (val > 0) {
                docId.put((String) p2ID, p2.get(p2ID));
                j++;
            }
        }

        while(i<p1.keySet().size()){
            p1ID=(String)p1.keySet().toArray()[i];
            docId.put((String) p1ID, p1.get(p1ID));
            i++;
        }
        while(j<p2.keySet().size()){
            p2ID=(String)p2.keySet().toArray()[j];
            docId.put((String) p2ID, p2.get(p2ID));
            j++;
        }
        return docId;
    }

    private TreeMap<String, ArrayList<Double>> OrAll( TreeMap<String, ArrayList<Double>> result,ArrayList<TreeMap<String, ArrayList<Double>>> next)
    {

        next.sort(new Comparator<TreeMap<String, ArrayList<Double>>>() {
            @Override
            public int compare(TreeMap<String, ArrayList<Double>> o1, TreeMap<String, ArrayList<Double>> o2) {
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
