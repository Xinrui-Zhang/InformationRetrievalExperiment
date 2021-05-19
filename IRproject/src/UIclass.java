/**
 * @创建人 柳靖洋
 * @创建时间 2021/5/10
 * @描述
 */
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import org.json.*;
import java.io.*;
import java.text.CollationKey;
import java.text.Collator;
import java.util.List;

public class UIclass extends JFrame{

    public JTextField searchText;
    public JButton search;
    public JSlider jSlider;
    public static JTextArea showArea;
    public static JScrollPane scroll;
    public JRadioButton tf;
    public JRadioButton wf;
    public ButtonGroup bgroup = new ButtonGroup();
    public ArrayList<Character> text;
    private searchMethod sm;
    public TreeMap<Character,Term> getDictionary;
    public dataProcessor data;
    private final TreeMap<Character,Term> terms;
    private final TreeMap<String, Poet> poets;
    private Color backColor;
    private Color contentColor;
    private Color textColor;
    private Color boderColor;
    public UIclass() throws IOException {
        super();
        textColor=new Color(228,223,215);
        contentColor=new Color(113,54,29);
        boderColor=new Color(217,145,86);
        this.setSize(1000, 800);     //窗口大小
        this.getContentPane().setLayout(null);
        this.add(getJTextField(), null);
        this.add(getJButton(), null);
        this.add(getScrollPane(),null);
        //this.add(getJTextArea(),null);
        this.setTitle("信息检索");

        this.getContentPane().setBackground(new Color(255,255,255));
        //String path="IRproject/dataset/poet.tang.1000.json";
        String path =".\\dataset\\poet.tang.1000.json";
        dataProcessor.readFile(path);
        dataProcessor.writeFile();
        poets = dataProcessor.getPoets();
        terms = dataProcessor.getPoetTerm();

    }



    private JTextField getJTextField()
    {
        if(searchText == null) {
            searchText = new javax.swing.JTextField();
            searchText.setBounds(300, 50, 300, 20);
        }
        return searchText;
    }

    private JButton getJButton()
    {
        if(search == null) {
            search = new javax.swing.JButton();
            search.setBounds(820, 50, 140, 20);
            search.setText("布尔模型搜索");
        }
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showArea.setText("");
                searchMethod s = new searchMethod();
                String text;
                text=searchText.getText();
                TreeMap<String, ArrayList<Double>> result = s.query(text,terms);
                List<Map.Entry<String, ArrayList<Double>>> list = new ArrayList<Map.Entry<String, ArrayList<Double>>>(result.entrySet());


                list.sort(new Comparator<Map.Entry<String, ArrayList<Double>>>() {
                    //升序排序
                    public int compare(Map.Entry<String, ArrayList<Double>> o1, Map.Entry<String, ArrayList<Double>> o2) {
                        //若为tfidf排序则get(1)，wfidf则为get(2)
                        return o2.getValue().get(1).compareTo(o1.getValue().get(1));
                    }
                });
                if(list.isEmpty()){
                    showArea.append("无结果");
                }else{
                    for(Map.Entry<String,ArrayList<Double>> d : list) {
                        Poet t = poets.get(d.getKey());
                        showArea.append(t.getTitle()+"\n"+t.getAuthor()+"\n"+t.getParagraphs()+"\n"+"tfidf:"+result.get(d.getKey()).get(1)+"\n"+"wfidf:"+result.get(d.getKey()).get(2)+"\n");
                    }
                }
            }
        });
        return search;

    }

    public JRadioButton getTf() {
        if(tf==null) {
            tf = new JRadioButton("tfidf",true);
            tf.setLocation(300,75);
        }
        return tf;
    }

    public JRadioButton getWf() {
        if(wf==null) {
            wf = new JRadioButton("wfidf");
            wf.setLocation(400, 75);
        }
        return wf;
    }

    private JTextArea getJTextArea()
    {
        if(showArea == null)
        {
            showArea =new JTextArea();
            showArea.setBounds(300,100,380,500);
            showArea.setLineWrap(true);
            showArea.setEditable(false);
            showArea.setBackground(contentColor);
            showArea.setForeground(textColor);

        }
        return showArea;
    }
    private JScrollPane getScrollPane()
    {
        if(scroll == null)
        {
            scroll =new JScrollPane();
            scroll.setBounds(0,150,1000,500);
            scroll.setViewportView(getJTextArea());
            scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scroll.setBorder(BorderFactory.createMatteBorder(50,220,50,220,boderColor));
        }
        return scroll;
    }
    public static void main(String[] args) throws IOException {

         UIclass w = new UIclass();
         w.setVisible(true);                         //设为可见






     }


}
